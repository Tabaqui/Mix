
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.StdDraw;
import java.util.HashSet;
import java.util.Set;

public class KdTree {

    private static class Node {

        private Point2D p;      // the point
        private RectHV rect;    // the axis-aligned rectangle corresponding to this node
        private Node lb;        // the left/bottom subtree
        private Node rt;        // the right/top subtree

        public Node(Point2D p, RectHV rect) {
            this.p = p;
            this.rect = rect;
        }
    }

    private Node root;
    private int size;

    public KdTree() {

    }

    public boolean isEmpty() {
        return size == 0;
    }

    public int size() {
        return size;
    }

    public void insert(Point2D p) {

        root = insert(root, p, new RectHV(0.0, 0.0, 1.0, 1.0), false);
    }

    private Node insert(Node node, Point2D p, RectHV rect, boolean horizontal) {
        if (node == null) {
            size++;
            return new Node(p, rect);
        }
        RectHV newRect = getRect(p, rect, node, horizontal);
        if (!horizontal) {
            if (p.x() < node.p.x()) {
                node.lb = insert(node.lb, p, newRect, true);
            } else {
                node.rt = insert(node.rt, p, newRect, true);
            }
        } else {
            if (p.y() < node.p.y()) {
                node.lb = insert(node.lb, p, newRect, false);
            } else {
                node.rt = insert(node.rt, p, newRect, false);
            }
        }
        return node;
    }

    private RectHV getRect(Point2D p, RectHV rect, Node node, boolean horizontal) {
        RectHV result;
        if (!horizontal) {
            if (p.x() < node.p.x()) {
                result = new RectHV(rect.xmin(), rect.ymin(), node.p.x(), rect.ymax());
            } else if (p.x() == node.p.x()) {
                result = new RectHV(rect.xmin(), rect.ymin(), rect.xmax(), rect.ymax());
            } else {
                result = new RectHV(node.p.x(), rect.ymin(), rect.xmax(), rect.ymax());
            }
        } else {
            if (p.y() < node.p.y()) {
                result = new RectHV(rect.xmin(), rect.ymin(), rect.xmax(), node.p.y());
            } else if (p.y() == node.p.y()) {
                result = new RectHV(rect.xmin(), rect.ymin(), rect.xmax(), rect.ymax());
            } else {
                result = new RectHV(rect.xmin(), node.p.y(), rect.xmax(), rect.ymax());
            }
        }
        return result;
    }

    public boolean contains(Point2D p) {
        return contains(root, p, true);
    }

    private boolean contains(Node node, Point2D p, boolean horizontal) {
        if (node == null) {
            return false;
        }
        if (p.equals(node.p)) {
            return true;
        }
        boolean localResult;
        if (!horizontal) {
            if (p.x() < node.p.x()) {
                localResult = contains(node.lb, p, false);
            } else {
                localResult = contains(node.rt, p, false);
            }
        } else {
            if (p.y() < node.p.y()) {
                localResult = contains(node.lb, p, true);
            } else {
                localResult = contains(node.rt, p, true);
            }
        }
        return localResult;
    }

    public void draw() {
        StdDraw.setPenColor(StdDraw.BLACK);
        StdDraw.setPenRadius(.01);
//        StdDraw.show(0);
        drawNodePoint(root, false);
//        StdDraw.show(0);

    }

    private void drawNodePoint(Node node, boolean horizontal) {
        if (node == null) {
            return;
        }
        drawNodePoint(node.lb, !horizontal);
        drawNodePoint(node.rt, !horizontal);
        StdDraw.setPenColor(StdDraw.BLACK);
        node.p.draw();
        StdDraw.show(0);
    }

    public Iterable<Point2D> range(RectHV rect) {
        Set<Point2D> result = new HashSet<>();
        range(root, rect, result);
        return result;
    }

    private void range(Node node, RectHV rect, Set<Point2D> points) {
        if (node == null || !rect.intersects(node.rect)) {
            return;
        }

        range(node.lb, rect, points);
        range(node.rt, rect, points);

        if (rect.contains(node.p)) {
            points.add(node.p);
        }
    }

    public Point2D nearest(Point2D p) {
        return nearest(root, p, root.p, false);
    }

    private Point2D nearest(Node node, Point2D p, Point2D candidate, boolean horizontal) {
        if (node == null) {
            return candidate;
        }
        double subDist = node.rect.distanceSquaredTo(p);
        if (p.distanceSquaredTo(candidate) < subDist && !node.rect.contains(p)) {
            return candidate;
        }
        Node first = node.rt;
        Node second = node.lb;
        if (!horizontal && p.x() < node.p.x() || horizontal && p.y() < node.p.y()) {
            first = node.lb;
            second = node.rt;
        }
        if (node.p.distanceSquaredTo(p) < candidate.distanceSquaredTo(p)) {
            candidate = node.p;
        }
        candidate = nearest(first, p, candidate, horizontal);
        candidate = nearest(second, p, candidate, horizontal);
        return candidate;
    }

    public static void main(String[] args) {
        System.out.println("tests");
        In in = new In(args[0]);
        KdTree tree = new KdTree();
//        tree.insert(new Point2D(0.5, 0.5));
//        tree.insert(new Point2D(.2, .2));
//        tree.insert(new Point2D(.2, .2));
//        tree.insert(new Point2D(.2, .2));
        while (!in.isEmpty()) {
            double x = in.readDouble();
            double y = in.readDouble();
            Point2D p = new Point2D(x, y);
            tree.insert(p);
        }
        System.out.println("size  " + tree.size());
        tree.draw();
    }

}
