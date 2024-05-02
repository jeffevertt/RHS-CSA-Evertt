import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.*;

public class PracticeTests {
    @Test
    public void strCatColumn() {
        Practice p = new Practice();
        String[][] m1 = { { "a", "b", "c" }, { "d", "e", "f" } };
        assertEquals(p.strCatColumn(m1, 0), "ad");
        assertEquals(p.strCatColumn(m1, 1), "be");
        assertNull(p.strCatColumn(m1, 3));
        assertNull(p.strCatColumn(m1, -1));
        String[][] m2 = { { "sbd", "axz" }, { "yy", "vhs" }, { " t", "shs" }, { "**", "854" } };
        assertEquals(p.strCatColumn(m2, 0), "sbdyy t**");
        assertEquals(p.strCatColumn(m2, 1), "axzvhsshs854");
    }

    @Test
    public void calcSeriesRecursive() {
        Practice p = new Practice();
        assertEquals(p.calcSeriesRecursive(2,1,3), 15);
        assertEquals(p.calcSeriesRecursive(-3,10,12), -114);
        assertEquals(p.calcSeriesRecursive(-3,10,0), 0);
    }

    @Test
    public void createFibonacciArray() {
        Practice p = new Practice();
        int[] fibA = { 1, 1, 2, 3, 5 };
        assertArrayEquals(p.createFibonacciArray(1, 5), fibA);
        int[] fibB = { 8, 13, 21, 34 };
        assertArrayEquals(p.createFibonacciArray(8, 4), fibB);
        assertNull(p.createFibonacciArray(4, 3));
        int[] fibC = { 233, 377 };
        assertArrayEquals(p.createFibonacciArray(233, 2), fibC);
    }
}
