import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import org.junit.*;

public class BasicTests {
    @Test
    public void testExercise5() {
        String res = runMethodGetConsoleOut( () -> Main.exercise5() );
        String[] lines = res.split("\r\n");
        assertTrue(lines.length == 6);
        assertEquals(lines[0], "A \"quoted\" String is");
        assertEquals(lines[1], "'much' better if you learn");
        assertEquals(lines[2], "the rules of \"escape sequences.\"");
        assertEquals(lines[3], "Also, \"\" represents an empty String.");
        assertEquals(lines[4], "Don't forget: use \\\" instead of \" !");
        assertEquals(lines[5], "'' is not the same as \"");
    }

    @Test
    public void testExercise6() {
        String res = runMethodGetConsoleOut( () -> Main.exercise6() );
        assertEquals(res, "Hello, World!\r\n");
    }

    @Test
    public void testExercise7() {
        String res = runMethodGetConsoleOut( () -> Main.exercise7() );
        String[] lines = res.split("\r\n");
        assertTrue(lines.length >= 3);
        assertEquals(lines[0], "There's one thing every coder must understand:");
        assertEquals(lines[1], "The System.out.println command.");
        assertEquals(lines[2], "");
    }

    @Test
    public void testExercise8() {
        String res = runMethodGetConsoleOut( () -> Main.exercise8() );
        String[] lines = res.split("\r\n");
        assertTrue(lines.length >= 5);
        assertEquals(lines[0], "//////////////////////");
        assertEquals(lines[1], "|| Vectory is mine! ||");
        assertEquals(lines[2], "\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\");
        assertEquals(lines[3], "|| Vectory is mine! ||");
        assertEquals(lines[4], "\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\");
    }

    @Test
    public void testExercise9() {
        String res = runMethodGetConsoleOut( () -> Main.exercise9() );
        String[] lines = res.split("\r\n");
        assertTrue(lines.length >= 4);
        assertEquals(lines[0], "  _______");
        assertEquals(lines[1], " /       \\");
        assertEquals(lines[2], "/         \\");
        assertEquals(lines[3], "-\"-'-\"-'-\"-");
    }

    @Test
    public void testExercise10() {
        String res = runMethodGetConsoleOut( () -> Main.exercise10() );
        String[] lines = res.split("\r\n");
        assertTrue(lines.length >= 6);
        assertEquals(lines[0],  "  _______");
        assertEquals(lines[1],  " /       \\");
        assertEquals(lines[2],  "/         \\");
        assertEquals(lines[3],  "\\         /");
        assertEquals(lines[4],  " \\_______/");
        assertEquals(lines[5],  "");
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
