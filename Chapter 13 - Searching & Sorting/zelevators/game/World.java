package game;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.AffineTransform;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.swing.JPanel;
import javax.swing.Timer;

public class World extends JPanel implements ActionListener, MouseListener {
    // Singleton...
	private static World instance = null;
	public static synchronized World get()
    {
        if (instance == null)
            instance = new World();
  
        return instance;
    }

    // Constants...
    public static final int FIELD_BORDER_TOP = 55;
    public static final int FIELD_BORDER_BOT = 10;
    public static final Color COLOR_BACKGROUND = new Color(198, 181, 177);
    public static final Color COLOR_FLOOR = new Color(99, 90, 88);
    public static final Color COLOR_SHADOW = new Color(51, 46, 45);
    public static final Color COLOR_SHADOW_TEXT = new Color(50, 50, 50);
    public static final double FLOOR_HEIGHT = 1.0;
    public static final Vec2 ELEVATOR_DOORS_HALFDIMS = new Vec2(0.35, 0.45);
    public static final double ELEVATOR_SPACING = ELEVATOR_DOORS_HALFDIMS.x;
    public static final double ELEVATOR_LEFT_RIGHT_SPACE = 3.0;
    public static final double ZOMBIE_WAITING_AREA_MAX_OFFSET = -0.75;  // Offset from ELEVATOR_LEFT_RIGHT_SPACE
    public static final double BUTTON_UP_DOWN_OFFSET = -0.3;            // Offset from ELEVATOR_LEFT_RIGHT_SPACE
    public static final int RENDER_TIMER_PERIOD = 16;  // In milliseconds

    // Member variables...
    private Vec2 origin = new Vec2(0, 0);
    private Vec2 canvasSize = new Vec2(0, 0);
    private double pixelsPerUnit = 1;
    private Timer renderTimer;

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
    protected World() {
        // Resize event...
        this.addComponentListener(new ComponentAdapter() {
            public void componentResized(ComponentEvent componentEvent) {
                // Set the origin and sizes (first, just figure out y)...
                canvasSize = new Vec2(0, getHeight() - FIELD_BORDER_TOP);
                pixelsPerUnit = Math.floor(canvasSize.y / (Game.get().getFloorCount() + 1));    // Top row is used to show elevator buttons
                origin = new Vec2(0, getHeight() - FIELD_BORDER_BOT);

                // Now we can figure out the x part...
                canvasSize.x = (ELEVATOR_LEFT_RIGHT_SPACE * 2 + 
                                ELEVATOR_DOORS_HALFDIMS.x * 2 * Game.get().getElevatorCount() + 
                                ELEVATOR_SPACING * Math.max(Game.get().getElevatorCount() - 1, 0)) * pixelsPerUnit;
                origin.x = (getWidth() - canvasSize.x) / 2;

                // Setup the level...
                if (!Game.get().isGameActive()) {
                    Game.get().onLevelSetup();
                }
            }
        });

        // Defaults...
        setBackground(COLOR_BACKGROUND);

        // Kick off render timer...
        if (renderTimer != null && renderTimer.isRunning()) {
            renderTimer.stop();
        }
        renderTimer = new Timer(RENDER_TIMER_PERIOD, this);
        renderTimer.start();

        // Mouse listener...
        this.addMouseListener(this);
    }

    // Render timer...
    public void actionPerformed(ActionEvent event) {
        if (event.getSource() == renderTimer) {
            repaint();
        }
    }

    // Mouse events...
    public void mousePressed(MouseEvent event) {
    }
    public void mouseReleased(MouseEvent event) {
    }
    public void mouseEntered(MouseEvent event) {
    }
    public void mouseExited(MouseEvent event) {
    }
    public void mouseClicked(MouseEvent event) {
        // Double click restarts it...
        if (event.getClickCount() == 2 && !event.isConsumed()) {
            Game.get().onLevelSetup();
        }
        else if ((event.getClickCount() == 1) && Game.get().isGameActive()) {
            // Pause/unpause the game...
            Game.get().setGamePause(!Game.get().isGamePaused());
        }
    }
    
    // Draw functions...
    private void drawFloors(Graphics2D g) {
        g.setTransform(new AffineTransform());
        g.setStroke(new BasicStroke());
        g.setColor(COLOR_FLOOR);
        int floorCount = Game.get().getFloorCount();
        Vec2 floorHalfDims = new Vec2(Util.maxCoordFrameUnits().x / 2, 0.025);
		for (int i = 0; i < floorCount; i++) {
            Vec2 floorPosLL = new Vec2(0, (double)i * FLOOR_HEIGHT - floorHalfDims.y);
            Draw.drawRect(g, Vec2.add(floorPosLL, floorHalfDims), floorHalfDims, 1, COLOR_FLOOR, Color.BLACK, floorHalfDims.y / 5, 0.05);
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
            Draw.drawRect(g, posButtonUp, halfDims, 1, Simulation.get().hasElevatorRequest(i, ElevatorController.Direction.Up) ? Color.WHITE : colorOff, Color.BLACK, 0.02, 1);
            Draw.drawRect(g, posButtonDown, halfDims, 1, Simulation.get().hasElevatorRequest(i, ElevatorController.Direction.Down) ? Color.WHITE : colorOff, Color.BLACK, 0.02, 1);
		}
    }
    private void drawElevatorFloorRequestButtons(Graphics2D g) {
        g.setTransform(new AffineTransform());
        g.setStroke(new BasicStroke());
        g.setColor(COLOR_FLOOR);
        int elevatorCount = Game.get().getElevatorCount();
        int floorCount = Game.get().getFloorCount();
        int numCols = Math.max(floorCount / 3, 1);
        int numRows = (floorCount / numCols) + ((floorCount % numCols != 0) ? 1 : 0);
        Color colorOff = new Color(100, 100, 100);
        for (int i = 0; i < elevatorCount; i++) {
            double widthSqueeze = 0.25;
            Vec2 floorPosLL = new Vec2(ELEVATOR_LEFT_RIGHT_SPACE + (ELEVATOR_DOORS_HALFDIMS.x * 2.0) * i + ELEVATOR_SPACING * i + ELEVATOR_DOORS_HALFDIMS.x * widthSqueeze, (double)floorCount * FLOOR_HEIGHT + 0.1 * FLOOR_HEIGHT);
            Vec2 floorPosUR = new Vec2(floorPosLL.x + (ELEVATOR_DOORS_HALFDIMS.x * 2.0) - ELEVATOR_DOORS_HALFDIMS.x * widthSqueeze * 2, floorPosLL.y + 0.85 * FLOOR_HEIGHT);
            double rowSpace = (floorPosUR.y - floorPosLL.y) / numRows;
            double colSpace = (floorPosUR.x - floorPosLL.x) / Math.max(numCols - 1, 1);
            double btnRadius = Math.min(rowSpace, colSpace) * 0.45;
            for (int r = 0; r < numRows; ++r) {
                for (int c = 0; c < numCols; ++c) {
                    int floorIdx = r * numCols + c;
                    if (floorIdx < floorCount) {
                        Vec2 btnPos = new Vec2(floorPosLL.x + colSpace * c, floorPosLL.y + rowSpace * r);
                        Draw.drawRect(g, btnPos, new Vec2(btnRadius, btnRadius), 1, Simulation.get().elevatorHasFloorRequest(i, floorIdx) ? Color.WHITE : colorOff, Color.BLACK, 0.02, 1);
                    }
                }
            }
		}
    }
    private void drawWorld(Graphics2D g) {
        // Wait till we're actually inialized...
        if (!Game.get().isInitialized()) {
            return;
        }

        // Setup...
        Draw.beginRender(g);

        // Rendering hints...
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        Map<Object, Object> hints = new LinkedHashMap<Object, Object>();
        hints.put(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        hints.put(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        hints.put(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);
        g.addRenderingHints(hints);

        // Full window clear...
        g.setColor(Color.LIGHT_GRAY);
        g.fillRect(0, 0, getWidth(), getHeight());

        // Background...
        Vec2 llPixels = new Vec2(Util.toPixelsX(0), Util.toPixelsY(0));
        Vec2 urPixels = new Vec2(Util.toPixelsX(Util.maxCoordFrameUnits().x), Util.toPixelsY(Util.maxCoordFrameUnits().y));
        g.setColor(COLOR_BACKGROUND);
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
            ArrayList<Elevator> elevators = Simulation.get().getElevators();
            for (int i = 0; i < elevators.size(); ++i) {
                Elevator elevator = elevators.get(i);
                if (pass == 0) elevator.drawShadow(g);
                else elevator.drawBackground(g);
            }

            // Zombies (inside of elevators)...
            ArrayList<Zombie> zombies = Simulation.get().getZombies();
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

        // Let the game do its drawing...
        Game.get().Draw(g);
    }
    public void paint(Graphics g) {
        drawWorld((Graphics2D)g);
    }
}
