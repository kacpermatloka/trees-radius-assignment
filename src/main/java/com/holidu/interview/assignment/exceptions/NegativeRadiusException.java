package com.holidu.interview.assignment.exceptions;

public class NegativeRadiusException extends RuntimeException {

    public NegativeRadiusException(double radius) {
        super("Radius value must not be negative. [value: " + radius + "]");
    }
}