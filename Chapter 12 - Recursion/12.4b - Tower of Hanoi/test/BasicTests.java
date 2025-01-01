import static org.junit.Assert.assertTrue;

import org.junit.*;

public class BasicTests {
    @Test
    public void testBasic() {
        TowersGame game = new TowersGame(3);
        game.disableAnimation();
        game.solve();
        assertTrue(game.isSolved());
    }

    @Test
    public void testMidSized() {
        TowersGame game = new TowersGame(5);
        game.disableAnimation();
        game.solve();
        assertTrue(game.isSolved());
    }
}
