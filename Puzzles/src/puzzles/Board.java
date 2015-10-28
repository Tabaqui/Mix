/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package puzzles;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.Queue;

/**
 *
 * @author User
 */
public class Board {

    private int[] board;
    private int moves;
    private final int dimension;
    private int[] goal;
    private Board twin;
    private int zero;
    


    public Board(int[][] blocks) {
        dimension = blocks.length;
        board = new int[dimension * dimension];
        goal = new int[dimension * dimension];
        for (int i = 0; i < dimension; i++) {
            for (int j = 0; j < dimension; j++) {
                if (blocks[i][j] == 0) {
                    zero = i * dimension + j;
                }
                board[i * dimension + j] = blocks[i][j];
                goal[i * dimension + j] = i * dimension + j + 1;
            }
        }
        goal[dimension * dimension - 1] = 0;
    }

    public int dimension() {
        return dimension;
    }

    public int hamming() {
        int sum = 0;
        for (int i = 0; i < dimension * dimension; i++) {
            if (board[i] != 0 && board[i] != goal[i]) {
                sum++;
            }
        }
        return sum;
    }

    public int manhattan() {
        int sum = 0;
        for (int i = 0; i < dimension * dimension; i++) {
            if (board[i] != 0 && board[i] != goal[i]) {
                sum += Math.abs(i - board[i] + 1) / dimension + Math.abs(i - board[i] + 1) % dimension;
            }
        }
        return sum;
    }

    public boolean isGoal() {
        return Arrays.equals(board, goal);
    }

    public Board twin() {
        twin = new Board(new int[dimension][dimension]);
        twin.board = Arrays.copyOf(this.board, this.board.length);
        twin.goal = Arrays.copyOf(this.goal, this.goal.length);
        twin.zero = this.zero;
        int a = 0;
        int b = 1;
        if (twin.board[a] == 0 || twin.board[b] == 0) {
            a += dimension;
            b += dimension;
        }
        twin.swap(a, b);
        return twin;
    }

    @Override
    public boolean equals(Object y) {
        if (this == y) {
            return true;
        }
        if (y == null) {
            return false;
        }
        if (this.getClass() != y.getClass()) {
            return false;
        }
        Board that = (Board) y;
        return Arrays.equals(this.board, that.board) ;
    }

    public Iterable<Board> neighbors() {
        Queue<Board> neighbors = new LinkedList<>();
        int left = -1;
        if (zero > 0 && zero % dimension != 0) {
            left = zero - 1;
        }
        int right = -1;
        if (zero + 1 != dimension * dimension && (zero + 1) % dimension != 0) {
            right = zero + 1;
        }
        int top = -1;
        if (zero - dimension >= 0) {
            top = zero - dimension;
        }
        int bottom = -1;
        if (zero + dimension < dimension * dimension) {
            bottom = zero + dimension;
        }

        if (bottom > 0) {
            neighbors.add(createBoard(bottom));
        }
        if (top >= 0) {
            neighbors.add(createBoard(top));
        }
        if (left >= 0) {
            neighbors.add(createBoard(left));
        }
        if (right > 0) {
            neighbors.add(createBoard(right));
        }
        return neighbors;
    }

    private Board createBoard(int toSwap) {
        Board neighbor = new Board(new int[dimension][dimension]);
        neighbor.board = Arrays.copyOf(this.board, this.board.length);
        neighbor.goal = Arrays.copyOf(this.goal, this.goal.length);
        neighbor.moves = this.moves + 1;
        neighbor.zero = toSwap;
        neighbor.swap(zero, toSwap);
        return neighbor;
    }

    private void swap(int a, int b) {
        int swap = this.board[a];
        this.board[a] = this.board[b];
        this.board[b] = swap;
    }

    @Override
    public String toString() {
        StringBuilder s = new StringBuilder();
        s.append(dimension).append("\n");
        for (int i = 0; i < dimension; i++) {
            for (int j = 0; j < dimension; j++) {
                s.append(String.format("%2d ", board[i * dimension + j]));
            }
            s.append("\n");
        }
        return s.toString();
    }

    public static void main(String[] args) {

        int[][] b = {{1, 2, 3}, {4, 5, 0}, {7, 8, 6}};
        Board result = new Board(b);
        System.out.println(result.manhattan());
    }
}
