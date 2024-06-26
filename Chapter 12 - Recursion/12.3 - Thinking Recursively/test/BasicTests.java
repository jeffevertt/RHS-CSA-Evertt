import static org.junit.Assert.assertEquals;

import org.junit.*;

public class BasicTests {
    @Test
    public void testIsPalindrome() {
        Practice p = new Practice();
        assertEquals(true, p.isPalindrome("racecar"));
        assertEquals(false, p.isPalindrome("test"));
    }

    @Test
    public void testSumDigits() {
        Practice p = new Practice();
        assertEquals(6, p.sumDigits(123));
        assertEquals(14, p.sumDigits(572));
    }

    @Test
    public void testCreateString() {
        Practice p = new Practice();
        assertEquals("321.123", p.createString(3));
        assertEquals("54321.12345", p.createString(5));
    }  
}
