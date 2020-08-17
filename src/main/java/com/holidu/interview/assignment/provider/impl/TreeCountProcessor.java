package com.holidu.interview.assignment.provider.impl;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.holidu.interview.assignment.model.SearchCircle;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

import static com.holidu.interview.assignment.provider.TreeDataQueryConstants.SPC_COMMON;
import static com.holidu.interview.assignment.provider.TreeDataQueryConstants.X_SP;
import static com.holidu.interview.assignment.provider.TreeDataQueryConstants.Y_SP;
import static com.holidu.interview.assignment.utils.MathUtils.isWithinCircle;

/**
 * JSON processing class that collects trees occurrences withing circle from list of json responses. Due to possible size of
 * responses it uses JSON Streaming API in order to improve performance of processing, avoiding loading data-binding approach
 * and preventing loading whole response into memory.
 */
@Component
@Slf4j
public class TreeCountProcessor {

    private final JsonFactory jsonFactory;

    public TreeCountProcessor() {
        this.jsonFactory = new JsonFactory();
    }

    public Map<String, Integer> getCount(List<String> responses, SearchCircle circle) {
        Map<String, Integer> occurrences = new ConcurrentHashMap<>();
        AtomicInteger counter = new AtomicInteger();
        AtomicInteger counterInCircle = new AtomicInteger();
        responses.stream()
                .parallel()
                .forEach(json -> {
                    try (JsonParser jsonParser = jsonFactory.createParser(json)) {
                        if (jsonParser.nextToken() != JsonToken.START_ARRAY) {
                            throw new IllegalStateException("Expected content to be an array");
                        }
                        while (jsonParser.nextToken() != JsonToken.END_ARRAY) {
                            counter.getAndIncrement();
                            checkTree(jsonParser, circle, occurrences, counterInCircle);

                        }
                    } catch (IOException e) {
                        log.error("Error while parsing json response", e.getCause());
                    }
                });
        log.debug("Processed {} trees. In circle: {}", counter, counterInCircle);
        return occurrences;
    }

    private void checkTree(JsonParser jsonParser, SearchCircle circle, Map<String, Integer> occurrences, AtomicInteger counterInCircle) throws IOException {
        double x = 0.0;
        double y = 0.0;
        String treeType = "";
        while (jsonParser.nextToken() != JsonToken.END_OBJECT) {
            String property = jsonParser.getCurrentName();
            jsonParser.nextToken();
            if (X_SP.equals(property)) {
                x = jsonParser.getValueAsDouble();
            }
            if (Y_SP.equals(property)) {
                y = jsonParser.getValueAsDouble();
            }
            if (SPC_COMMON.equals(property)) {
                treeType = jsonParser.getText();
            }
        }
        if (isWithinCircle(circle.getX(), circle.getY(), circle.getRadius(), x, y)) {
            counterInCircle.incrementAndGet();
            occurrences.merge(treeType, 1, Integer::sum);
        }
    }
}
