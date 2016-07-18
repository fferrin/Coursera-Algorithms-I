/******************************************************************************
 *  Compilation:  javac-algs4 BruteCollinearPoints.java
 *  Execution:    java-algs4 BruteCollinearPoints < input.txt
 *  Dependencies: In.java StdDraw.java StdOut.java
 *
 *  Find every (maximal) line segment that connects a subset of 4 or more points
 *  using brute force.
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
 *  % java BruteCollinearPoints input6.txt
 *  (14000, 10000) -> (32000, 10000)
 *
 *  % java BruteCollinearPoints input8.txt
 *  (10000, 0) -> (0, 10000)
 *  (3000, 4000) -> (20000, 21000)
 *
 ******************************************************************************/


import java.util.ArrayList;
import java.util.Arrays;

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;


/**
 *  BruteCollinearPoints examines 4 points at a time and checks whether they all
 *  lie on the same line segment, returning all such line segments. To check
 *  whether the 4 points p, q, r, and s are collinear, BruteCollinearPoints
 *  check whether the three slopes between p and q, between p and r, and between
 *  p and s are all equal.
 *
 *  The method segments() include each line segment containing 4 points exactly
 *  once. If 4 points appear on a line segment in the order p -> q -> r -> s,
 *  then BruteCollinearPoints include either the line segment p -> s or s -> p
 *  (but not both) and doesn't include subsegments such as p -> r or q -> r.
 *
 *  BruteCollinearPoints throws a NullPointerException either the argument to
 *  the constructor is null or if any point in the array is null. Throws an
 *  IllegalArgumentException if the argument to the constructor contains a
 *  repeated point.
 *
 *  The order of growth of the running time is N^4 in the worst case and uses
 *  space proportional to N plus the number of line segments returned.
 */

public class BruteCollinearPoints {
    // segments that connect 4 or more collinear points
    private LineSegment[] segments;

    /**
     * Constructor that finds all line segments containing 4 collinear points.
     *
     * @param  points given for pattern recognition
     * @throws NullPointerException if the given argument is null
     * @throws IllegalArgumentException if repeated points are given
     */
    public BruteCollinearPoints(Point[] points) {
        // check for null argument and duplicate points
        checkNullArgument(points);
        checkDuplicatePoints(points);

        ArrayList<LineSegment> arLS = new ArrayList<LineSegment>();
        Point[] p   = points.clone();   // we need to sort the points
        int     len = p.length;
        double  sl1;
        double  sl2;
        double  sl3;

        // sorting points avoids calculate the slope twice
        Arrays.sort(p);


        for (int i = 0; i < len - 3; i++) {
            for (int j = i + 1; j < len - 2; j++) {
                sl1 = p[i].slopeTo(p[j]);

                for (int k = j + 1; k < len - 1; k++) {
                    sl2 = p[j].slopeTo(p[k]);

                    for (int l = k + 1; l < len; l++) {
                        sl3 = p[k].slopeTo(p[l]);
                        // if both slopes are equal, the points are collinear
                        if ((sl1 == sl2) && (sl2 == sl3))
                            arLS.add(new LineSegment(p[i], p[l]));
                    }
                }
            }
        }

        segments = arLS.toArray(new LineSegment[arLS.size()]);
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

    /**
     * Returns the number of segments found.
     *
     * @return the number of line segments
     */
    public int numberOfSegments() {
        return this.segments.length;
    }

    /**
     * Returns the line segments found.
     *
     * @return an array with the line segments
     */
    public LineSegment[] segments() {
        return Arrays.copyOf(segments, numberOfSegments());
    }

    /**
     * Unit tests the BruteCollinearPoints data type.
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
