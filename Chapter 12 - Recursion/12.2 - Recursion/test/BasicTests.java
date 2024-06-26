import static org.junit.Assert.assertEquals;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import org.junit.*;

public class BasicTests {
    @Test
    public void testPowA() {
        Practice practice = new Practice();
        assertEquals(9, practice.powR(3, 2));
    }

    @Test
    public void testCreateStringA() {
        Practice practice = new Practice();
        assertEquals("[[[*]]]", practice.createStringR(3));
    }

    @Test
    public void testPrintBinaryA() {
        Practice practice = new Practice();
        String ret = runMethodGetConsoleOut(() -> practice.printBinary(5)).trim();
        while (ret.startsWith("0")) {
            ret = ret.substring(1);
        }
        if (ret.endsWith("\r\n")) {
            ret = ret.substring(0, ret.length() - 2);
        }
        assertEquals("101", ret);
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
