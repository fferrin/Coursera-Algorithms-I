
import java.util.Stack;
import edu.princeton.cs.algs4.SET;
import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;

public class PointSET {
    private SET<Point2D> set;
    private RectHV       rect;

    // construct an empty set of points
    public PointSET() {
        set  = new SET<>();
        rect = new RectHV(0, 0, 1, 1);
    }

    // is the set empty?
    public boolean isEmpty() {
        return (size() == 0);
    }

    // number of points in the set
    public int size() {
        return set.size();
    }

    // add the point to the set (if it is not already in the set)
    public void insert(Point2D p) {
        set.add(p);
    }

    // does the set contain point p?
    public boolean contains(Point2D p) {
        return set.contains(p);
    }

    // draw all points to standard draw
    public void draw() {
        for (Point2D p : range(rect))
            p.draw();
    }

    // all points that are inside the rectangle
    public Iterable<Point2D> range(RectHV r) {
        Stack<Point2D> stack = new Stack<>();

        for (Point2D p : set)
            if (r.contains(p)) stack.push(p);

        return stack;
    }

    // a nearest neighbor in the set to point p; null if the set is empty
    public Point2D nearest(Point2D p) {
        if (isEmpty()) return null;

        double  temp;
        double  dist    = Double.POSITIVE_INFINITY;
        Point2D nearest = p;

        for (Point2D q : set) {
            temp = p.distanceSquaredTo(q);
            // if (!p.equals(q) && (temp < dist)) {
            if (temp < dist) {
                nearest = q;
                dist    = temp;
            }
        }

        return nearest;
    }

    // unit testing of the methods (optional)
    public static void main(String[] args) {
    }
}
