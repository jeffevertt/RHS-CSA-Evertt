package game;

import java.util.ArrayList;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.event.*;
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
    public static final int POINTS_POWERUP_RANGE    = 20;   // Percentile increase
    public static final int POINTS_POWERUP_SPEED    = 15;   // Percentile increase

    private static final int UPDATE_TIMER_PERIOD  = 16;  // In milliseconds
    private static final Vec2 LEVELTIMER_TEXT_BOX_HALF_DIMS_PIXELS = new Vec2(40, (double)World.FIELD_BORDER_TOP * 0.5);
    private static final Vec2 PLAYER_DISPLAY_TEXT_BOX_HALF_DIMS_PIXELS = new Vec2(85, (double)World.FIELD_BORDER_TOP * 0.55);

    // Nested classes...
    protected class GameStats {
        public int playerCount = 1;
        public double timeRemaining = STARTING_LEVEL_TIME;
	    public int levelScore = 0;
        public int levelScore2 = 0;    // "Other" player score

        public void reset() {
            playerCount = 1;
            timeRemaining = STARTING_LEVEL_TIME;
	        levelScore = 0;
            levelScore2 = 0;
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
    public static boolean Create() {
        return Game.get().create();
    }

    // Accessors...
    protected int getPlayerCount() {
        return gameStats.playerCount;
    }
    protected double getLevelTimeRemaining() {
        return gameStats.timeRemaining;
    }
    protected int getLevelTimeMax() {
        return STARTING_LEVEL_TIME;
    }
    protected int getPlayerScore(int playerIdx) {
        if (playerIdx == 0) {
            return gameStats.levelScore;
        }
        else if (playerIdx == 1) {
            return gameStats.levelScore2;
        }
        return (int)-1;
    }

    // Member variables...
    private GameStats gameStats = new GameStats();
    private long milliSecondsLastUpdate = System.currentTimeMillis();
    private Timer updateTimer;

    // Member functions (methods)...
    protected boolean create() {
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
        boolean powerUpsOnlyPointsAndSpeed = true;
		int spawnTargetCount = 0;
        boolean spawnInCenterOfField = (gameStats.playerCount > 1) && firstUpdate;
		
		// If no powerups, spawn one...
        int spawnCount = Math.max(spawnPowerUpCount - Simulation.get().objectCount(PowerUp.class), 0);
		for (int i = 0; i < spawnCount; ++i) {
            Vec2 powerupLocation = pickSpawnLocation(6, 5, 5, 0.9, true, spawnInCenterOfField);
            double powerupRand = Util.randRange(0, 1);
            String powerupType = ((powerupRand < 0.333) ? "P" : ((powerupRand < 0.666) ? "R" : "S"));
            if (powerUpsOnlyPointsAndSpeed) {
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

    protected void update() {
        // Step the sim...
        long milliSecondsNow = System.currentTimeMillis();
        double deltaTime = (double)(milliSecondsNow - milliSecondsLastUpdate) / 1000;
        milliSecondsLastUpdate = milliSecondsNow;
        
        // Clamp deltaTime (slow down the sim, if needed)...
        deltaTime = Math.min(deltaTime, 0.1);
        
        // Sim manager...
        Simulation.get().update(deltaTime);

        // Update level logic...
        onLevelUpdate(deltaTime, false);
        
        // Code update...
        updateTankAI(deltaTime);
        
        // Cull anything we can...
        Simulation.get().cullNotInField();
        
        // Update levelTime...
        gameStats.timeRemaining = Math.max(gameStats.timeRemaining - deltaTime, 0);
    }

    protected void Draw(Graphics2D g) {
        // Time remaining...
        Vec2 levelTimePos = Util.toCoordFrame(new Vec2((Window.get().getWidth() - World.FIELD_BORDER * 2) / 2, World.FIELD_BORDER_TOP / 2 + 5));
        Vec2 levelTimeHalfDims = new Vec2(Util.toCoordFrameLength(LEVELTIMER_TEXT_BOX_HALF_DIMS_PIXELS.x), Util.toCoordFrameLength(LEVELTIMER_TEXT_BOX_HALF_DIMS_PIXELS.y));
        String levelTimeText = Util.toIntStringFloor(gameStats.timeRemaining);
        Color levelTimeColor = (gameStats.timeRemaining < 10) ? Color.red : new Color(50, 160, 130);
        Color levelTimeBckGndColor = (gameStats.timeRemaining < 10) ? new Color(255, 155, 155) : new Color(235, 235, 235);
        Color levelTimeStroke = (gameStats.timeRemaining < 10) ? Color.RED : Color.DARK_GRAY;
        Draw.drawRect(g, levelTimePos, 0, levelTimeHalfDims, 1.0, levelTimeBckGndColor, levelTimeStroke, 0.075 * 42 / World.get().getPixelsPerUnit(), 0.15 * 42 / World.get().getPixelsPerUnit());
		Draw.drawTextCentered(g, levelTimeText, levelTimePos, 0, Draw.FontSize.LARGE, levelTimeColor, Color.BLACK);

        // Player 1...
        Tank tank = getTank(0);
        if (tank != null) {
            Vec2 player1HalfDims = new Vec2(Util.toCoordFrameLength(PLAYER_DISPLAY_TEXT_BOX_HALF_DIMS_PIXELS.x), Util.toCoordFrameLength(PLAYER_DISPLAY_TEXT_BOX_HALF_DIMS_PIXELS.y));
            Vec2 player1Pos = Vec2.add(Util.toCoordFrame(new Vec2(World.FIELD_BORDER - 3, World.FIELD_BORDER_TOP / 2 + 6)), new Vec2(player1HalfDims.x, 0));
            String player1Text = "Score: " + gameStats.levelScore;
            Color player1Color = new Color(50, 180, 50);
            Color player1BckGndColor = Util.colorLerp(new Color(220, 245, 220), new Color(120, 165, 120), 1.0f - tank.getUIStats().timeSinceFeedbackCodeClean * 2);
            player1BckGndColor = Util.colorLerp(player1BckGndColor, new Color(165, 100, 100), 1.0f - tank.getUIStats().timeSinceFeedbackCodeError * 0.5);
            Color player1Stroke = new Color(10, 70, 10);
            Draw.drawRect(g, player1Pos, 0, player1HalfDims, 1.0, player1BckGndColor, player1Stroke, 0.05 * 42 / World.get().getPixelsPerUnit(), 0.1 * 42 / World.get().getPixelsPerUnit());
            Draw.drawTextCentered(g, tank.getPlayerName(), new Vec2(player1Pos.x, player1Pos.y + player1HalfDims.y * 0.55), 0, Draw.FontSize.XSMALL, player1Color, Color.BLACK);
            Draw.drawTextCentered(g, player1Text, new Vec2(player1Pos.x, player1Pos.y - player1HalfDims.y * 0.35), 0, Draw.FontSize.SMALL, player1Color, Color.BLACK);
        }
		
        // Player 2...
        tank = getTank(1);
        if ((tank != null) && (gameStats.playerCount > 1)) {
            Vec2 player2HalfDims = new Vec2(Util.toCoordFrameLength(PLAYER_DISPLAY_TEXT_BOX_HALF_DIMS_PIXELS.x), Util.toCoordFrameLength(PLAYER_DISPLAY_TEXT_BOX_HALF_DIMS_PIXELS.y));
            Vec2 player2Pos = Vec2.subtract(Util.toCoordFrame(new Vec2(Window.get().getWidth() - World.FIELD_BORDER - 13, World.FIELD_BORDER_TOP / 2 + 6)), new Vec2(player2HalfDims.x, 0));
            String player2Text = "Score: " + gameStats.levelScore2;
            Color player2Color = new Color(50, 50, 180);
            Color player2BckGndColor = Util.colorLerp(new Color(220, 220, 245), new Color(120, 120, 165), 1.0f - tank.getUIStats().timeSinceFeedbackCodeClean * 2);
            player2BckGndColor = Util.colorLerp(player2BckGndColor, new Color(165, 100, 100), 1.0f - tank.getUIStats().timeSinceFeedbackCodeError * 0.5);
            Color player2Stroke = new Color(10, 10, 70);
            Draw.drawRect(g, player2Pos, 0, player2HalfDims, 1.0, player2BckGndColor, player2Stroke, 0.05 * 42 / World.get().getPixelsPerUnit(), 0.1 * 42 / World.get().getPixelsPerUnit());
            Draw.drawTextCentered(g, tank.getPlayerName(), new Vec2(player2Pos.x, player2Pos.y + player2HalfDims.y * 0.55), 0, Draw.FontSize.XSMALL, player2Color, Color.BLACK);
            Draw.drawTextCentered(g, player2Text, new Vec2(player2Pos.x, player2Pos.y - player2HalfDims.y * 0.35), 0, Draw.FontSize.SMALL, player2Color, Color.BLACK);
        }

        // Winner UI...
        if (gameStats.timeRemaining == 0) {
            Vec2 finalTextPos = Vec2.multiply(Util.maxCoordFrameUnits(), 0.5);
            String finalText = (gameStats.playerCount == 1) ? ("Final Score: " + gameStats.levelScore) :
                                    ((gameStats.levelScore == gameStats.levelScore2) ? "TIE GAME!" :  
                                        ((gameStats.levelScore > gameStats.levelScore2) ? (getTank(0).getPlayerName() + " Wins!") : (getTank(1).getPlayerName() + " Wins!")));
            Color textColor = (gameStats.playerCount == 1) ? Tank.TANK_COLOR_TURRET_FILL_1 : 
                                    ((gameStats.levelScore == gameStats.levelScore2) ? Color.MAGENTA :  
                                        ((gameStats.levelScore > gameStats.levelScore2) ? Tank.TANK_COLOR_TURRET_FILL_1 : Tank.TANK_COLOR_TURRET_FILL_2));
            Draw.drawTextCentered(g, finalText, finalTextPos, 0, Draw.FontSize.XLARGE, textColor, Color.BLACK);                                       
        }
    }
}
