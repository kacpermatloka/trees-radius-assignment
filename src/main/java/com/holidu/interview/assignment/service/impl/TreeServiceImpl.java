package com.holidu.interview.assignment.service.impl;

import com.holidu.interview.assignment.exceptions.TreeServiceException;
import com.holidu.interview.assignment.model.SearchCircle;
import com.holidu.interview.assignment.model.SearchFrame;
import com.holidu.interview.assignment.provider.DataProvider;
import com.holidu.interview.assignment.provider.FetchTreesInFrameTask;
import com.holidu.interview.assignment.provider.impl.TreeCountProcessor;
import com.holidu.interview.assignment.service.TreeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static com.holidu.interview.assignment.model.SearchFrame.createFrameFor;
import static java.util.Collections.synchronizedList;
import static java.util.Comparator.reverseOrder;
import static java.util.Map.Entry.comparingByValue;
import static java.util.stream.Collectors.toMap;

@Service
@Slf4j
public class TreeServiceImpl implements TreeService {

    private final DataProvider dataProvider;
    private final TreeCountProcessor treeCountProcessor;

    public TreeServiceImpl(TreeCountProcessor treeCountProcessor, DataProvider dataProvider) {
        this.dataProvider = dataProvider;
        this.treeCountProcessor = treeCountProcessor;
    }

    @Override
    public Map<String, Integer> countTrees(SearchCircle circle) {
        SearchFrame originalFrame = createFrameFor(circle);
        List<String> responses = new ArrayList<>();
        try {
            if (originalFrame.isTooLarge()) {
                doConcurrentFetch(originalFrame, synchronizedList(responses));
            } else {
                doFetch(originalFrame, responses);
            }
            return sort(treeCountProcessor.getCount(responses, circle));
        } catch (Exception ex) {
            throw new TreeServiceException("An exception occurred during counting trees within circle " + circle);
        }
    }

    private void doConcurrentFetch(SearchFrame originalFrame, List<String> responses) {
        int subframesCount = originalFrame.getRequiredSubframesCount();

        ExecutorService executorService = Executors.newFixedThreadPool(10);
        for (int i = 0; i < subframesCount; i++) {
            SearchFrame subframe = originalFrame.subFrameByX(i, subframesCount - 1);
            executorService.execute(new FetchTreesInFrameTask(dataProvider, subframe, responses));
        }
        shutdownExecutorService(executorService);
    }

    private void doFetch(SearchFrame originalFrame, List<String> responses) {
        dataProvider.fetchTrees(originalFrame).ifPresent(responses::add);
    }

    private void shutdownExecutorService(ExecutorService executorService) {
        executorService.shutdown();
        try {
            if (!executorService.awaitTermination(30, TimeUnit.SECONDS)) {
                executorService.shutdownNow();
            }
        } catch (InterruptedException ex) {
            executorService.shutdownNow();
            Thread.currentThread().interrupt();
        }
    }

    private Map<String, Integer> sort(Map<String, Integer> unsortedMap) {
        return unsortedMap.entrySet()
                .stream()
                .sorted(comparingByValue(reverseOrder()))
                .collect(toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));
    }
}
