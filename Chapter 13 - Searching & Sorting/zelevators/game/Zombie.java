package game;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;

public class Zombie extends GameObject {
    // Consts...
    public static final Vec2 NOMINAL_HALFDIMS = new Vec2(0.3, 0.48);
    public static final double VERTICAL_OFFSET = -0.035;
    public static final double WALK_SPEED = 0.5;            // In units per second
    public static final double STARVATION_TIME = 30;

    // Enums...
    protected enum State {
        Entering,
        Waiting,
        Boarding,
        OnElevator,
        WalkingOff,
        Delivered,
        Starved,
    }

    // Private member variables...
    private State state = State.Entering;
    private double currentFloor = 0.0;
    private int targetFloor = -1;
    private int onElevatorIdx = -1;
    private BufferedImage image = null;
    private Vec2 halfDims = null;

    // Constructors...
    public Zombie(double initialFloor) {
        // Save off our params...
        state = State.Entering;
        currentFloor = initialFloor;

        // Init...
        halfDims = Vec2.multiply(NOMINAL_HALFDIMS, Util.randRange(0.9, 1.1));
        pos = new Vec2(halfDims.x * Util.randRange(0.75, 1.25), World.FLOOR_HEIGHT * currentFloor + halfDims.y + VERTICAL_OFFSET);

        // Figure out where we're going (random target floor)...
        targetFloor = Util.randRangeInt(0, Game.get().getFloorCount() - 1);
        targetFloor = (targetFloor < (int)currentFloor) ? targetFloor : (targetFloor + 1);

        // Texture...
        try {
            int textureIndex = Util.randRangeInt(0, 5);
            image = ImageIO.read(new File("textures/zombie_0" + textureIndex + ".png"));
        } 
        catch (IOException e) {
        }
    }

    // Accessors...
    protected State getState() {
        return state;
    }
    protected double getCurrentFloor() {
        return currentFloor;
    }
    protected boolean isOnElevator(int elevatorIdx) {
        return (state == State.OnElevator) && (onElevatorIdx == elevatorIdx);
    }

    // Update...
    protected void update(double deltaTime) {
        // Super...
        super.update(deltaTime);

        // Per state update...
        switch (state) {
            case Entering : {
                // Walk to the elevator...
                pos.x = Math.min(pos.x + WALK_SPEED * deltaTime, World.ELEVATOR_LEFT_RIGHT_SPACE);
                if (pos.x >= World.ELEVATOR_LEFT_RIGHT_SPACE) {
                    state = State.Waiting;
                }
            } break;
            case Waiting : {
                // Check for an open elevator...
                ArrayList<Elevator> elevators = Simulation.get().getElevators();
                for (int i = 0; i < elevators.size(); i++) {
                    Elevator elevator = elevators.get(i);
                    if ((elevator.getCurrentFloor() == currentFloor) && elevator.canAcceptNewZombiePassenger()) {
                        // Make a run for it...
                        state = State.Boarding;
                    }
                }

                // Check to see if we've starved (can only happen in the waiting state)
                if (timeSinceBorn >= STARVATION_TIME) {
                    // Zombie: Starved!
                    Game.get().awardPoints(Game.POINTS_ZOMBIE_STARVED);
                    state = State.Starved;
                }
            } break;
            case Boarding : {
                // Find the target elevator (note that we might no longer have one)...
                ArrayList<Elevator> elevators = Simulation.get().getElevators();
                Elevator trgElevator = null;
                for (int i = 0; i < elevators.size(); i++) {
                    Elevator elevator = elevators.get(i);
                    if ((elevator.getCurrentFloor() == currentFloor) && elevator.canAcceptNewZombiePassenger()) {
                        trgElevator = elevator;
                        break;
                    }
                }

                // Walk to the elevator (if no longer have an option, walk back)...
                if (trgElevator != null) {
                    double trgPosX = trgElevator.getPos().x;
                    if (trgPosX > pos.x) {
                        pos.x = Math.min(pos.x + WALK_SPEED * deltaTime, trgPosX);
                    }
                    else {
                        pos.x = Math.max(pos.x - WALK_SPEED * deltaTime, trgPosX);
                    }
                    if (pos.x == trgPosX) {
                        // Pop, we're on...
                        state = State.OnElevator;
                        onElevatorIdx = trgElevator.getIndex();
                    }
                }
                else {
                    // Walk back (sad)...
                    double trgPosX = World.ELEVATOR_LEFT_RIGHT_SPACE;
                    pos.x = Math.max(pos.x - WALK_SPEED * deltaTime, trgPosX);
                    if (pos.x <= trgPosX) {
                        state = State.Waiting;
                    }
                }
            } break;
            case OnElevator : {
                // Wait till we're at our target floor and the doors are open...
                Elevator elevator = Simulation.get().getElevators().get(onElevatorIdx);
                if ((elevator.getCurrentFloor() == targetFloor) && elevator.getAreDoorsOpen()) {
                    // We're here, get of...
                    state = State.WalkingOff;
                    onElevatorIdx = -1;
                }
            } break;
            case WalkingOff : {
                pos.x = pos.x + WALK_SPEED * deltaTime;
                if (pos.x >= World.get().getCanvasSize().x) {
                    // Zombie: Delivered!
                    Game.get().awardPoints(Game.POINTS_ZOMBIE_DELIVERED);
                    this.timeTillDeath = Math.max(this.timeTillDeath, 0.0001);
                    state = State.Delivered;
                }
            } break;
            case Delivered : {
                // Nothing to do here but wait...
            } break;
            case Starved : {
                // Nothing to do here, just wait for the cold embrace of death...
            } break;
        }
    }

    // Draw...
    protected void drawShadow(Graphics2D g) {
        // TODO
    }
    protected void draw(Graphics2D g) {
        if (image != null) {
            Draw.drawImage(g, image, pos, halfDims, calcDrawScale());
        }
    }
}
