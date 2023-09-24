
public class Ball {
    private Vec2 pos;
    private Vec2 vel;

    public Ball(Vec2 pos, Vec2 vel) {
        this.pos = pos;
        this.vel = vel;
    }

    public boolean isOutsidePlayfield() {
        return ((pos.x < -1) || (pos.x > Simulation.GRID_WIDTH + 1) ||
                (pos.y < -1) || (pos.y > Simulation.GRID_HEIGHT + 1));
    }

    public boolean isInGridSquare(int x, int y, Simulation sim) {
        return sim.isInGridSquare(pos, x, y);
    }    

    private void updateSubStep(double deltaTime, Simulation sim, Paddle paddle) {
        Vec2 pos0 = pos;
        Vec2 pos1 = Vec2.add(pos, Vec2.multiply(vel, deltaTime));

        // Walls (bounce if segment intersects, just deal with velocity, don't bother with sub path accuracy)
        if ((pos0.x > 0) && (pos1.x <= 0)) { // Left
            vel = Vec2.add(vel, Vec2.multiply(Vec2.right(), Vec2.dot(Vec2.right(), vel) * -2));
        }
        if ((pos0.x < Simulation.GRID_WIDTH - 1) && (pos1.x >= Simulation.GRID_WIDTH - 1)) { // Right
            vel = Vec2.add(vel, Vec2.multiply(Vec2.left(), Vec2.dot(Vec2.left(), vel) * -2));
        }
        if ((pos0.y < Simulation.GRID_HEIGHT - 2) && (pos1.y >= Simulation.GRID_HEIGHT - 2)) { // Top
            vel = Vec2.add(vel, Vec2.multiply(Vec2.down(), Vec2.dot(Vec2.down(), vel) * -2));
        }

        // Paddle
        Vec2 paddleNormal = paddle.rayCast(pos0, pos1);
        if (paddleNormal != null) {
            vel = Vec2.add(vel, Vec2.multiply(paddleNormal, Vec2.dot(paddleNormal, vel) * -2));
        }

        // Bricks
        int gridSquareX = (int)Math.round(pos.x);
        int gridSquareY = (int)Math.round(pos.y);
        if (sim.isBrickInGridSquare(gridSquareX, gridSquareY)) {
            sim.breakBrick(gridSquareX, gridSquareY);
            Vec2 brickToBall = Vec2.subtract(pos, new Vec2(gridSquareX, gridSquareY)).unit();
            Vec2 brickNormal = Math.abs(brickToBall.y) > Math.abs(brickToBall.x) ?
                                    (Vec2.multiply(Vec2.up(), (brickToBall.y < 0 ? -1 : 1))) :
                                    (Vec2.multiply(Vec2.right(), (brickToBall.x < 0 ? -1 : 1)));
            vel = Vec2.add(vel, Vec2.multiply(brickNormal, Vec2.dot(brickNormal, vel) * -2));
        }

        // Update position
        pos = Vec2.add(pos, Vec2.multiply(vel, deltaTime));

        // Make sure it stays in the world space
        pos.clamp(new Vec2(Simulation.EPSILON, -5), new Vec2(Simulation.GRID_WIDTH - 1 - Simulation.EPSILON, Simulation.GRID_HEIGHT - 1 - Simulation.EPSILON));
    }
  
    public void update(double deltaTime, Simulation sim, Paddle paddle) {
        // Substep it for improved accuracy
        int simSubSteps = 20;
        for (int i = 0; i < simSubSteps; ++i) {
            updateSubStep(deltaTime / simSubSteps, sim, paddle);
        }
    }
}
