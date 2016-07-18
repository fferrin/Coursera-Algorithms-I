/******************************************************************************
 *  Compilation:  javac-algs4 Subset.java
 *  Execution:    java-algs4 Subset k < set.txt
 *  Dependencies: StdIn.java StdOut.java
 *
 *  Client that takes k items from a set chosen uniformly at random
 *
 ******************************************************************************/


import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;


/**
 *  This class represent a client that takes a command-line integer k; reads
 *  in a sequence of N strings from standard input using StdIn.readString(); and
 *  prints out exactly k of them, uniformly at random. Each item from the
 *  sequence can be printed out at most once. You may assume that 0 ≤ k ≤ n,
 *  where N is the number of string on standard input.
*/

public class Subset {
    public static void main(String[] args) {
        RandomizedQueue<String> rq = new RandomizedQueue<String>();
        int k = Integer.parseInt(args[0]);

        // add items to the randomized queue
        while (!StdIn.isEmpty()) {
            String item = StdIn.readString();
            rq.enqueue(item);
        }

        // while the number chosen items is less than k
        while (k > 0) {
            StdOut.println(rq.dequeue());
            k--;
        }
    }
}
