public class AppMain {
    private static Maze maze = new Maze();

    public static void main(String[] args) {
        // Write a recursive method to solve the maze. It should make use of
        //  the instance member variable maze. It knows about the maze and
        //  can answer questions about open spaces, possible moves, etc.
        // On each step, you can only move in a cardinal direction (left/right/up/down).
        // Your solution should take the form of an array of Locations
        // TODO: ...

        // Print out your solution, maze can do that for you (it's already written)
        Location[] replaceWithYourSln = new Location[0];
        maze.printMazeAndPath(replaceWithYourSln);
    }


    // Helper methods for marking locations as visited
    //  You are probably going to want to use: hasVisitedNode & markVisited.
    private static boolean[][] visited = null;
    private static boolean hasVisitedNode(Location loc) {
        if (!isValidVisitedLoc(loc)) {
            return true;
        }
        return visited[loc.getY()][loc.getX()];
    }
    private static void markVisited(Location loc) {
        if (!isValidVisitedLoc(loc)) {
            return;
        }
        visited[loc.getY()][loc.getX()] = true;
    }
    private static boolean isValidVisitedLoc(Location loc) {
        if (visited == null) {
            visited = new boolean[maze.getHeight()][maze.getWidth()];
        }
        if ((loc.getX() < 0) || (loc.getX() >= visited[0].length) ||
            (loc.getY() < 0) || (loc.getY() >= visited.length)) {
            return false;
        }
        return true;
    }
}
