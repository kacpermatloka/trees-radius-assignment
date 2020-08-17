package com.holidu.interview.assignment.provider;

import com.holidu.interview.assignment.model.SearchFrame;

import java.util.Optional;

/**
 * Component that generates a query for searchFrame and calls external service in order to get tree data json.
 */
public interface DataProvider {

    /**
     * Returns all trees located withing frame in format of JSON response.
     */
    Optional<String> fetchTrees(SearchFrame frame);

}
