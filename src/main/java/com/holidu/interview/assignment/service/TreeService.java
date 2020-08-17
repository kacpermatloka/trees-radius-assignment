package com.holidu.interview.assignment.service;

import com.holidu.interview.assignment.model.SearchCircle;

import java.util.Map;

/**
 * Service providing methods to manipulate Tree data.
 */
public interface TreeService {

    /**
     * Counts trees occurrences within specified circle.
     */
    Map<String, Integer> countTrees(SearchCircle circle);

}
