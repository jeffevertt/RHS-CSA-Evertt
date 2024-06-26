import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.regex.Pattern;

import org.junit.*;

public class BasicTests {
    private static final double EPSILON_BASIC = (NumberGuesserBase.MAX_NUMBER - NumberGuesserBase.MIN_NUMBER) * 0.1;

    @Test
    public void testBasic() {
        String output = runMethodGetConsoleOut(() -> AppMain.main(null));
        String[] lines = output.split("\r\n");
        assertEquals(4, lines.length);

        String avgGuessesStr = (lines[1].split(Pattern.quote(":"))[1]).split(Pattern.quote("("))[0].trim();
        double avgGuesses = Double.parseDouble(avgGuessesStr);

        double expected = (NumberGuesserBase.MAX_NUMBER - NumberGuesserBase.MIN_NUMBER) / 2.0;
        assertTrue(avgGuesses > expected - EPSILON_BASIC);
        assertTrue(avgGuesses < expected + EPSILON_BASIC);
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
