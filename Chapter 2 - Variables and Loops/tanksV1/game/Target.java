import java.awt.Graphics2D;

public class Target extends GameObject {
	// Constants...
	public final double TARGET_RADIUS = 0.4;

	// Member variables...
	private double radius = TARGET_RADIUS;

	// Accessors...
	public double getRadius() {
		return radius;
	}

	// Member functions (methods)...
    public Target(Vec2 pos) {
		// Parent...
		super();

		// Defaults...
        this.pos = pos;
		this.radius = TARGET_RADIUS;
		this.timeTillDeath = 0;
		// this.raphaelObjects = [];
		// this.raphaelObjects.push(raphael.circle(toPixelsX(this.pos.x), toPixelsY(this.pos.y), this.radius * pixelsPerUnit).attr({'stroke-width': 2, fill: "red"}));
		// this.raphaelObjects.push(raphael.circle(toPixelsX(this.pos.x), toPixelsY(this.pos.y), this.radius * 0.666 * pixelsPerUnit).attr({'stroke-width': 0, fill: "white"}));
		// this.raphaelObjects.push(raphael.circle(toPixelsX(this.pos.x), toPixelsY(this.pos.y), this.radius * 0.333 * pixelsPerUnit).attr({'stroke-width': 0, fill: "red"}));
		
		// Set draw order...
		// for (let i = this.raphaelObjects.length - 1; i >= 0; --i) {
		// 	this.raphaelObjects[i].insertAfter(field);
		// }
    }

	public void destroy() {
		// Graphics objects...
		// for (let i = 0; i < this.raphaelObjects.length; ++i) {
		// 	if (this.raphaelObjects[i] != null) {
		// 		this.raphaelObjects[i].remove();
		// 		this.raphaelObjects[i] = null;
		// 	}
		// }
		// this.raphaelObjects = [];

		// Super...
		super.destroy();		
	}
	
	public void onHitByAmmo(int playerIdx) {
		// Trigger death spiral...
		this.timeTillDeath = Math.max(this.timeTillDeath, 0.0001);
		
		// Give up the points...
		Game.get().awardPoints(20, playerIdx);
		Util.log("(hit target: score +20)");
	}
	
	public boolean shouldBeCulled() {
		// Check death timer...
		if (this.timeTillDeath >= 1) {
			return true;
		}
		
		return false;
	}
	
	public void update(double deltaTime) {
		// Super...
		super.update(deltaTime);

		// If dying, continue...
		if (this.timeTillDeath > 0) {
			// Update it...
			this.timeTillDeath = Math.min(this.timeTillDeath + deltaTime * 2.0, 1.0);
		}
	}

    public void drawShadow(Graphics2D g) {
    }

    public void draw(Graphics2D g) {
    }	
}
