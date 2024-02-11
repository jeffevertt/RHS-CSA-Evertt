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
        public boolean hasHadFirstUpdate = false;
        public double timeRemaining = 0;
        public int gameTimeInSecondsMax;
        public int[] levelScores = new int[2];
        public double timeSinceSpawnedZombie = 100000.0;
        public int activePlayerIdx = 0;

        public void reset(int gameTimeInSeconds) {
            isPaused = false;
            hasHadFirstUpdate = false;
            timeRemaining = gameTimeInSeconds;
            gameTimeInSecondsMax = gameTimeInSeconds;
            levelScores[0] = 0;
            levelScores[1] = 0;
            timeSinceSpawnedZombie = 100000.0;
            activePlayerIdx = 0;
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
    public static boolean Create(ElevatorController elevatorController1, ElevatorController elevatorController2) {
        // If no config is specified, read it from the server...
        GameConfig gameConfig = readGameConfigFromServer();
        return Game.Create(gameConfig, elevatorController1, elevatorController2);
    }
    public static boolean Create(GameConfig config, ElevatorController elevatorController) {
        return Game.get().create(config, elevatorController);
    }
    public static boolean Create(GameConfig config, ElevatorController elevatorController1, ElevatorController elevatorController2) {
        return Game.get().create(config, elevatorController1, elevatorController2);
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
        Simulation.get(gameStats.activePlayerIdx).setElevatorTravelDirection(elevatorIdx, travelDirection);
    }
    public ElevatorController.Direction getElevatorTravelDirection(int elevatorIdx) {
        return Simulation.get(gameStats.activePlayerIdx).getElevatorTravelDirection(elevatorIdx);
    }    
    public boolean hasElevatorRequestUp(int floorIdx) {
        return Simulation.get(gameStats.activePlayerIdx).hasElevatorRequest(floorIdx, Direction.Up);
    }
    public boolean hasElevatorRequestDown(int floorIdx) {
        return Simulation.get(gameStats.activePlayerIdx).hasElevatorRequest(floorIdx, Direction.Down);
    }
    public boolean elevatorHasFloorRequest(int elevatorIdx, int floorIdx) {
        return Simulation.get(gameStats.activePlayerIdx).elevatorHasFloorRequest(elevatorIdx, floorIdx);
    }
    public double getElevatorFloor(int elevatorIdx) {
        return Simulation.get(gameStats.activePlayerIdx).getElevatorFloor(elevatorIdx);
    }
    public boolean isElevatorIsOnFloor(int elevatorIdx, int floorIdx) {
        return Simulation.get(gameStats.activePlayerIdx).isElevatorIsOnFloor(elevatorIdx, floorIdx);
    }
    public boolean isElevatorIsHeadingToFloor(int elevatorIdx, int floorIdx) {
        return Simulation.get(gameStats.activePlayerIdx).isElevatorIsHeadingToFloor(elevatorIdx, floorIdx);
    }
    public boolean isElevatorIdle(int elevatorIdx) {
        return Simulation.get(gameStats.activePlayerIdx).isElevatorIdle(elevatorIdx);
    }
    public int getPlayerScore() {
        return getPlayerScore(gameStats.activePlayerIdx);
    }
    protected int getPlayerScore(int playerIdx) {
        return gameStats.levelScores[playerIdx];
    }
    public boolean isGameActive() {
        return (this.gameStats.timeRemaining > 0);
    }
    public boolean hasGameHadFirstUpdate() {
        return this.gameStats.hasHadFirstUpdate;
    }
    public boolean isGamePaused() {
        return this.gameStats.isPaused;
    }
    public int getPlayerCount() {
        return (elevatorControllers[1] != null) ? 2 : 1;
    }
    public int getActivePlayerIdx() {
        return this.gameStats.activePlayerIdx;
    }
    protected void setActivePlayerIdx(int playerIdx) {
        this.gameStats.activePlayerIdx = playerIdx;
    }
    protected ElevatorController getElevatorController(int playerIdx) {
        return elevatorControllers[playerIdx];
    }

    // Member variables...
    private boolean initialized = false;
    private GameConfig gameConfig = null;
    private GameStats gameStats = new GameStats();
    private ElevatorController[] elevatorControllers = { null, null };
    private long milliSecondsLastUpdate = System.currentTimeMillis();
    private Timer updateTimer;

    // Member functions (methods)...
    protected boolean create(GameConfig config, ElevatorController elevatorController) {
        return create(config, elevatorController, null);
    }
    protected boolean create(GameConfig config, ElevatorController elevatorController1, ElevatorController elevatorController2) {
        // Save off the config & controller...
        this.gameConfig = config;
        this.elevatorControllers[0] = elevatorController1;
        this.elevatorControllers[1] = elevatorController2;

        // Create the Window (JFrame), which creates the WorldFrame (JPanel), which creates up the World(s)...
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

    protected void awardPoints(int playerIdx, int points) {
        gameStats.levelScores[playerIdx] += points;
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
        for (int playerIdx = 0; playerIdx < getPlayerCount(); ++playerIdx) {
            Simulation.get(playerIdx).destroyAll();
        }
        
        // Create the elevators...
        for (int playerIdx = 0; playerIdx < getPlayerCount(); ++playerIdx) {
            for (int i = 0; i < gameConfig.elevatorCount; ++i) {
                Simulation.get(playerIdx).createElevator(i, 0);
            }
        }

        // Zombies (initial set of them)...
        for (int i = 0; i < gameConfig.floorCount / 2; ++i) {
            int spawnFloor = Util.randRangeInt(0, Game.get().getFloorCount() - 1);
            int targetFloor = Zombie.calcRandomTargetFloor(spawnFloor);
            for (int playerIdx = 0; playerIdx < getPlayerCount(); ++playerIdx) {
                Simulation.get(playerIdx).createZombie(spawnFloor, targetFloor);
            }
            gameStats.timeSinceSpawnedZombie = 0.0f;
        }

        // Elevator requests...
        for (int playerIdx = 0; playerIdx < getPlayerCount(); ++playerIdx) {
            Simulation.get(playerIdx).createElevatorRequests(gameConfig.floorCount);
        }
        
        // Do an initial update...
        onLevelUpdate(0, true);

        // Mark us a initialized...
        initialized = true;
    }
    private void onLevelUpdate(double deltaTime, boolean firstUpdate) {
        // Elevator controller update event...
        for (int playerIdx = 0; playerIdx < getPlayerCount(); ++playerIdx) {
            ElevatorController controller = getElevatorController(playerIdx);
            if (controller != null) {
                gameStats.activePlayerIdx = playerIdx;
                if (firstUpdate) {
                    controller.onGameStarted(this);
                }
                controller.onUpdate(deltaTime);
            }
        }
        gameStats.activePlayerIdx = 0;

        // Possibly spawn new zombies...
        gameStats.timeSinceSpawnedZombie += deltaTime;
        if (gameStats.timeSinceSpawnedZombie >= gameConfig.zombieSpawnPeriod) {
            int spawnFloor = Util.randRangeInt(0, Game.get().getFloorCount() - 1);
            int targetFloor = Zombie.calcRandomTargetFloor(spawnFloor);
            for (int playerIdx = 0; playerIdx < getPlayerCount(); ++playerIdx) {
                Simulation.get(playerIdx).createZombie(spawnFloor, targetFloor);
            }
            gameStats.timeSinceSpawnedZombie -= gameConfig.zombieSpawnPeriod;
        }

        // First update...
        gameStats.hasHadFirstUpdate |= firstUpdate;
    }
    private boolean updateElevatorController(double deltaTime) {
        // Don't call if we are paused...
        if (isGamePaused() || !isGameActive()) {
            return true;
        }

        // Update it...
        for (int playerIdx = 0; playerIdx < getPlayerCount(); ++playerIdx) {
            ElevatorController controller = getElevatorController(playerIdx);
            if (controller != null) {
                // The controller's don't deal directly with the playerIdx, this takes care of it for them...
                gameStats.activePlayerIdx = playerIdx;
                controller.onUpdate(deltaTime);
            }
        }
        gameStats.activePlayerIdx = 0;
        
        return true;
    }

    public void actionPerformed(ActionEvent evt) {
        if (hasGameHadFirstUpdate()) {
            Game.get().update();
        }
    }

    protected static GameConfig readGameConfigFromServer() {
        // Default config...
        GameConfig gameConfig = new GameConfig();

        // Try to read it from the server...
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
        for (int playerIdx = 0; playerIdx < getPlayerCount(); ++playerIdx) {
            Simulation.get(playerIdx).update(deltaTime);
        }

        // Update level logic...
        onLevelUpdate(deltaTime, false);
        
        // Code update...
        updateElevatorController(deltaTime);
        
        // Cull anything we can...
        for (int playerIdx = 0; playerIdx < getPlayerCount(); ++playerIdx) {
            Simulation.get(playerIdx).cullNotInField();
        }
        
        // Update levelTime...
        double prevTimeRemaining = gameStats.timeRemaining;
        gameStats.timeRemaining = Math.max(gameStats.timeRemaining - deltaTime, 0);

        // End of game event (post to the leaderboard)...
        if ((gameStats.timeRemaining == 0) && (prevTimeRemaining > 0)) {
            // Go through all players...
            for (int playerIdx = 0; playerIdx < getPlayerCount(); ++playerIdx) {
                ElevatorController controller = getElevatorController(playerIdx);
                if ((controller != null) && 
                    (controller.getStudentPeriod() >= 1) && (controller.getStudentPeriod() <= 7) && 
                    (controller.getStudentName().length() > 0)) {
                    // Go ahead and post it (if the game was cloud configured)...
                    if (gameConfig.isCloudConfigured) {
                        postScoreToLeaderboard(controller.getStudentPeriod(), controller.getStudentName(), getPlayerScore(playerIdx));
                    }
                }
            }
        }
    }

    protected void Draw(Graphics2D g) {
        // Time remaining...
        for (int playerIdx = 0; playerIdx < getPlayerCount(); ++playerIdx) {
            Vec2 levelTimePos = Util.toCoordFrame(playerIdx, new Vec2((Window.get().getWidth() - World.FIELD_BORDER_BOT * 2) / 2, World.FIELD_BORDER_TOP / 2));
            Vec2 levelTimeHalfDims = new Vec2(Util.toCoordFrameLength(playerIdx, LEVELTIMER_TEXT_BOX_HALF_DIMS_PIXELS.x), Util.toCoordFrameLength(playerIdx, LEVELTIMER_TEXT_BOX_HALF_DIMS_PIXELS.y));
            String levelTimeText = isGamePaused() ? "PAUSED" : Util.toIntStringCeil(gameStats.timeRemaining);
            Color levelTimeColor = isGamePaused() ? Color.BLUE : ((gameStats.timeRemaining < 10) ? Color.red : new Color(50, 160, 130));
            Color levelTimeBckGndColor = isGamePaused() ? Color.YELLOW : ((gameStats.timeRemaining < 10) ? new Color(255, 155, 155) : new Color(235, 235, 235));
            Color levelTimeStroke = (gameStats.timeRemaining < 10) ? Color.RED : Color.DARK_GRAY;
            Draw.drawRect(playerIdx, g, levelTimePos, levelTimeHalfDims, 1.0, levelTimeBckGndColor, levelTimeStroke, 0.075 * 42 / World.get(0).getPixelsPerUnit(), 0.15 * 42 / World.get(0).getPixelsPerUnit());
            Draw.drawTextCentered(playerIdx, g, levelTimeText, levelTimePos, isGamePaused() ? Draw.FontSize.XSMALL : Draw.FontSize.LARGE, levelTimeColor, Color.BLACK);
        }

        // Player(s)...
        for (int playerIdx = 0; playerIdx < getPlayerCount(); ++playerIdx) {
            ElevatorController controller = getElevatorController(playerIdx);
            if (controller != null) {
                Vec2 playerHalfDims = new Vec2(Util.toCoordFrameLength(playerIdx, PLAYER_DISPLAY_TEXT_BOX_HALF_DIMS_PIXELS.x), Util.toCoordFrameLength(playerIdx, PLAYER_DISPLAY_TEXT_BOX_HALF_DIMS_PIXELS.y));
                double playerPosX = (playerIdx == 0) ? (World.FIELD_BORDER_BOT - 3) : (Window.get().getWidth() - World.FIELD_BORDER_BOT * 2 - 3 - PLAYER_DISPLAY_TEXT_BOX_HALF_DIMS_PIXELS.x * 2);
                Vec2 playerPos = Vec2.add(Util.toCoordFrame(playerIdx, new Vec2(playerPosX, World.FIELD_BORDER_TOP / 2 + 6)), new Vec2(playerHalfDims.x, 0));
                String playerText = "Score: " + getPlayerScore(playerIdx);
                Color playerColor = new Color(50, 180, 50);
                Color playerBckGndColor = Color.WHITE;
                Color playerStroke = new Color(10, 70, 10);
                Draw.drawRect(playerIdx, g, playerPos, playerHalfDims, 1.0, playerBckGndColor, playerStroke, 0.05 * 42 / World.get(0).getPixelsPerUnit(), 0.1 * 42 / World.get(0).getPixelsPerUnit());
                Draw.drawTextCentered(playerIdx, g, controller.getStudentName(), new Vec2(playerPos.x, playerPos.y + playerHalfDims.y * 0.55), Draw.FontSize.XSMALL, playerColor, Color.BLACK);
                Draw.drawTextCentered(playerIdx, g, playerText, new Vec2(playerPos.x, playerPos.y - playerHalfDims.y * 0.35), Draw.FontSize.SMALL, playerColor, Color.BLACK);
            }
        }

        // End of Game UI...
        if (gameStats.timeRemaining == 0) {
            int playerIdx = (getPlayerCount() == 1) ? 0 : (getPlayerScore(1) > getPlayerScore(0) ? 1 : 0);
            Vec2 finalTextPos = Vec2.multiply(Util.maxCoordFrameUnits(playerIdx), 0.5);
            Color textColor = Color.BLUE;
            Color bckGndColor = new Color(230, 230, 230);
            String finalText = ("Final Score: " + getPlayerScore(0));
            if (getPlayerCount() > 1) {
                // Two player mode, declare a winner...
                finalText = (getPlayerScore(0) > getPlayerScore(1)) ? ("Winner: " + getElevatorController(0).getStudentName()) :
                                (getPlayerScore(1) > getPlayerScore(0)) ? ("Winner: " + getElevatorController(1).getStudentName()) : "TIE GAME!!!";
            }
            Draw.drawRect(playerIdx, g, finalTextPos, new Vec2(3.5, 0.5), 1.0, bckGndColor, Color.BLACK, 0.05 * 42 / World.get(0).getPixelsPerUnit(), 0.1 * 42 / World.get(0).getPixelsPerUnit());
            Draw.drawTextCentered(playerIdx, g, finalText, finalTextPos, Draw.FontSize.XLARGE, textColor, Color.BLACK);                                       
        }
    }
}
