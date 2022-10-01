package hw2;

import edu.princeton.cs.algs4.WeightedQuickUnionUF;
public class Percolation {
    private int numberofopensites = 0;
    private WeightedQuickUnionUF site;
    private boolean[][] flagopen;
    public Percolation(int N) {
        if (N <= 0) {
            throw new IllegalArgumentException();
        }
        site = new WeightedQuickUnionUF(N * N);
        flagopen = new boolean[N][N];
    }
    private int xyTo1D(int r, int c) {
        return r * flagopen.length + c;
    }
    public void open(int row, int col) {
        if (!isvalid(row, col)) {
            throw new IndexOutOfBoundsException();
        }
        if (flagopen[row][col]) {
            return;
        }
        flagopen[row][col] = true;
        numberofopensites += 1;
        unionaround(row, col, row + 1, col);
        unionaround(row, col, row - 1, col);
        unionaround(row, col, row, col + 1);
        unionaround(row, col, row, col - 1);
    }
    private void unionaround(int row, int col, int nextrow, int nextcol) {
        if (!isvalid(row, col) || !isvalid(nextrow, nextcol)) {
            return;
        }
        if (flagopen[nextrow][nextcol]) {
            site.union(xyTo1D(row, col), xyTo1D(nextrow, nextcol));
        }
    }
    public boolean isOpen(int row, int col) {
        if (!isvalid(row, col)) {
            throw new IndexOutOfBoundsException();
        }
        return flagopen[row][col];
    }
    public boolean isFull(int row, int col) {
        if (!isvalid(row, col)) {
            throw new IndexOutOfBoundsException();
        }
        if (!isOpen(row, col)) {
            return false;
        } else {
            for (int i = 0; i < flagopen.length; i += 1) {
                if (site.connected(i, xyTo1D(row, col))) {
                    return true;
                }
            }
        }
        return false;
    }
    private boolean isvalid(int row, int col) {
        int height = flagopen.length;
        return row >= 0 && row < height && col >= 0 && col < height;
    }
    public int numberOfOpenSites() {
        return numberofopensites;
    }
    public boolean percolates() {
        int height = flagopen.length - 1;
        for (int col = 0; col <= height; col += 1) {
            if (isFull(height, col)) {
                return true;
            }
        }
        return false;
    }
    public static void main(String[] args) {
    }
}
