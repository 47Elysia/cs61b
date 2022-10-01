package hw2;
import edu.princeton.cs.introcs.StdRandom;
import edu.princeton.cs.introcs.StdStats;
public class PercolationStats {
    private double[] sample;
    private double T;
    public PercolationStats(int N, int T, PercolationFactory pf) {
        if (N <= 0 || T <= 0) {
            throw new IllegalArgumentException();
        }
        this.T = T;
        sample = new double[T];
        int x;
        int y;
        for (int i = 0; i < T; i += 1) {
            Percolation percolation = pf.make(N);
            while (!percolation.percolates()) {
                do {
                    x = StdRandom.uniform(N);
                    y = StdRandom.uniform(N);
                } while (percolation.isOpen(x, y));
                percolation.open(x, y);
            }
            sample[i] = (double) percolation.numberOfOpenSites() / (N * N);
        }
    }
    public double mean() {
        return StdStats.mean(sample);
    }
    public double stddev() {
        return StdStats.stddev(sample);
    }
    public double confidenceLow() {
        return mean() - 1.96 * Math.sqrt(stddev()) / Math.sqrt(T);
    }
    public double confidenceHigh() {
        return mean() + 1.96 * Math.sqrt(stddev()) / Math.sqrt(T);
    }




}
