import static org.junit.Assert.assertArrayEquals;

import org.junit.*;

public class BasicTests {
    @Test
    public void testCreateArrayOfLengthTen() {
        int[] expected = { -1, -1, -1, -1, -1, -1, -1, -1, -1, -1 };
        assertArrayEquals( expected, AppMain.createArrayOfLengthTen() );
    }

    @Test
    public void testCreateIntArray() {
        int[] list = { 6, 1, 8, 0, -1 };
        int[] expected = { 2, 2, 2, 2, 2 };
        assertArrayEquals(expected, AppMain.setAllElements(list, 2));
    } 
}
