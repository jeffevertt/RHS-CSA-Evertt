public class Disk {
    private TowersGameBase tower;
    private int radius;

    public Disk(TowersGameBase tower, int radius) {
        this.tower = tower;
        this.radius = radius;
    }

    public int getRadius() {
        return radius;
    }
    public static int getMaxChars(TowersGameBase tower) {
        return tower.numDisks() + 2;
    }

    public String toString() {
        String ret = "";
        int maxChars = getMaxChars(tower);
        for (int i = 0; i < (maxChars - radius) / 2; i++) {
            ret += " ";
        }
        ret += "[";
        for (int i = 0; i < radius; i++) {
            ret += "=";
        }
        ret += "]";
        for (int i = 0; i < (maxChars - radius) / 2 - ((maxChars - radius) % 2 == 1 ? 0 : 1); i++) {
            ret += " ";
        }
        return ret;
    }
}
