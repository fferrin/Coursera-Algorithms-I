/******************************************************************************
 *  Compilation:  javac-algs4 Percolation.java
 *  Execution:    java-algs4 Percolation
 *  Dependencies: WeightedQuickUnionUF.java
 *
 *  This class have the methods to deal with percolation problem.
 *  It can open sites, check for open and full sites and if the
 *  system percolates.
 *
 ******************************************************************************/

import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {
    private int[][] grid;
    private int     size;
    private int     upperVirtualSite;
    private int     bottomVirtualSite;
    private WeightedQuickUnionUF wQUF;

    // create N-by-N grid, with all sites blocked
    public Percolation(int N) {
        if (N <= 0)
            throw new java.lang.IllegalArgumentException("N <= 0.");
        else {
            size = N;
            upperVirtualSite  = 0;
            bottomVirtualSite = size * size + 1;
            grid = new int[size + 1][size + 1];
            wQUF = new WeightedQuickUnionUF(size * size + 2);

            for (int i = 1; i <= size; i++) {
                for (int j = 1; j <= size; j++)
                    grid[i][j] = 0;
            }
        }
    }

    // check if site (row i, column j) is out of bounds
    private boolean validSite(int i, int j) {
        if (i > size || i < 1)
            return false;
        else
            return !(j > size || j < 1);
    }

    // validate indices
    private void validateIndices(int i, int j) {
        if (i > size || i < 1)
            throw new java.lang.IndexOutOfBoundsException("Row out.");
        else if (j > size || j < 1)
            throw new java.lang.IndexOutOfBoundsException("Column out.");
    }

    // convert matrix indices to array index
    private int xyTo1D(int i, int j) {
        return (size * (i - 1) + j);
    }

    // open site (row i, column j) if it is not open already
    public void open(int i, int j) {
        validateIndices(i, j);

        if (!isOpen(i, j)) {
            grid[i][j] = 1;

            // open left site
            if (validSite(i, j - 1) && isOpen(i, j - 1))
                wQUF.union(xyTo1D(i, j - 1), xyTo1D(i, j));

            // open right site
            if (validSite(i, j + 1) && isOpen(i, j + 1))
                wQUF.union(xyTo1D(i, j + 1), xyTo1D(i, j));

            // if actual site is on top, connect with upper virtual site
            if (i == 1) wQUF.union(xyTo1D(i, j), upperVirtualSite);

            // if actual site is on bottom, connect with bottom virtual site
            if (i == size) wQUF.union(xyTo1D(i, j), bottomVirtualSite);

            // open upper site
            if (validSite(i - 1, j) && isOpen(i - 1, j))
                wQUF.union(xyTo1D(i - 1, j), xyTo1D(i, j));

            // open lower site
            if (validSite(i + 1, j) && isOpen(i + 1, j))
                wQUF.union(xyTo1D(i + 1, j), xyTo1D(i, j));

            // open right site
            if (validSite(i, j + 1) && isOpen(i, j + 1))
                wQUF.union(xyTo1D(i, j + 1), xyTo1D(i, j));

            // open left site
            if (validSite(i, j - 1) && isOpen(i, j - 1))
                wQUF.union(xyTo1D(i, j - 1), xyTo1D(i, j));
        }
    }

    // is site (row i, column j) open?
    public boolean isOpen(int i, int j) {
        validateIndices(i, j);
        return (grid[i][j] == 1);
    }

    // is site (row i, column j) full?
    public boolean isFull(int i, int j) {
        validateIndices(i, j);
        return wQUF.connected(xyTo1D(i, j), upperVirtualSite);
    }

    // does the system percolate?
    public boolean percolates() {
        return wQUF.connected(upperVirtualSite, bottomVirtualSite);
    }

    /*
    public static void main(String[] args) { // test client (optional)
        Percolation p = new Percolation(10);

        p.validateIndices(Integer.parseInt(args[0]), Integer.parseInt(args[1]));
    }
    */
}
