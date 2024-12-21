package game;

import java.util.ArrayList;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.event.*;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.swing.Timer;

import ai.OtherAI;

public class Game implements ActionListener {
    // Constants...
    public static final int STARTING_LEVEL_TIME     = 90;

    public static final int POINTS_CMD              = -1;
    public static final int POINTS_CMD_SHOT         = -2;
    public static final int POINTS_HIT_OTHER        = 10;
    public static final int POINTS_HIT_TARGET       = 25;
    public static final int POINTS_POWERUP_PTS      = 25;
    public static final int POINTS_POWERUP_RANGE    = 10;   // Percentile increase
    public static final int POINTS_POWERUP_SPEED    = 10;   // Percentile increase

    private static final int UPDATE_TIMER_PERIOD    = 16;   // In milliseconds
    private static final Vec2 LEVELTIMER_TEXT_BOX_HALF_DIMS_PIXELS = new Vec2(40, (double)World.FIELD_BORDER_TOP * 0.5);
    private static final Vec2 PLAYER_DISPLAY_TEXT_BOX_HALF_DIMS_PIXELS = new Vec2(85, (double)World.FIELD_BORDER_TOP * 0.55);

    public static final int TEST_HARNESS_SUPPORT    = 0;    // When non-zero, will run the specified timeSteps at a rate of 60 FPS

    public static boolean playWithTargets = false;          // By default, no targets...later we'll add them (main configures this when it creates the game)

    // Nested classes...
    protected class GameStats {
        public int playerCount = 0;
        public boolean isPaused = false;
        public double timeRemaining = 0;
	    public int levelScore = 0;
        public int levelScore2 = 0;    // "Other" player score

        public void reset() {
            playerCount = 1;
            isPaused = false;
            timeRemaining = STARTING_LEVEL_TIME;
	        levelScore = 0;
            levelScore2 = 0;
        }
    }

	// Singleton...
	private static Game instance = null;
	public static synchronized Game get()
    {
        if (instance == null)
            instance = new Game();
  
        return instance;
    }
    public static boolean Create() {
        return Create(false);
    }
    public static boolean Create(boolean playWithTargets) {
        return Game.get().create(playWithTargets);
    }

    // Accessors...
    public int getPlayerCount() {
        return gameStats.playerCount;
    }
    public double getLevelTimeRemaining() {
        return gameStats.timeRemaining;
    }
    public int getLevelTimeMax() {
        return STARTING_LEVEL_TIME;
    }
    public int getPlayerScore(int playerIdx) {
        if (playerIdx == 0) {
            return gameStats.levelScore;
        }
        else if (playerIdx == 1) {
            return gameStats.levelScore2;
        }
        return (int)-1;
    }
    public boolean isGameActive() {
        return (this.gameStats.timeRemaining > 0);
    }
    public boolean isGamePaused() {
        return this.gameStats.isPaused;
    }

    // Member variables...
    private GameStats gameStats = new GameStats();
    private long milliSecondsLastUpdate = System.currentTimeMillis();
    private Timer updateTimer;

    // Member functions (methods)...
    protected boolean create(boolean playWithTargets) {
        // Save off our game config...
        Game.playWithTargets = playWithTargets;

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

    protected Tank getTank() {
        return getTank(0);
    }
    protected Tank getTank(int playerIdx) {
        ArrayList<GameObject> gameObjects = Simulation.getGameObjects();
        for (int i = 0; i < gameObjects.size(); ++i) {
            GameObject gameObject = gameObjects.get(i);
            if (gameObject instanceof Tank) {
                Tank tank = (Tank)gameObject;
                if (tank.getPlayerIdx() == playerIdx) {
                    return tank;
                }
            }
        }
        return null;
    }

	protected void awardPoints(int points, int playerIdx) {
		gameStats.levelScore += (playerIdx == 0) ? points : 0;
		gameStats.levelScore2 += (playerIdx == 1) ? points : 0;
	}
    protected void awardPoints(int points) {
        awardPoints(points, 0);
    }

    protected void setGamePause(boolean isPaused) {
        // If the game is done, ignore this...
        if (!isGameActive()) {
            return;
        }

        // Set it...
        gameStats.isPaused = isPaused;
    }

    protected Vec2 pickSpawnLocation(double minDstFromTanks, double minDstFromTargets, double minDstFromPowerUps, double minDstFromSides, boolean clampToCenters, boolean favorMidX) {
		// Loop over some number of attempts...
		int maxAttempts = 20;
		Vec2 pos = new Vec2(Util.randRange(0, Util.toCoordFrameLength(World.get().getCanvasSize().x)), Util.randRange(0, Util.toCoordFrameLength(World.get().getCanvasSize().y)));
		for (int i = 0; i < maxAttempts; ++i) {
			// Random...
			pos = new Vec2(Util.randRange(0, Util.toCoordFrameLength(World.get().getCanvasSize().x)), Util.randRange(0, Util.toCoordFrameLength(World.get().getCanvasSize().y)));
			
			// Support favorMidX (we use this on multiplayer, to try to keep things more fair)...
			if (favorMidX) {
				pos.x = Util.toCoordFrameLength(World.get().getCanvasSize().x / 2 * Util.randRange(0.9, 1.1));
			}
			
			// Maybe clamp to square centers...
			pos = clampToCenters ? new Vec2(Math.floor(pos.x) + 0.5, Math.floor(pos.y) + 0.5) : pos;
		
			// Check it against our constraints (if last attempt, just go with it)...
			if (i < (maxAttempts - 1)) {
				// Check distance from sides...
				double minDstFromSide = Math.min(Math.min(Math.min(pos.x, pos.y), Util.toCoordFrameLength(World.get().getCanvasSize().x) - pos.x), Util.toCoordFrameLength(World.get().getCanvasSize().y) - pos.y);
				if (minDstFromSide < minDstFromSides) {
					continue;
				}

				// Next, distance from tanks, targets, powerups...
				double minDstFromTank = Simulation.get().calcMinDstFromATank(pos);
				if (minDstFromTank < minDstFromTanks) {
					continue;
				}
				double minDstFromTarget = Simulation.get().calcMinDstFromATarget(pos);
				if (minDstFromTarget < minDstFromTargets) {
					continue;
				}
				double minDstFromPowerUp = Simulation.get().calcMinDstFromAPowerUp(pos);
				if (minDstFromPowerUp < minDstFromPowerUps) {
					continue;
				}
			}
			
			// This one works, break out...
			break;
		}
		return pos;
	}    

    protected void onLevelSetup() {
		// Default time for level...
		gameStats.reset();

        // Figure out if it is a two player game, base on if the OtherAI has a player name set...
        if (!(new OtherAI()).getPlayerName().equals(TankAIBase.DEFAULT_PLAYER_NAME)) {
            gameStats.playerCount = 2;
        }
		
        // Reset the simulation...
        Simulation.get().destroyAll();
		
		// Level specific objects...
		Simulation.get().createTank(0, new Vec2(1.5, 1.5), Vec2.right());
        if (gameStats.playerCount > 1) {
            Simulation.get().createTank(1, new Vec2(Util.maxHalfUnitsInField().x - 1, 1.5), Vec2.left());
        }

        // Do an initial update...
        onLevelUpdate(0, true);

        // Tanks...
        for (int i = 0; i < 2; ++i) {
            Tank tank = getTank(i);
            if (tank != null) {
                tank.reset();
            }
        }        
    }
    private void onLevelUpdate(double deltaTime, boolean firstUpdate) {
		// Level specific objects...
        int spawnPowerUpCount = 3;
		int spawnTargetCount = playWithTargets ? 3 : 0;
        boolean spawnInCenterOfField = (gameStats.playerCount > 1) && firstUpdate;
		
		// If no powerups, spawn one...
        int spawnCount = Math.max(spawnPowerUpCount - Simulation.get().objectCount(PowerUp.class), 0);
		for (int i = 0; i < spawnCount; ++i) {
            Vec2 powerupLocation = pickSpawnLocation(6, 5, 5, 0.9, true, spawnInCenterOfField);
            double powerupRand = Util.randRange(0, 1);
            String powerupType = ((powerupRand < 0.333) ? "P" : ((powerupRand < 0.666) ? "R" : "S"));
            if (!playWithTargets) {
                powerupType = ((powerupRand < 0.5) ? "P" :"S");
            }
            Simulation.get().createPowerUp(powerupLocation, powerupType);
		}

		// If no targets, spawn one...
        spawnCount = Math.max(spawnTargetCount - Simulation.get().objectCount(Target.class), 0);
		for (int i = 0; i < spawnCount; ++i) {
            Vec2 targetLocation = pickSpawnLocation(6, 5, 5, 0.9, true, spawnInCenterOfField);
            Simulation.get().createTarget(targetLocation);
		}
    }
    private boolean updateTankAI(double deltaTime) {
        // Don't call if we are paused...
        if (isGamePaused()) {
            return true;
        }

        // Loop over the players, updating each AI...
        for (int i = 0; i < getPlayerCount(); ++i) {
            if (Simulation.get().isReadyToAskCodeForNextCommand(i) && (gameStats.timeRemaining > 0)) {
                Tank tank = getTank(i);
                if (tank != null) {
                    boolean success = tank.execPlayerAI();
                    if (success) { 
                        tank.getUIStats().timeSinceFeedbackCodeClean = 0;
                    } 
                    else { 
                        tank.getUIStats().timeSinceFeedbackCodeError = 0; 
                    }
                }
            }
        }
        
        return true;
    }

    public void actionPerformed(ActionEvent evt) {
        Game.get().update();
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

        // Game update...
        if (TEST_HARNESS_SUPPORT <= 0) {
            updateGameInternal(deltaTime);
        }
        else {
            // Test harness support...
            for (int i = 0; i < TEST_HARNESS_SUPPORT; i++) {
                updateGameInternal(1 / 60.0f);
            }
        }
    }

    private void updateGameInternal(double deltaTime)
    {
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
        updateTankAI(deltaTime);
        
        // Cull anything we can...
        Simulation.get().cullNotInField();
        
        // Update levelTime...
        double prevTimeRemaining = gameStats.timeRemaining;
        gameStats.timeRemaining = Math.max(gameStats.timeRemaining - deltaTime, 0);
        if (gameStats.timeRemaining == 0) {
            // Cancel all commands...
            for (int i = 0; i < gameStats.playerCount; ++i) {
                Tank tank = getTank(i);
                tank.cancelAllCommands();
            }

            // Post your score to the leaderboard...
            if (((prevTimeRemaining > 0) && (gameStats.timeRemaining == 0)) && (gameStats.playerCount == 1)) {
                Tank tank = getTank(0);
                TankAIBase ai = tank.getAI();
                int period = ai.getPlayerPeriod();
                if ((period >= 1) && (period <= 7) && !ai.getPlayerName().equals(TankAIBase.DEFAULT_PLAYER_NAME)) {
                    // Go ahead and post it...
                    postScoreToLeaderboard(period, ai.getPlayerName(), gameStats.levelScore);
                }
            }
        }
    }

    protected void Draw(Graphics2D g) {
        // Time remaining...
        Vec2 levelTimePos = Util.toCoordFrame(new Vec2((Window.get().getWidth() - World.FIELD_BORDER * Draw.getUIScale() * 2) / 2, World.FIELD_BORDER_TOP * Draw.getUIScale() / 2 + 5 * Draw.getUIScale()));
        Vec2 levelTimeHalfDims = new Vec2(Util.toCoordFrameLength(LEVELTIMER_TEXT_BOX_HALF_DIMS_PIXELS.x), Util.toCoordFrameLength(LEVELTIMER_TEXT_BOX_HALF_DIMS_PIXELS.y));
        String levelTimeText = isGamePaused() ? "PAUSED" : Util.toIntStringCeil(gameStats.timeRemaining);
        Color levelTimeColor = isGamePaused() ? Color.BLUE : ((gameStats.timeRemaining < 10) ? Color.red : new Color(50, 160, 130));
        Color levelTimeBckGndColor = isGamePaused() ? Color.YELLOW : ((gameStats.timeRemaining < 10) ? new Color(255, 155, 155) : new Color(235, 235, 235));
        Color levelTimeStroke = (gameStats.timeRemaining < 10) ? Color.RED : Color.DARK_GRAY;
        Draw.drawRect(g, levelTimePos, 0, levelTimeHalfDims, Draw.getUIScale(), levelTimeBckGndColor, levelTimeStroke, 0.075 * 42 / World.get().getPixelsPerUnit(), 0.15 * 42 / World.get().getPixelsPerUnit());
		Draw.drawTextCentered(g, levelTimeText, levelTimePos, 0, Draw.getUIScale(), isGamePaused() ? Draw.FontSize.XSMALL : Draw.FontSize.LARGE, levelTimeColor, Color.BLACK);

        // Player 1...
        Tank tank = getTank(0);
        if (tank != null) {
            Vec2 player1HalfDims = new Vec2(Util.toCoordFrameLength(PLAYER_DISPLAY_TEXT_BOX_HALF_DIMS_PIXELS.x), Util.toCoordFrameLength(PLAYER_DISPLAY_TEXT_BOX_HALF_DIMS_PIXELS.y));
            Vec2 player1Pos = Vec2.add(Util.toCoordFrame(new Vec2(World.FIELD_BORDER * Draw.getUIScale() - 3 * Draw.getUIScale(), World.FIELD_BORDER_TOP / 2 * Draw.getUIScale() + 6 * Draw.getUIScale())), new Vec2(player1HalfDims.x * Draw.getUIScale(), 0));
            String player1Text = "Score: " + gameStats.levelScore;
            Color player1Color = new Color(50, 180, 50);
            Color player1BckGndColor = Util.colorLerp(new Color(220, 245, 220), new Color(120, 165, 120), 1.0f - tank.getUIStats().timeSinceFeedbackCodeClean * 2);
            player1BckGndColor = Util.colorLerp(player1BckGndColor, new Color(165, 100, 100), 1.0f - tank.getUIStats().timeSinceFeedbackCodeError * 0.5);
            Color player1Stroke = new Color(10, 70, 10);
            Draw.drawRect(g, player1Pos, 0, player1HalfDims, Draw.getUIScale(), player1BckGndColor, player1Stroke, 0.05 * 42 / World.get().getPixelsPerUnit(), 0.1 * 42 / World.get().getPixelsPerUnit());
            Draw.drawTextCentered(g, tank.getPlayerName(), new Vec2(player1Pos.x, player1Pos.y + player1HalfDims.y * 0.55 * Draw.getUIScale()), 0, Draw.getUIScale(), Draw.FontSize.XSMALL, player1Color, Color.BLACK);
            Draw.drawTextCentered(g, player1Text, new Vec2(player1Pos.x, player1Pos.y - player1HalfDims.y * 0.35 * Draw.getUIScale()), 0, Draw.getUIScale(), Draw.FontSize.SMALL, player1Color, Color.BLACK);
        }
		
        // Player 2...
        tank = getTank(1);
        if ((tank != null) && (gameStats.playerCount > 1)) {
            Vec2 player2HalfDims = new Vec2(Util.toCoordFrameLength(PLAYER_DISPLAY_TEXT_BOX_HALF_DIMS_PIXELS.x), Util.toCoordFrameLength(PLAYER_DISPLAY_TEXT_BOX_HALF_DIMS_PIXELS.y));
            Vec2 player2Pos = Vec2.subtract(Util.toCoordFrame(new Vec2(Window.get().getWidth() - World.FIELD_BORDER - 13 * Draw.getUIScale(), World.FIELD_BORDER_TOP / 2 * Draw.getUIScale() + 6 * Draw.getUIScale())), new Vec2(player2HalfDims.x * Draw.getUIScale(), 0));
            String player2Text = "Score: " + gameStats.levelScore2;
            Color player2Color = new Color(50, 50, 180);
            Color player2BckGndColor = Util.colorLerp(new Color(220, 220, 245), new Color(120, 120, 165), 1.0f - tank.getUIStats().timeSinceFeedbackCodeClean * 2);
            player2BckGndColor = Util.colorLerp(player2BckGndColor, new Color(165, 100, 100), 1.0f - tank.getUIStats().timeSinceFeedbackCodeError * 0.5);
            Color player2Stroke = new Color(10, 10, 70);
            Draw.drawRect(g, player2Pos, 0, player2HalfDims, Draw.getUIScale(), player2BckGndColor, player2Stroke, 0.05 * 42 / World.get().getPixelsPerUnit(), 0.1 * 42 / World.get().getPixelsPerUnit());
            Draw.drawTextCentered(g, tank.getPlayerName(), new Vec2(player2Pos.x, player2Pos.y + player2HalfDims.y * 0.55 * Draw.getUIScale()), 0, Draw.getUIScale(), Draw.FontSize.XSMALL, player2Color, Color.BLACK);
            Draw.drawTextCentered(g, player2Text, new Vec2(player2Pos.x, player2Pos.y - player2HalfDims.y * 0.35 * Draw.getUIScale()), 0, Draw.getUIScale(), Draw.FontSize.SMALL, player2Color, Color.BLACK);
        }

        // Winner UI...
        if (gameStats.timeRemaining == 0) {
            Vec2 finalTextPos = Vec2.multiply(Util.maxCoordFrameUnits(), 0.5);
            String finalText = (gameStats.playerCount == 1) ? ("Final Score: " + gameStats.levelScore) :
                                    ((gameStats.levelScore == gameStats.levelScore2) ? "TIE GAME!" :  
                                        ((gameStats.levelScore > gameStats.levelScore2) ? (getTank(0).getPlayerName() + " WINS!") : (getTank(1).getPlayerName() + " Wins!")));
            Color textColor = (gameStats.playerCount == 1) ? Tank.TANK_COLOR_TURRET_FILL_1 : 
                                    ((gameStats.levelScore == gameStats.levelScore2) ? Color.MAGENTA :  
                                        ((gameStats.levelScore > gameStats.levelScore2) ? Tank.TANK_COLOR_TURRET_FILL_1 : Tank.TANK_COLOR_TURRET_FILL_2));
            Color bckGndColor = (gameStats.playerCount == 1) ? new Color(220, 245, 220) : 
                                    ((gameStats.levelScore == gameStats.levelScore2) ? Color.LIGHT_GRAY :  
                                        ((gameStats.levelScore > gameStats.levelScore2) ? new Color(220, 245, 220) : new Color(220, 220, 245)));
            Draw.drawRect(g, finalTextPos, 0, new Vec2(7.5 / Math.sqrt(Draw.getUIScale()), 1), Draw.getUIScale(), bckGndColor, Color.BLACK, 0.05 * 42 / World.get().getPixelsPerUnit(), 0.1 * 42 / World.get().getPixelsPerUnit());
            Draw.drawTextCentered(g, finalText, finalTextPos, 0, Draw.getUIScale(), Draw.FontSize.XLARGE, textColor, Color.BLACK);                                       
        }
    }
}
