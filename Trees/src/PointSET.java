

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.SET;
import edu.princeton.cs.algs4.StdDraw;
import java.util.LinkedList;
import java.util.List;

public class PointSET {

    private SET<Point2D> points;

    public PointSET() {
        this.points = new SET<>();
    }

    public boolean isEmpty() {
        return this.points.isEmpty();
    }

    public int size() {
        return points.size();
    }

    public void insert(Point2D p) {
        this.points.add(p);
    }

    public boolean contains(Point2D p) {
        return points.contains(p);
    }

    public void draw() {
        StdDraw.setPenColor(StdDraw.BLACK);
        StdDraw.setPenRadius(.001);
        for (Point2D p : this.points) {
            p.draw();
        }
        StdDraw.show();
    }

    public Iterable<Point2D> range(RectHV rect) {
        List<Point2D> result = new LinkedList<>();
        for (Point2D p : this.points) {
            if (rect.contains(p)) {
                result.add(p);
            }
        }
        return result;
    }

    public Point2D nearest(Point2D p) {
        if (this.points.isEmpty()) {
            return null;
        }
        Point2D result = null;
        double distSQR = -1;
        double next;
        for (Point2D p0 : this.points) {
            next = p.distanceSquaredTo(p0);
            if (next < distSQR || distSQR < 0) {
                distSQR = next;
                result = p0;
            }
        }
        return result;
    }

    public static void main(String[] args) {
        In in = new In(args[0]);
        PointSET brute = new PointSET();
        while (!in.isEmpty()) {
            double x = in.readDouble();
            double y = in.readDouble();
            Point2D p = new Point2D(x, y);
            brute.insert(p);
        }
        brute.draw();
    }
}
