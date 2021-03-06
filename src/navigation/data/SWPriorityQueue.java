package navigation.data;

import java.util.Iterator;
import java.util.NoSuchElementException;


public class SWPriorityQueue<Key extends Comparable<Key>> implements Iterable<Integer> {
    private int NMAX;        // maximum number of elements on PQ
    private int N;           // number of elements on PQ
    private int[] pq;        // binary heap using 1-based indexing
    private int[] qp;        // inverse of pq - qp[pq[i]] = pq[qp[i]] = i
    private Key[] keys;      // keys[i] = priority of i

   /**
     * Create an empty indexed priority queue with indices between 0 and NMAX-1.
     * @throws java.lang.IllegalArgumentException if NMAX < 0
     */
    @SuppressWarnings("unchecked")
	public SWPriorityQueue(int NMAX) {
        if (NMAX < 0) throw new IllegalArgumentException();
        this.NMAX = NMAX;
        keys = (Key[]) new Comparable[NMAX + 1];    
        pq   = new int[NMAX + 1];
        qp   = new int[NMAX + 1];                   
        for (int i = 0; i <= NMAX; i++) qp[i] = -1;
    }

   /**
     * checks if the queue is empty.
     */
    public boolean isEmpty() { return N == 0; }

   /**
     * Is i an index on the priority queue?
     * @throws java.lang.IndexOutOfBoundsException if i is under 0 or over NMAX.
     */
    public boolean contains(int i) {
        if (i < 0 || i >= NMAX) throw new IndexOutOfBoundsException();
        return qp[i] != -1;
    }

   /**
     * Return the number of keys in the priority queue.
     */
    public int size() {
        return N;
    }

   /**
     * add value to index i.
     * @throws java.lang.IndexOutOfBoundsException if i is under 0 or over NMAX.
     * @throws java.util.IllegalArgumentException if there already is a value for index i.
     */
    public void insert(int i, Key key) {
        if (i < 0 || i >= NMAX) throw new IndexOutOfBoundsException();
        if (contains(i)) throw new IllegalArgumentException("index is already in the priority queue");
        N++;
        qp[i] = N;
        pq[N] = i;
        keys[i] = key;
        swim(N);
    }

   /**
     * Delete the minimal value and return its associated index.
     * @throws java.util.NoSuchElementException if the priority queue is empty.
     */
    public int delMin() { 
        if (N == 0) throw new NoSuchElementException("Priority queue underflow");
        int min = pq[1];        
        exch(1, N--); 
        sink(1);
        qp[min] = -1;            // delete
        keys[pq[N+1]] = null;    // to help with garbage collection
        pq[N+1] = -1;            // not needed
        return min; 
    }

   /**
     * Return the value associated with index i.
     * @throws java.lang.IndexOutOfBoundsException if i is under 0 or over NMAX.
     * @throws java.util.NoSuchElementException no value is associated with index i
     */
    public Key keyOf(int i) {
        if (i < 0 || i >= NMAX) throw new IndexOutOfBoundsException();
        if (!contains(i)) throw new NoSuchElementException("index is not in the priority queue");
        else return keys[i];
    }

   /**
     * Change the value associated with index i to the specified value.
     * @throws java.lang.IndexOutOfBoundsException if i is under 0 or over NMAX.
     * @throws java.util.NoSuchElementException no value is associated with index i
     */
    public void changeKey(int i, Key key) {
        if (i < 0 || i >= NMAX) throw new IndexOutOfBoundsException();
        if (!contains(i)) throw new NoSuchElementException("index is not in the priority queue");
        keys[i] = key;
        swim(qp[i]);
        sink(qp[i]);
    }


   /**
     * Delete the value associated with index i.
     * @throws java.lang.IndexOutOfBoundsException if i is under 0 or over NMAX.
     * @throws java.util.NoSuchElementException no value is associated with index i
     */
    public void delete(int i) {
        if (i < 0 || i >= NMAX) throw new IndexOutOfBoundsException();
        if (!contains(i)) throw new NoSuchElementException("index is not in the priority queue");
        int index = qp[i];
        exch(index, N--);
        swim(index);
        sink(index);
        keys[i] = null;
        qp[i] = -1;
    }


   /**************************************************************
    * General helper functions
    **************************************************************/
    private boolean greater(int i, int j) {
        return keys[pq[i]].compareTo(keys[pq[j]]) > 0;
    }

    private void exch(int i, int j) {
        int swap = pq[i]; pq[i] = pq[j]; pq[j] = swap;
        qp[pq[i]] = i; qp[pq[j]] = j;
    }


   /**************************************************************
    * Heap helper functions
    **************************************************************/
    private void swim(int k)  {
        while (k > 1 && greater(k/2, k)) {
            exch(k, k/2);
            k = k/2;
        }
    }

    private void sink(int k) {
        while (2*k <= N) {
            int j = 2*k;
            if (j < N && greater(j, j+1)) j++;
            if (!greater(k, j)) break;
            exch(k, j);
            k = j;
        }
    }


   /***********************************************************************
    * Iterators
    **********************************************************************/

   /**
     * Return an iterator that iterates over all of the elements on the
     * priority queue in ascending order.
     * <p>
     * The iterator doesn't implement <tt>remove()</tt> since it's optional.
     */
    public Iterator<Integer> iterator() { return new HeapIterator(); }

    private class HeapIterator implements Iterator<Integer> {
        // create a new pq
        private SWPriorityQueue<Key> copy;

        // add all elements to copy of heap
        // takes linear time since already in heap order so no keys move
        public HeapIterator() {
            copy = new SWPriorityQueue<Key>(pq.length - 1);
            for (int i = 1; i <= N; i++)
                copy.insert(pq[i], keys[pq[i]]);
        }

        public boolean hasNext()  { return !copy.isEmpty();                     }
        public void remove()      { throw new UnsupportedOperationException();  }

        public Integer next() {
            if (!hasNext()) throw new NoSuchElementException();
            return copy.delMin();
        }
    }
}