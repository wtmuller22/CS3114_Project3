import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;

/**
 * @author Wyatt Muller (wtmuller22@vt.edu), Caroline Turner (ct21@vt.edu)
 * @version 2020.04.18
 * 
 * Performs a merge sort.
 */
public class MergeSort {

    /**
     * Static method for multiway merge sort
     * 
     * @param readFrom file
     * @param writeTo file
     * @param runSizes list
     * @return the file with the results
     * @throws IOException
     */
    public static File multiwayMerge(File readFrom, 
        File writeTo, 
        ArrayList<Integer> runSizes) throws IOException {
        MinHeap<Record> heap = new MinHeap<Record>(4096);
        int currRunSize = 0;
        int numRunsCompleted = 0;
        RandomAccessFile read = new RandomAccessFile(readFrom, "r");
        RandomAccessFile write = new RandomAccessFile(writeTo, "rw");
        int[] offsets = new int[runSizes.size()];
        int counter = 0;
        for (int i = 0; i < runSizes.size(); i++) {
            offsets[i] = counter;
            counter += runSizes.get(i);
        }
        ArrayList<Integer> newRuns = new ArrayList<Integer>();
        while (numRunsCompleted < runSizes.size()) {
            int thisNumRuns = (numRunsCompleted + 8 <= runSizes.size()) 
                ? 8 : (runSizes.size() % 8);
            int[] numInHeap = new int[thisNumRuns];
            int[] numInOut = new int[thisNumRuns];
            int[] theseOffsets = new int[thisNumRuns];
            int[] numInRuns = new int[thisNumRuns];
            for (int i = 0; i < thisNumRuns; i++) {
                theseOffsets[i] = offsets[numRunsCompleted + i];
                numInHeap[i] = 0;
                numInOut[i] = 0;
                numInRuns[i] = 0;
            }
            currRunSize = 0;
            for (int i = 0; i < thisNumRuns; i++) {
                int numInRun = runSizes.get(numRunsCompleted + i).intValue();
                numInRuns[i] = numInRun;
                int numInBlock = (numInRun >= 512) ? 512 : numInRun;
                Record[] thisRun = new Record[numInBlock];
                byte[] bArray = new byte[numInBlock * 16];
                read.seek(theseOffsets[i] * 16);
                read.read(bArray);
                for (int j = 0; j < numInBlock; j++) {
                    byte[] arr = new byte[16];
                    for (int k = 0; k < 16; k++) {
                        arr[k] = bArray[(j * 16) + k];
                    }
                    thisRun[j] = new Record(arr, i);
                }
                for (int j = 0; j < numInBlock; j++) {
                    heap.insert(thisRun[j]);
                }
                numInHeap[i] = numInBlock;
                currRunSize += numInRun;
            }
            newRuns.add(Integer.valueOf(currRunSize));
            Record[] outputBuffer = new Record[512];
            int idx = 0;
            while (heap.heapsize() > 0) {
                Record min = heap.getMin();
                heap.removeMin();
                outputBuffer[idx] = min;
                if (idx == 511) {
                    idx = 0;
                    for (int i = 0; i < 512; i++) {
                        write.write(outputBuffer[i].getTotal());
                    }
                }
                else {
                    idx++;
                }
                int thisMinRun = min.getRunNum();
                numInHeap[thisMinRun] -= 1;
                numInOut[thisMinRun] += 1;
                if (numInHeap[thisMinRun] == 0 
                    && numInOut[thisMinRun] < numInRuns[thisMinRun]) {
                    int numInBlock = (numInRuns[thisMinRun] 
                        - numInOut[thisMinRun] >= 512) 
                        ? 512 : numInRuns[thisMinRun] - numInOut[thisMinRun];
                    Record[] thisRun = new Record[numInBlock];
                    byte[] bArray = new byte[numInBlock * 16];
                    read.seek(
                        (theseOffsets[thisMinRun] 
                            + numInOut[thisMinRun]) * 16);
                    read.read(bArray);
                    for (int j = 0; j < numInBlock; j++) {
                        byte[] arr = new byte[16];
                        for (int k = 0; k < 16; k++) {
                            arr[k] = bArray[(j * 16) + k];
                        }
                        thisRun[j] = new Record(arr, thisMinRun);
                    }
                    for (int j = 0; j < numInBlock; j++) {
                        heap.insert(thisRun[j]);
                    }
                    numInHeap[thisMinRun] = numInBlock;
                }
            }
            if (idx != 0) {
                for (int i = 0; i < idx; i++) {
                    write.write(outputBuffer[i].getTotal());
                }
            }
            numRunsCompleted += thisNumRuns;
        }
        runSizes = newRuns;
        read.close();
        write.close();
        readFrom.delete();
        if (runSizes.size() > 1) {
            return multiwayMerge(writeTo, readFrom, runSizes);
        }
        else {
            return writeTo;
        }
    }
    
}
