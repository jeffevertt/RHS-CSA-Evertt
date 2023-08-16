package game;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.event.*;
import javax.swing.Timer;

public class Game implements ActionListener {
    // Constants...
    public static final int STARTING_LEVEL_TIME     = 90;

    public static final int POINTS_ELEVATOR_FLOOR   = -1;   // When the elevator move from one floor to another (cost)
    public static final int POINTS_ZOMBIE_DELIVERED = 50;
    public static final int POINTS_ZOMBIE_STARVED   = -200;

    private static final int UPDATE_TIMER_PERIOD    = 16;  // In milliseconds
    private static final Vec2 LEVELTIMER_TEXT_BOX_HALF_DIMS_PIXELS = new Vec2(40, (double)World.FIELD_BORDER_TOP * 0.4);
    private static final Vec2 PLAYER_DISPLAY_TEXT_BOX_HALF_DIMS_PIXELS = new Vec2(85, (double)World.FIELD_BORDER_TOP * 0.55);

    // Nested classes...
    protected class GameStats {
        public boolean isPaused = false;
        public double timeRemaining = 0;
	    public int levelScore = 0;

        public void reset() {
            isPaused = false;
            timeRemaining = STARTING_LEVEL_TIME;
	        levelScore = 0;
        }
    }

	// Singleton...
	private static Game instance = null;
	protected static synchronized Game get()
    {
        if (instance == null)
            instance = new Game();
  
        return instance;
    }
    public static boolean Create(GameConfig config, ElevatorController elevatorController) {
        return Game.get().create(config, elevatorController);
    }

    // Accessors...
    public double getLevelTimeRemaining() {
        return gameStats.timeRemaining;
    }
    public int getLevelTimeMax() {
        return STARTING_LEVEL_TIME;
    }
    public int getElevatorCount() {
        return (gameConfig == null) ? -1 : gameConfig.elevatorCount;
    }
    public int getFloorCount() {
        return (gameConfig == null) ? -1 : gameConfig.floorCount;
    }
    public int getPlayerScore() {
        return gameStats.levelScore;
    }
    public boolean isGameActive() {
        return (this.gameStats.timeRemaining > 0);
    }
    public boolean isGamePaused() {
        return this.gameStats.isPaused;
    }

    // Member variables...
    private GameConfig gameConfig = null;
    private GameStats gameStats = new GameStats();
    private ElevatorController elevatorController = null;
    private long milliSecondsLastUpdate = System.currentTimeMillis();
    private Timer updateTimer;

    // Member functions (methods)...
    protected boolean create(GameConfig config, ElevatorController elevatorController) {
        // Save off the config & controller...
        this.gameConfig = config;
        this.elevatorController = elevatorController;

        // Create the world...
        if (!Window.create()) {
            return false;
        }

        // Kick off update timer...
        if (updateTimer != null && updateTimer.isRunning()) {
            updateTimer.stop();
        }
        updateTimer = new Timer(UPDATE_TIMER_PERIOD, this);
        updateTimer.start();

        return true;
    }

	protected void awardPoints(int points) {
		gameStats.levelScore += points;
	}

    protected void setGamePause(boolean isPaused) {
        // If the game is done, ignore this...
        if (!isGameActive()) {
            return;
        }

        // Set it...
        gameStats.isPaused = isPaused;
    }

    protected void onLevelSetup() {
		// Default time for level...
		gameStats.reset();

        // Reset the simulation...
        Simulation.get().destroyAll();
		
		// Create the elevators...
        for (int i = 0; i < gameConfig.elevatorCount; ++i) {
            Simulation.get().createElevator(i, 0);
        }
        
        // Do an initial update...
        onLevelUpdate(0, true);
    }
    private void onLevelUpdate(double deltaTime, boolean firstUpdate) {
		// ...
    }
    private boolean updateElevatorController(double deltaTime) {
        // Don't call if we are paused...
        if (isGamePaused()) {
            return true;
        }

        // Update it...
        if (elevatorController != null) {
            elevatorController.update(deltaTime);
        }
        
        return true;
    }

    public void actionPerformed(ActionEvent evt) {
        Game.get().update();
    } 

    protected void update() {
        // Step the sim...
        long milliSecondsNow = System.currentTimeMillis();
        double deltaTime = (double)(milliSecondsNow - milliSecondsLastUpdate) / 1000;
        milliSecondsLastUpdate = milliSecondsNow;
        
        // Clamp deltaTime (slow down the sim, if needed)...
        deltaTime = Math.min(deltaTime, 0.1);

        // Deal with pause...
        if (isGamePaused()) {
            deltaTime = 0.0;
        }
        
        // Sim manager...
        Simulation.get().update(deltaTime);

        // Update level logic...
        onLevelUpdate(deltaTime, false);
        
        // Code update...
        updateElevatorController(deltaTime);
        
        // Cull anything we can...
        Simulation.get().cullNotInField();
        
        // Update levelTime...
        gameStats.timeRemaining = Math.max(gameStats.timeRemaining - deltaTime, 0);
        if (gameStats.timeRemaining == 0) {
            // TODO: Stop all motion
        }
    }

    protected void Draw(Graphics2D g) {
        // Time remaining...
        Vec2 levelTimePos = Util.toCoordFrame(new Vec2((Window.get().getWidth() - World.FIELD_BORDER_BOT * 2) / 2, World.FIELD_BORDER_TOP / 2));
        Vec2 levelTimeHalfDims = new Vec2(Util.toCoordFrameLength(LEVELTIMER_TEXT_BOX_HALF_DIMS_PIXELS.x), Util.toCoordFrameLength(LEVELTIMER_TEXT_BOX_HALF_DIMS_PIXELS.y));
        String levelTimeText = isGamePaused() ? "PAUSED" : Util.toIntStringCeil(gameStats.timeRemaining);
        Color levelTimeColor = isGamePaused() ? Color.BLUE : ((gameStats.timeRemaining < 10) ? Color.red : new Color(50, 160, 130));
        Color levelTimeBckGndColor = isGamePaused() ? Color.YELLOW : ((gameStats.timeRemaining < 10) ? new Color(255, 155, 155) : new Color(235, 235, 235));
        Color levelTimeStroke = (gameStats.timeRemaining < 10) ? Color.RED : Color.DARK_GRAY;
        Draw.drawRect(g, levelTimePos, levelTimeHalfDims, 1.0, levelTimeBckGndColor, levelTimeStroke, 0.075 * 42 / World.get().getPixelsPerUnit(), 0.15 * 42 / World.get().getPixelsPerUnit());
		Draw.drawTextCentered(g, levelTimeText, levelTimePos, isGamePaused() ? Draw.FontSize.XSMALL : Draw.FontSize.LARGE, levelTimeColor, Color.BLACK);

        // Player...
        if (elevatorController != null) {
            Vec2 player1HalfDims = new Vec2(Util.toCoordFrameLength(PLAYER_DISPLAY_TEXT_BOX_HALF_DIMS_PIXELS.x), Util.toCoordFrameLength(PLAYER_DISPLAY_TEXT_BOX_HALF_DIMS_PIXELS.y));
            Vec2 player1Pos = Vec2.add(Util.toCoordFrame(new Vec2(World.FIELD_BORDER_BOT - 3, World.FIELD_BORDER_TOP / 2 + 6)), new Vec2(player1HalfDims.x, 0));
            String player1Text = "Score: " + gameStats.levelScore;
            Color player1Color = new Color(50, 180, 50);
            Color player1BckGndColor = Color.WHITE;
            Color player1Stroke = new Color(10, 70, 10);
            Draw.drawRect(g, player1Pos, player1HalfDims, 1.0, player1BckGndColor, player1Stroke, 0.05 * 42 / World.get().getPixelsPerUnit(), 0.1 * 42 / World.get().getPixelsPerUnit());
            Draw.drawTextCentered(g, elevatorController.getStudentName(), new Vec2(player1Pos.x, player1Pos.y + player1HalfDims.y * 0.55), Draw.FontSize.XSMALL, player1Color, Color.BLACK);
            Draw.drawTextCentered(g, player1Text, new Vec2(player1Pos.x, player1Pos.y - player1HalfDims.y * 0.35), Draw.FontSize.SMALL, player1Color, Color.BLACK);
        }

        // Winner UI...
        if (gameStats.timeRemaining == 0) {
            Vec2 finalTextPos = Vec2.multiply(Util.maxCoordFrameUnits(), 0.5);
            String finalText = ("Final Score: " + gameStats.levelScore);
            Color textColor = Color.BLUE;
            Color bckGndColor = Color.LIGHT_GRAY;
            Draw.drawRect(g, finalTextPos, new Vec2(7.5, 1), 1.0, bckGndColor, Color.BLACK, 0.05 * 42 / World.get().getPixelsPerUnit(), 0.1 * 42 / World.get().getPixelsPerUnit());
            Draw.drawTextCentered(g, finalText, finalTextPos, Draw.FontSize.XLARGE, textColor, Color.BLACK);                                       
        }
    }
}
