package com.holidu.interview.assignment.rest;

import com.holidu.interview.assignment.model.SearchCircle;
import com.holidu.interview.assignment.service.TreeService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

import static com.holidu.interview.assignment.utils.MathUtils.convertToFeet;

@RestController
public class TreeController {

    private final TreeService treeService;

    public TreeController(TreeService treeService) {
        this.treeService = treeService;
    }

    @GetMapping("/trees")
    public Map<String, Integer> countTreesInCircle(@RequestParam("x") double x,
                                                   @RequestParam("y") double y,
                                                   @RequestParam("radius") double radius) {
        SearchCircle circle = new SearchCircle(x, y, convertToFeet(radius));
        return treeService.countTrees(circle);
    }
}
