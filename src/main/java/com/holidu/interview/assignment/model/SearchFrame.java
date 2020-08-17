package com.holidu.interview.assignment.model;

import lombok.Getter;

import static com.holidu.interview.assignment.utils.MathUtils.convertToFeet;

/**
 * Class holds a frame (rectangle) points based on provided circle which is inscribed within the frame. This is bounding
 * the area where the trees are fetch from.
 */
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

    /**
     * «
     * Generates subframe with offset from minX. Smaller subframe if a frame of dimensions
     * FRAME_THRESHOLD_X x original frameSize.
     */
    public SearchFrame subFrameByX(int offset, int subframesCount) {
        double delta = FRAME_THRESHOLD_X * offset;
        double newMinX = getMinX() + delta;

        double newMaxX = offset != subframesCount ? getMinX() + delta + FRAME_THRESHOLD_X : getMaxX();

        return new SearchFrame(newMinX, newMaxX, minY, maxY);
    }

    /**
     * Checks whether frame is too large to process at once.
     */
    public boolean isTooLarge() {
        return maxX - minX > FRAME_THRESHOLD_X;
    }

    /**
     * Returns no. of subframes required to fetch whole original frame, based on FRAME_THRESHOLD_X.
     */
    public int getRequiredSubframesCount() {
        return (int) Math.ceil((maxX - minX) / FRAME_THRESHOLD_X);
    }
}