
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

public class Subset {
    public static void main(String[] args) {
        RandomizedQueue<String> rq = new RandomizedQueue<String>();
        int k = Integer.parseInt(args[0]);

        while (!StdIn.isEmpty()) {
            String item = StdIn.readString();
            //StdOut.println(item);
            rq.enqueue(item);
        }

        while (k > 0) {
            StdOut.println(rq.dequeue());
            k--;
        }
    }
}
