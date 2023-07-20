import java.util.ArrayList;
import java.time.LocalTime;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.time.Duration;

public class Tank extends GameObject {
    // Constants...
	public final static Vec2 BODY_HALFSIZE = new Vec2(0.4, 0.325);
	public final static double TREAD_HALFWIDTH = 0.1;
	public final static double TREAD_HALFLENGTH = 0.45;
	public final static double TREAD_OFFSET = 0.35;
	public final static double TURRET_INSET = 0.225;
	public final static double TURRET_HALFLENGTH = 0.375;
	public final static double TURRET_HALFWIDTH = 0.1;

	public static final double TANK_HEIGHT_BODY = 0.7;
	public static final double TANK_HEIGHT_TREADS = 0.2;
	public static final double TANK_HEIGHT_TURRET = 1.2;
	private static final double TANK_STROKE_WIDTH = 0.05;
	private static final double TANK_TURRET_STROKE_WIDTH = 0.035;
	private static final double TANK_ROUNDED_SIZE = 0.1;

	private static final Color TANK_COLOR_BODY_FILL_1 = Color.GREEN;
	private static final Color TANK_COLOR_BODY_FILL_2 = Color.BLUE;
	private static final Color TANK_COLOR_TREAD_FILL = Color.DARK_GRAY;
	private static final Color TANK_COLOR_TURRET_FILL_1 = new Color(44, 120, 44);
	private static final Color TANK_COLOR_TURRET_FILL_2 = new Color(44, 4, 120);
	private static final Color TANK_COLOR_STROKE = Color.BLACK;	

	public final static double STARTING_AMMO_RANGE = 8;
	public final static double STARTING_MOVE_SPEED = 2.5;   // units per second
	public final static double STARTING_TURN_SPEED = 90;	// degrees per second

	// TankCmd (nested classes)...
    public abstract class TankCmd {
		public String type = "";
		public double progress = 0;

		public Vec2 startPos;
		public Vec2 startDir;
	}
    public class TankCmd_Move extends TankCmd {
        static final String TYPE = "move";
		public Vec2 moveVec;
        TankCmd_Move(Vec2 moveVec) {
            this.type = TankCmd_Move.TYPE;
            this.moveVec = moveVec;
        }
		public String toString() {
			return TYPE + ": moveVec=" + this.moveVec;
		}
    }
    public class TankCmd_Turn extends TankCmd {
        static final String TYPE = "turn";
		public Vec2 dir;
        TankCmd_Turn(Vec2 dir) {
            this.type = TankCmd_Turn.TYPE;
            this.dir = dir.unit();
        }
		public String toString() {
			return TYPE + ": dir=" + this.dir;
		}
    }
    public class TankCmd_Shoot extends TankCmd {
        static final String TYPE = "shoot";
		public Vec2 dir;
        TankCmd_Shoot(Vec2 dir) {
            this.type = TankCmd_Shoot.TYPE;
            this.dir = dir.unit();
        }
		public String toString() {
			return TYPE + ": dir=" + this.dir;
		}
    }

	// Track stats for UI display...
	public class UIStats {
        public double timeSinceFeedbackCodeClean = 100000;
	    public double timeSinceFeedbackCodeError = 100000;

        public void reset() {
            timeSinceFeedbackCodeClean = 100000;
            timeSinceFeedbackCodeError = 100000;
        }
        public void update(double deltaTime) {
			timeSinceFeedbackCodeClean += deltaTime;
			timeSinceFeedbackCodeError += deltaTime;
        }
    }

	// Member variables...
	private int playerIdx = -1;
	private Vec2 dir = null;
	private double ammoMaxRange = -1;
	private double tankMoveSpeed = STARTING_MOVE_SPEED; // units per second
	private double tankTurnSpeed = STARTING_TURN_SPEED; // degrees per second
	private LocalTime timeLastShot = LocalTime.now();
	private TankAIBase ai = null;
	private UIStats uiStats = new UIStats();

	private TankCmd activeCommand = null;
	private ArrayList<TankCmd> queuedCommands = new ArrayList<TankCmd>();

	// Accessors...
	public int getPlayerIdx() {
		return this.playerIdx;
	}
	public Vec2 getDir() {
		return dir;
	}
	public Vec2 forward() {
		return this.dir;
	}
	public Vec2 right() {
		return new Vec2(this.dir.y, -this.dir.x);
	}
	public Vec2 ammoSpawnLocation() {
		return this.toWorld(new Vec2(TURRET_HALFLENGTH - TURRET_INSET, 0));
	}
	public UIStats getUIStats() {
		return uiStats;
	}
	
	// Member functions (methods)...
    public Tank(int playerIdx, Vec2 pos, Vec2 dir) {
		// Parent...
		super();

		// Variables...
		this.playerIdx = playerIdx;
        this.pos = pos;
        this.dir = dir;

		// Reset...
		reset();
    }
	
	public void create() {
	}
	
	public void destroy() {
		// Super...
		super.destroy();		
	}

	public void reset() {
		// Stats...
		uiStats.reset();

		// AI...
		this.ai = (playerIdx == 0) ? new TankAI() : new OtherAI();
		this.ai.setTank(this);		

		// Config...
		this.tankMoveSpeed = STARTING_MOVE_SPEED;
		this.tankTurnSpeed = STARTING_TURN_SPEED;
		this.ammoMaxRange = STARTING_AMMO_RANGE;
		this.timeLastShot = LocalTime.now();		
	}
	
	public void kickBackTank(Vec2 dir) {
		// If we are in the middle of a move, stop it...
		if ((this.activeCommand != null) && (this.activeCommand.type.equals(TankCmd_Move.TYPE))) {
			this.finishActiveCommand();
		}
		
		// Do the kick back...
		Vec2 kickBackLoc = Util.clampToClosestHalfUnit(Vec2.add(this.pos, Vec2.multiply(dir.unit(), 1.414)));
		if (Util.isInsideField(kickBackLoc, 0.25)) {
			this.pos = kickBackLoc;
		}
		else {
			// Try fallback...
			kickBackLoc = Vec2.add(this.pos, Vec2.multiply(dir.unit(), 1.414 / 2));
			if (Util.isInsideField(kickBackLoc, 0.25)) {
				this.pos = kickBackLoc;
			}
		}
		
		// Check for tank overlap...
		Tank otherTank = Game.get().getTank(this.playerIdx == 0 ? 1 : 0);
		if ((otherTank != null) && Util.circlesIntersect(this.pos, BODY_HALFSIZE.x, otherTank.pos, BODY_HALFSIZE.x)) {
			this.pos = Vec2.add(this.pos, Vec2.multiply(dir, 0.1));
			otherTank.pos = Vec2.add(otherTank.pos, Vec2.multiply(dir, -0.1));
		}
	}	
	
	public Vec2 toWorld(Vec2 pt) {
		return Vec2.add(Vec2.add(this.pos, Vec2.multiply(this.forward(), pt.x)), Vec2.multiply(this.dir, pt.y));
	}
	
	public void onCollected(PowerUp powerup) {
		// Trigger death spiral...
		this.timeTillDeath = Math.max(this.timeTillDeath, 0.0001);
		
		// Give up the points...
		switch (powerup.getType()) {
			case "S" : // Speed
				tankMoveSpeed *= 1 + ((double)Game.POINTS_POWERUP_SPEED / 100);
				tankTurnSpeed *= 1 + ((double)Game.POINTS_POWERUP_SPEED / 100);
				Util.log("(powerup: speed +" + Game.POINTS_POWERUP_SPEED + "%)");
				break;
			case "R" : // Shot range
				ammoMaxRange *= 1 + ((double)Game.POINTS_POWERUP_RANGE / 100);
				Util.log("(powerup: range +" + Game.POINTS_POWERUP_RANGE + "%)");
				break;
			case "P" : // Points
				Game.get().awardPoints(Game.POINTS_POWERUP_PTS, this.playerIdx);
				Util.log("(powerup: score +" + Game.POINTS_POWERUP_PTS + ")");
				break;
			default :
				throw new Error("Invalid powerup type: " + powerup.getType());
		}
	}

	public boolean execPlayerAI() {
		// Basic checks...
		if (ai == null) {
			return false;
		}

		// Call the AI (lets the AI queue up some commands for the tank)...
		if (!ai.updateAI()) {
			return false;
		}

		return true;
	}
	
	public boolean queueCommand(String cmdStr, Vec2 param) {	
		return queueCommand(cmdStr, param, false);
	}
	private boolean queueCommand(String cmdStr, Vec2 param, boolean insertFront) {
		// Convert to TankCmd class type & validate...
		TankCmd cmd = null;
		if (cmdStr.toLowerCase().equals(TankCmd_Move.TYPE)) {
			TankCmd_Move cmdMove = new TankCmd_Move(param);
			if ((Math.abs(cmdMove.moveVec.x) > 0.000001) && (Math.abs(cmdMove.moveVec.y) > 0.000001)) {
				Util.log("INVALID MOVE: Tanks move only horizontally & vertically");
			}
			else if (Vec2.lengthSqr(cmdMove.moveVec) < 0.001) {
				Util.log("INVALID MOVE: Zero distance");
			}
			else {
				cmd = cmdMove;
			}
		}
		else if (cmdStr.toLowerCase().equals(TankCmd_Turn.TYPE)) {
			TankCmd_Turn cmdTurn = new TankCmd_Turn(param);
			if (Vec2.lengthSqr(cmdTurn.dir) < 0.001) {
				Util.log("INVALID TURN: Zero direction vector");
			}
			else {
				cmd = cmdTurn;
			}
		}
		else if (cmdStr.toLowerCase().equals(TankCmd_Shoot.TYPE)) {
			TankCmd_Shoot cmdShoot = new TankCmd_Shoot(param);
			if (Vec2.lengthSqr(cmdShoot.dir) < 0.001) {
				Util.log("INVALID SHOT: Zero direction vector");
			}
			else {
				cmd = cmdShoot;
			}
		}
		else {
			// Unknown command...
			Util.log("INVALID COMMAND: " + cmdStr);
			cmd = null;
		}

		// Check for invalid...
		if (cmd == null) {
			Game.get().awardPoints(Game.POINTS_CMD, this.playerIdx);
			uiStats.timeSinceFeedbackCodeError = 0;
			return false;
		}

		// Queue it...
		if (insertFront) {
			this.queuedCommands.add(0, cmd);
		}
		else {
			this.queuedCommands.add(cmd);
		}

		return true;
	}
	
	public void finishActiveCommand() {
		// Done with this one...
		this.activeCommand = null;
		
		// If no more, then exit out...
		if (this.queuedCommands.size() == 0) {
			return;
		}
		
		// If it's a move or shoot, might need to do a turn first...
		boolean needToTurnFirst = false;
		TankCmd nextCommand = this.queuedCommands.get(0);
		if (nextCommand instanceof TankCmd_Move) {
			TankCmd_Move cmdMove = (TankCmd_Move)nextCommand;
			if (Vec2.dot(this.dir.unit(), cmdMove.moveVec.unit()) < 0.999) {
				nextCommand = new TankCmd_Turn(cmdMove.moveVec.unit());
				needToTurnFirst = true;
			}
		}
		else if (nextCommand instanceof TankCmd_Shoot) {
			TankCmd_Shoot cmdShoot = (TankCmd_Shoot)nextCommand;
			if (Vec2.dot(this.dir.unit(), cmdShoot.dir.unit()) < 0.999) {
				nextCommand = new TankCmd_Turn(cmdShoot.dir.unit());
				needToTurnFirst = true;
			}
		}
		
		// Grab the next queued command...
		this.activeCommand = nextCommand;
		if (!needToTurnFirst) {
			this.queuedCommands.remove(0);
		}

		// Log it for the user...
		if (!needToTurnFirst) {
			String strPrevix = (Game.get().getPlayerCount() == 1) ? " " : (playerIdx + "");
			Util.log(strPrevix + (needToTurnFirst ? "  " : "> " + this.activeCommand));
		}
		
		// Setup next new command (save off starting information)...
		this.activeCommand.startPos = this.pos;
		this.activeCommand.startDir = this.dir;
		
		// Make it cost something...
		int pointCost = (this.activeCommand.type.equals(TankCmd_Shoot.TYPE)) ? Game.POINTS_CMD_SHOT : Game.POINTS_CMD;
		Game.get().awardPoints(pointCost, this.playerIdx);
	}
	
	public void updateCommand(double deltaTime) {
		if (this.activeCommand == null) {
			// Check for queued...
			if (this.queuedCommands.size() > 0) {
				this.finishActiveCommand();
			}
			if (this.activeCommand == null) {
				return;
			}
		}
		switch (this.activeCommand.type) {
			case TankCmd_Move.TYPE : {
				// Setup...
				TankCmd_Move cmdMove = (TankCmd_Move)this.activeCommand;
				Vec2 moveVec = cmdMove.moveVec;
				double moveDst = moveVec.length();
				double moveTravelTime = moveDst / this.tankMoveSpeed;
				
				// Update the active command...
				cmdMove.progress = Math.min(cmdMove.progress + deltaTime / moveTravelTime, 1);
				this.pos = Vec2.add(cmdMove.startPos, Vec2.multiply(moveVec, cmdMove.progress));
				
				// Check for tank overlap...
				Tank otherTank = Game.get().getTank(this.playerIdx == 0 ? 1 : 0);
				if ((otherTank != null) && Util.circlesIntersect(this.pos, BODY_HALFSIZE.x, otherTank.pos, BODY_HALFSIZE.x)) {
					Vec2 deltaVec = Vec2.subtract(this.pos, otherTank.pos).unit();
					this.pos = Vec2.add(this.pos, Vec2.multiply(deltaVec, 0.1));
					cmdMove.progress = 1;
				}
				
				// Keep in bounds...
				if (this.pos.x <= BODY_HALFSIZE.x) {
					this.pos = Vec2.add(this.pos, Vec2.multiply(Vec2.right(), 0.1));
					cmdMove.progress = 1;
				}
				if (this.pos.y <= BODY_HALFSIZE.y) {
					this.pos = Vec2.add(this.pos, Vec2.multiply(Vec2.up(), 0.1));
					cmdMove.progress = 1;
				}
				if (this.pos.x >= Util.toCoordFrameLength(World.get().getCanvasSize().x) - BODY_HALFSIZE.x) {
					this.pos = Vec2.add(this.pos, Vec2.multiply(Vec2.left(), 0.1));
					cmdMove.progress = 1;
				}
				if (this.pos.y >= Util.toCoordFrameLength(World.get().getCanvasSize().y) - BODY_HALFSIZE.y) {
					this.pos = Vec2.add(this.pos, Vec2.multiply(Vec2.down(), 0.1));
					cmdMove.progress = 1;
				}
				
				// And...check for done...
				if (cmdMove.progress == 1) {
					this.finishActiveCommand();
				}
			} break;
			case TankCmd_Turn.TYPE : {
				// Setup...
				TankCmd_Turn cmdTurn = (TankCmd_Turn)this.activeCommand;
				double trgAngle = cmdTurn.dir.angle();
				double angleDelta = Util.minAngleToAngleDelta(cmdTurn.startDir.angle(), trgAngle);
				double moveTravelTime = Math.abs(angleDelta) / this.tankTurnSpeed;
				
				// Update the active command...
				cmdTurn.progress = Math.min(cmdTurn.progress + deltaTime / moveTravelTime, 1);
				this.dir = Vec2.rotate(cmdTurn.startDir, angleDelta * cmdTurn.progress).unit();
				
				// And...check for done...
				if (cmdTurn.progress == 1) {
					this.finishActiveCommand();
				}				
			} break;
			case TankCmd_Shoot.TYPE : {
				// Enforce a max shot period...
				long secondsSinceShot = Duration.between(this.timeLastShot, LocalTime.now()).getSeconds();
				if (secondsSinceShot >= 1) {
					// Shoot...
					Simulation.get().createAmmo(this.playerIdx, this.ammoSpawnLocation(), this.dir, this.ammoMaxRange);
					
					// Save off last time...
					this.timeLastShot = LocalTime.now();

					// And, that's it...
					this.finishActiveCommand();
				}
			} break;
		}
	}
	
	public boolean hasCommand() {
		return ((this.activeCommand != null) || (this.queuedCommands.size() > 0)) ? true : false;
	}

    public void update(double deltaTime) {
		// Super...
		super.update(deltaTime);
		
		// Command...
		if (deltaTime != 0) {
			this.updateCommand(deltaTime);
		}

        // Stats...
        uiStats.update(deltaTime);
	}

	private AffineTransform calcTransform(double height, double scale, boolean isShadow) {
		// Calc the center position, using the height...
		double drawHeight = calcDrawHeight(height, scale);
		Vec2 drawPos = isShadow ? Vec2.subtract(pos, Vec2.multiply(Draw.HEIGHT_OFFSET_PER_HEIGHT, drawHeight)) : Vec2.add(pos, Vec2.multiply(Draw.HEIGHT_OFFSET_PER_HEIGHT, drawHeight));
        Vec2 drawPosPixels = Util.toPixels(drawPos);		

		// Transform from local to world (includes rotation about that center)...
		AffineTransform transform = new AffineTransform();
		transform.translate(drawPosPixels.x, drawPosPixels.y);
		transform.rotate(Math.toRadians(-this.dir.angle()));

		return transform;
	}

	public void drawShadow(Graphics2D g) {
		// Setup...
		double scale = calcDrawScale();
		Color colorShadow = Util.colorLerp(World.COLOR_BACKGROUND, World.COLOR_SHADOW, timeSinceBorn * 2.0f);
		AffineTransform transformBody = calcTransform(TANK_HEIGHT_BODY, scale, true);
		AffineTransform transformTread = calcTransform(TANK_HEIGHT_TREADS, scale, true);
		
		// Tread...
		Draw.drawRectShadow(g, transformTread, new Vec2(TREAD_HALFLENGTH, TREAD_HALFWIDTH), calcDrawScale(), colorShadow, TANK_ROUNDED_SIZE, new Vec2(0, TREAD_OFFSET));
		Draw.drawRectShadow(g, transformTread, new Vec2(TREAD_HALFLENGTH, TREAD_HALFWIDTH), calcDrawScale(), colorShadow, TANK_ROUNDED_SIZE, new Vec2(0, -TREAD_OFFSET));

		// Body...
		Draw.drawRectShadow(g, transformBody, BODY_HALFSIZE, calcDrawScale(), colorShadow, TANK_ROUNDED_SIZE, Vec2.zero());
	}

	public void draw(Graphics2D g) {
		// Setup...
		double scale = calcDrawScale();
		Color colorBodyFill = Util.colorLerp(World.COLOR_BACKGROUND, playerIdx == 0 ? TANK_COLOR_BODY_FILL_1 : TANK_COLOR_BODY_FILL_2, timeSinceBorn * 2.0f);
		Color colorTreadFill = Util.colorLerp(World.COLOR_BACKGROUND, TANK_COLOR_TREAD_FILL, timeSinceBorn * 2.0f);
		Color colorTurretFill = Util.colorLerp(World.COLOR_BACKGROUND, playerIdx == 0 ? TANK_COLOR_TURRET_FILL_1 : TANK_COLOR_TURRET_FILL_2, timeSinceBorn * 2.0f);
		Color colorStroke = Util.colorLerp(World.COLOR_BACKGROUND, TANK_COLOR_STROKE, timeSinceBorn * 2.0f);
		AffineTransform transformBody = calcTransform(TANK_HEIGHT_BODY, scale, false);
		AffineTransform transformTread = calcTransform(TANK_HEIGHT_TREADS, scale, false);
		AffineTransform transformTurret = calcTransform(TANK_HEIGHT_TURRET, scale, false);

		// Tread...
		Draw.drawRect(g, transformTread, new Vec2(TREAD_HALFLENGTH, TREAD_HALFWIDTH), calcDrawScale(), colorTreadFill, colorStroke, 0, TANK_ROUNDED_SIZE, new Vec2(0, TREAD_OFFSET));
		Draw.drawRect(g, transformTread, new Vec2(TREAD_HALFLENGTH, TREAD_HALFWIDTH), calcDrawScale(), colorTreadFill, colorStroke, 0, TANK_ROUNDED_SIZE, new Vec2(0, -TREAD_OFFSET));

		// Body...
		Draw.drawRect(g, transformBody, BODY_HALFSIZE, calcDrawScale(), colorBodyFill, colorStroke, TANK_STROKE_WIDTH, 0, Vec2.zero());

		// Turret...
		Draw.drawRect(g, transformTurret, new Vec2(TURRET_HALFLENGTH, TURRET_HALFWIDTH), calcDrawScale(), colorTurretFill, colorStroke, TANK_TURRET_STROKE_WIDTH, TANK_ROUNDED_SIZE, new Vec2(TURRET_INSET, 0));
	}
}
