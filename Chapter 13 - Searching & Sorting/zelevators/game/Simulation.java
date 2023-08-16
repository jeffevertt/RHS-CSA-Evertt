package game;

import java.util.ArrayList;
import java.util.EnumSet;

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
    private ArrayList<EnumSet<ElevatorController.Direction>> elevatorRequests = new ArrayList<EnumSet<ElevatorController.Direction>>();

    // Constructor...
    protected Simulation() {
    }

    // Accessors...
    protected boolean hasElevatorRequest(int floor, ElevatorController.Direction dir) {
        if ((floor < 0) || (floor >= elevatorRequests.size())) {
            return false;
        }
        return elevatorRequests.get(floor).contains(dir);
    }
    protected void addElevatorRequest(int floor, ElevatorController.Direction direction) {
        if ((floor < 0) || (floor >= elevatorRequests.size())) {
            return;
        }
        elevatorRequests.get(floor).add(direction);
    }
    protected void remElevatorRequest(int floor, ElevatorController.Direction direction) {
        if ((floor < 0) || (floor >= elevatorRequests.size())) {
            return;
        }
        elevatorRequests.get(floor).remove(direction);
    }
    
    // Member functions (methods)...
    protected Elevator createElevator(int elevatorIdx, int initialFloor) {
        Elevator elevator = new Elevator(elevatorIdx, (double)initialFloor, Game.get().getFloorCount());
        this.elevators.add(elevator);
        return elevator;
    }
    
    protected Zombie createZombie(int initialFloor) {
        Zombie zombie = new Zombie(initialFloor);
        this.zombies.add(zombie);
        return zombie;
    }

    protected void createElevatorRequests(int floorCount) {
        elevatorRequests.clear();
        for (int i = 0; i < floorCount; ++i) {
            EnumSet<ElevatorController.Direction> state = EnumSet.noneOf(ElevatorController.Direction.class);
            elevatorRequests.add(state);
        }
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
        elevatorRequests.clear();
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
