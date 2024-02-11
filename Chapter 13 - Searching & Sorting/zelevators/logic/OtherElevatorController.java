package logic;

import game.ElevatorController;
import game.Game;

// *********************************************************************************
//  This is the code for SECOND PLAYER (your code goes in MyElevatorController.java)
// *********************************************************************************

public class OtherElevatorController implements ElevatorController {

    // PASTE Other Person's code HERE (BELOW THIS)

    //  (replace these placeholder methods)
    // public String getStudentName() { return null; }
    // public int getStudentPeriod() { return -1; }
    // public void onGameStarted(Game game) { }
    // public void onElevatorRequestChanged(int floorIdx, Direction dir, boolean reqEnable) { }
    // public void onFloorRequestChanged(int elevatorIdx, int floorIdx, boolean reqEnable) { }
    // public void onElevatorArrivedAtFloor(int elevatorIdx, int floorIdx, Direction travelDirection) { }
    // public void onUpdate(double deltaTime) { }

    // PASTE Other Person's code HERE (ABOVE THIS)


    // TEST CODE
    public String getStudentName() { return "Other Player"; }
    public int getStudentPeriod() { return 3; }
    public void onGameStarted(Game game) { }
    public void onElevatorRequestChanged(int floorIdx, Direction dir, boolean reqEnable) { }
    public void onFloorRequestChanged(int elevatorIdx, int floorIdx, boolean reqEnable) { }
    public void onElevatorArrivedAtFloor(int elevatorIdx, int floorIdx, Direction travelDirection) { }
    public void onUpdate(double deltaTime) { }
    // TEST CODE
}