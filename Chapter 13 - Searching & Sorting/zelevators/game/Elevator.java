package game;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.ArrayList;

import game.ElevatorController.Direction;

public class Elevator extends GameObject {
    // Consts...
    public static final double TRAVEL_VELOCITY = 2.0;       // Units are floors per second
    public static final double DOOR_OPEN_VELOCITY = 2.0;    // Fully open/close in 1/DOOR_OPEN_VELOCITY seconds
    public static final int MAX_CAPACITY = 8;               // Maximum number of zombies it can hold

    // Private member variables...
    private int elevatorIdx = -1;
    private double currentFloor = 0.0;
    private int targetFloor = 0;
    private double doorClosePerc = 1.0;
    private ElevatorController.Direction travelDirection = ElevatorController.Direction.None; // None == Either/Any-Which-Way
    private boolean floorRequests[] = null;     // Indexed by the floor index (zero based, like everything else)

    // Constructors...
    public Elevator(int elevatorIdx, double initialFloor, int floorCount) {
        // Save off our params...
        this.elevatorIdx = elevatorIdx;
        this.currentFloor = initialFloor;

        // Init...
        this.doorClosePerc = 1.0;
        this.pos = getPos();
        this.travelDirection = ElevatorController.Direction.None;
        this.floorRequests = new boolean[floorCount];
    }

    // Accessors...
    public Vec2 getPos() {
        Vec2 posLL = new Vec2(World.ELEVATOR_LEFT_RIGHT_SPACE + (World.ELEVATOR_DOORS_HALFDIMS.x * 2 + World.ELEVATOR_SPACING) * elevatorIdx, 
                              World.FLOOR_HEIGHT * currentFloor);
        return Vec2.add(posLL, getHalfDims());
    }
    private Vec2 getFramePos(int floorIdx) {
        Vec2 posLL = new Vec2(World.ELEVATOR_LEFT_RIGHT_SPACE + (World.ELEVATOR_DOORS_HALFDIMS.x * 2 + World.ELEVATOR_SPACING) * elevatorIdx, 
                              World.FLOOR_HEIGHT * floorIdx);
        return Vec2.add(posLL, getHalfDims());
    }
    public Vec2 getHalfDims() {
        return World.ELEVATOR_DOORS_HALFDIMS;
    }
    public int getIndex() {
        return elevatorIdx;
    }
    protected double getCurrentFloor() {
        return currentFloor;
    }
    protected int getTargetFloor() {
        return targetFloor;
    }
    protected void setTargetFloor(int targetFloor) {
        this.targetFloor = targetFloor;
    }
    protected ElevatorController.Direction getTravelDirection() {
        return travelDirection;
    }
    protected void setTravelDirection(ElevatorController.Direction direction) {
        travelDirection = direction;
    }
    protected double getDoorClosedPercent() {
        return doorClosePerc;
    }
    protected boolean getAreDoorsOpen() {
        return (doorClosePerc == 0.0);
    }
    protected boolean getAreDoorsClosed() {
        return (doorClosePerc == 1.0);
    }    
    protected int getNumberOfZombiesOnElevator() {
        int count = 0;
        ArrayList<Zombie> zombies = Simulation.get().getZombies();
        for (int i = 0; i < zombies.size(); i++) {
            count += zombies.get(i).isOnElevator(elevatorIdx) ? 1 : 0;
        }
        return count;
    }
    protected boolean canAcceptNewZombiePassenger(Zombie zombie) {
        return getAreDoorsOpen() && 
               (getCurrentFloor() == (double)zombie.getCurrentFloor()) && 
               (getNumberOfZombiesOnElevator() < MAX_CAPACITY) &&
               ((travelDirection == ElevatorController.Direction.None) || 
                ((travelDirection == ElevatorController.Direction.Up) && (zombie.getTargetFloor() > zombie.getCurrentFloor())) ||
                ((travelDirection == ElevatorController.Direction.Down) && (zombie.getTargetFloor() < zombie.getCurrentFloor())));
    }
    protected boolean requestFloor(int floorIdx) {
        if ((floorIdx < 0) || (floorIdx >= floorRequests.length)) {
            return false;
        }
        if (floorRequests[floorIdx] == true) {
            return true;
        }

        // Track it...
        floorRequests[floorIdx] = true;
        
        // Event...
        Game.get().getElevatorController().onFloorRequestChanged(elevatorIdx, floorIdx, true);

        return true;
    }
    protected void clearRequestForFloor(int floorIdx) {
        if ((floorIdx < 0) || (floorIdx >= floorRequests.length)) {
            return;
        }
        if (floorRequests[floorIdx] == false) {
            return;
        }

        // Track it...
        floorRequests[floorIdx] = false;

        // Event...
        Game.get().getElevatorController().onFloorRequestChanged(elevatorIdx, floorIdx, false);
    }
    protected boolean hasRequestForFloor(int floorIdx) {
        if ((floorIdx < 0) || (floorIdx >= floorRequests.length)) {
            return false;
        }
        return floorRequests[floorIdx];
    }
    protected boolean isIdle() {
        return (doorClosePerc == 0.0) && !Simulation.get().anyZombiesGettingOnElevator(this) && (currentFloor == (double)targetFloor);
    }

    // Update...
    protected void update(double deltaTime) {
        // Super...
        super.update(deltaTime);

        // Update floor...
        if (currentFloor != (double)targetFloor) {
            // Only move when the doors are closed...
            if (doorClosePerc == 1.0) {
                double prevFloor = currentFloor;
                currentFloor = ((double)targetFloor < currentFloor) ? Math.max(currentFloor - deltaTime * TRAVEL_VELOCITY, targetFloor) : Math.min(currentFloor + deltaTime * TRAVEL_VELOCITY, targetFloor);

                // There's a cost for motion...
                if ((deltaTime > 0.0) && ((int)currentFloor != (int)prevFloor)) {
                    Game.get().awardPoints(Game.POINTS_ELEVATOR_FLOOR);
                }

                // If we get there, clear the request...
                if (currentFloor == targetFloor) {
                    clearRequestForFloor((int)currentFloor);
                }
            }
            else {
                doorClosePerc = Math.min(doorClosePerc + DOOR_OPEN_VELOCITY * deltaTime, 1.0f);

                // Check for closed, if so clear the floor reqest...
                if (doorClosePerc == 1.0) {
                    // Remove any "outside" requests...
                    if ((travelDirection == ElevatorController.Direction.Up) || (travelDirection == ElevatorController.Direction.None)) {
                        Simulation.get().remElevatorRequest((int)currentFloor, Direction.Up);
                    }
                    if ((travelDirection == ElevatorController.Direction.Down) || (travelDirection == ElevatorController.Direction.None)) {
                        Simulation.get().remElevatorRequest((int)currentFloor, Direction.Down);
                    }

                    // Do the controller callback...
                    Game.get().getElevatorController().onElevatorArrivedAtFloor(elevatorIdx, (int)currentFloor, travelDirection);
                }
            }
        }
        else {
            // Make sure the doors are open...
            if (doorClosePerc != 0.0) {
                doorClosePerc = Math.max(doorClosePerc - DOOR_OPEN_VELOCITY * deltaTime, 0.0f);
            }
        }
    }

    // Draw...
    protected void drawBackground(Graphics2D g) {
        // Draw all the doors (closed or otherwise)...
        int floorCount = Game.get().getFloorCount();
        Vec2 halfDims = getHalfDims();
		for (int i = 0; i < floorCount; i++) {
            // Background...
            Vec2 posFrame = getFramePos(i);
            Draw.drawRect(g, posFrame, halfDims, 1, Color.DARK_GRAY, Color.BLACK, 0.02, 0.05);
        }
	}    
    protected void drawDoors(Graphics2D g) {
        // Draw all the doors (closed or otherwise)...
        int floorCount = Game.get().getFloorCount();
        Vec2 halfDims = getHalfDims();
		for (int i = 0; i < floorCount; i++) {
            // Figure out the floor dims for this floor (may be open)...
            Vec2 posFrame = getFramePos(i);
            double thisFloorDoorClosePerc = (i == (int)Math.round(currentFloor)) ? doorClosePerc : 1;
            Vec2 halfDimsDoor = new Vec2((halfDims.x * 0.5) * thisFloorDoorClosePerc, halfDims.y);

            // Doors...
            double guardSpace = 0.01;
            Vec2 posFrameLeft = new Vec2(posFrame.x - halfDimsDoor.x - (halfDims.x * 0.5 - halfDimsDoor.x) * 2 + guardSpace, posFrame.y);
            Vec2 posFrameRight = new Vec2(posFrame.x + halfDimsDoor.x + (halfDims.x * 0.5 - halfDimsDoor.x) * 2 - guardSpace, posFrame.y);
            Draw.drawRect(g, posFrameLeft, halfDimsDoor, 1, new Color(191,193,194), Color.BLACK, 0.01, 0.0);
            Draw.drawRect(g, posFrameRight, halfDimsDoor, 1, new Color(191,193,194), Color.BLACK, 0.01, 0.0);
        }
	}
}
