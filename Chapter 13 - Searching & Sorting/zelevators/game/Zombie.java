package game;

import java.awt.Graphics2D;

public class Zombie extends GameObject {
    protected enum State {
        Entering,
        Waiting,
        Boarding,
        OnElevator,
        WalkingOff,
        Starved,
    }

    private State state = State.Entering;
    private double currentFloor = 0.0;

    // Constructors...
    public Zombie(double initialFloor) {
        // Save off our params...
        this.state = State.Entering;
        this.currentFloor = initialFloor;

        // Init...
        this.pos = Vec2.zero();
    }

    // Accessors...
    public State getState() {
        return state;
    }
    public double getCurrentFloor() {
        return currentFloor;
    }

    // Update...
    protected void update(double deltaTime) {
        // Super...
        super.update(deltaTime);

        // ...
    }

    // Draw...
    protected void drawShadow(Graphics2D g) {
        // TODO
    }
    protected void draw(Graphics2D g) {
        // TODO
    }
}
