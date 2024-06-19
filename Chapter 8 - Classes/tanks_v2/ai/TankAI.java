package ai;

import game.PowerUp;
import game.Target;
import game.Tank;
import game.TankAIBase;
import game.Vec2;

public class TankAI extends TankAIBase {

    public String getPlayerName() {
        return "YOUR-NAME-HERE";  // <---- Put your first name here
    }
    public int getPlayerPeriod() {
        return -1;                // <---- Put your period here
    }
        
    // You are free to add member variables & methods to this class (and delete this comment).
    //  You should use the methods in its base class (TankAIBase) to query the world. 
    //  Note that you are not allowed to reach into game code directly or make any
    //  modifications to code in the game package. Use your judgement and ask your 
    //  teacher if you are not sure. If it feels like cheating, it probably is.

    public boolean updateAI() {

        // TODO: Your code goes here

        return true;
    }
}