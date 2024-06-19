public class AppMain {
    /* In this lab, you are implementing three classes: Rectangle, Square, and Circle.
     *  Each should be implemented using inheritance. They should all derive directly
     *  or indirectly from Shape (they are all Shapes). Note that a Square is also a
     *  Rectangle.
     * You should create .java files for each of your classes. You should minimize 
     *  code duplication. Start by checking out the Shape class. 
     * 
     * You are provided some code in main(...) to test out your classes. The provided
     *  code should compile when you are complete. 
     * 
     * When everything is working properly, your code should output the following...
     *    Rectangle(blue): area = 200.0, perim = 60.0
     *    Square(green): area = 225.0, perim = 60.0
     *    Circle(yellow): area = 78.5, perim = 31.4
     */
    public static void main(String[] args) {
        // Create up some shapes and print them
        Rectangle rect = new Rectangle("blue", 10, 20);
        Square square = new Square("green", 15);
        Circle circle = new Circle("yellow", 5);
        System.out.println(rect + "\n" + square + "\n" + circle);
    }
}