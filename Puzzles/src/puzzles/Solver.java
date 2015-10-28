package puzzles;

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.StdOut;
import java.util.LinkedList;
import java.util.Objects;

public class Solver {

    private int moves = 0;
    private SearchNode initialNode;
    private boolean solvable;
    private LinkedList<Board> solution;

    private class SearchNode implements Comparable<SearchNode> {

        private int moves;
        private SearchNode previous;
        private Board board;
        private Integer manhattan;

        public SearchNode(Board board, int moves, SearchNode previous) {
            this.board = board;
            this.moves = moves;
            this.previous = previous;
        }

        @Override
        public int compareTo(SearchNode o) {
            return this.manhattan() - o.manhattan();
        }
        
        private int manhattan() {
            if (manhattan != null) {
                return manhattan;
            }
            manhattan = this.moves + this.board.manhattan();
            return manhattan;
        }

    }

    public Solver(Board initial) {
        solution = new LinkedList<>();
        solvable = true;
        initialNode = new SearchNode(initial, moves, null);
        SearchNode initialTwinNode = new SearchNode(initial.twin(), moves, null);
        SearchNode node = initialNode;
        MinPQ<SearchNode> pq = new MinPQ<>();
        MinPQ<SearchNode> twinPQ = new MinPQ<>();
        pq.insert(node);
        twinPQ.insert(initialTwinNode);
        int nodeMoves = 0;
        while (!node.board.isGoal()) {
            node = pq.delMin();
            SearchNode twinNode = twinPQ.delMin();
            nodeMoves++;
//            while(!pq.isEmpty()) {
//                pq.delMin();
//            }
//            while(!twinPQ.isEmpty()) {
//                twinPQ.delMin();
//            }
//            SearchNode nnn= null;
            for (Board b : node.board.neighbors()) {
                if (node.previous == null || !b.equals(node.previous.board)) {
//                    nnn = new SearchNode(b, nodeMoves, node);
                    pq.insert(new SearchNode(b, nodeMoves, node));
                }
            }
            for (Board b : twinNode.board.neighbors()) {
                if (twinNode.previous == null || !b.equals(twinNode.previous.board)) {
                    twinPQ.insert(new SearchNode(b, nodeMoves, twinNode));
                }
            }
            if (twinNode.board.isGoal()) {
                solvable = false;
                break;
            }
//            System.out.println(nodeMoves);
        }
        moves = 1;
        solution.add(node.board);
        while (node.previous != null) {
            solution.addFirst(node.previous.board);
            node = node.previous;
            moves++;  
        }
        
        
    }

    public boolean isSolvable() {
        return solvable;
    }

    public int moves() {
        if (!isSolvable()) {
            return -1;
        }
        if (moves != 0) {
            moves--;
        }
        return moves;
    }

    public Iterable<Board> solution() {
        if (!isSolvable()) {
            return null;
        }
        return solution;
    }

    public static void main(String[] args) {
        In in = new In(args[0]);
        int N = in.readInt();
        int[][] blocks = new int[N][N];
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                blocks[i][j] = in.readInt();
            }
        }
        Board initial = new Board(blocks);

        // solve the puzzle
        Solver solver = new Solver(initial);

        // print solution to standard output
        if (!solver.isSolvable()) {
            StdOut.println("No solution possible");
        } else {
            StdOut.println("Minimum number of moves = " + solver.moves());
            for (Board board : solver.solution()) {
                StdOut.println(board);
            }
        }
    }

}
