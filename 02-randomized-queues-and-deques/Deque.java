/******************************************************************************
 *  Compilation:  javac-algs4 Deque.java
 *
 ******************************************************************************/

import java.util.Iterator;
import java.util.NoSuchElementException;

 public class Deque<Item> implements Iterable<Item> {
    private int  N;
    private Node first;
    private Node last;

    // construct an empty deque
    public Deque() {
        first = null;
        last  = null;
        N     = 0;
    }

    // private class for the item structures
    private class Node {
        private Item item;
        private Node next;
        private Node prev;

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

    // is the deque empty?
    public boolean isEmpty() {
        return (N == 0);
    }

    // return the number of items on the deque
    public int size() {
        return N;
    }

    // check for null items to add
    private void checkItem(Item item) {
        if (item == null)
            throw new java.lang.NullPointerException("Adding null item.");
    }

    // check for empty deque
    private void checkEmptyDeque() {
        if (isEmpty())
            throw new java.util.NoSuchElementException("Null deque.");
    }

    private void setFirstAndLast(Node node) {
        first = node;
        last  = node;
    }

    // add the item to the front
    public void addFirst(Item item) {
        checkItem(item);

        Node node  = new Node(item);

        if (isEmpty())
            setFirstAndLast(node);
        else {
            node.next  = first;
            node.prev  = null;
            first.prev = node;
            first      = node;
        }
        N++;
    }

    // add the item to the end
    public void addLast(Item item) {
        checkItem(item);

        Node node = new Node(item);

        if (isEmpty())
            setFirstAndLast(node);
        else {
            node.next = null;
            node.prev = last;
            last.next = node;
            last      = node;
        }
        N++;
    }

    // remove and return the item from the front
    public Item removeFirst() {
        checkEmptyDeque();

        Item item = first.item;
        if (N == 1)
            setFirstAndLast(null);
        else {
            first      = first.next;
            first.prev = null;
        }
        N--;

        return item;
    }

    // remove and return the item from the end
    public Item removeLast() {
        checkEmptyDeque();

        Item item = last.item;
        if (N == 1)
            setFirstAndLast(null);
        else {
            last = last.prev;
            last.next = null;
        }
        N--;

        return item;
    }

    // return an iterator over items in order from front to end
    public Iterator<Item> iterator() {
        return new DequeIterator();
    }

    // unit testing
    public static void main(String[] args) {
        Deque<Integer> d = new Deque<Integer>();

        d.addFirst(2);
        d.addFirst(4);
        d.addFirst(4);
        d.addLast(9);
        d.addLast(7);
        d.addLast(0);
        d.addLast(4);

        for (int i : d) {
            System.out.format("%d\n", d.removeFirst());
        }
    }
}
