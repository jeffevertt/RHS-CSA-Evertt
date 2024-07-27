package logic;

import game.ElevatorController;
import game.Game;

// *********************************************************************************
//  This is the code for SECOND PLAYER (your code goes in MyElevatorController.java)
// *********************************************************************************

public class OtherElevatorController implements ElevatorController {

    // PASTE Other Person's code HERE (BELOW THIS)

    //  (replace these placeholder methods)
    public String getStudentName() { return "<NONE>"; }
    public int getStudentPeriod() { return -1; }
    public void onGameStarted(Game game) { }
    public void onElevatorRequestChanged(int floorIdx, Direction dir, boolean enableRequest) { }
    public void onFloorRequestChanged(int elevatorIdx, int floorIdx, boolean enableRequest) { }
    public void onElevatorArrivedAtFloor(int elevatorIdx, int floorIdx, Direction headingDirection) { }
    public void onUpdate(double deltaTime) { }

    // PASTE Other Person's code HERE (ABOVE THIS)
}