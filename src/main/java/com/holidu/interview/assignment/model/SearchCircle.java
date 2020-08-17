package com.holidu.interview.assignment.model;

import com.holidu.interview.assignment.exceptions.NegativeRadiusException;
import lombok.Getter;

/**
 * Class holding circle properties.
 */
@Getter
public class SearchCircle {
    private final double x;
    private final double y;
    private final double radius;

    public SearchCircle(double x, double y, double radius) {
        if (radius < 0) {
            throw new NegativeRadiusException(radius);
        }
        this.radius = radius;
        this.x = x;
        this.y = y;
    }
}
