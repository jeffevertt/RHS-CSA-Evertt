import game.Game;
import logic.MyElevatorController;

public class Main {
    public static void main(String[] args) {
        // Game config (use this if you'd like to test a custom config, otherwise it is configured by the teacher's server config)
        // ...

        // Create up the game, it kicks off the whole process...
        if (!Game.Create(new MyElevatorController())) {
            throw new Error("Could not create the game :(");
        }
    }
}
