/**
 * @author Wyatt Muller (wtmuller22@vt.edu), Caroline Turner (ct21@vt.edu)
 * @version 2020.04.14
 * 
 * Represents a generic MinHeap tree.
 *
 * @param <T> The Comparable data type to be stored in the tree.
 */
public class MinHeap<T extends Comparable<T>> {
    private T[] heap; // Pointer to the heap array
    private int size;          // Maximum size of the heap
    private int n;             // Number of things now in heap
    
    /**
     * Constructor for MinHeap
     * 
     * @param max size
     */
    public MinHeap(int max) {
        heap = (T[])new Comparable[max];
        size = max;
        n = 0;
    }

    /** 
     * Return current size of the heap
     * 
     * @return n
     */
    public int heapsize() { 
        return n; 
    }
    
    /**
     * Set the current size of the heap
     * 
     * @param s new size
     */
    public void setHeapSize(int s) {
        n = s;
    }

    /**
     * Return true if pos a leaf position, false otherwise
     * 
     * @param pos to check
     * @return true iff leaf
     */
    public boolean isLeaf(int pos) { 
        return (pos >= n / 2) && (pos < n); 
    }

    /**
     * Return position for left child of pos
     * 
     * @param pos of parent
     * @return child pos
     */
    public int leftchild(int pos) {
        if (pos >= n / 2) {
            return -1;
        }
        return 2 * pos + 1;
    }

    /**
     * Return position for parent
     * 
     * @param pos of child
     * @return parent pos
     */
    public int parent(int pos) {
        if (pos <= 0) {
            return -1;
        }
        return (pos - 1) / 2;
    }

    /**
     * Insert val into heap
     * 
     * @param key to insert
     */
    public void insert(T key) {
        if (n >= size) {
            System.out.println("Heap is full");
            return;
        }
        int curr = n++;
        heap[curr] = key;  // Start at end of heap
        // Now sift up until curr's parent's key > curr's key
        while ((curr != 0) && (heap[curr].compareTo(heap[parent(curr)]) < 0)) {
            swap(curr, parent(curr));
            curr = parent(curr);
        }
    }

    /**
     * Heapify contents of Heap
     */
    public void buildheap() { 
        for (int i = n / 2 - 1; i >= 0; i--) {
            siftdown(i); 
        }
    }

    /**
     * Put element in its correct place
     * 
     * @param pos to shift
     */
    public void siftdown(int pos) {
        if ((pos < 0) || (pos >= n)) {
            return; // Illegal position
        }
        while (!isLeaf(pos)) {
            int j = leftchild(pos);
            if ((j < (n - 1)) && (heap[j].compareTo(heap[j + 1]) > 0)) {
                j++; // j is now index of child with greater value
            }
            if (heap[pos].compareTo(heap[j]) <= 0) {
                return;
            }
            swap(pos, j);
            pos = j;  // Move down
        }
    }

    /**
     * Remove and return minimum value
     * 
     * @return true iff no elements left
     */
    public boolean removeMin() {
        swap(0, --n); // Swap minimum with last value
        siftdown(0);   // Put new heap root val in correct place
        return (n == 0);
    }
    
    /**
     * Gets the minimum element
     * 
     * @return min element
     */
    public T getMin() {
        if (n == 0) {
            return null;
        }
        return heap[0];
    }
    
    /**
     * Sets the minimum element
     * 
     * @param newMin to set
     */
    public void setMin(T newMin) {
        heap[0] = newMin;
    }
    
    /**
     * Swaps two elements
     * 
     * @param pos1 element 1
     * @param pos2 element 2
     */
    public void swap(int pos1, int pos2) {
        T temp = heap[pos1];
        heap[pos1] = heap[pos2];
        heap[pos2] = temp;
    }
    
    /**
     * Sets the hidden values to the front
     * 
     * @param numHidden to set
     */
    public void setHidden(int numHidden) {
        for (int i = 0; i < numHidden; i++) {
            insert(heap[(size - numHidden) + i]);
        }
    }
}
