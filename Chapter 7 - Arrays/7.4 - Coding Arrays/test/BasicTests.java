import static org.junit.Assert.assertEquals;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import org.junit.*;

public class BasicTests {
    @Test
    public void testA() {
        AppMain.UPDATE_LOOPS = 10;
        AppMain.ducks = new Duck[] { null, null, null, null, null, new MotherDuck(), new Duck(), new Duck(), new Duck(), null, null };

        String res = runMethodGetConsoleOut(() -> AppMain.main(null));
        assertEquals("     Dddd  \r\n" + //
                     "    Dddd   \r\n" + //
                     "   Dddd    \r\n" + //
                     "  Dddd     \r\n" + //
                     " Dddd      \r\n" + //
                     "Dddd       \r\n" + //
                     "ddd       D\r\n" + //
                     "dd       Dd\r\n" + //
                     "d       Ddd\r\n" + //
                     "       Dddd\r\n" + //
                     "      Dddd \r\n", res);
    }

    @Test
    public void testB() {
        AppMain.UPDATE_LOOPS = 4;
        AppMain.ducks = new Duck[] { null, new MotherDuck(), new Duck(), new Duck(), new Duck(), new Duck(), new Duck(), null, null };

        String res = runMethodGetConsoleOut(() -> AppMain.main(null));
        assertEquals(" Dddddd  \r\n" + //
                     "Dddddd   \r\n" + //
                     "ddddd   D\r\n" + //
                     "dddd   Dd\r\n" + //
                     "ddd   Ddd\r\n", res);
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
