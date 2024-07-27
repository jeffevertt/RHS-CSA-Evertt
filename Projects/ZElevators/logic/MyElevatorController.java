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
    public int getStudentPeriod() {
        return -1;              // <-- TODO: Replace with your class period
    }

    // Event: Game has started
    public void onGameStarted(Game game) {
        this.game = game;
    }

    // Event: "outside-the-elevator" request, requesting an elevator.
    //  The event will be triggered when a button is pressed AND when it is cleared (enableRequest indicates which).
    public void onElevatorRequestChanged(int floorIdx, Direction dir, boolean enableRequest) {
        System.out.println("onElevatorRequestChanged(" + floorIdx + ", " + dir + ", " + enableRequest + ")");

        // TODO

    }

    // Event: "inside-the-elevator" request, requesting to go to a floor.
    //  The event will be triggered when a button is pressed AND  & when it is cleared (enableRequest indicates which).
    public void onFloorRequestChanged(int elevatorIdx, int floorIdx, boolean enableRequest) {
        System.out.println("onFloorRequesteChanged(" + elevatorIdx + ", " + floorIdx + ", " + enableRequest + ")");

        // TODO

    }

    // Event: Elevator has arrived at the floor & doors are open.
    public void onElevatorArrivedAtFloor(int elevatorIdx, int floorIdx, Direction headingDirection) {
        System.out.println("onElevatorArrivedAtFloor(" + elevatorIdx + ", " + floorIdx + ", " + headingDirection + ")");

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