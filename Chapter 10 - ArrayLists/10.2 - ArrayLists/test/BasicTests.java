import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Arrays;

import org.junit.*;

public class BasicTests {
    @Test
    public void testToArrayList() {
        Practice practice = new Practice();
        String[] arrayABC = { "a", "b", "c" };
        assertEquals("[a, b, c]", practice.toArrayList( arrayABC ).toString());
    }

    @Test
    public void testReverseList() {
        Practice practice = new Practice();
        String[] arrayABC = { "a", "b", "c" };
        String[] reverseABC = { "c", "b", "a" };
        ArrayList<String> reversedABC = new ArrayList<>( Arrays.asList( reverseABC ) );
        assertEquals(reversedABC.toString(), practice.reverseList( arrayABC ).toString());
    }

    @Test
    public void testSplitString() {
        Practice practice = new Practice();
        String[] splitStrings = { "This", "is", "a", "test" };
        assertArrayEquals(splitStrings, practice.splitString( "This is a test" ).toArray());
    }

    @Test
    public void testJustPrimes() {
        Practice practice = new Practice();
        int[] array123 = { 1, 3, 4, 7, 8, 11 };
        Integer[] primes123 = { 3, 7, 11 };
        assertArrayEquals(primes123, practice.justPrimes( array123 ).toArray());
    }
}
