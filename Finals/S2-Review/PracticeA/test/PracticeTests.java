import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.*;

public class PracticeTests {
    @Test
    public void testStrPatternA() {
        Practice p = new Practice();
        assertEquals("--\n-----\n--------", p.strPatternA(3, 2));
        assertEquals("-----\n-------", p.strPatternA(2, 5));
        assertEquals("---", p.strPatternA(1, 3));
        assertEquals("---\n-----", p.strPatternA(2, 3));
    }

    @Test
    public void testStrPatternB() {
        Practice p = new Practice();
        assertEquals("abc, abc, abc", p.strPatternB("abc", 3));
        assertEquals("x, x, x, x", p.strPatternB("x", 4));
        assertEquals("ttfn", p.strPatternB("ttfn", 1));
        assertEquals("", p.strPatternB("", 5));
    }

    @Test
    public void testPercToLetterGrade() {
        Practice p = new Practice();
        assertEquals("A", p.percToLetterGrade(95));
        assertEquals("B", p.percToLetterGrade(89.9));
        assertEquals("C", p.percToLetterGrade(70));
        assertEquals("D", p.percToLetterGrade(61.8));
        assertEquals("N", p.percToLetterGrade(20));
    }

    @Test
    public void testStrList() {
        Practice p = new Practice();
        int[] resultA = { 1, 2, 3, 4 };
        Assert.assertArrayEquals(resultA, p.strToList("1,2,3,4", ','));
        int[] resultB = { 5, 2 };
        Assert.assertArrayEquals(resultB, p.strToList("5; 2", ';'));
        Assert.assertArrayEquals(resultB, p.strToList("5; ;  2", ';'));
        int[] resultC = { 12, 51 };
        Assert.assertArrayEquals(resultC, p.strToList("12* 51", '*'));
        assertNull(p.strToList("5; 2", ','));
        assertNull(p.strToList("5; 1 2", ';'));
    }
}
