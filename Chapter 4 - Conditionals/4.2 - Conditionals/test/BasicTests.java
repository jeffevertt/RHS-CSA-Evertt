import static org.junit.Assert.assertEquals;

import org.junit.*;

public class BasicTests {
    @Test
    public void testFirstStars() {
        assertEquals(true, Conditionals.firstStars("**"));
        assertEquals(false, Conditionals.firstStars("*-"));
    }

    @Test
    public void testCompareDouble() {
        //assertTrue(0.01 == AppMain.EPSILON);
        assertEquals(true, Conditionals.compareDouble(6.001, 6));
        assertEquals(false, Conditionals.compareDouble(6.011, 6));
    }

    @Test
    public void testLogicCheck() {
        assertEquals(true, Conditionals.logicCheck(11, 12, true));
        assertEquals(false, Conditionals.logicCheck(9, 8, false));
    }
}
