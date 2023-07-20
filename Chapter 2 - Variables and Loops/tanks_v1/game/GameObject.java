package game;

import java.awt.Graphics2D;

public class GameObject {
	// Member variables...
    protected Vec2 pos = null;
	protected double timeTillDeath = 0;
    protected double timeSinceBorn = 0;

    // Accessors...
    public Vec2 getPos() {
        return Vec2.copy(this.pos);
    }

	// Member functions (methods)...
    protected GameObject() {
    }

    protected void destroy() {
    }

    protected boolean shouldBeCulled() {
        return false;
    }

	public boolean isDying() {
		return (this.timeTillDeath > 0);
	}    

    protected void update(double deltaTime) {
        timeSinceBorn += deltaTime;
    }

    protected double calcDrawScale() {
		return Math.max(1.0 - this.timeTillDeath, 0.001);
	}

	protected double calcDrawHeight(double height, double scale) {
		return height * Math.min(timeSinceBorn * 2, 1) * scale;
	}

    protected void drawShadow(Graphics2D g) {
    }

    protected void draw(Graphics2D g) {
    }
}
