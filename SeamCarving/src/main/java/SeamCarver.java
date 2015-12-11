
import edu.princeton.cs.algs4.Picture;
import edu.princeton.cs.algs4.StdOut;
import java.awt.Color;
import java.util.Arrays;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author User
 */
public class SeamCarver {

    private final Picture orig;
    private Picture current;
    private boolean horizontal;
    private double[][] distTo;
    private int[][][] edgeTo;

    public SeamCarver(Picture picture) {
        if (picture == null) {
            throw new NullPointerException();
        }
        this.orig = new Picture(picture);
        this.current = new Picture(this.orig);
        this.distTo = new double[this.orig.height()][this.orig.width()];
        this.edgeTo = new int[this.orig.height()][this.orig.width()][2];
    }

    public Picture picture() {
        return current;
    }

    public int width() {
        return current.width();
    }

    public int height() {

        return current.height();
    }

    public double energy(int x, int y) {
        if (x < 0 || y < 0 || x > width() - 1 || y > height() - 1) {
            throw new IndexOutOfBoundsException();
        }
        if (x == 0 || y == 0 || x == width() - 1 || y == height() - 1) {
            return 1000;
        }
        return Math.sqrt((double) getDX(x, y) + (double) getDY(x, y));
    }

    private int getDX(int x, int y) {
        int rx = getColor(x + 1, y, Color.RED) - getColor(x - 1, y, Color.RED);
        int gx = getColor(x + 1, y, Color.GREEN) - getColor(x - 1, y, Color.GREEN);
        int bx = getColor(x + 1, y, Color.BLUE) - getColor(x - 1, y, Color.BLUE);
        return rx * rx + gx * gx + bx * bx;
    }

    private int getDY(int x, int y) {
        int ry = getColor(x, y + 1, Color.RED) - getColor(x, y - 1, Color.RED);
        int gy = getColor(x, y + 1, Color.GREEN) - getColor(x, y - 1, Color.GREEN);
        int by = getColor(x, y + 1, Color.BLUE) - getColor(x, y - 1, Color.BLUE);
        return ry * ry + gy * gy + by * by;
    }

    private int getColor(int x, int y, Color c) {
        Color result = current.get(x, y);
        if (c.equals(Color.RED)) {
            return result.getRed();
        }
        if (c.equals(Color.BLUE)) {
            return result.getBlue();
        }
        if (c.equals(Color.GREEN)) {
            return result.getGreen();
        }
        return -1;
    }

    public int[] findHorizontalSeam() {
        if (!horizontal) {
            horizontal = !horizontal;
            current = transposePicture(current);
            this.distTo = new double[height()][width()];
            this.edgeTo = new int[height()][width()][2];
        }
        for (int i = 0; i < width(); i++) {
            distTo[0][i] = 1000;
        }
        return findSeam();
    }

    private int[] edges(int col, int row) {
        if (col - 1 >= 0 && col + 1 < width()) {
            int[] result = {col - 1, row + 1, col, row + 1, col + 1, row + 1};
            return result;
        } else if (col - 1 >= 0) {
            int[] result = {col - 1, row + 1, col, row + 1};
            return result;
        } else if (col + 1 < width()) {
            int[] result = {col, row + 1, col + 1, row + 1};
            return result;
        }
        int[] result = {col, row + 1};
        return result;
    }

    private void relax(int col, int row, int colFrom, int rowFrom) {
        if (distTo[row][col] > distTo[rowFrom][colFrom] + energy(col, row)) {
            distTo[row][col] = distTo[rowFrom][colFrom] + energy(col, row);
            edgeTo[row][col][0] = rowFrom;
            edgeTo[row][col][1] = colFrom;
        }
    }

    private Picture transposePicture(Picture p) {
        Picture temp = new Picture(height(), width());
        for (int i = 0; i < height(); i++) {
            for (int j = 0; j < width(); j++) {
                temp.set(i, j, new Color(p.get(j, i).getRGB()));
            }
        }
        return temp;
    }

    public int[] findVerticalSeam() {
        if (horizontal) {
            horizontal = !horizontal;
            current = transposePicture(current);
            this.distTo = new double[height()][width()];
            this.edgeTo = new int[height()][width()][2];
        }
        for (int i = 0; i < width(); i++) {
            distTo[0][i] = 1000;
        }
        return findSeam();
    }

    private int[] findSeam() {

        for (int row = 0; row < height() - 1; row++) {
            for (int col = 0; col < width(); col++) {
                int[] e = edges(col, row);
                for (int i = 0; i < e.length; i += 2) {
                    int colTo = e[i];
                    int rowTo = e[i + 1];
                    if (distTo[rowTo][colTo] == 0) {
                        distTo[rowTo][colTo] = Double.POSITIVE_INFINITY;
                    }
                    relax(colTo, rowTo, col, row);
                }
            }
        }
        double minDist = Double.POSITIVE_INFINITY;
        int col = 0;
        for (int i = 0; i < width(); i++) {
            if (minDist > distTo[height() - 1][i]) {
                minDist = distTo[height() - 1][i];
                col = i;
            }
        }
        int[] result = new int[height()];
        for (int i = height() - 1; i >= 0; i--) {
            result[i] = col;
            col = edgeTo[i][col][1];

        }
        return result;
    }

    public void removeHorizontalSeam(int[] seam) {
        if (seam == null) {
            throw new NullPointerException();
        }
        if (horizontal) {
            horizontal = !horizontal;
            current = transposePicture(current);
            this.distTo = new double[height()][width()];
            this.edgeTo = new int[height()][width()][2];
        }
        System.out.println(width());
        if (height() <= 1 || seam.length != width()) {
            throw new IllegalArgumentException();
        }
        int cache = seam[0];
        for (int i : seam) {
            if ((i - cache) / 2 > 0) {
                throw new IllegalArgumentException();
            }
            if (i < 0 || i >= height()) {
                throw new IndexOutOfBoundsException();
            }
            cache = i;
        }
        Picture temp = new Picture(width(), height() - 1);
        for (int i = 0; i < width(); i++) {
            for (int j = 0; j < seam[i]; j++) {
                temp.set(i, j, new Color(current.get(i, j).getRGB()));
            }
            for (int j = seam[i] + 1; j < height(); j++) {
                temp.set(i, j - 1, new Color(current.get(i, j).getRGB()));
            }
        }
        current = temp;
    }

    public void removeVerticalSeam(int[] seam) {
        if (seam == null) {
            throw new NullPointerException();
        }
        if (horizontal) {
            horizontal = !horizontal;
            current = transposePicture(current);
            this.distTo = new double[height()][width()];
            this.edgeTo = new int[height()][width()][2];
        }
        if (width() <= 1 || seam.length != height()) {
            throw new IllegalArgumentException();
        }
        int cache = seam[0];
        for (int i : seam) {
            if ((i - cache) / 2 > 0) {
                throw new IllegalArgumentException();
            }
            if (i < 0 || i >= width()) {
                throw new IndexOutOfBoundsException();
            }
            cache = i;
        }
        Picture temp = new Picture(width() - 1, height());
        for (int i = 0; i < height(); i++) {
            for (int j = 0; j < seam[i]; j++) {
                temp.set(j, i, new Color(current.get(j, i).getRGB()));
            }
            for (int j = seam[i] + 1; j < width(); j++) {
                temp.set(j - 1, i, new Color(current.get(j, i).getRGB()));
            }
        }
        current = temp;
    }

    public static void main(String[] args) {
        Picture p = new Picture(args[0]);
//        printSeams(p);
        SeamCarver sc = new SeamCarver(p);
//        System.out.println(Arrays.toString(sc.findHorizontalSeam()));
        sc.removeHorizontalSeam(sc.findHorizontalSeam());
//        printSeams(sc.current);
        System.out.println(sc.current.width());
        System.out.println(sc.current.height());
    }

//    public static void printSeams(Picture picture) {
//        StdOut.printf("image is %d pixels wide by %d pixels high.\n", picture.width(), picture.height());
//
//        SeamCarver sc = new SeamCarver(picture);
//
//        StdOut.printf("Printing energy calculated for each pixel.\n");
//
//        for (int j = 0; j < sc.height(); j++) {
//            for (int i = 0; i < sc.width(); i++) {
//                StdOut.printf("%9.0f ", sc.energy(i, j));
//            }
//            StdOut.println();
//        }
//    }
}
