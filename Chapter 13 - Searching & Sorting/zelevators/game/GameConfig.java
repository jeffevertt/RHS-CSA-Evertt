package game;

public class GameConfig {
    // Data
    public int gameTimeInSeconds = 120;
    public int floorCount = 8;
    public int elevatorCount = 2;
    public double zombieSpawnPeriod = 2.0;      // Spawn period for zombies

    // Constructor(s)
    public GameConfig() {
    }
    public GameConfig(int gameTimeInSeconds, int floorCount, int elevatorCount, double zombieSpawnPeriod) {
        this.gameTimeInSeconds = gameTimeInSeconds;
        this.floorCount = floorCount;
        this.elevatorCount = elevatorCount;
        this.zombieSpawnPeriod = zombieSpawnPeriod;
    }
}
