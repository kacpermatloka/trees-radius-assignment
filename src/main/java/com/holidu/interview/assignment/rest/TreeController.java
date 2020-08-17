package com.holidu.interview.assignment.rest;

import com.holidu.interview.assignment.exceptions.NegativeRadiusException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.Map;

@RestController
public class TreeController {


    @GetMapping("/trees")
    public Map<String, Integer> countTreesInCircle(@RequestParam("x") double x,
                                                   @RequestParam("y") double y,
                                                   @RequestParam("radius") double radius) {

        if (radius < 0) {
            throw new NegativeRadiusException(radius);
        }

        return Collections.emptyMap();
    }
}
