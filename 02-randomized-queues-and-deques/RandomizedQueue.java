/******************************************************************************
 *  Compilation:  javac-algs4 RandomizedQueue.java
 *  Execution:    java-algs4 RandomizedQueue
 *  Dependencies: StdRandom.java
 *
 *  A generic queue where items are removed uniformly at random.
 *
 ******************************************************************************/


import java.util.Iterator;
import java.util.NoSuchElementException;

import edu.princeton.cs.algs4.StdRandom;


/**
 *  This class implements a randomized queue, similar to a stack or queue,
 *  except that the item removed is chosen uniformly at random from items in the
 *  data structure.
 *
 *  It supports the usual enqueue and dequeue operations, testing if the queue
 *  is empty, along with special methods for peek items at random and iterate
 *  through the items in random order.
 *
 *  This implementation uses an array of Items with a nested class for iterate
 *  over the items uniformly at random.
 *
 *  This class implements Iterable, so items can be iterated with foreach
 *  notation. It also uses the convention that values cannot be null; when
 *  trying to add a null item, it throws a NullPointerException.
 *
 *  The size, sample, and is-empty operations all take constant time in the
 *  worst case. Enqueue and dequeue take time proportional to N in the worst
 *  case.
 *
 *  @param <Item> the generic type of an item in this queue
*/

public class RandomizedQueue<Item> implements Iterable<Item> {
    private int    size;    // size of the queue
    private Item[] items;   // items in queue

    /**
    * Construct an empty randomized queue
    */
    public RandomizedQueue() {
        this.size  = 0;
        this.items = (Item[]) new Object[1];
    }

    /**
    * Number of items currently on this randomized queue.
    *
    * @return number of items N on this randomized queue
    */
    public int size() {
        return this.size;
    }

    /**
    * Check if this randomized queue is empty.
    *
    * @return true if this randomized queue is empty. False otherwise
    */
    public boolean isEmpty() {
        return (this.size == 0);
    }

    /**
    * Add the item to this queue.
    *
    * @param the item to be added
    * @throws NullPointerException if the item is null
    */
    public void enqueue(Item item) {
        // check if item is null
        checkItem(item);

        // add the new item at the end of the array
        this.items[size] = item;
        this.size++;
        // if there is no more space in this randomized queue, move to another
        // array of double size
        if (this.size == items.length)
            resize(2 * this.size);
    }

    // check for avoid adding null items
    private void checkItem(Item item) {
        if (item == null)
            throw new NullPointerException("add a null item.");
    }

    /**
     * Removes and returns one item at random of this randomized queue.
     *
     * @return the item of this queue that was last recently added
     * @throws NoSuchElementException if this queue is empty
     */
    public Item randomize dequeue() {
        // check if this randomized queue is empty
        checkEmptyQueue();

        // pick an integer uniformly at random and use it as the index of the
        // item
        int  rand = StdRandom.uniform(this.size);
        Item item = this.items[rand];

        // put last item in the position of the item picked at random and null
        // in the last position
        this.items[rand] = this.items[--this.size];
        this.items[this.size] = null;

        // if the size of the randomized queue is less than one quarter of the
        // length of the array, resize to the half of its capacity
        if (this.size < this.items.length / 4)
            this.resize(this.items.length / 2);

        return item;
    }

    // check for empty randomized queue
    private void checkEmptyQueue() {
        if (this.isEmpty())
            throw new NoSuchElementException("Null queue.");
    }

    // resize an array of Item to one of new length N
    private void resize(int N) {
        Item[] temp = (Item[]) new Object[N];
        for (int i = 0; i < this.size; i++)
            temp[i] = this.items[i];
        items = temp;
    }

    /**
     * Returns (but don't remove) one item at random of this randomized queue.
     *
     * @return the item of this queue that was last recently added
     * @throws NoSuchElementException if this queue is empty
     */
    public Item sample() {
        // check if this randomized queue is empty
        checkEmptyQueue();
        int rand = StdRandom.uniform(size());
        return this.items[rand];
    }

    /**
     * Return an independent Iterator over items in random order.
     * To iterate over all of the items in this randomized queue, use the
     * foreach notation:
     *      for (Item item : queue).
     *
     * @return an Iterator over the items of this randomized queue
     */
    public Iterator<Item> iterator() {
        return new RandomizedQueueIterator();
    }

    // private class for iterate in this queue in random order
    private class RandomizedQueueIterator implements Iterator {
        private Item[] temp;    // auxiliar array to hold items in random order
        private int    index;

        public RandomizedQueueIterator() {
            index = 0;
            temp = (Item[]) new Object[size];
            // copy items in auxiliary array
            for (int i = 0; i < size; i++)
                temp[i] = items[i];

            StdRandom.shuffle(temp);
        }

        public boolean hasNext() {
            return index < size;
        }

        public void remove() {
            throw new UnsupportedOperationException();
        }

        public Item next() {
            if (!hasNext())
                throw new NoSuchElementException();

            return temp[index++];
        }
    }
}
