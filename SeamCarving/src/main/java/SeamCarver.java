
import edu.princeton.cs.algs4.Picture;
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
        this.orig = new Picture(picture);
        this.current = new Picture(this.orig);
        this.distTo = new double[this.orig.height()][this.orig.width()];
//        for (int i = 0; i < this.orig.width(); i++) {
//            distTo[0][i] = 1000;
//            distTo[this.orig.height() - 1][i] = 1000;
//        }
//        for (int i = 0; i < this.orig.height(); i++) {
//            distTo[i][0] = 1000;
//            distTo[i][this.orig.width() - 1] = 1000;
//        }
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

    private Picture transposePicture(Picture p) {
        Picture temp = new Picture(height(), width());
        for (int i = 0; i < height(); i++) {
            for (int j = 0; j < width(); j++) {
                temp.set(i, j, new Color(p.get(j, i).getRGB()));
            }
        }
        return temp;
    }

//    private double[][] transposeMatrix(double[][] m) {
//        double[][] temp = new double[m[0].length][m.length];
//        for (int i = 0; i < m.length; i++) {
//            for (int j = 0; j < m[0].length; j++) {
//                temp[j][i] = m[i][j];
//            }
//        }
//        return temp;
//    }
//    private static int[][][] transposeMatrix(int[][][] m) {
//        System.out.println(m[0].length);
//        System.out.println(m.length);
//        int[][][] temp = new int[m[0].length][m.length][2];
//        for (int i = 0; i < m.length; i++) {
//            for (int j = 0; j < m[0].length; j++) {
//                temp[j][i][0] = m[i][j][0];
//                temp[j][i][1] = m[i][j][1];
//            }
//        }
//        return temp;
//    }
    public int[] findVerticalSeam() {

//        System.out.println(Arrays.deepToString(distTo));
        if (horizontal) {
            horizontal = !horizontal;
            current = transposePicture(current);
            this.distTo = new double[height()][width()];
            this.edgeTo = new int[height()][width()][2];
        }
        for (int i = 0; i < width(); i++) {
            distTo[0][i] = 1000;
//            distTo[this.orig.height() - 1][i] = 1000;
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
        Picture temp = new Picture(height(), width() - 1);
        for (int i = 0; i < width() - 1; i++) {
            for (int j = 0; j < seam[i]; j++) {
                temp.set(j, i, new Color(current.get(j, i).getRGB()));
            }
            for (int j = seam[i] + 1; j < width() - 1; j++) {
                temp.set(j, i, new Color(current.get(j, i).getRGB()));
            }
        }
        current = temp;
    }

    public void removeVerticalSeam(int[] seam) {
        Picture temp = new Picture(height() - 1, width());
        for (int i = 0; i < height() - 1; i++) {
            for (int j = 0; j < seam[i]; j++) {
                temp.set(i, j, new Color(current.get(i, j).getRGB()));
            }
            for (int j = seam[i] + 1; j < width() - 1; j++) {
                temp.set(i, j, new Color(current.get(i, j).getRGB()));
            }
        }
        current = temp;
    }

    public static void main(String[] args) {
//        int[][][] test = new int[3][2][2];
//        test[0][0][0] = 0;
//        test[0][0][1] = 1;
//        test[0][1][0] = 2;
//        test[0][1][1] = 3;
//        test[1][0][0] = 4;
//        test[1][0][1] = 5;
//        test[1][1][0] = 6;
//        test[1][1][1] = 7;
//        test[2][0][0] = 8;
//        test[2][0][1] = 9;
//        test[2][1][0] = 10;
//        test[2][1][1] = 11;
//        System.out.println(Arrays.deepToString(test));
//        test = transposeMatrix(test);
//        System.out.println(Arrays.deepToString(test));

        testSeam(args);
    }

    private static void testSeam(String[] args) {
        Picture picture = new Picture(args[0]);
//        picture.show();
        SeamCarver sc = new SeamCarver(picture);
//
//        sc.findHorizontalSeam();
//        sc.current.show();
//        SCUtility.showEnergy(sc);
        SCUtility.seamOverlay(picture, false, sc.findVerticalSeam()).show();
        SCUtility.seamOverlay(picture, true, sc.findHorizontalSeam()).show();
    }

}
