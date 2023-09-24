/* The simulation class manages the game simulation. 
 *  The game grid size is defined by the constants in this class (GRID_WIDTH, GRID_HEIGHT)
 *  The simulation handles simulating the game, doing physics, input, etc.
 *  What it does NOT DO is the graphics/display (the students code should do this) 
 * 
 * The grid origin is lower left with positive x going right and positive y going up.
 *  There are implicit walls on the sides and top (there are no query methods for these).
 *  Grid positions reference grid square centers (square have a width/height of one).
 * 
 * Students should use the public query functions, like...
 *  isBallInGridSquare   - To check if the ball is an individual grid square.
 *  isPaddleInGridSquare - To check if the paddle is an individual grid square.
 *  isBrickInGridSquare  - To check if there is a brick in a grid square.
 */
public class Simulation {
    public static final int GRID_WIDTH = 24;
    public static final int GRID_HEIGHT = 12;
    public static final int BRICK_ROWS = 3;

    public static final double INITIAL_BALL_VELOCITY = 6;
    public static final double PADDLE_WIDTH = 3;

    public static final double EPSILON = 0.00001;

    private Ball ball = null;
    private Paddle paddle = null;
    private boolean[][] bricks = null;

    public Simulation() {
        // Create up ball & paddle
        Vec2 ballVel = Vec2.rotate(new Vec2(0, INITIAL_BALL_VELOCITY), ((Math.random() - 0.5) * 2) * 60);
        ball = new Ball(new Vec2(GRID_WIDTH / 2, 1), ballVel);
        paddle = new Paddle(GRID_WIDTH / 2, PADDLE_WIDTH);

        // Bricks
        bricks = new boolean[BRICK_ROWS][GRID_WIDTH];
        for (int y = 0; y < BRICK_ROWS; ++y) {
            for (int x = 0; x < GRID_WIDTH; ++x) {
                bricks[y][x] = ((x != 0) && (x != GRID_WIDTH - 1));
            }
        }
    }

    public boolean isGameActive() {
        // Done when ball is out of bounds
        if (ball.isOutsidePlayfield()) {
            return false;
        }

        // Or if there are no blocks left
        return (getBrickCount() > 0);
    }

    public boolean isBallInGridSquare(int x, int y) {
        return ball.isInGridSquare(x, y, this);
    }

    public boolean isPaddleInGridSquare(int x, int y) {
        return paddle.isInGridSquare(x, y);
    }

    public boolean isBrickInGridSquare(int x, int y) {
        if ((y >= GRID_HEIGHT - 1) || (y < GRID_HEIGHT - BRICK_ROWS - 1)) {
            return false;
        }

        // Bricks are index from top row down (so, GRID_HEIGHT - 2 is brick row index 0)
        return bricks[gridYToBrickRow(y)][x];
    }

    public void breakBrick(int x, int y) {
        if (!isBrickInGridSquare(x, y)) {
            return;
        }
        bricks[gridYToBrickRow(y)][x] = false;
    }

    private int getBrickCount() {
        int brickCount = 0;
        for (int y = 0; y < BRICK_ROWS; ++y) {
            for (int x = 0; x < GRID_WIDTH; ++x) {
                brickCount += bricks[y][x] ? 1 : 0;
            }
        }
        return brickCount;
    }

    private int gridYToBrickRow(int y) {
        return (GRID_HEIGHT - y - 2);
    }

    public boolean isInGridSquare(Vec2 pos, int x, int y) {
        Vec2 squareMin = new Vec2(x - 0.5, y - 0.5);
        Vec2 squareMax = new Vec2(x + 0.5, y + 0.5);
        return ((pos.x > squareMin.x) && (pos.x <= squareMax.x) &&
                (pos.y > squareMin.y) && (pos.y <= squareMax.y));
    }

    private void update(double deltaTime) {
        ball.update(deltaTime, this, paddle);
        paddle.update(deltaTime);
    }

    /* Call this function when you are done drawing/printing your "Frame" of animation.
     *  It will leave your scene on the screen for a short time
     *    and then clear the console so that you can print out your next frame.
     *  If you call endFrame(), that will default to a 150ms (or 0.15 second) delay.
     *  If you call endFrame(100), you control the delay time (this example is 100ms).
     */
    public void endFrame() {
        endFrame(150);
    }
    public void endFrame(int delayMilliSeconds) {  
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
        update((double)delayMilliSeconds / 1000);
    } 
    public void resetColor() {
        System.out.print("\033[0m");
    }
}
