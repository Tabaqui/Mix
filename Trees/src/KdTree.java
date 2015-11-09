
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.StdDraw;
import java.util.LinkedList;

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
        root = insert(root, p, new RectHV(0.0, 0.0, 1.0, 1.0), true);
    }

    private Node insert(Node node, Point2D p, RectHV rect, boolean horizontal) {
        if (node == null) {
            size++;
            return new Node(p, rect);
        }
//        System.out.println("--------------");
        if (horizontal) {
            if (leftThen(p, node.p)) {
                RectHV newRect = new RectHV(rect.xmin(), rect.ymin(), node.p.x(), rect.ymax());
                node.lb = insert(node.lb, p, newRect, false);
            } else {
                System.out.println(rect.toString());
                System.out.println(node.p.x() + " " + rect.ymin() + " " + rect.xmax() + " " + rect.ymax());
                RectHV newRect = new RectHV(node.p.x(), rect.ymin(), rect.xmax(), rect.ymax());
                node.rt = insert(node.rt, p, newRect, false);
            }
        } else {
            if (botThen(p, node.p)) {
                RectHV newRect = new RectHV(rect.xmin(), rect.ymin(), rect.xmax(), node.p.y());
                node.lb = insert(node.lb, p, newRect, true);
            } else {
                RectHV newRect = new RectHV(rect.xmin(), node.p.y(), rect.xmax(), rect.ymax());
                node.rt = insert(node.rt, p, newRect, true);
            }
        }
        return node;
    }

    private boolean leftThen(Point2D p1, Point2D p2) {
        return p1.x() < p2.y();
    }

    private boolean botThen(Point2D p1, Point2D p2) {
        return p1.y() < p2.y();
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
        if (horizontal) {
            if (leftThen(p, node.p)) {
                localResult = contains(node.lb, p, false);
            } else {
                localResult = contains(node.rt, p, false);
            }
        } else {
            if (botThen(p, node.p)) {
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
//        StdDraw.setPenColor(StdDraw.BLUE);
//        node.rect.draw();
        StdDraw.show(0);
    }

    public Iterable<Point2D> range(RectHV rect) {
        return new LinkedList<>();
    }// all points that are inside the rectangle 

    public Point2D nearest(Point2D p) {
        return null;
    }// a nearest neighbor in the set to point p; null if the set is empty 

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
