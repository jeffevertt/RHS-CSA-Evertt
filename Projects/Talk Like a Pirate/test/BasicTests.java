import static org.junit.Assert.assertEquals;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import org.junit.*;

public class BasicTests {
    @Test
    public void testXYZ() {
        String actual = runMethodGetConsoleOut(() -> AppMain.main(null));
        String[] actualLines = actual.split("\n");
        String expected = "-------------- ENGLISH --------------\r\n" + //
                          "Hello Stranger!\r\n" + //
                          "\r\n" + //
                          "Can you help me find my old Friend?\r\n" + //
                          "The time is late and I wonder where he is...\r\n" + //
                          "-------------- PIRATE! --------------\r\n" + //
                          "Arrr! Ahoy Scurvy dog!\r\n" + //
                          "\r\n" + //
                          "Can ye help me find me barnacle covered Bucko?\r\n" + //
                          "Th' time be late and I wonder whar he be...\r\n" + //
                          "-------------------------------------";
        String[] expectedLines = expected.split("\r\n");
        for (int i = 0; i < expectedLines.length; i++) {
            assertEquals(expectedLines[i].trim(), actualLines[i].replace("\r", "").trim());
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
