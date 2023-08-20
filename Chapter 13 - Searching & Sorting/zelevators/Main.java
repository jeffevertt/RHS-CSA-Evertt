import game.Game;
import logic.MyElevatorController;

public class Main {
    public static void main(String[] args) {
        // Game config
        // ...

        // Create up the game, it kicks off the whole process...
        if (!Game.Create(new MyElevatorController())) {
            throw new Error("Could not create the game :(");
        }
    }
}
