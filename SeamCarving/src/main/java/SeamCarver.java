
import edu.princeton.cs.algs4.Picture;
import java.awt.Color;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
//    private int[][] energy;
    private double[][] distTo;
    private int[][][] edgeTo;

    public SeamCarver(Picture picture) {
        this.orig = new Picture(picture);
        this.current = new Picture(this.orig);
//        this.energy = new int[this.orig.width()][this.orig.height()];
        this.distTo = new double[this.orig.height()][this.orig.width()];
        for (int i = 0; i < this.orig.width(); i++) {
            distTo[0][i] = 1000;
        }
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
        return null;
    }

    private int[] edges(int col, int row) {
        if (col == 0) {
            int[] result = {col, row + 1, col + 1, row + 1};
            return result;
        }
        if (col == width() - 1) {
            int[] result = {col - 1, row + 1, col, row + 1};
            return result;
        }
        int[] result = {col - 1, row + 1, col, row + 1, col + 1, row + 1};
        return result;
    }

    private void relax(int col, int row, int colFrom, int rowFrom) {
        if (distTo[row][col] > distTo[rowFrom][colFrom] + energy(col, row)) {
            distTo[row][col] = distTo[rowFrom][colFrom] + energy(col, row);
            edgeTo[row][col][0] = rowFrom;
            edgeTo[row][col][1] = colFrom;
        }
    }

    public int[] findVerticalSeam() {
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
    }

    public void removeVerticalSeam(int[] seam) {
    }

}
