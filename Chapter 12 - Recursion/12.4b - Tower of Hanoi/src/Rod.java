import java.util.ArrayList;

public class Rod {
    public TowersGameBase tower;
    public ArrayList<Disk> disks;

    public Rod(TowersGameBase tower) {
        this.tower = tower;
        disks = new ArrayList<Disk>();
    }

    public void pushDisk(Disk disk) {
        disks.add(disk);
    }

    public Disk popDisk() {
        if (disks.size() < 1) {
            return null;
        }
        Disk disk = disks.get(disks.size() - 1);
        disks.remove(disks.size() - 1);
        return disk;
    }

    public int numDisks() {
        return disks.size();
    }

    public boolean isEmpty() {
        return disks.size() == 0;
    }

    public boolean isValid() {
        if (disks.size() > tower.numDisks()) {
            return false;
        }
        for (int i = 1; i < disks.size(); i++) {
            if (disks.get(i - 1).getRadius() <= disks.get(i).getRadius()) {
                return false;
            }
        }
        return true;
    }
    public boolean isSolved() {
        if (!isValid()) {
            return false;
        }
        if (disks.size() != tower.numDisks()) {
            return false;
        }
        return true;
    }    

    public String toString() {
        String ret = "";
        int numRows = tower.numDisks();
        for (int i = 0; i < numRows; i++) { // prints top to bottom
            int diskIdx = numRows - i - 1;
            if (diskIdx < disks.size()) {
                ret += disks.get(diskIdx).toString();
            }
            else {
                ret += toStringEmptyRow();
            }
            ret += "\n";
        }
        return ret;
    }

    public String toStringEmptyRow() {
        String ret = "";
        int blankChars = Disk.getMaxChars(tower);
        for (int i = 0; i < blankChars / 2; i++) {
            ret += " ";
        }
        ret += "|";
        for (int i = 0; i < blankChars / 2 + (blankChars % 2); i++) {
            ret += " ";
        }
        return ret;
    }
}
