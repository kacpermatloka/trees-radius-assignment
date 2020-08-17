package com.holidu.interview.assignment.service.impl;

import com.holidu.interview.assignment.model.SearchCircle;
import com.holidu.interview.assignment.provider.DataProvider;
import com.holidu.interview.assignment.provider.impl.TreeCountProcessor;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class TreeServiceImplTest {

    @InjectMocks
    private TreeServiceImpl treeService;

    @Mock
    private DataProvider dataProvider;

    @Mock
    private TreeCountProcessor treeCountProcessor;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void test_countTrees_shouldReturnEmptyMapIfNoTreesFound() {
        SearchCircle circle = new SearchCircle(5, 5, 10);

        when(dataProvider.fetchTrees(any())).thenReturn(Optional.empty());
        when(treeCountProcessor.getCount(anyList(), any())).thenReturn(Collections.emptyMap());

        Map<String, Integer> result = treeService.countTrees(circle);
        assertTrue(result.isEmpty());
    }
}