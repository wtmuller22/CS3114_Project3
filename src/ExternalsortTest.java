import static org.junit.Assert.*;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.Scanner;
import org.junit.Before;
import org.junit.Test;

/**
 * @author Wyatt Muller (wtmuller22@vt.edu), Caroline Turner (ct21@vt.edu)
 * @version 2020.04.20
 * 
 * Tests the Externalsort program.
 */
public class ExternalsortTest {
    
    private final ByteArrayOutputStream outContent = 
        new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;

    /**
     * Sets up the testing environment
     * 
     * @throws Exception
     */
    @Before
    public void setUp() throws Exception {
        System.setOut(new PrintStream(outContent));
        File input = new File("sampleInput16.bin");
        File data = new File("sampleTextFile.txt");
        Files.copy(data.toPath(), 
            input.toPath(), 
            StandardCopyOption.REPLACE_EXISTING);
    }


    /**
     * Tests the program.
     * 
     * @throws IOException
     */
    @Test
    public void test() throws IOException {
        Externalsort.main(new String[]{"sampleInput16.bin"});
        Scanner scan = new Scanner(outContent.toString());
        Scanner given = new Scanner(new File("Output.txt"));
        System.setOut(originalOut);
        while (scan.hasNextLine()) {
            assertTrue(given.hasNextLine());
            String sLine = scan.nextLine();
            System.out.println(sLine);
            String gLine = given.nextLine();
            assertTrue(sLine.equals(gLine));
        }
        assertFalse(given.hasNextLine());
        scan.close();
        given.close();
    }

}
