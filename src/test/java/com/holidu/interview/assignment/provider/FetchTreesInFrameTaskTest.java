package com.holidu.interview.assignment.provider;

import com.holidu.interview.assignment.model.SearchCircle;
import com.holidu.interview.assignment.model.SearchFrame;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.holidu.interview.assignment.model.SearchFrame.createFrameFor;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class FetchTreesInFrameTaskTest {

    private FetchTreesInFrameTask task;
    private List<String> responses;
    private DataProvider mockDataProvider;

    @Before
    public void setUp() {
        SearchFrame frame = createFrameFor(new SearchCircle(1, 2, 3));
        responses = new ArrayList<>();
        mockDataProvider = mock(DataProvider.class);
        task = new FetchTreesInFrameTask(mockDataProvider, frame, responses);
    }

    @Test
    public void test_run_shouldAddResponseToListIfExist() {
        when(mockDataProvider.fetchTrees(any())).thenReturn(Optional.of("response"));

        task.run();

        assertFalse(responses.isEmpty());
        assertEquals(1, responses.size());
    }

    @Test
    public void test_run_shouldNotAddResponseToListIfEmpty() {
        when(mockDataProvider.fetchTrees(any())).thenReturn(Optional.empty());

        task.run();

        assertTrue(responses.isEmpty());
    }
}