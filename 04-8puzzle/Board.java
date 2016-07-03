
import java.util.Arrays;
import java.util.Stack;
import edu.princeton.cs.algs4.In;

public class Board {
    private int[][] board;
    private int     N;

    // construct a board from an N-by-N array of blocks
    // (where blocks[i][j] = block in row i, column j)
    public Board(int[][] blocks) {
        N     = blocks[0].length;
        board = new int[N][N];

        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++)
                board[i][j] = blocks[i][j];
        }

    }

    private int[][] deepCopy(int[][] b, int n) {
        int[][] res = new int[n][n];

        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                res[i][j] = b[i][j];

        return res;
    }

    // board dimension N
    public int dimension() {
        return N;
    }

    // number of blocks out of place
    public int hamming() {
        int sum = 0;

        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                if ((i == (N - 1)) && (j == (N - 1))) return sum;
                if (board[i][j] != (i * N + (j + 1))) sum++;
            }
        }
        return sum;
    }

    // sum of Manhattan distances between blocks and goal
    public int manhattan() {
        int sum = 0;
        int offset_row;
        int offset_col;

        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                if (0 < board[i][j]) {
                    offset_row = (board[i][j] - 1) / N;
                    offset_col = board[i][j] - offset_row * N - 1;
                    sum += Math.abs(i - offset_row) + Math.abs(j - offset_col);
                }
            }
        }
        return sum;
    }

    // is this board the goal board?
    public boolean isGoal() {
        return (manhattan() == 0);
    }

    private void swap(int[][] b, int i, int j, int m, int n) {
        int aux = b[i][j];
        b[i][j] = b[m][n];
        b[m][n] = aux;
    }

    // a board that is obtained by exchanging any pair of blocks
    public Board twin() {
        int[][] b = deepCopy(board, N);

        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N - 1; j++) {
                if (b[i][j] * b[i][j + 1] != 0) {
                    swap(b, i, j, i, j + 1);
                    return new Board(b);
                }
            }
        }
        return null;
    }

    // does this board equal y?
    public boolean equals(Object y) {
        if (y == this) return true;
        if (y == null) return false;
        if (y.getClass() != this.getClass()) return false;

        Board that = (Board) y;
        return (this.N == that.N) && Arrays.deepEquals(this.board, that.board);
    }

    // helper method to push neighbor board onto a stack
    private void pushBoardOntoStack(Stack<Board> s, int x, int y, int m, int n) {
        int[][] neighborBoard = deepCopy(board, N);
        swap(neighborBoard, x, y, m, n);
        s.push(new Board(neighborBoard));
    }

    // all neighboring boards
    public Iterable<Board> neighbors() {
        Stack<Board> boards     = new Stack<Board>();
        boolean      blankFound = false;

        for (int i = 0; i < N && !blankFound; i++) {
            for (int j = 0; j < N; j++) {
                if (board[i][j] == 0) {
                    if (i > 0)     pushBoardOntoStack(boards, i - 1, j, i, j);
                    if (j > 0)     pushBoardOntoStack(boards, i, j - 1, i, j);
                    if (i < N - 1) pushBoardOntoStack(boards, i + 1, j, i, j);
                    if (j < N - 1) pushBoardOntoStack(boards, i, j + 1, i, j);
                    blankFound = true;
                    break;
                }
            }
        }
        return boards;
    }

    // string representation of this board (in the output format specified below)
    // public String toString() {
    //     StringBuilder s = new StringBuilder();
    //     String NEW_LINE = System.getProperty("line.separator");

    //     s.append(String.format("%d" + NEW_LINE, N));
    //     for (int i = 0; i < N; i++) {
    //         for (int j = 0; j < N; j++) {
    //             if ((N / 100.0) > 1)
    //                 s.append(String.format("%4d", board[i][j]));
    //             else
    //                 s.append(String.format("%3d", board[i][j]));
    //         }

    //         s.append(NEW_LINE);
    //     }
    //     return s.toString();
    // }
    public String toString() {
        StringBuilder s = new StringBuilder();
        s.append(N + "\n");
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                s.append(String.format("%2d ", board[i][j]));
            }
            s.append("\n");
        }
        return s.toString();
    }

    // unit tests (not graded)
    public static void main(String[] args) {

        // create initial board from file
        In in = new In(args[0]);
        int N = in.readInt();
        int[][] blocks = new int[N][N];

        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++)
                blocks[i][j] = in.readInt();
        }

        Board initial = new Board(blocks);

        for (Board b : initial.neighbors())
            System.out.println(b);
    }
}
