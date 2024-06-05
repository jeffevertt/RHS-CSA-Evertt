import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.*;

public class PracticeTests {
    @Test
    public void strCatColumn() {
        Practice p = new Practice();
        String[][] m1 = { { "a", "b", "c" }, { "d", "e", "f" } };
        assertEquals("ad", p.strCatColumn(m1, 0));
        assertEquals("be", p.strCatColumn(m1, 1));
        assertNull(p.strCatColumn(m1, 3));
        assertNull(p.strCatColumn(m1, -1));
        String[][] m2 = { { "sbd", "axz" }, { "yy", "vhs" }, { " t", "shs" }, { "**", "854" } };
        assertEquals("sbdyy t**", p.strCatColumn(m2, 0));
        assertEquals("axzvhsshs854", p.strCatColumn(m2, 1));
    }

    @Test
    public void calcSeriesRecursive() {
        Practice p = new Practice();
        assertEquals(15, p.calcSeriesRecursive(2,1,3));
        assertEquals(-114, p.calcSeriesRecursive(-3,10,12));
        assertEquals(0, p.calcSeriesRecursive(-3,10,0));
    }

    @Test
    public void createFibonacciArray() {
        Practice p = new Practice();
        int[] fibA = { 1, 1, 2, 3, 5 };
        assertArrayEquals(fibA, p.createFibonacciArray(1, 5));
        int[] fibB = { 8, 13, 21, 34 };
        assertArrayEquals(fibB, p.createFibonacciArray(8, 4));
        assertNull(p.createFibonacciArray(4, 3));
        int[] fibC = { 233, 377 };
        assertArrayEquals(fibC, p.createFibonacciArray(233, 2));
    }
}
