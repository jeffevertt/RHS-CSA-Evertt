package logic;

import game.ElevatorController;

public class MyElevatorController implements ElevatorController {
    public String getStudentName() {
        return "<YOUR NAME>";   // <-- TODO: Replace with your name
    }

    // Event: "outside-the-elevator" request, requesting an elevator.
    public void onElevatorRequest(int floorIdx, Direction dir) {
        // ...
    }

    // Event: "inside-the-elevator" request, requesting to go to a floor.
    public void onFloorRequest(int elevatorIdx, int floorIdx) {
        // ...
    }

    // Event: Called when the elevator has reached a floor, has its doors open, and zombie 'transations' are complete.
    //  This is called after, all Zombies have had time to enter/exit the elevator.
    public void onElevatorIdle(int elevatorIdx) {
        // ...
    }

    // Event: Called each frame of the simulation (i.e. called continuously)
    public void onUpdate(double deltaTime) {
    }
}