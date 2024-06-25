import static org.junit.Assert.assertEquals;

import org.junit.*;

public class BasicTests {
    @Test
    public void testSequenceA() {
        Sequence seq = new Sequence(3, 1);
        assertEquals("Sequence: 3, 4, 5, 6, 7, 8, ...", seq.toString());
    }

    @Test
    public void testSeriesB() {
        Series ser = new Series(new Sequence(3, 1));
        assertEquals("Series Sum (5 terms): 25", ser.toString(5));
    }

    @Test
    public void testBoth() {
        Sequence seq = new Sequence(20, 5);
        Series ser = new Series(seq);
        assertEquals("Series Sum (10 terms): 425", ser.toString(10));
    }
}
