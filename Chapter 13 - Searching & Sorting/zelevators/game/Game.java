package game;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.event.*;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.swing.Timer;

import game.ElevatorController.Direction;

public class Game implements ActionListener {
    // Constants...
    public static final int POINTS_ELEVATOR_FLOOR   = -1;   // When the elevator move from one floor to another (cost)
    public static final int POINTS_ELEVATOR_COMMAND = -1;   // Each command issued to the elevator (cost)
    public static final int POINTS_ZOMBIE_DELIVERED = 50;
    public static final int POINTS_ZOMBIE_STARVED   = -200;

    private static final int UPDATE_TIMER_PERIOD    = 16;  // In milliseconds
    private static final Vec2 LEVELTIMER_TEXT_BOX_HALF_DIMS_PIXELS = new Vec2(60, 21);
    private static final Vec2 PLAYER_DISPLAY_TEXT_BOX_HALF_DIMS_PIXELS = new Vec2(85, 28);

    // Nested classes...
    protected class GameStats {
        public boolean isPaused = false;
        public double timeRemaining = 0;
        public int gameTimeInSecondsMax;
        public int levelScore = 0;
        public double timeSinceSpawnedZombie = 100000.0;

        public void reset(int gameTimeInSeconds) {
            isPaused = false;
            timeRemaining = gameTimeInSeconds;
            gameTimeInSecondsMax = gameTimeInSeconds;
            levelScore = 0;
            timeSinceSpawnedZombie = 100000.0;
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
    public static boolean Create(ElevatorController elevatorController) {
        // If no config is specified, read it from the server...
        GameConfig gameConfig = readGameConfigFromServer();
        return Game.Create(gameConfig, elevatorController);
    }
    public static boolean Create(GameConfig config, ElevatorController elevatorController) {
        return Game.get().create(config, elevatorController);
    }

    // Accessors...
    public boolean isInitialized() {
        return initialized;
    }
    public double getLevelTimeRemaining() {
        return gameStats.timeRemaining;
    }
    public int getLevelTimeMax() {
        return gameStats.gameTimeInSecondsMax;
    }
    public int getElevatorCount() {
        return (gameConfig == null) ? -1 : gameConfig.elevatorCount;
    }
    public int getFloorCount() {
        return (gameConfig == null) ? -1 : gameConfig.floorCount;
    }
    public void setElevatorTravelDirection(int elevatorIdx, ElevatorController.Direction travelDirection) {
        Simulation.get().setElevatorTravelDirection(elevatorIdx, travelDirection);
    }
    public ElevatorController.Direction getElevatorTravelDirection(int elevatorIdx) {
        return Simulation.get().getElevatorTravelDirection(elevatorIdx);
    }    
    public boolean hasElevatorRequestUp(int floorIdx) {
        return Simulation.get().hasElevatorRequest(floorIdx, Direction.Up);
    }
    public boolean hasElevatorRequestDown(int floorIdx) {
        return Simulation.get().hasElevatorRequest(floorIdx, Direction.Down);
    }
    public boolean elevatorHasFloorRequest(int elevatorIdx, int floorIdx) {
        return Simulation.get().elevatorHasFloorRequest(elevatorIdx, floorIdx);
    }
    public double getElevatorFloor(int elevatorIdx) {
        return Simulation.get().getElevatorFloor(elevatorIdx);
    }
    public boolean isElevatorIsOnFloor(int elevatorIdx, int floorIdx) {
        return Simulation.get().isElevatorIsOnFloor(elevatorIdx, floorIdx);
    }
    public boolean isElevatorIsHeadingToFloor(int elevatorIdx, int floorIdx) {
        return Simulation.get().isElevatorIsHeadingToFloor(elevatorIdx, floorIdx);
    }
    public boolean isElevatorIdle(int elevatorIdx) {
        return Simulation.get().isElevatorIdle(elevatorIdx);
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
    protected ElevatorController getElevatorController() {
        return elevatorController;
    }

    // Member variables...
    private boolean initialized = false;
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
        gameStats.reset(gameConfig.gameTimeInSeconds);

        // Reset the simulation...
        Simulation.get().destroyAll();
        
        // Create the elevators...
        for (int i = 0; i < gameConfig.elevatorCount; ++i) {
            Simulation.get().createElevator(i, 0);
        }

        // Zombies (initial set of them)...
        for (int i = 0; i < gameConfig.floorCount / 2; ++i) {
            Simulation.get().createZombie(Util.randRangeInt(0, Game.get().getFloorCount() - 1));
            gameStats.timeSinceSpawnedZombie = 0.0f;
        }

        // Elevator requests...
        Simulation.get().createElevatorRequests(gameConfig.floorCount);
        
        // Do an initial update...
        onLevelUpdate(0, true);

        // Mark us a initialized...
        initialized = true;
    }
    private void onLevelUpdate(double deltaTime, boolean firstUpdate) {
        // Elevator controller update event...
        ElevatorController elevatorController = Game.get().getElevatorController();
        if (elevatorController != null) {
            if (firstUpdate) {
                elevatorController.onGameStarted(this);
            }
            elevatorController.onUpdate(deltaTime);
        }

        // Possibly spawn new zombies...
        gameStats.timeSinceSpawnedZombie += deltaTime;
        if (gameStats.timeSinceSpawnedZombie >= gameConfig.zombieSpawnPeriod) {
            Simulation.get().createZombie(Util.randRangeInt(0, Game.get().getFloorCount() - 1));
            gameStats.timeSinceSpawnedZombie -= gameConfig.zombieSpawnPeriod;
        }
    }
    private boolean updateElevatorController(double deltaTime) {
        // Don't call if we are paused...
        if (isGamePaused()) {
            return true;
        }

        // Update it...
        if (elevatorController != null) {
            elevatorController.onUpdate(deltaTime);
        }
        
        return true;
    }

    public void actionPerformed(ActionEvent evt) {
        Game.get().update();
    }

    protected static GameConfig readGameConfigFromServer() {
        GameConfig gameConfig = new GameConfig();

        try {
            // Data...
            String jsonPayload = "{\"TableName\":\"classLeaderboardTable\",\"EventType\":\"getZelevatorConfig\"}";

            // Do the POST request...
            URL url = new URL( "https://u9m0v5iwsk.execute-api.us-west-2.amazonaws.com/default/classBuzzer" );
            HttpURLConnection conn= (HttpURLConnection) url.openConnection();           
            conn.setDoOutput( true );
            conn.setInstanceFollowRedirects( false );
            conn.setRequestMethod( "POST" );
            conn.setRequestProperty( "Accept", "application/json"); 
            conn.setRequestProperty( "Content-Type", "application/json"); 
            conn.setRequestProperty( "charset", "utf-8");
            conn.setUseCaches( false );
            try (OutputStream os = conn.getOutputStream()) {
                byte[] input = jsonPayload.getBytes("utf-8");
                os.write(input, 0, input.length);			
            }

            // Response..
            try (BufferedReader br = new BufferedReader( new InputStreamReader(conn.getInputStream(), "utf-8") )) {
                StringBuilder response = new StringBuilder();
                String responseLine = null;
                while ((responseLine = br.readLine()) != null) {
                    response.append(responseLine.trim());
                }

                // Parse it...
                String configDataStr = response.toString().split(":")[1];
                configDataStr = configDataStr.replace("\"}", "").replace("\"", "");
                String[] configData = configDataStr.split(";");
                gameConfig = new GameConfig(Integer.parseInt(configData[0]), Integer.parseInt(configData[1]), Integer.parseInt(configData[2]), Double.parseDouble(configData[3]));
                gameConfig.isCloudConfigured = true;
            }
        } catch (Exception e) {
            System.out.println("Leaderboard config read failed - " + e.toString());
        }
        return gameConfig;
    }

    protected void postScoreToLeaderboard(int period, String name, int score) {
        try {
            // Data...Example payload: {'TableName':'classLeaderboardTable','EventType':'postLeaderboardStudent','Item':{'periodKey':'1','StudentName':'testName','score':'10'}}
            String jsonPayload = "{\"TableName\":\"classLeaderboardTable\",\"EventType\":\"postLeaderboardStudent\",\"Item\":{\"periodKey\":\"" + period + "\",\"StudentName\":\"" + name + "\",\"score\":\"" + score + "\"}}";

            // Do the POST request...
            URL url = new URL( "https://u9m0v5iwsk.execute-api.us-west-2.amazonaws.com/default/classBuzzer" );
            HttpURLConnection conn= (HttpURLConnection) url.openConnection();           
            conn.setDoOutput( true );
            conn.setInstanceFollowRedirects( false );
            conn.setRequestMethod( "POST" );
            conn.setRequestProperty( "Accept", "application/json"); 
            conn.setRequestProperty( "Content-Type", "application/json"); 
            conn.setRequestProperty( "charset", "utf-8");
            conn.setUseCaches( false );
            try (OutputStream os = conn.getOutputStream()) {
                byte[] input = jsonPayload.getBytes("utf-8");
                os.write(input, 0, input.length);			
            }

            // Response..
            try (BufferedReader br = new BufferedReader( new InputStreamReader(conn.getInputStream(), "utf-8") )) {
                StringBuilder response = new StringBuilder();
                String responseLine = null;
                while ((responseLine = br.readLine()) != null) {
                    response.append(responseLine.trim());
                }
                System.out.println("Leaderboard post - " + response.toString());
            }
        } catch (Exception e) {
            System.out.println("Leaderboard post failed - " + e.toString());
        }
    }

    protected void update() {
        // Step the sim...
        long milliSecondsNow = System.currentTimeMillis();
        double deltaTime = (double)(milliSecondsNow - milliSecondsLastUpdate) / 1000;
        milliSecondsLastUpdate = milliSecondsNow;
        
        // Clamp deltaTime (slow down the sim, if needed)...
        deltaTime = Math.min(deltaTime, 0.1);
        if (gameStats.timeRemaining == 0) {
            deltaTime = 0;
        }

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
        double prevTimeRemaining = gameStats.timeRemaining;
        gameStats.timeRemaining = Math.max(gameStats.timeRemaining - deltaTime, 0);
        if ((gameStats.timeRemaining == 0) && (prevTimeRemaining > 0)) {
            // End of game event (post to the leaderboard)...
            if ((elevatorController != null) && 
                (elevatorController.getStudentPeriod() >= 1) && (elevatorController.getStudentPeriod() <= 7) && 
                (elevatorController.getStudentName().length() > 0)) {
                // Go ahead and post it (if the game was cloud configured)...
                if (gameConfig.isCloudConfigured) {
                    postScoreToLeaderboard(elevatorController.getStudentPeriod(), elevatorController.getStudentName(), gameStats.levelScore);
                }
            }
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
            Color bckGndColor = new Color(230, 230, 230);
            Draw.drawRect(g, finalTextPos, new Vec2(3.5, 0.5), 1.0, bckGndColor, Color.BLACK, 0.05 * 42 / World.get().getPixelsPerUnit(), 0.1 * 42 / World.get().getPixelsPerUnit());
            Draw.drawTextCentered(g, finalText, finalTextPos, Draw.FontSize.XLARGE, textColor, Color.BLACK);                                       
        }
    }
}
