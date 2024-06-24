import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import org.junit.*;

public class BasicTests {
    @Test
    public void testMethod1() {
        String res = runMethodGetConsoleOut(() -> AppMain.main(null));
        String[] lines = res.split("\r\n");

        // should have at least one line of numbers
        assertTrue(lines.length >= 1);

        // should have five numbers in the line
        String[] nums = lines[0].split(" ");
        assertEquals(5, nums.length);
        for (String num : nums) {
            int numInt = Integer.parseInt(num.trim());

            // check range of the numbers
            assertTrue(numInt >= 0);
            assertTrue(numInt < 100);
        }
    }

    @Test
    public void testMethod2() {
        String res = runMethodGetConsoleOut(() -> AppMain.main(null));
        String[] lines = res.split("\r\n");

        // should have at least one line of numbers
        assertTrue(lines.length >= 1);

        // should have five numbers in the line
        String[] nums = lines[1].split(" ");
        assertEquals(5, nums.length);
        for (String num : nums) {
            int numInt = Integer.parseInt(num.trim());

            // check range of the numbers
            assertTrue(numInt >= 100);
            assertTrue(numInt <= 500);
        }
    }

    // Console output helper methods
    private ByteArrayOutputStream consoleStringOutput = null;
    private PrintStream consoleStringOutputPrev = null;
    private void consoleToString_Push() {
        if (consoleStringOutput != null) {
            consoleToString_Pop();
        }

        // Stream to hold the output
        consoleStringOutput = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(consoleStringOutput);

        // Save the old system out
        consoleStringOutputPrev = System.out;

        // Set the new output
        System.setOut(ps);
    }
    private String consoleToString_Pop() {
        // restore
        System.out.flush();
        System.setOut(consoleStringOutputPrev);

        return consoleStringOutput.toString();
    }
    private String runMethodGetConsoleOut(Runnable method) {
        consoleToString_Push();
        method.run();
        return consoleToString_Pop();
    }
}
