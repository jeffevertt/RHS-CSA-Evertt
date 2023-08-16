package game;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class Zombie extends GameObject {
    // Consts...
    public static final Vec2 NOMINAL_HALFDIMS = new Vec2(0.3, 0.48);
    public static final double VERTICAL_OFFSET = -0.035;

    // Enums...
    protected enum State {
        Entering,
        Waiting,
        Boarding,
        OnElevator,
        WalkingOff,
        Starved,
    }

    // Private member variables...
    private State state = State.Entering;
    private double currentFloor = 0.0;
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

        // Texture...
        try {
            int textureIndex = Util.randRangeInt(0, 5);
            image = ImageIO.read(new File("textures/zombie_0" + textureIndex + ".png"));
        } 
        catch (IOException e) {
        }
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
        if (image != null) {
            Draw.drawImage(g, image, pos, halfDims, calcDrawScale());
        }
    }
}
