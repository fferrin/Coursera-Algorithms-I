
import java.util.Stack;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.StdOut;

public class Solver {
    private SearchNode last;
    private int        totalMoves;
    private boolean    isSolvable;

    private class SearchNode implements Comparable<SearchNode> {
        private Board      board;
        private int        priority;
        private SearchNode prev;
        private int        moves;

        public SearchNode(Board b, SearchNode prev, int moves) {
            board      = b;
            priority   = b.manhattan() + moves;
            this.prev  = prev;
            this.moves = moves;
        }

        public int compareTo(SearchNode s) {
            return Integer.compare(this.priority, s.priority);
        }
    }

    // find a solution to the initial board (using the A* algorithm)
    public Solver(Board initial) {
        if (initial == null)
            throw new NullPointerException("null argument");

        MinPQ<SearchNode> minPQSearchNode = new MinPQ<>();
        MinPQ<SearchNode> minPQSearchTwin = new MinPQ<>();
        SearchNode s     = new SearchNode(initial, null, 0);
        SearchNode sTwin = new SearchNode(initial.twin(), null, 0);

        minPQSearchNode.insert(s);
        minPQSearchTwin.insert(sTwin);

        while (true) {
            s     = minPQSearchNode.delMin();
            sTwin = minPQSearchTwin.delMin();

            if (s.board.isGoal()) {
                last       = s;
                totalMoves = s.moves;
                isSolvable = true;
                break;
            } else if (sTwin.board.isGoal()) {
                last       = null;
                totalMoves = -1;
                isSolvable = false;
                break;
            }
            enqueueNodes(s, minPQSearchNode);
            enqueueNodes(sTwin, minPQSearchTwin);
        }
    }

    private void enqueueNodes(SearchNode sn, MinPQ<SearchNode> minPQ) {
        for (Board b : sn.board.neighbors()) {
            if ((sn.prev == null) || !(b.equals(sn.prev.board)))
                minPQ.insert(new SearchNode(b, sn, sn.moves + 1));
        }
    }

    // is the initial board solvable?
    public boolean isSolvable() {
        return isSolvable;
    }

    // min number of moves to solve initial board; -1 if unsolvable
    public int moves() {
        return totalMoves;
    }

    // sequence of boards in a shortest solution; null if unsolvable
    public Iterable<Board> solution() {
        if (!isSolvable)
            return null;
        else {
            Stack<Board> solution = new Stack<>();

            SearchNode s = last;
            while (s != null) {
                solution.push(s.board);
                s = s.prev;
            }
            return solution;
        }
    }

    // solve a slider puzzle (given below)
    public static void main(String[] args) {
        // create initial board from file
        In in = new In(args[0]);
        int N = in.readInt();
        int[][] blocks = new int[N][N];
        for (int i = 0; i < N; i++)
            for (int j = 0; j < N; j++)
                blocks[i][j] = in.readInt();
        Board initial = new Board(blocks);

        // solve the puzzle
        Solver solver = new Solver(initial);

        // print solution to standard output
        if (!solver.isSolvable())
            StdOut.println("No solution possible");
        else {
            StdOut.println("Minimum number of moves = " + solver.moves());
            for (Board board : solver.solution())
                StdOut.println(board);
        }
    }
}
