import java.awt.Graphics2D;

public class GameObject {
	// Member variables...
    protected Vec2 pos = null;
	protected double timeTillDeath = 0;
    protected double timeSinceBorn = 0;

    // Accessors...
    public Vec2 getPos() {
        return this.pos;
    }

	// Member functions (methods)...
    public GameObject() {
    }

    public void destroy() {
    }

    public boolean shouldBeCulled() {
        return false;
    }

	public boolean isDying() {
		return (this.timeTillDeath > 0);
	}    

    public void update(double deltaTime) {
        timeSinceBorn += deltaTime;
    }

    public double calcDrawScale() {
		return Math.max(1.0 - this.timeTillDeath, 0.001);
	}

	public double calcDrawHeight(double height, double scale) {
		return height * Math.min(timeSinceBorn * 2, 1) * scale;
	}

    public void drawShadow(Graphics2D g) {
    }

    public void draw(Graphics2D g) {
    }
}
