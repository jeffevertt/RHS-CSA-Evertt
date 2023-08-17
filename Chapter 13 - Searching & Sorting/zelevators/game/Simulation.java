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

    // Accessors/Query functions...
    protected boolean hasElevatorRequest(int floorIdx, ElevatorController.Direction dir) {
        if ((floorIdx < 0) || (floorIdx >= elevatorRequests.size())) {
            return false;
        }
        return elevatorRequests.get(floorIdx).contains(dir);
    }
    protected void addElevatorRequest(int floorIdx, ElevatorController.Direction direction) {
        if ((floorIdx < 0) || (floorIdx >= elevatorRequests.size())) {
            return;
        }
        elevatorRequests.get(floorIdx).add(direction);

        // Event...
        Game.get().getElevatorController().onElevatorRequest(floorIdx, direction);
    }
    protected void remElevatorRequest(int floorIdx, ElevatorController.Direction direction) {
        if ((floorIdx < 0) || (floorIdx >= elevatorRequests.size())) {
            return;
        }
        elevatorRequests.get(floorIdx).remove(direction);
    }
    protected boolean elevatorHasFloorRequest(int elevatorIdx, int floorIdx) {
        if ((elevatorIdx < 0) || (elevatorIdx >= elevators.size())) {
            return false;
        }
        Elevator elevator = elevators.get(elevatorIdx);
        return elevator.hasRequestForFloor(floorIdx);
    }
    protected boolean anyZombiesGettingOnElevator(Elevator elevator) {
        for (int i = 0; i < zombies.size(); ++i) {
            Zombie zombie = zombies.get(i);
            if (zombie.isGettingOnElevator(elevator)) {
                return true;
            }
        }
        return false;
    }
    protected boolean isElevatorIsOnFloor(int elevatorIdx, int floorIdx) {
        if ((elevatorIdx < 0) || (elevatorIdx >= elevators.size())) {
            return false;
        }
        Elevator elevator = elevators.get(elevatorIdx);
        return (elevator.getCurrentFloor() == (double)floorIdx);
    }
    protected boolean isElevatorIsHeadingToFloor(int elevatorIdx, int floorIdx) {
        if ((elevatorIdx < 0) || (elevatorIdx >= elevators.size())) {
            return false;
        }
        Elevator elevator = elevators.get(elevatorIdx);
        return (elevator.getTargetFloor() == (double)floorIdx);
    }
    protected boolean isElevatorIdle(int elevatorIdx) {
        if ((elevatorIdx < 0) || (elevatorIdx >= elevators.size())) {
            return false;
        }
        Elevator elevator = elevators.get(elevatorIdx);
        return elevator.isIdle();
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

    protected Elevator getElevator(int elevatorIdx) {
        if ((elevatorIdx < 0) || (elevatorIdx >= elevators.size())) {
            return null;
        }
        return elevators.get(elevatorIdx);
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
