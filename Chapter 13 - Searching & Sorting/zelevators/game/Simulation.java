package game;

import java.util.ArrayList;

public class Simulation {
    // Singleton...
    private static Simulation instance = null;
    protected static synchronized Simulation get() {
        if (instance == null)
            instance = new Simulation();
  
        return instance;
    }

    // Member variables...
    private ArrayList<Elevator> elevators = new ArrayList<Elevator>();
    private ArrayList<Zombie> zombies = new ArrayList<Zombie>();

    // Member functions (methods)...
    protected Simulation() {
    }
    
    protected Elevator createElevator(int elevatorIdx, int initialFloor) {
        Elevator elevator = new Elevator(elevatorIdx, (double)initialFloor, Game.get().getFloorCount());
        this.elevators.add(elevator);
        return elevator;
    }
    
    protected Zombie createZombie(int initialFloor) {
        Zombie zombie = new Zombie((double)initialFloor);
        this.zombies.add(zombie);
        return zombie;
    }

    protected ArrayList<Elevator> getElevators() {
        return elevators;
    }

    protected ArrayList<Zombie> getZombies() {
        return zombies;
    }

    protected boolean destroy(GameObject gameObject) {
        int index = this.elevators.indexOf(gameObject);
        if (index > -1) {
            gameObject.destroy();
            this.elevators.remove(index);
            return true;
        }
        index = this.zombies.indexOf(gameObject);
        if (index > -1) {
            gameObject.destroy();
            this.zombies.remove(index);
            return true;
        }
        return false;
    }

    protected void destroyAll() {
        for (int i = 0; i < this.elevators.size(); i++) { 
            GameObject gameObject = this.elevators.get(i);
            gameObject.destroy();
        }
        this.elevators.clear();
        for (int i = 0; i < this.zombies.size(); i++) { 
            GameObject gameObject = this.zombies.get(i);
            gameObject.destroy();
        }
        this.zombies.clear();
    }

    protected double calcMinDstFromZombie(Vec2 pos) {
        double minDst = 10000000;
        for (int i = 0; i < this.zombies.size(); i++) { 
            Zombie zombie = this.zombies.get(i);
            double thisDst = pos.distance(zombie.getPos());
            minDst = Math.min(minDst, thisDst);
        }
        return minDst;
    }
    
    protected void update(double deltaTime) {
        for (int i = 0; i < this.elevators.size(); ++i) { 
            Elevator elevator = this.elevators.get(i);
            elevator.update(deltaTime); 
        }
        for (int i = 0; i < this.zombies.size(); ++i) { 
            Zombie zombie = this.zombies.get(i);
            zombie.update(deltaTime); 
        }
    }
    
    protected void cullNotInField() {
        // Check with all of them calling their "shouldBeCulled" function...
        int i = 0;
        while (i < this.zombies.size()) { 
            Zombie zombie = this.zombies.get(i);
            if (zombie.shouldBeCulled()) {
                this.destroy(zombie);
                continue;
            }
            ++i;
        }
    }
}
