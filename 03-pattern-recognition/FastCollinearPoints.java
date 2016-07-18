/******************************************************************************
 *  Compilation:  javac-algs4 FastCollinearPoints.java
 *  Execution:    java-algs4 FastCollinearPoints < input.txt
 *  Dependencies: In.java StdDraw.java StdOut.java
 *
 *  Find every (maximal) line segment that connects a subset of 4 or more points
 *  using a faster, sorting-based algorithm.
 *
 *  The input file need to be in the following format: An integer N, followed by
 *  N pairs of integers (x, y), each between 0 and 32.767. Bellow there are two
 *  examples:
 *
 *  % more input6.txt       % more input8.txt
 *  6                       8
 *  19000  10000             10000      0
 *  18000  10000                 0  10000
 *  32000  10000              3000   7000
 *  21000  10000              7000   3000
 *   1234   5678             20000  21000
 *  14000  10000              3000   4000
 *                           14000  15000
 *                            6000   7000
 *
 * Usage:
 *  % java FastCollinearPoints input6.txt
 *  (14000, 10000) -> (32000, 10000)
 *
 *  % java FastCollinearPoints input8.txt
 *  (10000, 0) -> (0, 10000)
 *  (3000, 4000) -> (20000, 21000)
 *
 ******************************************************************************/


import java.util.Arrays;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;


/**
 *  FastCollinearPoints is a faster, sorting-based solution for the problem of
 *  find collinear points. Remarkably, it is possible to solve the problem much
 *  faster than the brute-force solution. Given a point p, the following method
 *  determines whether p participates in a set of 4 or more collinear points.
 *
 *      - Think of p as the origin.
 *      - For each other point q, calculate the slope it makes with p.
 *      - Sort the points according to the slopes they makes with p.
 *      - Check if any 3 (or more) adjacent points in the sorted order have
 *        equal slopes with respect to p. If so, these points, together with p,
 *        are collinear.
 *
 *  Applying this method for each of the N points in turn yields an efficient
 *  algorithm to the problem. The algorithm solves the problem because points
 *  that have equal slopes with respect to p are collinear, and sorting brings
 *  such points together. The algorithm is fast because the bottleneck operation
 *  is sorting.
 *
 *  The method segments() include each maximal line segment containing 4 (or
 *  more) points exactly once. For example, if 5 points appear on a line segment
 *  in the order p -> q -> r -> s -> t, then FastCollinearPoints don't include
 *  the subsegments p -> s or q -> t.
 *
 *  FastCollinearPoints throws a NullPointerException either the argument to the
 *  constructor is null or if any point in the array is null. Throws an
 *  IllegalArgumentException if the argument to the constructor contains a
 *  repeated point.
 *
 *  The order of growth of the running time is N^2 log N in the worst case and
 *  uses space proportional to N plus the number of line segments returned.
 *
 *  FastCollinearPoints works properly even if the input has 5 or more collinear
 *  points.
 */

public class FastCollinearPoints {
    // HashMap that stores a list of end points with the same slope
    private HashMap<Double, List<Point>> foundSegments = new HashMap<>();
    private List<LineSegment> segments = new ArrayList<>();

    /**
     * Constructor that finds all line segments containing 4 or more collinear
     * points.
     *
     * @param  points given for pattern recognition
     * @throws NullPointerException if the given argument is null
     * @throws IllegalArgumentException if repeated points are given
     */
    public FastCollinearPoints(Point[] points) {
        // check for null argument and duplicate points
        checkNullArgument(points);
        checkDuplicatePoints(points);
        // we need to sort the points
        Point[] pointsCopy = Arrays.copyOf(points, points.length);
        Arrays.sort(pointsCopy);

        for (Point startPoint : points) {
            // sort points according to slope with startPoint
            Arrays.sort(pointsCopy, startPoint.slopeOrder());

            // ArrayList used to store points that have the same slope with
            // respect to startPoint
            List<Point> slopePoints = new ArrayList<>();
            double slope = 0;
            double previousSlope = Double.NEGATIVE_INFINITY;

            for (int i = 1; i < pointsCopy.length; i++) {
                slope = startPoint.slopeTo(pointsCopy[i]);
                if (slope == previousSlope) {
                    slopePoints.add(pointsCopy[i]);
                } else {
                    // when a new point have a greater slope with respect to
                    // startPoint than the previous one, and if slopePoints
                    // collects 3 or more points, add the segment
                    if (slopePoints.size() >= 3) {
                        slopePoints.add(startPoint);
                        addSegmentIfNew(slopePoints, previousSlope);
                    }
                    // then, clear the array and add the current point
                    slopePoints.clear();
                    slopePoints.add(pointsCopy[i]);
                    previousSlope = slope;
                }
            }

            // for the last point
            if (slopePoints.size() >= 3) {
                slopePoints.add(startPoint);
                addSegmentIfNew(slopePoints, slope);
            }
        }
    }

    // check for null argument, either vector or points
    private void checkNullArgument(Point[] points) {
        if (points == null)
            throw new NullPointerException("null argument");
        else {
            for (int i = 0; i < points.length; i++) {
                if (points[i] == null)
                    throw new NullPointerException("null argument");
            }
        }
    }

    // check for duplicate points
    private void checkDuplicatePoints(Point[] points) {
        for (int i = 0; i < points.length - 1; i++) {
            for (int j = i + 1; j < points.length; j++) {
                if (points[i].compareTo(points[j]) == 0)
                    throw new IllegalArgumentException("repeated points");
            }
        }
    }

    // add the segment if isn't in the list already
    private void addSegmentIfNew(List<Point> slopePoints, double slope) {
        List<Point> endPoints = foundSegments.get(slope);
        Collections.sort(slopePoints);

        // get the end points of the segment founded
        Point startPoint = slopePoints.get(0);
        Point endPoint = slopePoints.get(slopePoints.size() - 1);

        // if there's no segment founded with the current slope, add segment
        if (endPoints == null) {
            endPoints = new ArrayList<>();
            endPoints.add(endPoint);
            foundSegments.put(slope, endPoints);
            segments.add(new LineSegment(startPoint, endPoint));
        // else, compare current end point with those retrieved from the
        // foundSegments element and add it if it isn't in the HashMap
        } else {
            for (Point currentEndPoint : endPoints) {
                if (currentEndPoint.compareTo(endPoint) == 0) {
                    return;
                }
            }
            // as we sort the points, we can be sure that if the current end
            // point isn't in foundSegments end points its because either the
            // current segment cointains a previous segment or the current
            // segment is parallel to other
            endPoints.add(endPoint);
            segments.add(new LineSegment(startPoint, endPoint));
        }
    }

    /**
     * Returns the number of segments found.
     *
     * @return the number of line segments
     */
    public int numberOfSegments() {
        return segments.size();
    }

    /**
     * Returns the line segments found.
     *
     * @return an array with the line segments
     */
    public LineSegment[] segments() {
        return segments.toArray(new LineSegment[segments.size()]);
    }

    /**
     * Unit tests the FastCollinearPoints data type.
     */
    public static void main(String[] args) {
        // read the N points from a file
        In in = new In(args[0]);
        int N = in.readInt();
        Point[] points = new Point[N];
        for (int i = 0; i < N; i++) {
            int x = in.readInt();
            int y = in.readInt();
            points[i] = new Point(x, y);
        }

        // draw the points
        StdDraw.show(0);
        StdDraw.setXscale(0, 32768);
        StdDraw.setYscale(0, 32768);
        for (Point p : points)
            p.draw();
        StdDraw.show();

        // print and draw the line segments
        BruteCollinearPoints collinear = new BruteCollinearPoints(points);
        for (LineSegment segment : collinear.segments()) {
            StdOut.println(segment);
            segment.draw();
        }
    }
}
