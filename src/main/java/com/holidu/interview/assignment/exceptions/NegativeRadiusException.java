package com.holidu.interview.assignment.exceptions;

/**
 * Exception that is thrown on negative radius value.
 */
public class NegativeRadiusException extends RuntimeException {

    public NegativeRadiusException(double radius) {
        super("Radius value must not be negative. [value: " + radius + "]");
    }
}