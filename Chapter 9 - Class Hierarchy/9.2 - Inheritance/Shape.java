public class Shape {
    // Data
    public String name;
    public String color;

    // Constructor(s)
    public Shape(String name, String color) {
        this.name = name;
        this.color = color;
    }

    // Methods
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    // Methods (to be overridden by subclasses)
    public double calcArea() {
        // To be implemented in subclasses
        return -1;
    }
    public double calcPerimeter() {
        // To be implemented in subclasses
        return -1;
    }

    // toString
    public String toString() {
        return name + "(" + color + "): " + 
                    "area = " + String.format("%.1f", calcArea()) + 
                    ", perim = " + String.format("%.1f", calcPerimeter());
    }
}
