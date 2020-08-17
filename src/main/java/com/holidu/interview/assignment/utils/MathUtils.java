package com.holidu.interview.assignment.utils;

/**
 * Utility class providing common helper methods.
 */
public class MathUtils {

    public static final double METER_TO_FEET_RATE = 3.280839895;

    private MathUtils() {
    }

    /**
     * Converts meters to feet distance unit.
     */
    public static double convertToFeet(double meters) {
        return meters * METER_TO_FEET_RATE;
    }

    /**
     * Checks whether current tree(x,y) is within provided circle on the x-y plane.
     */
    public static boolean isWithinCircle(double x, double y, double radius, double treeX, double treeY) {
        double dx = Math.abs(x - treeX);
        double dy = Math.abs(y - treeY);

        // In order to improve performance, before directly going to circle equation,
        // check the distance from  center of the circle to tree
        if (dx > radius || dy > radius) {
            return false;
        }
        if (dx + dy < radius) {
            return true;
        }
        return Math.pow(dx, 2) + Math.pow(dy, 2) < Math.pow(radius, 2);
    }
}
