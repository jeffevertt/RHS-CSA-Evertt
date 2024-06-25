import static org.junit.Assert.assertEquals;

import org.junit.*;

public class BasicTests {
    @Test
    public void testRect() {
        Rectangle rect = new Rectangle("blue", 10, 20);
        assertEquals("Rectangle(blue): area = 200.0, perim = 60.0", rect.toString());
    }

    @Test
    public void testSquare() {
        Square square = new Square("green", 15);
        assertEquals("Square(green): area = 225.0, perim = 60.0", square.toString());
    }

    @Test
    public void testCircle() {
        Circle circle = new Circle("yellow", 5);
        assertEquals("Circle(yellow): area = 78.5, perim = 31.4", circle.toString());
    }
}
