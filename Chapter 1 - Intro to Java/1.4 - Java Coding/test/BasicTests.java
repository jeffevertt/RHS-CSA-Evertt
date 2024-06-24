import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import org.junit.*;

public class BasicTests {
    @Test
    public void testFish() {
        String res = runMethodGetConsoleOut( () -> AppMain.printFish() );
        assertEquals("><(((('>\r\n", res);
    }

    @Test
    public void testCat() {
        String res = runMethodGetConsoleOut( () -> AppMain.printCat() );
        assertEquals("=^..^=\r\n", res);
    }

    @Test
    public void testRose() {
        String res = runMethodGetConsoleOut( () -> AppMain.printRose() );
        assertEquals("--------{---(@\r\n", res);
    }

    @Test
    public void testWorm() {
        String res = runMethodGetConsoleOut( () -> AppMain.printWorm() );
        assertTrue(res.length() > 10);
        assertEquals("_/\\_", res.substring(0, 4));
        assertEquals("\\__0>\r\n", res.substring(res.length() - 7));
    }

    @Test
    public void testPersonalMessage() {
        String res = runMethodGetConsoleOut( () -> AppMain.printPersonalMessage() );
        assertTrue(res.length() > 20);
        assertEquals("/·._.·\\·.", res.substring(0, 9));
        assertEquals("_.·/·._.·\\\r\n", res.substring(res.length() - 12));
        
    }

    @Test
    public void testCaterpillar() {
        String res = runMethodGetConsoleOut( () -> AppMain.printCaterpillar() );
        assertTrue(res.length() > 19);
        assertEquals(",/\\,/\\,/\\,/\\", res.substring(0, 12));
        assertEquals("/\\,/\\,o\r\n", res.substring(res.length() - 9));
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
