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
        assertEquals("A \"quoted\" String is", lines[0]);
        assertEquals("'much' better if you learn", lines[1]);
        assertEquals("the rules of \"escape sequences.\"", lines[2]);
        assertEquals("Also, \"\" represents an empty String.", lines[3]);
        assertEquals("Don't forget: use \\\" instead of \" !", lines[4]);
        assertEquals("'' is not the same as \"", lines[5]);
    }

    @Test
    public void testExercise6() {
        String res = runMethodGetConsoleOut( () -> Main.exercise6() );
        assertEquals("Hello, World!\r\n", res);
    }

    @Test
    public void testExercise7() {
        String res = runMethodGetConsoleOut( () -> Main.exercise7() );
        String[] lines = res.split("\r\n");
        assertTrue(lines.length >= 3);
        assertEquals("There's one thing every coder must understand:", lines[0]);
        assertEquals("The System.out.println command.", lines[1]);
        assertEquals("", lines[2]);
    }

    @Test
    public void testExercise8() {
        String res = runMethodGetConsoleOut( () -> Main.exercise8() );
        String[] lines = res.split("\r\n");
        assertTrue(lines.length >= 5);
        assertEquals("//////////////////////", lines[0]);
        assertEquals("|| Vectory is mine! ||", lines[1]);
        assertEquals("\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\", lines[2]);
        assertEquals("|| Vectory is mine! ||", lines[3]);
        assertEquals("\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\", lines[4]);
    }

    @Test
    public void testExercise9() {
        String res = runMethodGetConsoleOut( () -> Main.exercise9() );
        String[] lines = res.split("\r\n");
        assertTrue(lines.length >= 4);
        assertEquals("  _______", lines[0]);
        assertEquals(" /       \\", lines[1]);
        assertEquals("/         \\", lines[2]);
        assertEquals("-\"-'-\"-'-\"-", lines[3]);
    }

    @Test
    public void testExercise10() {
        String res = runMethodGetConsoleOut( () -> Main.exercise10() );
        String[] lines = res.split("\r\n");
        assertTrue(lines.length >= 6);
        assertEquals("  _______", lines[0]);
        assertEquals(" /       \\", lines[1]);
        assertEquals("/         \\", lines[2]);
        assertEquals("\\         /", lines[3]);
        assertEquals(" \\_______/", lines[4]);
        assertEquals("", lines[5]);
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
