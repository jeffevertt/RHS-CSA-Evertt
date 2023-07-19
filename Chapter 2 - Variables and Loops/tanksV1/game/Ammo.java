import java.awt.Graphics2D;
import java.util.ArrayList;

public class Ammo extends GameObject {
    // Constants...
    public final int AMMO_SPEED = 8;
    public final double AMMO_RADIUS = 0.15;

    // Member variables...
    private int playerIdx = -1;
    private Vec2 startPos = null;
    private double maxRange = -1.0;
    private Vec2 vel = null;
    private double radius = -1.0;

	// Accessors...
	public int getPlayerIdx() {
		return this.playerIdx;
	}

    // Member functions (methods)...
    public Ammo(int playerIdx, Vec2 pos, Vec2 dir, double maxRange) {
		// Parent...
		super();

		// Defaults...
		this.playerIdx = playerIdx;
		this.startPos = pos;
        this.pos = pos;
		this.maxRange = maxRange;
		this.vel = Vec2.multiply(dir.unit(), AMMO_SPEED);
		this.radius = AMMO_RADIUS;
		this.timeTillDeath = 0;

		// Graphics...
		// this.raphaelObject = raphael.circle(toPixelsX(this.pos.x), toPixelsY(this.pos.y), this.radius * pixelsPerUnit);
        // this.raphaelObject.attr({stroke: 'black', 'stroke-width': 2, fill: "darkred"});
    }

	public void destroy() {
		// if (this.raphaelObject != null) {
		// 	this.raphaelObject.remove();
		// 	this.raphaelObject = null;
		// }

		// Super...
		super.destroy();
	}
	
	public boolean shouldBeCulled() {
		// Check if on field...
		// if (!inRect(field, this.raphaelObject)) {
		// 	return true;
		// }
		
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
			this.timeTillDeath = Math.min(this.timeTillDeath + deltaTime * 10.0, 1.0);
			
			// Scale it down...
			this.radius = AMMO_RADIUS * Math.max(1.0 - this.timeTillDeath, 0.001);
			//this.raphaelObject.attr("r", this.radius * pixelsPerUnit);
		}
		else {
			// Update position...
			Vec2 prevPos = this.pos;
			this.pos = Vec2.add(this.pos, Vec2.multiply(this.vel, deltaTime));
			Vec2 motionVec = Vec2.subtract(this.pos, prevPos);
			//this.raphaelObject.attr({cx: toPixelsX(this.pos.x), cy: toPixelsY(this.pos.y)});

			// Check for hitting things (do swept collision to avoid missing it)...
			ArrayList<GameObject> gameObjects = Simulation.getGameObjects();
			for (int i = 0; i < gameObjects.size(); ++i) {
				GameObject gameObject = gameObjects.get(i);

				// Targets...
				if (gameObject instanceof Target) {
					Target target = (Target)gameObject;
					if (Util.intersectCircleCapsule(target.pos, target.getRadius(), prevPos, motionVec, this.radius)) {
						// Tell the target about it...
						target.onHitByAmmo(this.playerIdx);
					
						// That's it for us...
						this.timeTillDeath = Math.max(this.timeTillDeath, 0.0001);
					}
				}
				
				// Tanks...
				if (gameObject instanceof Tank) {
					Tank tank = (Tank)gameObject;
					if ((this.playerIdx != tank.getPlayerIdx()) && // Don't let someone hit themselves
					    (Util.intersectCircleCapsule(tank.pos, Tank.BODY_HALFSIZE.x, prevPos, motionVec, this.radius))) {
						// Award points...
						Game.get().awardPoints(25, this.playerIdx);
						Util.log("(hit tank: score +25)");
						
						// Kick the tank back (if you can)...
						tank.kickBackTank(Vec2.subtract(tank.pos, this.pos).unit());
						
						// That's it for us...
						this.timeTillDeath = Math.max(this.timeTillDeath, 0.0001);
					}
				}
			}
						
			// Check max range...
			if ((Vec2.subtract(this.pos, this.startPos)).lengthSqr() >= (this.maxRange * this.maxRange)) {
				// That's it for us...
				this.timeTillDeath = Math.max(this.timeTillDeath, 0.0001);
			}
		}
	}

	public void drawShadow(Graphics2D g) {
    }

    public void draw(Graphics2D g) {
    }
}
