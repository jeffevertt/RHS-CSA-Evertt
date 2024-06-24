import static org.junit.Assert.assertEquals;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import org.junit.*;

public class BasicTests {
    @Test
    public void testFirstHalf() {
        assertEquals("01234", StringUtil.firstHalf("0123456789"));
    }

    @Test
    public void testBeforeSpace() {
        assertEquals("abcd", StringUtil.beforeSpace("abcd ef"));
    }

    @Test
    public void testAfterSpace() {
        assertEquals("ef", StringUtil.afterSpace("abcd ef"));
    }

    @Test
    public void testSwapAtSpace() {
        assertEquals("ef abcd", StringUtil.swapAtSpace("abcd ef"));
    }

    @Test
    public void testRepeatString() {
        assertEquals("abc abc abc ", StringUtil.repeatString("abc", 3));
    }
}
