package com.holidu.interview.assignment.model;

import lombok.Getter;

import static com.holidu.interview.assignment.utils.MathUtils.convertToFeet;

@Getter
public class SearchFrame {

    public static final double FRAME_THRESHOLD_X = convertToFeet(2000);

    private final double minX;
    private final double maxX;
    private final double minY;
    private final double maxY;

    public static SearchFrame createFrameFor(SearchCircle circle) {
        return new SearchFrame(circle);
    }

    private SearchFrame(SearchCircle circle) {
        minX = circle.getX() - circle.getRadius();
        maxX = circle.getX() + circle.getRadius();
        minY = circle.getY() - circle.getRadius();
        maxY = circle.getY() + circle.getRadius();
    }

    private SearchFrame(double minX, double maxX, double minY, double maxY) {
        this.minX = minX;
        this.maxX = maxX;
        this.minY = minY;
        this.maxY = maxY;
    }

    public SearchFrame subFrameByX(int offset, int subframesCount) {
        double delta = FRAME_THRESHOLD_X * offset;
        double newMinX = getMinX() + delta;

        double newMaxX = offset != subframesCount ? getMinX() + delta + FRAME_THRESHOLD_X : getMaxX();

        return new SearchFrame(newMinX, newMaxX, minY, maxY);
    }

    public boolean isTooLarge() {
        return maxX - minX > FRAME_THRESHOLD_X;
    }

    public int getRequiredSubframesCount() {
        return (int) Math.ceil((maxX - minX) / FRAME_THRESHOLD_X);
    }
}