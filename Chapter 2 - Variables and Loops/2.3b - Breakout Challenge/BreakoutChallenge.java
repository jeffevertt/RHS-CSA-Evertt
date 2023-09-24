
public class BreakoutChallenge {
    /* Challenge Goal
     *  TODO: Write up challenge here...
     *          Not writing the game code, just the display
     *          If complete, modify the game to support 'multi-ball' powerup
     * https://en.wikipedia.org/wiki/Breakout_(video_game)
     */
    private static Simulation sim = new Simulation();

    public static void main(String[] args) {
        // TODO: Put your code here, you should use sim to get information about the game state
    }

    /* Call this function when you are done drawing/printing your "Frame" of animation.
     *  It will leave your Ascii art on the screen for a short time
     *    and then clear the console so that you can print out your next frame.
     *  If you call endFrame(), that will default to a 250ms (or 0.25 second) delay.
     *  If you call endFrame(100), you control the delay time (this example is 100ms).
     */
    public static void endFrame() {
        endFrame(150);
    }
    public static void endFrame(int delayMilliSeconds) {  
        // Delay a for a short time (leave it on the screen)
        try {
            Thread.sleep(delayMilliSeconds);
        } 
        catch (Exception ex) {
        }

        // Clear the console
        System.out.print("\033[H\033[2J");  
        System.out.flush();

        // Simulation update
        sim.update((double)delayMilliSeconds / 1000);
    } 
    public static void resetColor() {
        System.out.print("\033[0m");
    }
}
