import java.awt.Color;
import java.awt.Graphics2D;

public class Target extends GameObject {
	// Constants...
	public final double TARGET_RADIUS = 0.4;
	public static final double TARGET_HEIGHT = 0.5;
	private static final double TARGET_STROKE_WIDTH = 0.075;
	private static final Color TARGET_COLOR_FILL_RED = Color.RED;
	private static final Color TARGET_COLOR_FILL_WHITE = Color.WHITE;
	private static final Color TARGET_COLOR_STROKE = Color.BLACK;

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
    }

	public void destroy() {
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

    public double calcDrawScale(int ringIdx) {
		double startUpRingFactor = (ringIdx == 0) ? 2 : ((ringIdx == 1) ? 1.5 : 1);
		double startupScalar = Math.min(timeSinceBorn * startUpRingFactor, 1);
		return startupScalar * super.calcDrawScale();
	}	

    public void drawShadow(Graphics2D g) {
		// Setup...
		double scale = calcDrawScale(0);
		Color colorShadow = Util.colorLerp(World.COLOR_BACKGROUND, World.COLOR_SHADOW, timeSinceBorn * 2.0f);

		// Body...
		Draw.drawRectShadow(g, this.pos, calcDrawHeight(TARGET_HEIGHT, scale), new Vec2(TARGET_RADIUS, TARGET_RADIUS), scale, colorShadow, TARGET_RADIUS * 2);
    }

    public void draw(Graphics2D g) {
		// Setup...
		double height = calcDrawHeight(TARGET_HEIGHT, calcDrawScale());
		Color colorFillRed = Util.colorLerp(World.COLOR_BACKGROUND, TARGET_COLOR_FILL_RED, timeSinceBorn * 2.0f);
		Color colorFillWhite = Util.colorLerp(World.COLOR_BACKGROUND, TARGET_COLOR_FILL_WHITE, timeSinceBorn * 2.0f);
		Color colorStroke = Util.colorLerp(World.COLOR_BACKGROUND, TARGET_COLOR_STROKE, timeSinceBorn * 2.0f);

		// Body...
		Draw.drawRect(g, this.pos, height, new Vec2(TARGET_RADIUS, TARGET_RADIUS), calcDrawScale(0), colorFillRed, colorStroke, TARGET_STROKE_WIDTH, TARGET_RADIUS * 2);
		Draw.drawRect(g, this.pos, height, (new Vec2(TARGET_RADIUS, TARGET_RADIUS)).multiply(0.66), calcDrawScale(1), colorFillWhite, colorStroke, 0, TARGET_RADIUS * 2);
		Draw.drawRect(g, this.pos, height, (new Vec2(TARGET_RADIUS, TARGET_RADIUS)).multiply(0.33), calcDrawScale(2), colorFillRed, colorStroke, 0, TARGET_RADIUS * 2);
    }
}
