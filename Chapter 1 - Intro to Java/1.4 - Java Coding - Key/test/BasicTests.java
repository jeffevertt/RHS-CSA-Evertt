import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import org.junit.*;

public class BasicTests {
    @Test
    public void testFish() {
        String res = runMethodGetConsoleOut( () -> AppMain.printFish() );
        assertEquals(res, "><(((('>\r\n");
    }

    @Test
    public void testCat() {
        String res = runMethodGetConsoleOut( () -> AppMain.printCat() );
        assertEquals(res, "=^..^=\r\n");
    }

    @Test
    public void testRose() {
        String res = runMethodGetConsoleOut( () -> AppMain.printRose() );
        assertEquals(res, "--------{---(@\r\n");
    }

    @Test
    public void testWorm() {
        String res = runMethodGetConsoleOut( () -> AppMain.printWorm() );
        assertTrue(res.length() > 10);
        assertEquals(res.substring(0, 4), "_/\\_");
        assertEquals(res.substring(res.length() - 7), "\\__0>\r\n");
    }

    @Test
    public void testPersonalMessage() {
        String res = runMethodGetConsoleOut( () -> AppMain.printPersonalMessage() );
        assertTrue(res.length() > 20);
        assertEquals(res.substring(0, 9), "/·._.·\\·.");
        assertEquals(res.substring(res.length() - 12), "_.·/·._.·\\\r\n");
        
    }

    @Test
    public void testCaterpillar() {
        String res = runMethodGetConsoleOut( () -> AppMain.printCaterpillar() );
        assertTrue(res.length() > 19);
        assertEquals(res.substring(0, 12), ",/\\,/\\,/\\,/\\");
        assertEquals(res.substring(res.length() - 9), "/\\,/\\,o\r\n");
    }

    @Test
    public void testMickey() {
        String res = runMethodGetConsoleOut( () -> AppMain.printMickey() );
        assertTrue(res.contains(".'_   `  _     'Y88888888b"));
        assertTrue(res.contains("_  | /  \\  /  \\      8888888888.d888888b."));
        assertTrue(res.contains("/          `          `      `Y8888888888888888"));
        assertTrue(res.contains("'._                  .'     .'  `Y888888P`"));
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
