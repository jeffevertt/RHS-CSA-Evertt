public class Move {
    public int r;
    public int c;
    public String mark;

    public Move() {
        this(0, 0, " ");
    }
    public Move(int r, int c, String mark) {
        this.r = r;
        this.c = c;
        this.mark = mark;
    }

    // Can be used to convert user input to a move.
    //  Takes a string of user input (e.g. "1,2") and a mark (e.g. "X"),
    //  and converts it to a move. Returns that move object. 
    public static Move StringToMove(String str, String mark) {
        // Parse the input
        Integer r = null, c = null;
        String comma = "";
        try {
            r = Integer.parseInt(str.substring(0, 1));
            comma = str.substring(1, 2);
            c = Integer.parseInt(str.substring(2, str.length()));
        } catch (Exception ex) {
            // Ignore them, we'll just try again
        }

        // Do a bit of error checking, then convert it to a move
        if (r != null && comma.equals(",") && c != null) {
            if ((r >= 0 && r <= 2) && (c >= 0 && c <= 2)) {
                return new Move(r, c, mark);
            }
        }
        return null;
    }
    public String toString() {
        return r + "," + c;
    }
}
