
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.StdDraw;
import static edu.princeton.cs.algs4.StdDraw.point;
import edu.princeton.cs.algs4.StdStats;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

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
//        if (contains(p)) {
//            return;
//        }
        if (size == 0) {
            root = new Node(p, new RectHV(0.0, 0.0, 1.0, 1.0));
            size = 1;
            return;
        }
        insert(root, p, new RectHV(0.0, 0.0, 1.0, 1.0), false);
    }

    private Node insert(Node node, Point2D p, RectHV rect, boolean horizontal) {
        if (node == null) {
            size++;
            return new Node(p, rect);
        }
        if (node.p.equals(p)) {
            return node;
        }
        RectHV newRect;
        if (!horizontal && p.x() < node.p.x() || horizontal && p.y() < node.p.y()) {
            if (node.lb == null) {
                newRect = getRect(p, node, horizontal);
                node.lb = insert(node.lb, p, newRect, !horizontal);
            } else {
                node.lb = insert(node.lb, p, node.lb.rect, !horizontal);
            }
        } else {
            if (node.rt == null) {
                newRect = getRect(p, node, horizontal);
                node.rt = insert(node.rt, p, newRect, !horizontal);
            } else {
                node.rt = insert(node.rt, p, node.rt.rect, !horizontal);
            }
        }

        return node;
    }

    private RectHV getRect(Point2D p, Node node, boolean horizontal) {
        RectHV result;
        if (!horizontal) {
            if (p.x() < node.p.x()) {
                result = new RectHV(node.rect.xmin(), node.rect.ymin(), node.p.x(), node.rect.ymax());
            } else if (p.x() == node.p.x()) {
                result = new RectHV(node.rect.xmin(), node.rect.ymin(), node.rect.xmax(), node.rect.ymax());
            } else {
                result = new RectHV(node.p.x(), node.rect.ymin(), node.rect.xmax(), node.rect.ymax());
            }
        } else {
            if (p.y() < node.p.y()) {
                result = new RectHV(node.rect.xmin(), node.rect.ymin(), node.rect.xmax(), node.p.y());
            } else if (p.y() == node.p.y()) {
                result = new RectHV(node.rect.xmin(), node.rect.ymin(), node.rect.xmax(), node.rect.ymax());
            } else {
                result = new RectHV(node.rect.xmin(), node.p.y(), node.rect.xmax(), node.rect.ymax());
            }
        }
        return result;
    }

    public boolean contains(Point2D p) {
        return contains(root, p, false);
    }

    private boolean contains(Node node, Point2D p, boolean horizontal) {
        if (node == null) {
            return false;
        }
        if (p.equals(node.p)) {
            return true;
        }
        boolean localResult;
        if (!horizontal && p.x() < node.p.x() || horizontal && p.y() < node.p.y()) {
            localResult = contains(node.lb, p, !horizontal);
        } else {
            localResult = contains(node.rt, p, !horizontal);
        }

        return localResult;
    }

    public void draw() {
        StdDraw.setPenColor(StdDraw.BLACK);
        StdDraw.setPenRadius(.01);
        StdDraw.show(0);
        drawNodePoint(root, false);
        StdDraw.show(0);

    }

    private void drawNodePoint(Node node, boolean horizontal) {
        if (node == null) {
            return;
        }
        StdDraw.setPenColor(StdDraw.BLACK);
        node.p.draw();
        StdDraw.show(0);

//        StdDraw.setPenColor(StdDraw.BLUE);
//        node.rect.draw();
        StdDraw.show(0);

        drawNodePoint(node.lb, !horizontal);
        drawNodePoint(node.rt, !horizontal);

        node.rect.draw();

    }

    public Iterable<Point2D> range(RectHV rect) {
        Set<Point2D> result = new TreeSet<>();
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
        if (size == 0) {
            return null;
        }
        return nearest(root, p, root.p, false);
    }

    private Point2D nearest(Node node, Point2D p, Point2D candidate, boolean horizontal) {
        if (node == null) {
            System.out.println("null get");
            return candidate;
        }
        double subdist = p.distanceSquaredTo(candidate);
        System.out.println(" candidate -- " + candidate.toString());
        System.out.println(" point     -- " + node.p.toString());
        if (p.distanceSquaredTo(candidate) <= node.rect.distanceSquaredTo(p)) {
            System.out.println("distance to candidate <");
            return candidate;
        }

//        if (node.lb == null && node.rt == null) {
//            return candidate;
//        }
        System.out.println("distance to candidate >");
        Node first = node.rt;
        Node second = node.lb;
        if (!horizontal && p.x() < node.p.x() || horizontal && p.y() < node.p.y()) {
            first = node.lb;
            second = node.rt;
        }
        if (node.p.distanceSquaredTo(p) < subdist) {
            candidate = node.p;
        }

        candidate = nearest(first, p, candidate, horizontal);
        candidate = nearest(second, p, candidate, horizontal);
        return candidate;
    }

    public static void main(String[] args) {
        int multiply = 1;

        List<Point2D> pointList = new ArrayList<>();
        KdTree tree = new KdTree();
        
        tree.insert(new Point2D(0.4, 0.4));
        tree.insert(new Point2D(0.2, 0.4));
        tree.insert(new Point2D(0, 0.2));
        tree.insert(new Point2D(0, 0));
        tree.nearest(new Point2D(.5, .5));
        
        
        
        

//        In in = new In(args[0]);
//        while (!in.isEmpty()) {
//            double x = in.readDouble();
//            double y = in.readDouble();
//            Point2D p = new Point2D(x, y);
//            pointList.add(p);
//            tree.insert(p);
//        }
        tree.nearest(new Point2D(0, 0));
//
////        tree.draw();
////
//        System.out.println("Kdtree size " + tree.size());
//        System.out.println("List size " + pointList.size());
//        for (Point2D point : pointList) {
//            double x = Math.random() * multiply;
//            double y = Math.random() * multiply;
////            System.out.println("Point " + x + " " + y);
//            if (tree.contains(point)) {
////                System.out.println("Point found " + point);
//            } else {
//                System.out.println("Point not found" + point);
//            }
//
//        }
    }

}
