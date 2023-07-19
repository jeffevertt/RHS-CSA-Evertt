import java.util.ArrayList;

public class Simulation {
	// Singleton...
	private static Simulation instance = null;
	public static synchronized Simulation get() {
        if (instance == null)
            instance = new Simulation();
  
        return instance;
    }
	public static ArrayList<GameObject> getGameObjects() {
		return instance.gameObjects;
	}

    // Member variables...
    private ArrayList<GameObject> gameObjects = new ArrayList<GameObject>();

    // Member functions (methods)...
    public Simulation() {
	}
	
	public Tank createTank(int playerIdx, Vec2 pos, Vec2 dir) {
		Tank tank = new Tank(playerIdx, pos, dir);
		tank.create();
		this.gameObjects.add(tank);
		return tank;
	}
	
	public Target createTarget(Vec2 pos) {
		Target target = new Target(pos);
		this.gameObjects.add(target);
        return target;
	}

	public PowerUp createPowerUp(Vec2 pos, String type) {
		PowerUp powerup = new PowerUp(pos, type);
		this.gameObjects.add(powerup);
        return powerup;
	}
	
	public Ammo createAmmo(int playerIdx, Vec2 pos, Vec2 dir, double maxRange) {
		Ammo ammo = new Ammo(playerIdx, pos, dir, maxRange);
		this.gameObjects.add(ammo);
        return ammo;
	}
    
    public boolean destroy(GameObject gameObject) {
		int index = this.gameObjects.indexOf(gameObject);
		if (index > -1) {
			gameObject.destroy();
			this.gameObjects.remove(index);
            return true;
		}
        return false;
	}

	public int objectCount(Class<?> type) {
		int count = 0;
		for (int i = 0; i < this.gameObjects.size(); i++) { 
            GameObject gameObject = this.gameObjects.get(i);
            if (type.isInstance(gameObject)) {
				++count;
			}
		}
		return count;
	}
	
	public void destroyAll() {
		for (int i = 0; i < this.gameObjects.size(); i++) { 
            GameObject gameObject = this.gameObjects.get(i);
			gameObject.destroy();
		}
		this.gameObjects.clear();
	}

	public double calcMinDstFromGameObject(Vec2 pos, Class<?> type) {
		double minDst = 10000000;
		for (int i = 0; i < this.gameObjects.size(); i++) { 
            GameObject gameObject = this.gameObjects.get(i);
            if (!type.isInstance(gameObject))
                continue;
			double thisDst = pos.distance(gameObject.getPos());
			minDst = Math.min(minDst, thisDst);
		}
		return minDst;
	}
	public double calcMinDstFromATank(Vec2 pos) {
		return calcMinDstFromGameObject(pos, Tank.class);
	}
    public double calcMinDstFromATarget(Vec2 pos) {
		return calcMinDstFromGameObject(pos, Target.class);
	}
	public double calcMinDstFromAPowerUp(Vec2 pos) {
		return calcMinDstFromGameObject(pos, PowerUp.class);
	}
	
	public void update(double deltaTime) {
		for (int i = 0; i < this.gameObjects.size(); ++i) { 
            GameObject gameObject = this.gameObjects.get(i);
			gameObject.update(deltaTime); 
		}
	}
	
	public boolean execPlayerAI(int playerIdx) {
		// queuedCodeCommands = [];
		// let theTank = null, otherTank = null;
		// for (let i = 0; i < this.tanks.length; ++i) {
		// 	theTank = (this.tanks[i].playerIdx == playerIdx) ? this.tanks[i] : theTank;
		// 	otherTank = (this.tanks[i].playerIdx != playerIdx) ? this.tanks[i] : otherTank;
		// }
		// tank = (theTank != null) ? theTank.createCodeStruct() : null;
		// other = (otherTank != null) ? otherTank.createCodeStruct() : null;
		// target = (this.targets.length > 0) ? this.targets[0].createCodeStruct() : null;
		// powerup = (this.powerups.length > 0) ? this.powerups[0].createCodeStruct() : null;
		// if (!executeCode(code)) {
		// 	return false;
		// }
		// this.execCodeRequestedTankCommands(theTank);

		return true;
	}
	
	public void cullNotInField() {
        // Check with all of them calling their "shouldBeCulled" function...
		int i = 0;
		while (i < this.gameObjects.size()) { 
            GameObject gameObject = this.gameObjects.get(i);
			if (gameObject.shouldBeCulled()) {
				this.destroy(gameObject);
				continue;
			}
			++i;
		}
	}
	
	public boolean isReadyToAskCodeForNextCommand(int playerIdx) {
		for (int i = 0; i < this.gameObjects.size(); ++i) { 
            GameObject gameObject = this.gameObjects.get(i);

            // Need to special case some things...
            if (gameObject instanceof Tank) {
                Tank tank = (Tank)gameObject;
                if (tank.getPlayerIdx() != playerIdx) {
                    continue;
                }
                if (tank.hasCommand()) {
			    	return false;
			    }
            }
            else if (gameObject instanceof Ammo) {
                Ammo ammo = (Ammo)gameObject;
                if (ammo.getPlayerIdx() != playerIdx) {
                    continue;
                }
                return true;
            }
            else if (gameObject.isDying()) {
                return false;
            }
		}
		return true;
	}
	
	public boolean isAnyActionHappening() {
		if (!this.isReadyToAskCodeForNextCommand(0) || 
            !this.isReadyToAskCodeForNextCommand(1)) {
			return true;
		}
		return false;
	}
}
