import static org.junit.Assert.assertEquals;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import org.junit.*;

public class BasicTests {
    @Test
    public void testXYZ() {
        String actual = runMethodGetConsoleOut(() -> AppMain.main(null));
        String[] actualLines = actual.split("\r\n");
        String[] expected = { "*********************************",
                              "OOOOO*~****    ~ **   ***     OOO",
                              "**** * *     ***    ***   **** **",
                              "*    *~** **    ****** ****    **",
                              "* ***     ***** **~***    ** * **",
                              "* ***  **  **       ***** ** *  *",
                              "*      ***     ***           **~*",
                              "* *****   *****  *  **** *** ** *",
                              "*      ~*        *  ~   ** ***  *",
                              "*********************************" };

        // All solutions need to meet these requirements...
        //  '*' characters in expected matches actual
        //  'O' characters in expected matches actual
        //  '~' characters in expected correspond to a ' ' in actual
        assertEquals(expected.length, actualLines.length);
        for (int i = 0; i < expected.length; i++) {
            assertEquals(expected[i].length(), actualLines[i].length());
            for (int j = 0; j < expected[i].length(); j++) {
                char exp = expected[i].charAt(j);
                char act = actualLines[i].charAt(j);
                if (exp == '*') {
                    assertEquals('*', act);
                }
                else if (exp == 'O') {
                    assertEquals('O', act);
                }
                else if (exp == '~') {
                    assertEquals(' ', act);
                }
            }
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
