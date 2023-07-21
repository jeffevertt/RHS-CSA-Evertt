package game;

import java.util.ArrayList;

public abstract class TankAIBase {
    // Constants
	// ...

	// Member variables
	protected Tank tank;

	// Accessors
	public Tank getTank() {
        return tank;
    }
    protected void setTank(Tank tank) {
        this.tank = tank;
    }
    public String getPlayerName() {
        return "UNKNOWN";
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
    public boolean queueCommand(String cmdStr, Vec2 param) {
        return tank.queueCommand(cmdStr, param);
    }
}
