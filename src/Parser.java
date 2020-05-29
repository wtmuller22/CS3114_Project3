import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;

/**
 * @author Wyatt Muller (wtmuller22@vt.edu), Caroline Turner (ct21@vt.edu)
 * @version 2020.04.14
 * 
 * Parses through the bin file.
 */
public class Parser {
    
    /**
     * Constructor for parser
     * 
     * @param fileName to read from
     * @throws IOException
     */
    public Parser(String fileName) throws IOException {
        File sorted = new File("sortedRuns.bin");
        File given = new File(fileName);
        //ArrayList<Record> og = makeAL(given);
        //og.sort(null);
        ArrayList<Integer> runLengths = 
            ReplacementSort.replacementSelectionSort(given, sorted);
        File result = MergeSort.multiwayMerge(sorted, given, runLengths);
        //ArrayList<Record> f = makeAL(result);
        //compALs(og, f);
        resultsPrint(result);
        if (sorted.equals(result)) {
            given.delete();
            result.renameTo(given);
        }
        else {
            sorted.delete();
        }
    }
    
    /**
     * Returns next record in the random access file
     * 
     * @param raf to read
     * @return the next record
     * @throws IOException
     */
    private Record nextRecord(RandomAccessFile raf) throws IOException {
        byte[] arr = new byte[16];
        int numPut = raf.read(arr);
        if (numPut == -1) {
            return null;
        }
        return new Record(arr);
    }
    
    /**
     * Prints the results to the standard output stream
     * 
     * @param results the file containing results
     * @throws IOException
     */
    private void resultsPrint(File results) throws IOException {
        RandomAccessFile raf = new RandomAccessFile(results, "r");
        int numRecs = ((int)raf.length()) / 16;
        int numBlocks = numRecs / 512;
        if (numRecs % 512 != 0) {
            numBlocks++;
        }
        for (int i = 0; i < numBlocks; i++) {
            raf.seek(i * 8192);
            Record here = nextRecord(raf);
            System.out.print(Long.toString(here.getData()) 
                + " " + Double.toString(here.getKey()));
            if ((i + 1) % 5 == 0) {
                System.out.print("\n");
            }
            else {
                System.out.print(" ");
            }
        }
        raf.close();
    }
    /*
    private void resultsPrint(File results) throws IOException {
        RandomAccessFile raf = new RandomAccessFile(results, "r");
        System.out.println((int)raf.length());
        int numRecs = ((int)raf.length()) / 16;
        Record prev = null;
        for (int i = 0; i < numRecs; i++) {
            Record here = nextRecord(raf);
            if (prev != null && (prev.compareTo(here) >= 0)) {
            System.out.println(Long.toUnsignedString(here.getData()) 
                + " " + Double.toString(here.getKey()));
            return;
            }
            prev = here;
        }
        System.out.println("None out of order");
        raf.close();
    }
    
    private ArrayList<Record> makeAL(File given) throws IOException {
        ArrayList<Record> arr = new ArrayList<Record>();
        RandomAccessFile raf = new RandomAccessFile(given, "r");
        Record r = nextRecord(raf);
        while (r != null) {
            arr.add(r);
            r = nextRecord(raf);
        }
        return arr;
    }
    
    private void compALs(ArrayList<Record> og, ArrayList<Record> f) {
        if (og.size() != f.size()) {
            System.out.println("Different sizes");
            return;
        }
        for (int i = 0; i < og.size(); i++) {
            Record ogR = og.get(i);
            Record fR = f.get(i);
            boolean ids = ogR.getData() == fR.getData();
            boolean keys = ogR.getKey() == ogR.getKey();
            if (!ids) {
                System.out.println("Data is different:");
                System.out.println("OG: " + ogR.getData());
                System.out.println("F: " + fR.getData());
                return;
            }
            if (!keys) {
                System.out.println("Key is different:");
                System.out.println("OG: " + ogR.getKey());
                System.out.println("F: " + fR.getKey());
                return;
            }
        }
        System.out.println("No differences");
    }
    */
}
