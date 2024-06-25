import static org.junit.Assert.assertEquals;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import org.junit.*;

public class BasicTests {
    @Test
    public void testPoemC() {
        AppMain.POEM = AppMain.POEM_C;
        String stats = runMethodGetConsoleOut(() -> AppMain.main(null));
        String expected = "  a (3)\n  is (3)\n  poem (3)\n  simple (3)\n  this (2)\n  done (1)\n  so (1)";
        assertEquals(expected, cleanPoemStats(stats));
    }

    @Test
    public void testPoemB() {
        AppMain.POEM = AppMain.POEM_B;
        String stats = runMethodGetConsoleOut(() -> AppMain.main(null));
        String expected = "  oh (9)\n" + //
                        "  the (8)\n" + //
                        "  a (6)\n" + //
                        "  pete (6)\n" + //
                        "  pete's (6)\n" + //
                        "  so (6)\n" + //
                        "  he (5)\n" + //
                        "  in (5)\n" + //
                        "  and (4)\n" + //
                        "  he's (4)\n" + //
                        "  with (4)\n" + //
                        "  of (3)\n" + //
                        "  bark (2)\n" + //
                        "  boundless (2)\n" + //
                        "  eyes (2)\n" + //
                        "  jumps (2)\n" + //
                        "  love (2)\n" + //
                        "  one (2)\n" + //
                        "  our (2)\n" + //
                        "  paws (2)\n" + //
                        "  runs (2)\n" + //
                        "  this (2)\n" + //
                        "  to (2)\n" + //
                        "  adventure (1)\n" + //
                        "  all (1)\n" + //
                        "  around (1)\n" + //
                        "  beat (1)\n" + //
                        "  best (1)\n" + //
                        "  bright (1)\n" + //
                        "  carefree (1)\n" + //
                        "  central (1)\n" + //
                        "  cheerful (1)\n" + //
                        "  cherished (1)\n" + //
                        "  companion (1)\n" + //
                        "  dales (1)\n" + //
                        "  dear (1)\n" + //
                        "  decree (1)\n" + //
                        "  delight (1)\n" + //
                        "  echoing (1)\n" + //
                        "  every (1)\n" + //
                        "  fine (1)\n" + //
                        "  free (1)\n" + //
                        "  friend (1)\n" + //
                        "  from (1)\n" + //
                        "  glee (1)\n" + //
                        "  heart (1)\n" + //
                        "  hearts (1)\n" + //
                        "  here's (1)\n" + //
                        "  hills (1)\n" + //
                        "  i've (1)\n" + //
                        "  indeed (1)\n" + //
                        "  joy (1)\n" + //
                        "  joyful (1)\n" + //
                        "  keen (1)\n" + //
                        "  lead (1)\n" + //
                        "  leave (1)\n" + //
                        "  life's (1)\n" + //
                        "  lovable (1)\n" + //
                        "  loyal (1)\n" + //
                        "  meadow (1)\n" + //
                        "  morning (1)\n" + //
                        "  my (1)\n" + //
                        "  night (1)\n" + //
                        "  over (1)\n" + //
                        "  part (1)\n" + //
                        "  poem (1)\n" + //
                        "  praise (1)\n" + //
                        "  prints (1)\n" + //
                        "  pup (1)\n" + //
                        "  pure (1)\n" + //
                        "  sands (1)\n" + //
                        "  seen (1)\n" + //
                        "  shall (1)\n" + //
                        "  sign (1)\n" + //
                        "  sound (1)\n" + //
                        "  such (1)\n" + //
                        "  sun's (1)\n" + //
                        "  sweet (1)\n" + //
                        "  swift (1)\n" + //
                        "  tail (1)\n" + //
                        "  takes (1)\n" + //
                        "  tale (1)\n" + //
                        "  that (1)\n" + //
                        "  time (1)\n" + //
                        "  treasure (1)\n" + //
                        "  treat (1)\n" + //
                        "  under (1)\n" + //
                        "  wagging (1)\n" + //
                        "  warms (1)\n" + //
                        "  wild (1)";
        assertEquals(expected, cleanPoemStats(stats));
    }

    public String cleanPoemStats(String stats) {
        String[] lines = stats.split("\n");
        String cleaned = "";
        for (String line : lines) {
            line = line.trim();
            if (line.length() == 0) {
                continue;
            }
            cleaned += ((cleaned.length() > 0) ? "\n" : "") + "  " + line.trim();
        }
        return cleaned;
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
