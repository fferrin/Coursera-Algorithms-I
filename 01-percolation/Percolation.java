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


import java.lang.IllegalArgumentException;
import java.lang.IndexOutOfBoundsException;

import edu.princeton.cs.algs4.WeightedQuickUnionUF;


public class Percolation {
    private int[][] grid;
    private int     size;
    private int     upperVirtualSite;
    private int     bottomVirtualSite;
    private WeightedQuickUnionUF wQUF;

    /**
    * Create N-by-N grid, with all sites blocked. The grid have two virtual
    * sites, one at the top and the other at the bottom, that help to check
    * percolation.
    * @param N the dimension of the grid
    * @throws IllegalArgumentException if N is less or equals than 0
    */
    public Percolation(int N) {
        if (N <= 0)
            throw new llegalArgumentException("N <= 0.");
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
            throw new IndexOutOfBoundsException("Row out.");
        else if (j > size || j < 1)
            throw new IndexOutOfBoundsException("Column out.");
    }

    // convert matrix indices to array index
    private int xyTo1D(int i, int j) {
        return (size * (i - 1) + j);
    }

    /**
    * Open site if it's not open already and connect it with its neighborhoods
    * if ther are open too.
    * @param i the row
    * @param j the column
    * @throws IndexOutOfBoundsException indeces are out of bounds
    */
    public void open(int i, int j) {
        validateIndices(i, j);

        // if site isn't open
        if (!isOpen(i, j)) {
            grid[i][j] = 1;

            // connect to left site if it's open
            if (validSite(i, j - 1) && isOpen(i, j - 1))
                wQUF.union(xyTo1D(i, j - 1), xyTo1D(i, j));

            // connect to right site if it's open
            if (validSite(i, j + 1) && isOpen(i, j + 1))
                wQUF.union(xyTo1D(i, j + 1), xyTo1D(i, j));

            // connect to upper site if it's open
            if (validSite(i - 1, j) && isOpen(i - 1, j))
            wQUF.union(xyTo1D(i - 1, j), xyTo1D(i, j));

            // connect to lower site if it's open
            if (validSite(i + 1, j) && isOpen(i + 1, j))
            wQUF.union(xyTo1D(i + 1, j), xyTo1D(i, j));

            // if actual site is on top, connect with upper virtual site
            if (i == 1) wQUF.union(xyTo1D(i, j), upperVirtualSite);

            // if actual site is on bottom, connect with bottom virtual site
            if (i == size) wQUF.union(xyTo1D(i, j), bottomVirtualSite);
        }
    }

    /**
    * Check if site is open
    * @param i the row
    * @param j the column
    * @return true if site is open. False otherwise
    * @throws IndexOutOfBoundsException indeces are out of bounds
    */
    public boolean isOpen(int i, int j) {
        validateIndices(i, j);
        return (grid[i][j] == 1);
    }

    /**
    * Check if site is open (i.e., if site is an open site that can be connected
    * to an open site in the top row via a chain of neighboring open sites)
    * @param i the row
    * @param j the column
    * @return true if site is full. False otherwise
    * @throws IndexOutOfBoundsException indeces are out of bounds
    */
    public boolean isFull(int i, int j) {
        validateIndices(i, j);
        return wQUF.connected(xyTo1D(i, j), upperVirtualSite);
    }

    /**
    * Check if system percolates
    * @return true if system percolates. False otherwise
    */
    // does the system percolate?
    public boolean percolates() {
        return wQUF.connected(upperVirtualSite, bottomVirtualSite);
    }

    /**
     * Unit testing
     */
    // public static void main(String[] args) { // test client (optional)
    //     Percolation p = new Percolation(10);
    //
    //     p.validateIndices(Integer.parseInt(args[0]), Integer.parseInt(args[1]));
    // }
}
