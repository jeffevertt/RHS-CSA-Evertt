public class Location {
    private int x, y;

    public Location() {
        x = 0;
        y = 0;
    }
    public Location(int x, int y) {
        this.x = x;
        this.y = y;
    }
    public Location(Location other) {
        this.x = other.x;
        this.y = other.y;
    }
    public int getX() {
        return x;
    }
    public int getY() {
        return y;
    }
    public Location incRight() {
        x = x + 1;
        return this;
    }
    public Location incLeft() {
        x = x - 1;
        return this;
    }
    public Location incUp() {
        y = y + 1;
        return this;
    }
    public Location incDown() {
        y = y - 1;
        return this;
    }
    public boolean equals(Location other) {
        return (this.x == other.x) && (this.y == other.y);
    }
}
