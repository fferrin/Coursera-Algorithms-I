
import java.util.Stack;
import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.StdDraw;

public class KdTree {
    private Node    root;
    private int     size;
    private Point2D closest;
    private double  dMin;

    private static class Node {
        private Point2D p;
        private RectHV  rect;
        private Node    left, right;

        public Node(Point2D p, RectHV rect) {
            this.p    = p;
            this.rect = rect;
            left      = null;
            right     = null;
        }
    }

    // construct an empty set of points
    public KdTree() {
        root = null;
        size = 0;
    }

    // is the set empty?
    public boolean isEmpty() {
        return (size == 0);
    }

    // number of points in the set
    public int size() {
        return size;
    }

    // add the point to the set (if it is not already in the set)
    public void insert(Point2D p) {
        if (root == null) {
            root = new Node(p, new RectHV(0, 0, 1, 1));
            size++;
        } else
            root = insertV(root, null, p, 0);
    }

    private Node insertV(Node node, Node prev, Point2D p, double cmp) {
        if (node == null) {
            size++;
            if (0 < cmp)
                return new Node(p, new RectHV(prev.rect.xmin(), prev.p.y(),
                                        prev.rect.xmax(), prev.rect.ymax()));
            else
                return new Node(p, new RectHV(prev.rect.xmin(),
                            prev.rect.ymin(), prev.rect.xmax(), prev.p.y()));
        }

        if (!node.p.equals(p)) {
            cmp = p.x() - node.p.x();
            if (0 < cmp) node.right = insertH(node.right, node, p, cmp);
            else         node.left  = insertH(node.left, node, p, cmp);
        }
        return node;
    }

    private Node insertH(Node node, Node prev, Point2D p, double cmp) {
        if (node == null) {
            size++;
            if (0 < cmp)
                return new Node(p, new RectHV(prev.p.x(), prev.rect.ymin(),
                                        prev.rect.xmax(), prev.rect.ymax()));
            else
                return new Node(p, new RectHV(prev.rect.xmin(),
                            prev.rect.ymin(), prev.p.x(), prev.rect.ymax()));
        }

        if (!node.p.equals(p)) {
            cmp = p.y() - node.p.y();
            if (0 < cmp) node.right = insertV(node.right, node, p, cmp);
            else         node.left  = insertV(node.left, node, p, cmp);
        }
        return node;
    }

    // does the set contain point p?
    public boolean contains(Point2D p) {
        return containsX(root, p);
    }

    private boolean containsX(Node node, Point2D p) {
        if (node == null)       return false;
        if (node.p.equals(p))   return true;
        if (node.p.x() < p.x()) return containsY(node.right, p);
        else                    return containsY(node.left, p);
    }

    private boolean containsY(Node node, Point2D p) {
        if (node == null)       return false;
        if (node.p.equals(p))   return true;
        if (node.p.y() < p.y()) return containsX(node.right, p);
        else                    return containsX(node.left, p);
    }

    // draw all points to standard draw
    public void draw() {
        drawYLine(root, 0, 1);
    }

    private void drawYLine(Node node, double ymin, double ymax) {
        if (node != null) {
            StdDraw.setPenColor(StdDraw.RED);
            StdDraw.setPenRadius(0.002);
            StdDraw.line(node.p.x(), ymin, node.p.x(), ymax);
            StdDraw.setPenColor(StdDraw.BLACK);
            StdDraw.setPenRadius(0.01);
            node.p.draw();
            drawXLine(node.left, node.rect.xmin(), node.p.x());
            drawXLine(node.right, node.p.x(), node.rect.xmax());
        }
    }

    private void drawXLine(Node node, double xmin, double xmax) {
        if (node != null) {
            StdDraw.setPenColor(StdDraw.BLUE);
            StdDraw.setPenRadius(0.002);
            StdDraw.line(xmin, node.p.y(), xmax, node.p.y());
            StdDraw.setPenColor(StdDraw.BLACK);
            StdDraw.setPenRadius(0.01);
            node.p.draw();
            drawYLine(node.left, node.rect.ymin(), node.p.y());
            drawYLine(node.right, node.p.y(), node.rect.ymax());
        }
    }

    // all points that are inside the rectangle
    public Iterable<Point2D> range(RectHV r) {
        Stack<Point2D> stack = new Stack<>();
        push(root, stack, r);
        return stack;
    }

    private void push(Node node, Stack<Point2D> stack, RectHV r) {
        if (node != null) {
            if (r.contains(node.p)) stack.push(node.p);
            if (r.intersects(node.rect)) {
                push(node.left, stack, r);
                push(node.right, stack, r);
            }
        }
    }

    // a nearest neighbor in the set to point p; null if the set is empty
    public Point2D nearest(Point2D p) {
        if (size == 0) return null;
        closest = null;
        dMin    = Double.POSITIVE_INFINITY;

        nearestX(root, p);
        return closest;
    }

    private void nearestX(Node node, Point2D p) {
        if (node != null) {
            if (node.rect.distanceSquaredTo(p) < dMin) {
                double dist = node.p.distanceSquaredTo(p);
                if (dist < dMin) {
                    closest = node.p;
                    dMin    = dist;
                }

                if (p.x() < node.p.x()) {
                    nearestY(node.left, p);
                    nearestY(node.right, p);
                } else {
                    nearestY(node.right, p);
                    nearestY(node.left, p);
                }
            }
        }
    }

    private void nearestY(Node node, Point2D p) {
        if (node != null) {
            if (node.rect.distanceSquaredTo(p) < dMin) {
                double dist = node.p.distanceSquaredTo(p);
                if (dist < dMin) {
                    closest = node.p;
                    dMin    = dist;
                }

                if (p.y() < node.p.y()) {
                    nearestX(node.left, p);
                    nearestX(node.right, p);
                } else {
                    nearestX(node.right, p);
                    nearestX(node.left, p);
                }
            }
        }
    }

    // unit testing of the methods (optional)
    public static void main(String[] args) {
    }
}
