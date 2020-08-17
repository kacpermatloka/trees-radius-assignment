package com.holidu.interview.assignment.service.impl;

import com.holidu.interview.assignment.model.SearchCircle;
import com.holidu.interview.assignment.provider.DataProvider;
import com.holidu.interview.assignment.processor.TreeCountProcessor;
import org.junit.Before;
import org.junit.Test;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class TreeServiceImplTest {

    private TreeServiceImpl treeService;
    private DataProvider dataProvider;
    private TreeCountProcessor treeCountProcessor;

    @Before
    public void setup() {
        dataProvider = mock(DataProvider.class);
        treeCountProcessor = mock(TreeCountProcessor.class);
        treeService = new TreeServiceImpl(treeCountProcessor, dataProvider);
    }

    @Test
    public void test_countTrees_shouldReturnEmptyMapIfNoTreesFound() {
        SearchCircle circle = new SearchCircle(5, 5, 10);

        when(dataProvider.fetchTrees(any())).thenReturn(Optional.empty());
        when(treeCountProcessor.getCount(anyList(), any())).thenReturn(Collections.emptyMap());

        Map<String, Integer> result = treeService.countTrees(circle);
        assertTrue(result.isEmpty());
    }

    @Test
    public void test_countTrees_shouldReturnMapOfTreesCountForSmallFrame() {
        SearchCircle circle = new SearchCircle(5, 5, 10);

        when(dataProvider.fetchTrees(any())).thenReturn(Optional.of("[{}]"));
        Map<String, Integer> treeCount = new HashMap<>();
        treeCount.put("oak", 6);
        treeCount.put("pine", 3);
        when(treeCountProcessor.getCount(anyList(), any())).thenReturn(treeCount);

        Map<String, Integer> result = treeService.countTrees(circle);

        assertFalse(result.isEmpty());
        assertEquals(2, result.size());
    }
}