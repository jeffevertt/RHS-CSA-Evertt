import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

import org.junit.*;

public class BasicTests {
    @Test
    public void testArrayToString() {
        int[] list = { 1, 2, 3, 4 };
        assertEquals("1, 2, 3, 4", AppMain.arrayToString(list));
    }

    @Test
    public void testCreateIntArray() {
        int[] list = { 2, 2, 2, 2, 2 };
        assertArrayEquals(list, AppMain.createIntArray(5, 2));
    }

    @Test
    public void testCountAdjacentMatches() {
        int[] list = { 1, 2, 2, 3, 2, 4, 4 };
        assertEquals(2, AppMain.countAdjacentMatches(list));
        list = new int[] { 5, 5, 5 };
        assertEquals(2, AppMain.countAdjacentMatches(list));
    }    
}
