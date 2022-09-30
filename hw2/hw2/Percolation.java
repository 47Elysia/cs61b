package hw2;

import edu.princeton.cs.algs4.WeightedQuickUnionUF;

import java.awt.*;
import java.util.HashSet;

public class Percolation {
    private int numberofopensites = 0;
    private boolean[][] grid;
    private boolean[][] full;
    private HashSet hashSet = new HashSet();
    public Percolation(int N) throws IllegalArgumentException {
       grid = new boolean[N][N];
       full = new boolean[N][N];
       hashSet.add(0);
    }
    public void open(int row, int col) throws IndexOutOfBoundsException {
        if (grid[row][col] == false) {
           grid[row][col] = true;
        }
    }
    public boolean isOpen(int row, int col) throws IndexOutOfBoundsException {
        numberofopensites += 1;
        return grid[row][col];
    }
    public boolean isFull(int row, int col) throws IndexOutOfBoundsException {
        if (!isOpen(row, col)) {
            return false;
        } else {
            if (row == 0) {
                full[row][col] = true;
                return true;
            }
            if (isvalid(row + 1, col) && full[row + 1][col]) {
                full[row][col] = true;
                return true;
            }
            if (isvalid(row - 1, col) && full[row - 1][col]) {
                full[row][col] = true;
                return true;
            }
            if (isvalid(row, col + 1) && full[row][col + 1]) {
                full[row][col] = true;
                return true;
            }
            if (isvalid(row, col - 1) && full[row][col - 1]) {
                full[row][col] = true;
                return true;
            }
        }
        return false;
    }
    private boolean isvalid(int row, int col) {
        int height = grid.length;
        return row >= 0 && row < height && col >= 0 && col < height;
    }
    public int numberOfOpenSites() {
        return numberofopensites;
    }
    public boolean percolates() {
        int height = grid.length - 1;
        for (int col = 0; col < grid.length; col += 1) {
            if (isFull(height, col)) {
                return true;
            }
        }
        return false;
    }


}
