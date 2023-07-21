package ai;

import game.TankAIBase;
import game.Vec2;

public class OtherAI extends TankAIBase {

    // TODO: Your opponents code goes here

    public String getPlayerName() { 
        return "Opponent"; 
    }
    public boolean updateAI() {
        queueCommand("move", new Vec2(getPowerUp().getPos().x - tank.getPos().x, 0));
        queueCommand("move", new Vec2(0, getPowerUp().getPos().y - tank.getPos().y));
        return true;
    }
}