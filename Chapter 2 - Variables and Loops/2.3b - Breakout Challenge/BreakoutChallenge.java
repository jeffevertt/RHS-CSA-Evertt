
public class BreakoutChallenge {
    /* Challenge Goal
     *  ...
     * https://en.wikipedia.org/wiki/Breakout_(video_game)
     */
    private static Simulation sim = new Simulation();

    public static void main(String[] args) {
        while (sim.isGameActive()) {
            for (int y = Simulation.GRID_HEIGHT - 1; y >= 0; --y) {
                for (int x = 0; x < Simulation.GRID_WIDTH; ++x) {
                    // Figure out the character to draw
                    //  Wall characters
                    char ch = ' ';
                    if (y == Simulation.GRID_HEIGHT - 1) {
                        ch = '_';
                    }
                    else if ((x == 0) || (x + 1 == Simulation.GRID_WIDTH)) {
                        ch = '|';
                    }
                    // Ball
                    if (sim.isBallInGridSquare(x, y)) {
                        ch = '*';
                    }

                    // Draw the character
                    System.out.print(ch);
                }
                System.out.println();
            }

            // Frame complete
            endFrame();
        }
        
        resetColor();
    }

    /* Tips...
     *  - After you've draw your 'scene' for the frame, call delayAndClearForNextFrame()
     *  - If you'd like to use colors, you can use this...
     *      System.out.println("\033[0m");  // RESET TO DEFAULT COLOR
     *      System.out.println("\033[31m"); // RED
     *      System.out.println("\033[32m"); // GREEN
     *      System.out.println("\033[33m"); // YELLOW
     *      System.out.println("\033[34m"); // BLUE
     *      System.out.println("\033[35m"); // MAGENTA
     *      System.out.println("\033[36m"); // CYAN
     *      System.out.println("\033[37m"); // WHITE
     */

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
