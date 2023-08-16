package logic;

import game.ElevatorController;
import game.Game;

public class MyElevatorController implements ElevatorController {
    public String getStudentName() {
        return "<YOUR NAME>";   // <-- TODO: Replace with your name
    }

    // Event: Game has started
    public void onGameStarted(Game game) {
        // TODO
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

    // Event: Called when the elevator has reached a floor, has its doors open, and zombie 'transations' are complete.
    //  This is called after, all Zombies have had time to enter/exit the elevator.
    public void onElevatorIdle(int elevatorIdx) {
        System.out.println("onElevatorIdle(" + elevatorIdx + ")");

        // TODO
    }

    // Event: Called each frame of the simulation (i.e. called continuously)
    public void onUpdate(double deltaTime) {
    }
}