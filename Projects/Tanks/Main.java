import game.Game;

public class Main {
    public static void main(String[] args) {
        // Create up the game, it kicks off the whole process...
        if (!Game.Create()) {
            throw new Error("Could not create the game :(");
        }
    }
}
