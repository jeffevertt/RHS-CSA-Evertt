package logic;

import game.ElevatorController;
import game.Game;

public class MyElevatorController implements ElevatorController {
    // Private member data
    private Game game;

    // Students should implement this function to return their name
    public String getStudentName() {
        return "<YOUR NAME>";   // <-- TODO: Replace with your name
    }

    // Event: Game has started
    public void onGameStarted(Game game) {
        this.game = game;
    }

    // Event: "outside-the-elevator" request, requesting an elevator.
    public void onElevatorRequest(int floorIdx, Direction dir) {
        System.out.println("onElevatorRequest(" + floorIdx + ", " + floorIdx + ")");

        // TODO
    }

    // Event: "inside-the-elevator" request, requesting to go to a floor.
    public void onFloorRequest(int elevatorIdx, int floorIdx) {
        System.out.println("onFloorRequest(" + elevatorIdx + ", " + floorIdx + ")");

        // TODO
    }

    // Event: Called each frame of the simulation (i.e. called continuously)
    public void onUpdate(double deltaTime) {
        if (game == null) {
            return;
        }

        // TODO
    }
}