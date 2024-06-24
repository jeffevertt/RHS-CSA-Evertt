import static org.junit.Assert.assertEquals;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import org.junit.*;

public class BasicTests {
    @Test
    public void testLoop1() {
        assertEquals("2 7 12 17 22", runMethodGetConsoleOut(() -> AppMain.loop1()).trim());
    }

    @Test
    public void testLoop2() {
        assertEquals("4 7 10 13 16", runMethodGetConsoleOut(() -> AppMain.loop2()).trim());
    }

    @Test
    public void testLoop3() {
        assertEquals("6 9 12 15 18 21", runMethodGetConsoleOut(() -> AppMain.loop3()).trim());
    }

    @Test
    public void testLoop4() {
        assertEquals("-15 -7 1 9 17 25", runMethodGetConsoleOut(() -> AppMain.loop4()).trim());
    }

    @Test
    public void testLoop5() {
        assertEquals("10 9 8 7 6 5", runMethodGetConsoleOut(() -> AppMain.loop5()).trim());
    }

    @Test
    public void testLoop6() {
        assertEquals("6 8 10", runMethodGetConsoleOut(() -> AppMain.loop6()).trim());
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
