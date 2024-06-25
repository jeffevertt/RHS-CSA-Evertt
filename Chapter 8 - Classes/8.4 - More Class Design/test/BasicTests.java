import static org.junit.Assert.assertEquals;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import org.junit.*;

public class BasicTests {
    @Test
    public void testLineA() {
        Line lineA = new Line(2, 5);
        assertEquals("y = 2.00x + 5.00", lineA.toString().trim());
    }

    @Test
    public void testLineC() {
        Line lineC = new Line(0, 1, 10, -5);
        assertEquals("y = -0.60x + 1.00", lineC.toString().trim());
    }

    @Test
    public void testLineInterceptAB() {
        Line lineA = new Line(2, 5);
        Line lineB = new Line(-0.5, -10);
        String res = runMethodGetConsoleOut(() -> lineA.printIntercept(lineB));
        assertEquals("intercept: (-6.00,-7.00)\r\n", res);
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
