/******************************************************************************
 *  Compilation:  javac-algs4 PercolationStats.java
 *  Execution:    java PercolationStats N T
 *  Dependencies: StdStats.java StdRandom.java Percolation.java
 *
 *  This ṕrogram takes the size of the grid N and the times you want
 *  to repeat the experiment T and calculate the mean, deviation and
 *  the confidence interval of the threshold value.
 *
 ******************************************************************************/

import edu.princeton.cs.algs4.StdStats;
import edu.princeton.cs.algs4.StdRandom;

public class PercolationStats {
    private double[]      res;
    private int           valN;
    private int           valT;
    private Percolation[] p;

    // perform T independent experiments on an N-by-N grid
    public PercolationStats(int N, int T) {
        if (N <= 0 || T <= 0)
            throw new java.lang.IllegalArgumentException("N <= 0 or T <= 0.");

        valT   = T;
        valN   = N;
        res    = new double[valT];
        p      = new Percolation[valN];
    }

    // sample mean of percolation threshold
    public double mean() {
        return StdStats.mean(res);
    }

    // sample standard deviation of percolation threshold
    public double stddev() {
        return StdStats.stddev(res);
    }

    // low  endpoint of 95% confidence interval
    public double confidenceLo() {
        return (mean() - 1.96 * stddev() / Math.sqrt(valT));
    }

    // high endpoint of 95% confidence interval
    public double confidenceHi() {
        return (mean() + 1.96 * stddev() / Math.sqrt(valT));
    }

    // test client (described below)
    public static void main(String[] args) {
        if (args.length != 2) {
            System.out.println("Usage 'java-algs4 PercolationStats N T'.");
        }
        else {
            PercolationStats pStats;
            int n = Integer.parseInt(args[0]);
            int t = Integer.parseInt(args[1]);
            pStats = new PercolationStats(n, t);
            int i, j;

            for (int k = 0; k < pStats.valT; k++) {
                int counter = 0;

                while (!pStats.p[k].percolates()) {
                    i = StdRandom.uniform(pStats.valN) + 1;
                    j = StdRandom.uniform(pStats.valN) + 1;

                    if (!pStats.p[k].isOpen(i, j)) {
                        pStats.p[k].open(i, j);
                        counter = counter + 1;
                    }
                }

                pStats.res[k] = counter * 1.0 / (pStats.valN * pStats.valN);
            }

            double hi  = pStats.confidenceHi();
            double lo  = pStats.confidenceLo();

            System.out.format("mean                    = %.16f\n", pStats.mean());
            System.out.format("stddev                  = %.16f\n", pStats.stddev());
            System.out.format("95%% confidence interval = %.16f, %.16f\n", lo, hi);
        }
    }
}
