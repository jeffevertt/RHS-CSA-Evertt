public class Simulation {
    public static final int GRID_WIDTH = 24;
    public static final int GRID_HEIGHT = 12;

    private final double EPSILON = 0.00001;

    private Vec2 ballPos = new Vec2(GRID_WIDTH/2, 0);
    private Vec2 ballVel = new Vec2(0, 0);

    public Simulation() {
        double randAngle = ((Math.random() - 0.5) * 2) * 60;
        ballVel = Vec2.rotate(new Vec2(0, 4), randAngle);
    }

    public boolean isGameActive() {
        // Done when ball is out of bounds
        return (ballPos.y > -0.5);
    }

    public boolean isBallInGridSquare(int x, int y) {
        Vec2 squareMin = new Vec2(x - 0.5, y - 0.5);
        Vec2 squareMax = new Vec2(x + 0.5, y + 0.5);
        return isInGridSquare(ballPos, squareMin, squareMax);
    }

    private boolean isInGridSquare(Vec2 pos, Vec2 squareMin, Vec2 squareMax) {
        return ((pos.x > squareMin.x) && (pos.x <= squareMax.x) &&
                (pos.y > squareMin.y) && (pos.y <= squareMax.y));
    }

    private void updateBall(double deltaTime) {
        Vec2 pos0 = ballPos;
        Vec2 pos1 = Vec2.add(ballPos, Vec2.multiply(ballVel, deltaTime));

        // Walls (bounce if segment intersects, just deal with velocity, don't bother with sub path accuracy)
        if ((pos0.x > 0) && (pos1.x <= 0)) { // Left
            ballVel = Vec2.add(ballVel, Vec2.multiply(Vec2.right(), Vec2.dot(Vec2.right(), ballVel) * -2));
        }
        if ((pos0.x < GRID_WIDTH - 1) && (pos1.x >= GRID_WIDTH - 1)) { // Right
            ballVel = Vec2.add(ballVel, Vec2.multiply(Vec2.left(), Vec2.dot(Vec2.left(), ballVel) * -2));
        }
        if ((pos0.y < GRID_HEIGHT - 2) && (pos1.y >= GRID_HEIGHT - 2)) { // Top
            ballVel = Vec2.add(ballVel, Vec2.multiply(Vec2.down(), Vec2.dot(Vec2.down(), ballVel) * -2));
        }

        // Bottom (physics test)
        // if ((pos0.y > 0) && (pos1.y <= 0)) {
        //     ballVel = Vec2.add(ballVel, Vec2.multiply(Vec2.up(), Vec2.dot(Vec2.up(), ballVel) * -2));
        // }

        // Update position
        ballPos = Vec2.add(ballPos, Vec2.multiply(ballVel, deltaTime));

        // Make sure it stays in the world space
        ballPos.clamp(new Vec2(EPSILON, -5), new Vec2(GRID_WIDTH - 1 - EPSILON, GRID_HEIGHT - 1 - EPSILON));
    }

    public void update(double deltaTime) {
        // Update ball (substep it for improved accuracy)
        int simSubSteps = 20;
        for (int i = 0; i < simSubSteps; ++i) {
            updateBall(deltaTime / simSubSteps);
        }
    }
}
