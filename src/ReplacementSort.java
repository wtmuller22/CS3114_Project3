import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;

/**
 * @author Wyatt Muller (wtmuller22@vt.edu), Caroline Turner (ct21@vt.edu)
 * @version 2020.04.18
 * 
 * Performs a replacement selection sort.
 */
public class ReplacementSort {

    /**
     * Static method for performing a replacement selection sort
     * 
     * @param given to read
     * @param sorted to write
     * @return the arraylist of runs
     * @throws IOException
     */
    public static ArrayList<Integer> replacementSelectionSort(
        File given, File sorted) throws IOException {
        RandomAccessFile read = new RandomAccessFile(given, "r");
        RandomAccessFile write = new RandomAccessFile(sorted, "rw");
        MinHeap<Record> heap = new MinHeap<Record>(4096);
        ArrayList<Integer> runSizes = new ArrayList<Integer>();
        int currRunSize = 0;
        fillHeap(heap, read);
        Record[] inputBuffer = makeInput(read);
        Record[] outputBuffer = new Record[512];
        while (inputBuffer != null) {
            for (int i = 0; i < 512; i++) {
                Record out = heap.getMin();
                outputBuffer[i] = out;
                currRunSize++;
                Record in = inputBuffer[i];
                heap.setMin(in);
                if (in.compareTo(out) >= 0) {
                    heap.siftdown(0);
                }
                else {
                    boolean result = heap.removeMin();
                    if (result) {
                        runSizes.add(Integer.valueOf(currRunSize));
                        currRunSize = 0;
                        heap.setHeapSize(4096);
                        heap.buildheap();
                    }
                }
            }
            for (int i = 0; i < 512; i++) {
                write.write(outputBuffer[i].getTotal());
            }
            inputBuffer = makeInput(read);
        }
        clearHeap(heap, runSizes, currRunSize, write);
        read.close();
        write.close();
        given.delete();
        return runSizes;
    }
    
    /**
     * Sends the contents of heap to output
     * 
     * @param heap to use
     * @param runSizes to check
     * @param currRunSize of continuing run
     * @param write to file
     * @throws IOException
     */
    private static void clearHeap(MinHeap<Record> heap, 
        ArrayList<Integer> runSizes, 
        int currRunSize, 
        RandomAccessFile write) throws IOException {
        int hidden = 4096 - heap.heapsize();
        for (int j = 0; j < 8; j++) {
            Record[] outputBuffer = new Record[512];
            for (int i = 0; i < 512; i++) {
                Record out = heap.getMin();
                if (out != null) {
                    outputBuffer[i] = out;
                    currRunSize++;
                    heap.removeMin();
                }
                else {
                    i--;
                    runSizes.add(Integer.valueOf(currRunSize));
                    currRunSize = 0;
                    heap.setHidden(hidden);
                }
            }
            for (int i = 0; i < 512; i++) {
                Record r = outputBuffer[i];
                write.write(r.getTotal());
            }
        }
        runSizes.add(Integer.valueOf(currRunSize));
    }
    
    /**
     * Makes an input buffer
     * 
     * @param read from file
     * @return input buffer
     * @throws IOException
     */
    private static Record[] makeInput(
        RandomAccessFile read) throws IOException {
        Record[] input = new Record[512];
        byte[] bArray = new byte[8192];
        int numPut = read.read(bArray);
        if (numPut != -1) {
            for (int i = 0; i < 512; i++) {
                Record result = nextRecord(bArray, i * 16);
                input[i] = result;
            }
        }
        else {
            return null;
        }
        return input;
    }
    
    /**
     * Reads next record from file
     * 
     * @param bArray to be read
     * @param offset to start
     * @return next record
     * @throws IOException
     */
    private static Record nextRecord(byte[] bArray, 
        int offset) throws IOException {
        byte[] arr = new byte[16];
        for (int i = 0; i < 16; i++) {
            arr[i] = bArray[offset + i];
        }
        return new Record(arr);
    }
    
    /**
     * Fills the heap with 8 blocks of data
     * 
     * @param heap to fill
     * @param read from file
     * @throws IOException
     */
    private static void fillHeap(MinHeap<Record> heap, 
        RandomAccessFile read) throws IOException {
        for (int j = 0; j < 8; j++) {
            Record[] input = makeInput(read);
            for (int i = 0; i < 512; i++) {
                heap.insert(input[i]);
            }
        }
    }
    
}
