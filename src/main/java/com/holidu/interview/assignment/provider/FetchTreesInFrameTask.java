package com.holidu.interview.assignment.provider;

import com.holidu.interview.assignment.model.SearchFrame;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Optional;

@Slf4j
public class FetchTreesInFrameTask implements Runnable {

    private final DataProvider dataProvider;
    private final SearchFrame subframe;
    private final List<String> responses;

    public FetchTreesInFrameTask(DataProvider dataProvider, SearchFrame subframe, List<String> responses) {
        this.dataProvider = dataProvider;
        this.subframe = subframe;
        this.responses = responses;
    }

    @Override
    public void run() {
        Optional<String> responseOptional = dataProvider.fetchTrees(subframe);
        responseOptional.ifPresent(responses::add);
    }
}
