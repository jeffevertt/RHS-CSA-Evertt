// Maze keeps track of the maze object itself.
//  The coordinate system is 0,0 in the lower left with
//    positive x going right & positive y going up.
//  It supports query methods, like "can go right".
//  It uses Location objects for representing positions.
public class Maze {
    public final static Location START = new Location(0, 8);
    public final static Location EXIT = new Location(32, 8);

    private String[] maze = { 
        "*********************************", 
        "     * ****      **   ***        ",
        "**** * *     ***    ***   **** **",
        "*    * ** **    ****** ****    **",
        "* ***     ***** ** ***    ** * **",
        "* ***  **  **       ***** ** *  *",
        "*      ***     ***           ** *",
        "* *****   *****  *  **** *** ** *",
        "*       *        *      ** ***  *",
        "*********************************"
    };
    
    private boolean isOnMapBoard(Location loc) {
        if (loc.getX() < 0 || loc.getX() >= maze[0].length()) {
            return false;
        }
        if (loc.getY() < 0 || loc.getY() >= maze.length) {
            return false;
        }
        return true;
    }
    public boolean isMazeLocationClear(Location loc) {
        if (!isOnMapBoard(loc)) {
            return false;
        }
        char ch = maze[maze.length - loc.getY() - 1].charAt(loc.getX());
        return (ch == ' ');
    }

    public boolean canGoRight(Location fromLoc) {
        Location toLoc = (new Location(fromLoc)).incRight();
        return isMazeLocationClear(toLoc);
    }
    public boolean canGoLeft(Location fromLoc) {
        Location toLoc = (new Location(fromLoc)).incLeft();
        return isMazeLocationClear(toLoc);
    }
    public boolean canGoUp(Location fromLoc) {
        Location toLoc = (new Location(fromLoc)).incUp();
        return isMazeLocationClear(toLoc);
    }
    public boolean canGoDown(Location fromLoc) {
        Location toLoc = (new Location(fromLoc)).incDown();
        return isMazeLocationClear(toLoc);
    }

    public Location getStartLoc() {
        return new Location(START);
    }
    public boolean isExit(Location loc) {
        return loc.equals(EXIT);
    }
    public int getWidth() {
        return maze[0].length();
    }
    public int getHeight() {
        return maze.length;
    }

    private boolean isLegalMove(Location from, Location to) {
        if (!isOnMapBoard(from) || !isOnMapBoard(to)) {
            return false;
        }
        return ((new Location(from)).incRight().equals(to) || 
                (new Location(from)).incLeft().equals(to) || 
                (new Location(from)).incUp().equals(to) || 
                (new Location(from)).incDown().equals(to));
    }
    private String[] copyMaze() {
        String[] copy = new String[maze.length];
        for (int i = 0; i < maze.length; i++) {
            copy[i] = maze[i];
        }
        return copy;
    }
    private void markMapLocation(String[] lclMaze, Location loc, boolean validPath) {
        String str = lclMaze[maze.length - loc.getY() - 1];
        String node = (validPath ? "O" : "x");
        str = str.substring(0, loc.getX()) + node + str.substring(loc.getX() + 1);
        lclMaze[maze.length - loc.getY() - 1] = str;
    }
    public void printMazeAndPath(Location[] path) {
        // update a copy of the maze
        String[] lclMaze = copyMaze();
        if (path != null) {
            Location prevLoc = null;
            boolean isValidPath = true;
            for (int i = 0; i < path.length; i++) {
                Location loc = path[i];
                isValidPath = isValidPath && ((prevLoc == null) || isLegalMove(prevLoc, loc));
                markMapLocation(lclMaze, loc, isValidPath);
                prevLoc = loc;
            }
        }

        // Print the maze
        for (int i = 0; i < lclMaze.length; i++) {
            System.out.println(lclMaze[i]);
        }
    }
}