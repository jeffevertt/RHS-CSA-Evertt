import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import org.junit.*;

public class BasicTests {
    @Test
    public void testFirstStars() {
        assertEquals(true, Conditionals.firstStars("**"));
        assertEquals(false, Conditionals.firstStars("*-"));
    }

    @Test
    public void testCompareDouble() {
        assertTrue(0.01 == AppMain.EPSILON);
        assertEquals(true, Conditionals.compareDouble(6.001, 6));
        assertEquals(false, Conditionals.compareDouble(6.011, 6));
    }

    @Test
    public void testLogicCheck() {
        assertEquals(true, Conditionals.logicCheck(11, 12, true));
        assertEquals(false, Conditionals.logicCheck(9, 8, false));
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
