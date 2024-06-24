import static org.junit.Assert.assertEquals;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import org.junit.*;

public class BasicTests {
    @Test
    public void testSize3() {
        AppMain.SIZE = 3;
        String res = runMethodGetConsoleOut(() -> AppMain.main(null)).trim();
        String[] lines = res.split("\r\n");
        assertEquals(8, lines.length);
        assertEquals("#============#", lines[0]);
        assertEquals("|    <><>    |", lines[1]);
        assertEquals("|  <>....<>  |", lines[2]);
        assertEquals("|<>........<>|", lines[3]);
        assertEquals("|<>........<>|", lines[4]);
        assertEquals("|  <>....<>  |", lines[5]);
        assertEquals("|    <><>    |", lines[6]);
        assertEquals("#============#", lines[7]);
    }

    @Test
    public void testSize4() {
        AppMain.SIZE = 4;
        String res = runMethodGetConsoleOut(() -> AppMain.main(null)).trim();
        String[] lines = res.split("\r\n");
        assertEquals(10, lines.length);
        assertEquals("#================#", lines[0]);
        assertEquals("|      <><>      |", lines[1]);
        assertEquals("|    <>....<>    |", lines[2]);
        assertEquals("|  <>........<>  |", lines[3]);
        assertEquals("|<>............<>|", lines[4]);
        assertEquals("|<>............<>|", lines[5]);
        assertEquals("|  <>........<>  |", lines[6]);
        assertEquals("|    <>....<>    |", lines[7]);
        assertEquals("|      <><>      |", lines[8]);
        assertEquals("#================#", lines[9]);
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
