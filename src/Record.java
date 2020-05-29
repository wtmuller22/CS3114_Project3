import java.nio.ByteBuffer;

/**
 * @author Wyatt Muller (wtmuller22@vt.edu), Caroline Turner (ct21@vt.edu)
 * @version 2020.04.14
 * 
 * Represents a record in the file.
 */
public class Record implements Comparable<Record> {
    
    private long data;
    private double key;
    private byte[] total;
    private int runNum;
    
    /**
     * Constructor for replacement selection.
     * 
     * @param arr total
     */
    public Record(byte[] arr) {
        this(arr, -1);
    }
    
    /**
     * Constructor for multiway merge sort.
     * 
     * @param arr total
     * @param num run number
     */
    public Record(byte[] arr, int num) {
        total = arr;
        ByteBuffer buf = ByteBuffer.allocate(16);
        buf.put(arr);
        buf.flip();
        data = buf.getLong();
        key = buf.getDouble();
        runNum = num;
    }
    
    /**
     * Getter for data
     * 
     * @return data
     */
    public long getData() {
        return data;
    }
    
    /**
     * Getter for key
     * 
     * @return key
     */
    public double getKey() {
        return key;
    }
    
    /**
     * Getter for total
     * 
     * @return total
     */
    public byte[] getTotal() {
        return total;
    }
    
    /**
     * Getter for runNum
     * 
     * @return runNum
     */
    public int getRunNum() {
        return runNum;
    }
    
    /**
     * Overriden compareTo method.
     * 
     * @param o to be compared to
     * @return correct result
     */
    @Override
    public int compareTo(Record o) {
        if (key - o.getKey() < 0) {
            return -1;
        }
        else if (key == o.getKey()) {
            return 0;
        }
        else {
            return 1;
        }
    }
}
