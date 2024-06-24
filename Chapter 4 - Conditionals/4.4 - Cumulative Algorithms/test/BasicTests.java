import static org.junit.Assert.assertEquals;

import org.junit.*;

public class BasicTests {
    @Test
    public void testAlphabet() {
        Cumulative c = new Cumulative();
        assertEquals("abcde", c.alphabet('a', 5));
        assertEquals("cdef", c.alphabet('c', 4));
    }

    @Test
    public void testFactorSum() {
        Cumulative c = new Cumulative();
        assertEquals(6, c.factorSum(6));
        assertEquals(10, c.factorSum(14));
    }

    @Test
    public void testIsPerfect() {
        Cumulative c = new Cumulative();
        assertEquals(true, c.isPerfect(6));
        assertEquals(false, c.isPerfect(14));
    }
}
