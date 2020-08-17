package com.holidu.interview.assignment.model;

import org.junit.Test;

import static com.holidu.interview.assignment.utils.MathUtils.convertToFeet;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class SearchFrameTest {

    private SearchFrame frame;

    @Test
    public void test_createFrameFor_shouldCreateCorrectFrameForCircle() {
        SearchCircle circle = new SearchCircle(5, 5, 10);
        frame = SearchFrame.createFrameFor(circle);
        assertEquals(-5, frame.getMinX(), 0.1);
        assertEquals(15, frame.getMaxX(), 0.1);
        assertEquals(-5, frame.getMinY(), 0.1);
        assertEquals(15, frame.getMaxY(), 0.1);
    }

    @Test
    public void test_isTooLarge_shouldRecognizeTooLargeFrames() {
        SearchCircle circle = new SearchCircle(5, 5, convertToFeet(1001));
        frame = SearchFrame.createFrameFor(circle);
        assertTrue(frame.isTooLarge());

        circle = new SearchCircle(5, 5, convertToFeet(999));
        frame = SearchFrame.createFrameFor(circle);
        assertFalse(frame.isTooLarge());
    }

    @Test
    public void test_getRequiredSubframesCount_shouldReturnCorrectSubframesCount() {
        SearchCircle circle = new SearchCircle(5, 5, convertToFeet(2000));
        frame = SearchFrame.createFrameFor(circle);
        assertEquals(2, frame.getRequiredSubframesCount());

        circle = new SearchCircle(5, 5, convertToFeet(4000));
        frame = SearchFrame.createFrameFor(circle);

        assertEquals(4, frame.getRequiredSubframesCount());

        circle = new SearchCircle(5, 5, convertToFeet(5500));
        frame = SearchFrame.createFrameFor(circle);

        assertEquals(6, frame.getRequiredSubframesCount());
    }

    @Test
    public void test_subFrameByX_shouldCalculateSubframes() {
        SearchCircle circle = new SearchCircle(5, 5, convertToFeet(2000));
        frame = SearchFrame.createFrameFor(circle);

        SearchFrame subframe = frame.subFrameByX(0, 2);

        assertEquals(frame.getMinX(), subframe.getMinX(), 0.1);
        assertEquals(5, subframe.getMaxX(), 0.1);

        subframe = frame.subFrameByX(1, 2);

        assertEquals(5, subframe.getMinX(), 0.1);
        assertEquals(frame.getMaxX(), subframe.getMaxX(), 0.1);
    }
}