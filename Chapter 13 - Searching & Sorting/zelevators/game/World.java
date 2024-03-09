package game;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.util.ArrayList;

public class World {
    // Singleton...
	private static World[] instances = { null, null };
	protected static synchronized World get(int playerIdx) {
        if (instances[playerIdx] == null)
            instances[playerIdx] = new World(playerIdx);
  
        return instances[playerIdx];
    }

    // Constants...
    public static final int FIELD_BORDER_TOP = 55;
    public static final int FIELD_BORDER_BOT = 10;
    public static final Color COLOR_FLOOR = new Color(99, 90, 88);
    public static final Color COLOR_SHADOW = new Color(51, 46, 45);
    public static final Color COLOR_SHADOW_TEXT = new Color(50, 50, 50);
    public static final double FLOOR_HEIGHT = 1.0;
    public static final Vec2 ELEVATOR_DOORS_HALFDIMS = new Vec2(0.35, 0.45);
    public static final double ELEVATOR_SPACING = ELEVATOR_DOORS_HALFDIMS.x;
    public static final double ELEVATOR_LEFT_RIGHT_SPACE = 3.0;
    public static final double TWO_PLAYER_MID_GAP = 1.5;
    public static final double ZOMBIE_WAITING_AREA_MAX_OFFSET = -0.75;  // Offset from ELEVATOR_LEFT_RIGHT_SPACE
    public static final double BUTTON_UP_DOWN_OFFSET = -0.3;            // Offset from ELEVATOR_LEFT_RIGHT_SPACE

    // Member variables...
    private int playerIdx = -1;
    private Vec2 origin = new Vec2(0, 0);
    private Vec2 canvasSize = new Vec2(0, 0);
    private double pixelsPerUnit = 1;

    // Accessors...
    public Vec2 getOrigin() {
        return origin;
    }
    public Vec2 getCanvasSize() {
        return canvasSize;
    }
    public double getPixelsPerUnit() {
        return pixelsPerUnit;
    }

    // Member functions (methods)...
    protected World(int playerIdx) {
        // Save off the playerIdx for this world...
        this.playerIdx = playerIdx;
    }

    protected void onWorldFrameResized() {
        // Setup...
        WorldFrame worldFrame = WorldFrame.get();

        // Set the origin and sizes (first, just figure out y)...
        canvasSize = new Vec2(0, worldFrame.getFrameHeight() - FIELD_BORDER_TOP);
        pixelsPerUnit = Math.floor(canvasSize.y / (Game.get().getFloorCount() + 1));    // Top row is used to show elevator buttons
        origin = new Vec2(0, worldFrame.getFrameHeight() - FIELD_BORDER_BOT);

        // Now we can figure out the x part...
        canvasSize.x = (ELEVATOR_LEFT_RIGHT_SPACE * 2 + 
                        ELEVATOR_DOORS_HALFDIMS.x * 2 * Game.get().getElevatorCount() +
                        ELEVATOR_SPACING * Math.max(Game.get().getElevatorCount() - 1, 0)) * pixelsPerUnit;
        origin.x = (Game.get().getPlayerCount() == 1) ? ((worldFrame.getFrameWidth() - canvasSize.x) / 2) :
                        (playerIdx == 0) ? ((worldFrame.getFrameWidth() - canvasSize.x * 2 - TWO_PLAYER_MID_GAP * pixelsPerUnit) / 2) :
                                            (worldFrame.getFrameWidth() / 2 + TWO_PLAYER_MID_GAP / 2 * pixelsPerUnit);
    }
    
    // Draw functions...
    private void drawFloors(Graphics2D g) {
        g.setTransform(Draw.getBaseTransform());
        g.setStroke(new BasicStroke());
        g.setColor(COLOR_FLOOR);
        int floorCount = Game.get().getFloorCount();
        Vec2 floorHalfDims = new Vec2(Util.maxCoordFrameUnits(playerIdx).x / 2, 0.025);
		for (int i = 0; i < floorCount; i++) {
            Vec2 floorPosLL = new Vec2(0, (double)i * FLOOR_HEIGHT - floorHalfDims.y);
            Draw.drawRect(playerIdx, g, Vec2.add(floorPosLL, floorHalfDims), floorHalfDims, 1, COLOR_FLOOR, Color.BLACK, floorHalfDims.y / 5, 0.05);
		}
    }
    private void drawElevatorUpDownButtons(Graphics2D g) {
        int floorCount = Game.get().getFloorCount();
        Color colorOff = new Color(100, 100, 100);
        for (int i = 0; i < floorCount; i++) {
            Vec2 posButtonCenter = new Vec2(ELEVATOR_LEFT_RIGHT_SPACE + BUTTON_UP_DOWN_OFFSET, FLOOR_HEIGHT * ((double)i + 0.4));
            Vec2 posButtonUp = Vec2.add(posButtonCenter, Vec2.multiply(Vec2.up(), FLOOR_HEIGHT * 0.125));
            Vec2 posButtonDown = Vec2.add(posButtonCenter, Vec2.multiply(Vec2.up(), FLOOR_HEIGHT * -0.125));
            Vec2 halfDims = new Vec2(FLOOR_HEIGHT * 0.1, FLOOR_HEIGHT * 0.1);
            Draw.drawRect(playerIdx, g, posButtonUp, halfDims, 1, Simulation.get(playerIdx).hasElevatorRequest(i, ElevatorController.Direction.Up) ? Color.WHITE : colorOff, Color.BLACK, 0.02, 1);
            Draw.drawRect(playerIdx, g, posButtonDown, halfDims, 1, Simulation.get(playerIdx).hasElevatorRequest(i, ElevatorController.Direction.Down) ? Color.WHITE : colorOff, Color.BLACK, 0.02, 1);
		}
    }
    private void drawElevatorFloorRequestButtons(Graphics2D g) {
        g.setTransform(Draw.getBaseTransform());
        g.setStroke(new BasicStroke());
        g.setColor(COLOR_FLOOR);
        int elevatorCount = Game.get().getElevatorCount();
        int floorCount = Game.get().getFloorCount();
        int numCols = floorCount > 8 ? 3 : 2;
        int numRows = (floorCount / numCols) + ((floorCount % numCols != 0) ? 1 : 0);
        double columSpacing = (numCols == 2) ? (ELEVATOR_DOORS_HALFDIMS.x * 0.4) : (ELEVATOR_DOORS_HALFDIMS.x * 0.3);
        Color colorOff = new Color(100, 100, 100);
        for (int i = 0; i < elevatorCount; i++) {
            // Buttons...
            Vec2 floorPosLL = new Vec2(ELEVATOR_LEFT_RIGHT_SPACE + (ELEVATOR_DOORS_HALFDIMS.x * 2.0) * i + ELEVATOR_SPACING * i + ELEVATOR_DOORS_HALFDIMS.x * 1.05 - columSpacing * numCols / 2, (double)floorCount * FLOOR_HEIGHT + 0.05 * FLOOR_HEIGHT);
            Vec2 floorPosUR = new Vec2(floorPosLL.x + columSpacing * numCols, floorPosLL.y + 0.85 * FLOOR_HEIGHT);
            double rowSpace = (floorPosUR.y - floorPosLL.y) / numRows;
            double colSpace = (floorPosUR.x - floorPosLL.x) / Math.max(numCols - 1, 1);
            double btnRadius = Math.min(Math.min(rowSpace, colSpace), 0.2) * 0.45;
            for (int r = 0; r < numRows; ++r) {
                for (int c = 0; c < numCols; ++c) {
                    int floorIdx = r * numCols + c;
                    if (floorIdx < floorCount) {
                        Vec2 btnPos = new Vec2(floorPosLL.x + colSpace * c, floorPosLL.y + rowSpace * r);
                        Draw.drawRect(playerIdx, g, btnPos, new Vec2(btnRadius, btnRadius), 1, Simulation.get(playerIdx).elevatorHasFloorRequest(i, floorIdx) ? Color.WHITE : colorOff, Color.BLACK, 0.02, 1);
                    }
                }
            }

            // Current floor...
            Vec2 textPos = new Vec2(ELEVATOR_LEFT_RIGHT_SPACE + (ELEVATOR_DOORS_HALFDIMS.x * 2.0) * i + ELEVATOR_SPACING * i + ELEVATOR_DOORS_HALFDIMS.x, (double)floorCount * FLOOR_HEIGHT + 0.1 * FLOOR_HEIGHT + btnRadius * 2 + rowSpace * (numRows - 1));
            Draw.drawTextCentered(playerIdx, g, "" + (int)(Simulation.get(playerIdx).getElevator(i).getCurrentFloor() + 1), textPos, Draw.getUIScale(), Draw.fontSizeFromScale(0.25), new Color(255, 140, 20), Color.BLACK);
		}
    }
    protected void drawWorld(Graphics2D g) {
        // Background...
        Vec2 llPixels = new Vec2(Util.toPixelsX(playerIdx, 0), Util.toPixelsY(playerIdx, 0));
        Vec2 urPixels = new Vec2(Util.toPixelsX(playerIdx, Util.maxCoordFrameUnits(playerIdx).x), Util.toPixelsY(playerIdx, Util.maxCoordFrameUnits(playerIdx).y));
        g.setColor(WorldFrame.COLOR_BACKGROUND);
        g.fillRect((int)llPixels.x, (int)urPixels.y, (int)(urPixels.x - llPixels.x), (int)(llPixels.y - urPixels.y));
        g.setColor(!Game.get().isGamePaused() ? (Game.get().isGameActive() ? new Color(95, 95, 143) : Color.LIGHT_GRAY) : Color.RED);
        g.setStroke(new BasicStroke(2));
        g.drawRect((int)llPixels.x, (int)urPixels.y, (int)(urPixels.x - llPixels.x), (int)(llPixels.y - urPixels.y));

        // Loop twice, first for shadow, then for geometry...
        for (int pass = 0; pass < 2; ++pass) {
            // Floors...
            if (pass == 1) {
                drawFloors(g);
                drawElevatorUpDownButtons(g);
                drawElevatorFloorRequestButtons(g);
            }

            // Elevators (background)...
            ArrayList<Elevator> elevators = Simulation.get(playerIdx).getElevators();
            for (int i = 0; i < elevators.size(); ++i) {
                Elevator elevator = elevators.get(i);
                if (pass == 0) elevator.drawShadow(g);
                else elevator.drawBackground(g);
            }

            // Zombies (inside of elevators)...
            ArrayList<Zombie> zombies = Simulation.get(playerIdx).getZombies();
            for (int i = 0; i < zombies.size(); ++i) {
                Zombie zombie = zombies.get(i);
                if (zombie.isOnAnElevator()) {
                    zombie.draw(g);
                }
            }

            // Elevators (doors)...
            for (int i = 0; i < elevators.size(); ++i) {
                Elevator elevator = elevators.get(i);
                elevator.drawDoors(g);
            }

            // Zombies (outside of elevators)...
            for (int i = 0; i < zombies.size(); ++i) {
                Zombie zombie = zombies.get(i);
                if (!zombie.isOnAnElevator()) {
                    if (pass == 0) zombie.drawShadow(g); 
                    else zombie.draw(g);
                }
            }
        }
    }
}
