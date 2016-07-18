/******************************************************************************
 *  Compilation:  javac-algs4 Deque.java
 *  Execution:    java-algs4 Deque
 *  Dependencies:
 *
 *  A generic double-ended queue.
 *
 ******************************************************************************/

 import java.lang.NullPointerException;
 import java.util.Iterator;
 import java.util.NoSuchElementException;

/**
 *  This class implements a double-deque queue or deque data structure, a
 *  generalization of a stack and a queue. It supports inserting and removing
 *  items from either the front or the back of the data structure, check for
 *  empty deque and retrieve the size.
 *
 *  This class also implements Iterable, so items can be iterated with foreach
 *  notation.
 *
 *  Deque's class uses the convention that values cannot be null; when trying to
 *  implement a null item, Deque throws a NullPointerException.
 *
 *  @param <Item> the generic type of an item in this queue
*/

public class Deque<Item> implements Iterable<Item> {
    private int  N;     // size of the deque
    private Node first; // first item of the deque
    private Node last;  // last item of the deque

    /**
    * Construct an empty deque, with pointers pointing to null and size 0.
    */
    public Deque() {
        first = null;
        last  = null;
        N     = 0;
    }

    // private class for node structures
    private class Node {
        private Item item;
        private Node next;
        private Node prev;

        // create a node with given item
        public Node(Item it) {
            item = it;
        }
    }

    // private class for implement iterator
    private class DequeIterator implements Iterator<Item> {
        private Node current = first;

        public boolean hasNext() {
            return current != null;
        }

        public void remove() {
            throw new UnsupportedOperationException();
        }

        public Item next() {
            if (!hasNext())
                throw new NoSuchElementException();
            Item item = current.item;
            current = current.next;
            return item;
        }
    }

    /**
    * Number of items currently on this deque.
    *
    * @return number of items N on this deque
    */
    public int size() {
        return N;
    }

    /**
    * Check if this deque is empty.
    *
    * @return true if this deque is empty. False otherwise
    */
    public boolean isEmpty() {
        return (N == 0);
    }

    /**
    * Add the item to the front of this deque.
    *
    * @param the item to be added
    * @throws NullPointerException if the item is null
    */
    public void addFirst(Item item) {
        // check if item is null
        checkItem(item);

        Node node  = new Node(item);
        // if this deque is empty, set first and last nodes as the current nodes
        if (isEmpty())
            setFirstAndLast(node);
        // if not, add item to the front
        else {
            node.next  = first;
            node.prev  = null;
            first.prev = node;
            first      = node;
        }
        // update number of items of this deque
        N++;
    }

    /**
    * Add the item to the end of this deque.
    *
    * @param the item to be added
    * @throws NullPointerException if the item is null
    */
    public void addLast(Item item) {
        // check if item is null
        checkItem(item);

        Node node = new Node(item);

        // if this deque is empty, set first and last nodes as the current nodes
        if (isEmpty())
            setFirstAndLast(node);
        // if not, add item to the end
        else {
            node.next = null;
            node.prev = last;
            last.next = node;
            last      = node;
        }
        // update number of items of the deque
        N++;
    }

    // check for avoid adding null items
    private void checkItem(Item item) {
        if (item == null)
            throw new NullPointerException("Adding null item.");
    }

    // set first and last items as the given node (for deques with one element)
    private void setFirstAndLast(Node node) {
        first = node;
        last  = node;
    }

    /**
    * Remove and return the item from the front of this deque.
    *
    * @return the first item of the deque
    * @throws NoSuchElementException if this deque is empty
    */
    public Item removeFirst() {
        // check if this deque is empty
        checkEmptyDeque();

        // first item to be returned
        Item item = first.item;

        // if this deque has one element, set first and last as null
        if (N == 1)
            setFirstAndLast(null);
        // if not, move first pointer to the new first item
        else {
            first      = first.next;
            first.prev = null;
        }
        // update number of items of this deque
        N--;

        return item;
    }

    /**
    * Remove and return the item from the end.
    *
    * @return the first item of this deque
    * @throws NoSuchElementException if this deque is empty
    */
    public Item removeLast() {
        // check if this deque is empty
        checkEmptyDeque();

        // last item to be returned
        Item item = last.item;

        // if this deque has one element, set first and last as null
        if (N == 1)
            setFirstAndLast(null);
        // if not, move last pointer to the new last item
        else {
            last = last.prev;
            last.next = null;
        }
        // update number of items of this deque
        N--;

        return item;
    }

    // check if this deque is empty
    private void checkEmptyDeque() {
        if (isEmpty())
            throw new NoSuchElementException("Empty deque.");
    }

    /**
     * Returns all keys on this deque as an Iterable in order from front to end.
     * To iterate over all of the items in this deque, use the foreach notation:
     * for (Item item : deque).
     *
     * @return all items in this deque as an Iterable
     */
    public Iterator<Item> iterator() {
        return new DequeIterator();
    }

    /**
     * Unit tests the Deque data type.
     */
    // public static void main(String[] args) {
    //     Deque<Integer> d = new Deque<Integer>();
    //
    //     d.addFirst(2);
    //     d.addFirst(4);
    //     d.addFirst(4);
    //     d.addLast(9);
    //     d.addLast(7);
    //     d.addLast(0);
    //     d.addLast(4);
    //
    //     for (int i : d) {
    //         System.out.format("%d\n", d.removeFirst());
    //     }
    // }
}
