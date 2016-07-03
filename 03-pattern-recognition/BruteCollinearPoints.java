
import java.util.Arrays;
import java.util.ArrayList;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

public class BruteCollinearPoints {
    private LineSegment[] segments;

    // finds all line segments containing 4 points
    public BruteCollinearPoints(Point[] points) {
        checkNullArgument(points);
        checkDuplicatePoints(points);

        ArrayList<LineSegment> arLS = new ArrayList<LineSegment>();
        Point[] p   = points.clone();
        int     len = p.length;
        double  sl1;
        double  sl2;
        double  sl3;

        Arrays.sort(p);

        for (int i = 0; i < len - 3; i++) {
            for (int j = i + 1; j < len - 2; j++) {
                sl1 = p[i].slopeTo(p[j]);

                for (int k = j + 1; k < len - 1; k++) {
                    sl2 = p[j].slopeTo(p[k]);

                    for (int l = k + 1; l < len; l++) {
                        sl3 = p[k].slopeTo(p[l]);

                        if ((sl1 == sl2) && (sl2 == sl3))
                            arLS.add(new LineSegment(p[i], p[l]));
                    }
                }
            }
        }

        segments = arLS.toArray(new LineSegment[arLS.size()]);
    }

    private void checkNullArgument(Point[] points) {
        if (points == null)
            throw new NullPointerException("null argument");
        else {
            int len = points.length;
            for (int i = 0; i < len; i++) {
                if (points[i] == null)
                    throw new NullPointerException("null argument");
            }
        }
    }

    private void checkDuplicatePoints(Point[] points) {
        int len = points.length;
        for (int i = 0; i < len - 1; i++) {
            for (int j = i + 1; j < len; j++) {
                if (points[i].compareTo(points[j]) == 0)
                    throw new IllegalArgumentException("repeated points");
            }
        }
    }

    // the number of line segments
    public int numberOfSegments() {
        return this.segments.length;
    }

    // the line segments
    public LineSegment[] segments() {
        return Arrays.copyOf(segments, numberOfSegments());
    }

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
    for (Point p : points) {
        p.draw();
    }
    StdDraw.show();

    // print and draw the line segments
    BruteCollinearPoints collinear = new BruteCollinearPoints(points);
    for (LineSegment segment : collinear.segments()) {
        StdOut.println(segment);
        segment.draw();
    }
}
}
