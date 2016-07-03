
import java.util.Iterator;
import edu.princeton.cs.algs4.StdRandom;
import java.util.NoSuchElementException;

public class RandomizedQueue<Item> implements Iterable<Item> {
    private int    size;
    private Item[] items;

    // construct an empty randomized queue
    public RandomizedQueue() {
        this.size  = 0;
        this.items = (Item[]) new Object[1];
    }

    // check for null items to add
    private void checkItem(Item item) {
        if (item == null)
            throw new NullPointerException("add a null item.");
    }

    // check for empty deque
    private void checkEmptyQueue() {
        if (this.isEmpty())
            throw new NoSuchElementException("Null queue.");
    }

    private void resize(int N) {
        Item[] temp = (Item[]) new Object[N];
        for (int i = 0; i < this.size; i++)
            temp[i] = this.items[i];
        items = temp;
    }

    // is the queue empty?
    public boolean isEmpty() {
        return (this.size == 0);
    }

    // return the number of items on the queue
    public int size() {
        return this.size;
    }

    // add the item
    public void enqueue(Item item) {
        checkItem(item);

        this.items[size] = item;
        this.size++;
        if (this.size == items.length)
            resize(2 * this.size);
    }

    // remove and return a random item
    public Item dequeue() {
        checkEmptyQueue();

        int rand  = StdRandom.uniform(this.size());
        Item item = this.items[rand];
        this.items[rand] = this.items[--this.size];
        this.items[this.size] = null;

        if (this.size < this.items.length / 4)
            this.resize(this.items.length / 2);

        return item;
    }

    // return (but do not remove) a random item
    public Item sample() {
        checkEmptyQueue();

        int rand = StdRandom.uniform(size());
        return this.items[rand];
    }

    // return an independent iterator over items in random order
    public Iterator<Item> iterator() {
        return new RandomizedQueueIterator();
    }

    private class RandomizedQueueIterator implements Iterator {
        private Item[] temp;
        private int    index;

        public RandomizedQueueIterator() {
            index = 0;
            temp = (Item[]) new Object[size];
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
