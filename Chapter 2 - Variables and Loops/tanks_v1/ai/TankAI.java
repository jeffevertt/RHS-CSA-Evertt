package ai;

import game.TankAIBase;
import game.Vec2;

public class TankAI extends TankAIBase {
    public String getPlayerName() {
        return "<Your Name>";  // <---- Put your name in there
    }
        
    // You are free to add member variables and functions to this class.
    //  However, you can only call function in this classes base class (TankAIBase). 
    //  (you cannot mess with the work state directly, that is considered cheating)

    public boolean updateAI() {
        // TODO: Your code goes here

        // Test...
        queueCommand("move", new Vec2(getPowerUp().getPos().x - tank.getPos().x, 0));
        queueCommand("move", new Vec2(0, getPowerUp().getPos().y - tank.getPos().y));

        return true;
    }
}