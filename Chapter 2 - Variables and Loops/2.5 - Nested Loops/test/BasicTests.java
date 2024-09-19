import static org.junit.Assert.assertEquals;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import org.junit.*;

public class BasicTests {
    @Test
    public void testPart1() {
        String res = runMethodGetConsoleOut(() -> AppMain.part1());
        String[] lines = res.split("\r\n");
        assertEquals(6, lines.length);
        assertEquals("1", lines[0]);
        assertEquals("22", lines[1]);
        assertEquals("333", lines[2]);
        assertEquals("4444", lines[3]);
        assertEquals("55555", lines[4]);
        assertEquals("666666", lines[5]);
    }

    @Test
    public void testPart2() {
        String res = runMethodGetConsoleOut(() -> AppMain.part2());
        String[] lines = res.split("\r\n");
        assertEquals(5, lines.length);
        assertEquals("    1", lines[0]);
        assertEquals("   2", lines[1]);
        assertEquals("  3", lines[2]);
        assertEquals(" 4", lines[3]);
        assertEquals("5", lines[4]);
    }

    @Test
    public void testPart3() {
        String res = runMethodGetConsoleOut(() -> AppMain.part3());
        String[] lines = res.split("\r\n");
        assertEquals(3, lines.length);
        assertEquals("000111222333444555666777888999", lines[0].trim());
        assertEquals("000111222333444555666777888999", lines[1].trim());
        assertEquals("000111222333444555666777888999", lines[2].trim());
    }

    @Test
    public void testPart4() {
        String res = runMethodGetConsoleOut(() -> AppMain.part4());
        String[] lines = res.split("\r\n");
        assertEquals(5, lines.length);
        assertEquals("-----1-----", lines[0].trim());
        assertEquals("----333----", lines[1].trim());
        assertEquals("---55555---", lines[2].trim());
        assertEquals("--7777777--", lines[3].trim());
        assertEquals("-999999999-", lines[4].trim());
    }

    @Test
    public void testPart5() {
        String res = runMethodGetConsoleOut(() -> AppMain.part5());
        String[] lines = res.split("\r\n");
        assertEquals(9, lines.length);
        assertEquals("9 8 7 6 5 4 3 2 1", lines[0].trim());
        assertEquals("8 7 6 5 4 3 2 1 9", lines[1].trim());
        assertEquals("7 6 5 4 3 2 1 9 8", lines[2].trim());
        assertEquals("6 5 4 3 2 1 9 8 7", lines[3].trim());
        assertEquals("5 4 3 2 1 9 8 7 6", lines[4].trim());
        assertEquals("4 3 2 1 9 8 7 6 5", lines[5].trim());
        assertEquals("3 2 1 9 8 7 6 5 4", lines[6].trim());
        assertEquals("2 1 9 8 7 6 5 4 3", lines[7].trim());
        assertEquals("1 9 8 7 6 5 4 3 2", lines[8].trim());
    }

    @Test
    public void testPart6() {
        String res = runMethodGetConsoleOut(() -> AppMain.part6());
        String[] lines = res.split("\r\n");
        assertEquals(1, lines.length);
        assertEquals("1 1 2 3 5 8 13 21 34 55 89 144 233 377 610", lines[0].trim());
    }

    @Test
    public void testPart7() {
        String res = runMethodGetConsoleOut(() -> AppMain.part7());
        String[] lines = res.split("\r\n");
        assertEquals(2, lines.length);
        assertEquals("         |         |         |         |", lines[0]);
        assertEquals("1234567890123456789012345678901234567890", lines[1].trim());
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
