import java.util.ArrayList;
import java.awt.event.*;
import javax.swing.Timer;

public class Game implements ActionListener {
    // Constants...
    private final int STARTING_LEVEL_TIME = 90;
    private final int UPDATE_TIMER_PERIOD = 16;  // In milliseconds

    // Nested classes...
    public class GameStats {
        public double timeRemaining = STARTING_LEVEL_TIME;
	    public int levelScore = 0;
        public int levelScore2 = 0;    // "Other" player score

        public void reset() {
            timeRemaining = STARTING_LEVEL_TIME;
	        levelScore = 0;
            levelScore2 = 0;
        }
    }
    public class UIStats {
        public double timeFeedbackCodeClean1 = 0;
	    public double timeFeedbackCodeError1 = 0;
	    public double timeFeedbackCodeClean2 = 0;
	    public double timeFeedbackCodeError2 = 0;

        public void reset() {
            timeFeedbackCodeClean1 = 0;
            timeFeedbackCodeError1 = 0;
            timeFeedbackCodeClean2 = 0;
            timeFeedbackCodeError2 = 0;
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

    // Member variables...
    private GameStats gameStats = new GameStats();
    private UIStats uiStats = new UIStats();
    private long milliSecondsLastUpdate = System.currentTimeMillis();
    private Timer updateTimer;

    // Member functions (methods)...
    public boolean create() {
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

    public Tank getTank() {
        return getTank(0);
    }
    public Tank getTank(int playerIdx) {
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

	public void awardPoints(int points, int playerIdx) {
		gameStats.levelScore += (playerIdx == 0) ? points : 0;
		gameStats.levelScore2 += (playerIdx == 1) ? points : 0;
		updateTimerAndScoreText();
	}
    public void awardPoints(int points) {
        awardPoints(points, 0);
    }
    public void updateTimerAndScoreText() {
		// textTimer.attr({ "text": "Time: " + Math.ceil(levelTime) });
		// textTimer.toFront();
		// textTimer.attr({ x: canvasSize.w - textTimer.getBBox().width - 10, y:16 });
		
		// textScore.attr({ "text": "Score: " + levelScore + ((currentLevel == 4) ? " v " + levelScore2 : "") });
		// textScore.toFront();
		// textScore.attr({ x: canvasSize.w - textScore.getBBox().width - 10, y:38 });
	}    

    public Vec2 pickSpawnLocation(double minDstFromTanks, double minDstFromTargets, double minDstFromPowerUps, double minDstFromSides, boolean clampToCenters, boolean favorMidX) {
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

    public void onLevelSetup() {
		// Default time for level...
		gameStats.reset();
		uiStats.reset();

        // Reset the simulation...
        Simulation.get().destroyAll();
		
		// Level specific objects...
		Simulation.get().createTank(0, new Vec2(1.5, 1.5), Vec2.right());

        // Do an initial update...
        onLevelUpdate(0, true);

        // Text...
		//textLevel.attr({ "text":"level " + currentLevel + " (" + calcLevelDesc(currentLevel) + ")" });
		//textLevel.toFront();
		//updateTimerAndScoreText();        
    }
    private void onLevelUpdate(double deltaTime, boolean firstUpdate) {
		// Level specific objects...
        boolean spawnPowerUp = true;
        boolean powerUpsOnlyPoints = false;
		boolean spawnTarget = false;
        boolean spawnInCenterOfField = false;
		
		// If no powerups, spawn one...
		if (spawnPowerUp) {
			if (Simulation.get().objectCount(PowerUp.class) == 0) {
				Vec2 powerupLocation = pickSpawnLocation(6, 5, 5, 0.9, true, spawnInCenterOfField);
				double powerupRand = Util.randRange(0, 1);
				String powerupType = (powerUpsOnlyPoints || (powerupRand < 0.333)) ? "P" : ((powerupRand < 0.666) ? "R" : "S");
				Simulation.get().createPowerUp(powerupLocation, powerupType);
			}
		}

		// If no targets, spawn one...
		if (spawnTarget) {
			if (Simulation.get().objectCount(Target.class) == 0) {
				Vec2 targetLocation = pickSpawnLocation(6, 5, 5, 0.9, true, spawnInCenterOfField);
				Simulation.get().createTarget(targetLocation);
			}
		}
    }

    public void actionPerformed(ActionEvent evt) {
        Game.get().update();
    } 
    public void update() {
        // Step the sim...
        long milliSecondsNow = System.currentTimeMillis();
        double deltaTime = (double)(milliSecondsNow - milliSecondsLastUpdate) / 1000;
        milliSecondsLastUpdate = milliSecondsNow;
        
        // Clamp deltaTime (slow down the sim, if needed)...
        deltaTime = Math.min(deltaTime, 0.1);
        
        // Sim manager...
        Simulation.get().update(deltaTime);
        
        // Code update...
        //boolean hitError = false;
        // if (simMan.isReadyToAskCodeForNextCommand(0) && (levelTime > 0)) {
        //     hitError = !simMan.execCode(0, document.getElementById('textCode1').value);
        //     if (!hitError) { timeFeedbackCodeClean1 = 1; } else { timeFeedbackCodeError1 = 1; }
        // }
        // if (!hitError && (simMan.isReadyToAskCodeForNextCommand(1) && (levelTime > 0) && (currentLevel == 4))) {
        //     hitError = !simMan.execCode(1, document.getElementById('textCode2').value);
        //     if (!hitError) { timeFeedbackCodeClean2 = 1; } else { timeFeedbackCodeError2 = 1; }
        // }

        // Code success / error feedback...
        // if ((timeFeedbackCodeClean1 > 0) || (timeFeedbackCodeError1 > 0)) {
        //     timeFeedbackCodeClean1 = (timeFeedbackCodeClean1 > 0) ? Math.max(timeFeedbackCodeClean1 - deltaTime * 3, 0) : 0;
        //     timeFeedbackCodeError1 = (timeFeedbackCodeError1 > 0) ? Math.max(timeFeedbackCodeError1 - deltaTime * 3, 0) : 0;
        //     let color0 = (timeFeedbackCodeClean1 > timeFeedbackCodeError1) ? 'rgb(190, 255, 190)' : 'rgb(255, 190, 190)';
        //     let color = colorLerp(color0, 'rgb(255, 255, 255)', 1 - Math.max(timeFeedbackCodeClean1, timeFeedbackCodeError1));
        //     document.getElementById('textCode1').style="background-color: " + rgbToHex(color.r, color.g, color.b) + "; width:100%; height:90%;";
        // }
        // if ((timeFeedbackCodeClean2 > 0) || (timeFeedbackCodeError2 > 0)) {
        //     timeFeedbackCodeClean2 = (timeFeedbackCodeClean2 > 0) ? Math.max(timeFeedbackCodeClean2 - deltaTime * 3, 0) : 0;
        //     timeFeedbackCodeError2 = (timeFeedbackCodeError2 > 0) ? Math.max(timeFeedbackCodeError2 - deltaTime * 3, 0) : 0;
        //     let color0 = (timeFeedbackCodeClean2 > timeFeedbackCodeError2) ? 'rgb(190, 255, 190)' : 'rgb(255, 190, 190)';
        //     let color = colorLerp(color0, 'rgb(255, 255, 255)', 1 - Math.max(timeFeedbackCodeClean2, timeFeedbackCodeError2));
        //     document.getElementById('textCode2').style="background-color: " + rgbToHex(color.r, color.g, color.b) + "; width:100%; height:90%;";
        // }
        
        // Cull anything we can...
        Simulation.get().cullNotInField();
        
        // Update levelTime...
        gameStats.timeRemaining = Math.max(gameStats.timeRemaining - deltaTime, 0);
        updateTimerAndScoreText();
        
        // Update level logic...
        onLevelUpdate(deltaTime, false);
    }
}
