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

    public void drawShadow(Graphics2D g) {
    }

    public void draw(Graphics2D g) {
    }
}
