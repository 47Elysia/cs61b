package hw4.puzzle;
import edu.princeton.cs.algs4.Queue;

public class Board implements WorldState{
    private int[][] tiles;
    private int N;
    private static int blank = 0;
    private int estimatedist;
    /**Constructs a board from an N-by-N array of tiles where
     * tiles[i][j] = tile at row i, column j*/
    public Board(int[][] tiles) {
        if (tiles == null || tiles[0] == null || tiles.length != tiles[0].length) {
            throw new IllegalArgumentException();
        }
        this.N = tiles.length;
        this.tiles = new int[N][N];
        estimatedist = -1;
        for (int i = 0; i < N; i += 1) {
            for (int j = 0; j < N; j += 1) {
                this.tiles[i][j] = tiles[i][j];
            }
        }
    }

    /**
     * Returns value of tile at row i, column j (or 0 if blank)*/
    public int tileAt(int i, int j) {
        validate(i, j);
        return tiles[i][j];
    }
    private void validate(int i, int j) {
        if (i >= N || j >= N || i < 0 || j < 0) {
            throw new IndexOutOfBoundsException();
        }
    }
    /*
    Returns the board size N
     */
    public int size() {
        return N;
    }
    /*
    Returns the neighbors of the current board
     */
    @Override
    public Iterable<WorldState> neighbors() {
        Queue<WorldState> neighbor = new Queue<>();
        int blankX = -1;
        int blankY = -1;
        for (int i = 0; i< N; i += 1) {
            for (int j = 0; j < N; j += 1) {
                if (tiles[i][j] == blank) {
                    blankX = i;
                    blankY = j;
                    break;
                }
            }
        }
        int[][] around = new int[N][N];
        for (int i = 0; i < N; i += 1) {
            for (int j = 0; j < N; j += 1) {
                around[i][j] =tileAt(i, j);
            }
        }
        for (int i = 0; i < N; i += 1) {
            for (int j = 0; j < N; j += 1) {
                if (Math.abs(blankX - i) + Math.abs(blankY - j) == 1) {
                    around[blankX][blankY] = around[i][j];
                    around[i][j] = blank;
                    neighbor.enqueue(new Board(around));
                    around[i][j] = around[blankX][blankY];
                    around[blankX][blankY] = blank;
                }
            }
        }
        return neighbor;
    }
    /*
    Hamming estimate described below
     */
    public int hamming() {
        int distance = 0;
        for (int i = 0; i < N; i += 1) {
            for (int j = 0; j < N; j += 1) {
                if (tiles[i][j] == 0) {
                    continue;
                }
                if (tiles[i][j] != i * N + j + 1) {
                    distance += 1;
                }
            }
        }
        return distance;
    }
    /*
    Manhattan estimate described below
     */
    public int manhattan() {
        int distance = 0;
        for (int i = 0; i < N; i += 1) {
            for (int j = 0; j < N; j += 1) {
                if (tiles[i][j] == 0) {
                    continue;
                }
                if (tiles[i][j] != i * N + j + 1) {
                    int x = (tiles[i][j] - 1) / N;
                    int y = (tiles[i][j] - 1) % N;
                    distance += Math.abs(x - i) + Math.abs(y - j);
                }
            }
        }
        return distance;
    }
    /*
     * Estimated distance to goal. This method should
     * simply return the results of manhattan() when submitted to
     * Gradescope.
     */
    public int estimatedDistanceToGoal() {
        if (estimatedist == -1) {
            estimatedist = manhattan();
        }
        return estimatedist;
    }
    /*
     *Returns true if this board's tile values are the same
     *position as y's
     */
    public boolean equals(Object y) {
        if (y == this) {
            return true;
        }
        if (y == null || y.getClass() != this.getClass()) {
            return false;
        }
        Board newy= (Board) y;
        if (newy.hamming() == this.hamming()) {
            return true;
        }
        return false;
    }
    /** Returns the string representation of the board. 
      * Uncomment this method. */
    public String toString() {
        StringBuilder s = new StringBuilder();
        int N = size();
        s.append(N + "\n");
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                s.append(String.format("%2d ", tileAt(i,j)));
            }
            s.append("\n");
        }
        s.append("\n");
        return s.toString();
    }

}
