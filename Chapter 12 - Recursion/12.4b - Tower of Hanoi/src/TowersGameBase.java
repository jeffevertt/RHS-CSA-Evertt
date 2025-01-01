
public class TowersGameBase {
    public static final int DEFAULT_NUM_RODS = 3;
    public static final int LEADING_SPACES = 5;

    private int numDisks = -1;
    private boolean valid = true;
    private Rod[] rods = null;
    private boolean disableAnimation = false;

    public TowersGameBase(int numDisks) {
        this(numDisks, DEFAULT_NUM_RODS);
    }
    public TowersGameBase(int numDisks, int numRods) {
        // init
        this.numDisks = numDisks;

        // init the rods
        rods = new Rod[numRods];
        for (int i = 0; i < rods.length; ++i) {
            rods[i] = new Rod(this);
        }

        // init the disks
        for (int i = numDisks(); i >= 1; i--) {
            rods[0].pushDisk(new Disk(this, i));
        }

        // draw it
        drawFrame();
    }

    public int numDisks() {
        return numDisks;
    }
    public int numRows() {
        return numDisks;
    }
    public int numRods() {
        return rods.length;
    }
    public void disableAnimation() {
        disableAnimation = true;
    }

    // move single disk from srcRod to dstRod
    public boolean moveDisk(int srcRod, int dstRod) {
        // validate
        if (srcRod < 0 || srcRod >= numRods()) {
            return false;
        }
        if (dstRod < 0 || dstRod >= numRods()) {
            return false;
        }

        // do the move
        Disk disk = rods[srcRod].popDisk();
        rods[dstRod].pushDisk(disk);

        return isValid();
    }

    public boolean isSolved() {
        // corner case
        if (numRods() <= 1) {
            return false;
        }

        // check for valid
        if (!isValid()) {
            return false;
        }

        // all rods empty except for the last
        for (int i = 0; i < numRods() - 1; ++i) {
            if (!rods[i].isEmpty()) {
                return false;
            }
        }

        // far right / last needs to be in order
        Rod farRight = rods[rods.length - 1];
        return farRight.isSolved();
    }

    public boolean isValid() {
        // count the number of disks
        int diskCount = 0;
        for (int i = 0; i < numRods(); i++) {
            diskCount += rods[i].numDisks();
        }
        if (diskCount != numDisks()) {
            valid = false;
        }

        // validate order for each rod
        for (int i = 0; i < numRods(); i++) {
            if (!rods[i].isValid()) {
                valid = false;
            }
        }

        return valid;
    }

    public String toString() {
        String ret = "";

        // convert each rod to strings
        String[][] rodStrs = new String[numRows()][numRods()];
        for (int i = 0; i < numRods(); i++) {
            String rodStr = rods[i].toString();
            String[] splits = rodStr.split("\n");
            for (int j = 0; j < Math.min(splits.length, rodStrs.length); j++) {
                rodStrs[j][i] = splits[j];
            }
        }

        // merge the rods string
        for (int i = 0; i < rodStrs.length; i++) {
            for (int j = 0; j < LEADING_SPACES; j++) {
                ret += " ";
            }
            for (int j = 0; j < rodStrs[i].length; j++) {
                ret += " " + rodStrs[i][j] + "  ";
            }
            ret += "\n";
        }

        // bases
        for (int j = 0; j < LEADING_SPACES; j++) {
            ret += " ";
        }
        for (int i = 0; i < numRods(); i++) {
            ret += " ";
            for (int j = 0; j < Disk.getMaxChars(this) + 2; j++) {
                ret += "-";
            }
            ret += " ";
        }

        return ret;
    }

    public void drawFrame() {
        drawFrame(false);
    }
    public void drawFinalFrame() {
        drawFrame(0, true);
    }
    public void drawFrame(boolean finalFrame) {
        int delayMilliSeconds = (int)(12000 * (1.0 / Math.pow(2, numDisks())));
        delayMilliSeconds = Math.max(Math.min(delayMilliSeconds, 1000), 10);
        drawFrame(delayMilliSeconds, finalFrame);
    }
    public void drawFrame(int delayMilliSeconds, boolean finalFrame) {  
        if (disableAnimation) {
            return;
        }

        // delay a for a short time (leave it on the screen)
        try {
            if (delayMilliSeconds > 0) {
                Thread.sleep(delayMilliSeconds);
            }
        } 
        catch (Exception ex) {
        }

        // clear the terminal/console
        clearTerminal();

        // figure out the color to use
        String colorCode = isValid() ? "\033[32m" : "\033[31m";
        if (finalFrame && isValid()) {
            colorCode = "\033[33m";
        }

        // and print the tower
        System.out.println(colorCode);
        System.out.println(toString());
        System.out.println("\033[0m"); // reset the color
    }

    public void clearTerminal() {
        System.out.print("\033[H\033[2J");  
        System.out.flush();
    }
}
