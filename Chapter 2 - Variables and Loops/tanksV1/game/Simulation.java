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

	public ArrayList<GameObject> getObjectsOfType(Class<?> type) {
		ArrayList<GameObject> gameObjects = new ArrayList<GameObject>();
		for (int i = 0; i < this.gameObjects.size(); i++) { 
            GameObject gameObject = this.gameObjects.get(i);
            if (type.isInstance(gameObject)) {
				gameObjects.add(gameObject);
			}
		}
		return gameObjects;
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

            // Here are the set of things that can block checking for commands...
            if (gameObject instanceof Tank) {
                Tank tank = (Tank)gameObject;
                if (tank.getPlayerIdx() != playerIdx) {
                    continue;
                }
				// If the tank is doing something or has queued commands...
                if (tank.hasCommand()) {
			    	return false;
			    }
            }
            else if (gameObject instanceof Ammo) {
                Ammo ammo = (Ammo)gameObject;
                if (ammo.getPlayerIdx() != playerIdx) {
                    continue;
                }
				// If the tank has an active shot in the air...
                return false;
            }
            else if (gameObject.isDying()) {
				// If any game object is dying, then wait till it is gone...
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
