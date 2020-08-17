package com.holidu.interview.assignment.provider.impl;

import com.holidu.interview.assignment.model.SearchFrame;
import com.holidu.interview.assignment.provider.DataProvider;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.time.Instant;
import java.util.Optional;

import static com.holidu.interview.assignment.provider.TreeDataQueryConstants.AND;
import static com.holidu.interview.assignment.provider.TreeDataQueryConstants.LIMIT;
import static com.holidu.interview.assignment.provider.TreeDataQueryConstants.SELECT;
import static com.holidu.interview.assignment.provider.TreeDataQueryConstants.SPC_COMMON;
import static com.holidu.interview.assignment.provider.TreeDataQueryConstants.WHERE;
import static com.holidu.interview.assignment.provider.TreeDataQueryConstants.X_SP;
import static com.holidu.interview.assignment.provider.TreeDataQueryConstants.Y_SP;

@Component
@Slf4j
public class TreeDataProvider implements DataProvider {

    private final RestTemplate restTemplate;

    @Value("${integrations.treeData.url}")
    private String baseUrl;

    @Value("${integrations.treeData.searchLimit}")
    private int searchLimit;

    public TreeDataProvider(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
    public Optional<String> fetchTrees(SearchFrame frame) {
        // limit queried fields to only relevant ones
        String[] fields = {SPC_COMMON, X_SP, Y_SP};
        URI uri = getURI(fields, getWhereConditions(frame), searchLimit);
        try {
            long before = Instant.now().toEpochMilli();
            String response = restTemplate.getForObject(uri, String.class);
            double executionTime = (Instant.now().toEpochMilli() - before);
            log.debug("Frame fetched in {}ms", executionTime);
            return Optional.ofNullable(response);
        } catch (RestClientException e) {
            log.error("Could not fetch tree data for request: {}. Cause: {}", uri, e.getMessage());
        }
        return Optional.empty();
    }

    private URI getURI(String[] fields, String whereConditions, int limit) {
        return UriComponentsBuilder
                .fromHttpUrl(baseUrl)
                .queryParam(SELECT, String.join(",", fields))
                .queryParam(WHERE, whereConditions)
                .queryParam(LIMIT, limit)
                .build()
                .toUri();
    }

    private String getWhereConditions(SearchFrame frame) {
        return X_SP + " >= " + frame.getMinX() + AND +
                X_SP + " <= " + frame.getMaxX() + AND +
                Y_SP + " >= " + frame.getMinY() + AND +
                Y_SP + " <= " + frame.getMaxY();
    }
}
