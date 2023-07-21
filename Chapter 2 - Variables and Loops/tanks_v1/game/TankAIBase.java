package game;

import java.util.ArrayList;

public abstract class TankAIBase {
    // Constants
	public static final String DEFAULT_PLAYER_NAME = "UNKNOWN";

	// Member variables
	protected Tank tank;

    // Override this function, to specify your name
    public String getPlayerName() {
        return DEFAULT_PLAYER_NAME;
    }

	// Accessors
    public Vec2 getTankPos() {
        return tank.getPos();
    }
    public Vec2 getTankDir() {
        return tank.getDir();
    }
    public double getTankAngle() {
        return tank.getDir().angle();
    }
	public double getTankMoveSpeed() {
		return tank.getMoveSpeed();
	}
	public double getTankTurnSpeed() {
		return tank.getTurnSpeed();
	}
	public double getTankShotRange() {
		return tank.getShotRange();
	}
    public Vec2 getTankVel() {
        return tank.getVel();
    }
    public double getTankShotSpeed() {
        return Ammo.AMMO_SPEED;
    }
	public Tank getTank() {
        return tank;
    }
    protected void setTank(Tank tank) {
        this.tank = tank;
    }
    public double getLevelTimeRemaining() {
        return Game.get().getLevelTimeRemaining();
    }
    public double getLevelTimeMax() {
        return Game.get().getLevelTimeMax();
    }

    // Object query functions
    public PowerUp getPowerUp() {
        ArrayList<GameObject> list = Simulation.get().getObjectsOfType(PowerUp.class);
        return (list.size() > 0) ? (PowerUp)list.get(0) : null;
    } 
    public PowerUp[] getPowerUps() {
        ArrayList<GameObject> list = Simulation.get().getObjectsOfType(PowerUp.class);
        PowerUp[] powerUps = new PowerUp[list.size()];
        for (int i = 0; i < list.size(); ++i) {
            powerUps[i] = (PowerUp)list.get(i);
        }
        return powerUps;
    } 
    public Target getTarget() {
        ArrayList<GameObject> list = Simulation.get().getObjectsOfType(Target.class);
        return (list.size() > 0) ? (Target)list.get(0) : null;
    } 
    public Target[] getPowerups() {
        ArrayList<GameObject> list = Simulation.get().getObjectsOfType(Target.class);
        Target[] targets = new Target[list.size()];
        for (int i = 0; i < list.size(); ++i) {
            targets[i] = (Target)list.get(i);
        }
        return targets;
    }
    public Tank getOther() {
        return Game.get().getTank(tank.getPlayerIdx() == 0 ? 1 : 0);
    }

	// Member functions (methods)
    protected TankAIBase() {
    }

    // This function should be overidden by the derived AI classes.
    //  The purpose of the function is to queue commands for the tank.
    protected boolean updateAI() {
        return false;
    }

    // Queue a command to be executed by the tank...updateCommands should call this.
    //  Note that you can queue multiple commands, but updateCommands will not be called 
    //  again until all queued commands have completed.
    public boolean queueCmd(String cmdStr, Vec2 param) {
        return tank.queueCmd(cmdStr, param);
    }
}
