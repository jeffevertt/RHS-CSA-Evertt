import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.*;

public class PracticeTests {
    @Test
    public void testStrPatternA() {
        Practice p = new Practice();
        assertEquals(p.strPatternA(3, 2), "--\n-----\n--------");
        assertEquals(p.strPatternA(2, 5), "-----\n-------");
        assertEquals(p.strPatternA(1, 3), "---");
        assertEquals(p.strPatternA(2, 3), "---\n-----");
    }

    @Test
    public void testStrPatternB() {
        Practice p = new Practice();
        assertEquals(p.strPatternB("abc", 3), "abc, abc, abc");
        assertEquals(p.strPatternB("x", 4), "x, x, x, x");
        assertEquals(p.strPatternB("ttfn", 1), "ttfn");
        assertEquals(p.strPatternB("", 5), "");
    }

    @Test
    public void testPercToLetterGrade() {
        Practice p = new Practice();
        assertEquals(p.percToLetterGrade(95), "A");
        assertEquals(p.percToLetterGrade(89.9), "B");
        assertEquals(p.percToLetterGrade(70), "C");
        assertEquals(p.percToLetterGrade(61.8), "D");
        assertEquals(p.percToLetterGrade(20), "N");
    }

    @Test
    public void testStrList() {
        Practice p = new Practice();
        int[] resultA = { 1, 2, 3, 4 };
        Assert.assertArrayEquals(p.strToList("1,2,3,4", ','), resultA);
        int[] resultB = { 5, 2 };
        Assert.assertArrayEquals(p.strToList("5; 2", ';'), resultB);
        Assert.assertArrayEquals(p.strToList("5; ;  2", ';'), resultB);
        int[] resultC = { 12, 51 };
        Assert.assertArrayEquals(p.strToList("12* 51", '*'), resultC);
        assertNull(p.strToList("5; 2", ','));
        assertNull(p.strToList("5; 1 2", ';'));
    }
}
