package game;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.ArrayList;

public class PowerUp extends GameObject {
	// Constants...
	public static final Vec2 POWERUP_HALFDIMS = new Vec2(0.4, 0.4);
	public static final double POWERUP_HEIGHT = 0.5;
	private static final double POWERUP_STROKE_WIDTH = 0.05;
	private static final double POWERUP_ROUNDED_SIZE = 0.1;
	private static final Color POWERUP_COLOR_FILL = Color.YELLOW;
	private static final Color POWERUP_COLOR_STROKE = Color.BLACK;
	private static final Color POWERUP_COLOR_TEXT = new Color(96, 96, 255);

	// Member variables...
	private String type;

	// Accessors...
	public String getType() {
		return type;
	}
	public Vec2 getHalfDims() {
		return Vec2.copy(POWERUP_HALFDIMS);
	}

	// Member functions (methods)...
	protected PowerUp(Vec2 pos, String type) {
		// Parent...
		super();

		// Member vars...
        this.pos = pos;
		this.type = type;
		this.timeTillDeath = 0;
    }
	protected PowerUp(Vec2 pos) {
		this(pos, "P");
	}

	protected void destroy() {
		// Super...
		super.destroy();		
	}
	
	protected boolean shouldBeCulled() {
		// Check death timer...
		if (this.timeTillDeath >= 1) {
			return true;
		}
		return false;
	}
	
	protected void update(double deltaTime) {
		// Super...
		super.update(deltaTime);

		// If dying, continue...
		if (this.timeTillDeath > 0) {
			// Update it...
			this.timeTillDeath = Math.min(this.timeTillDeath + deltaTime * 2.0, 1.0);
		}
		else if (!Game.get().isGamePaused()) {
			// Check for tanks collecting us...
			ArrayList<GameObject> gameObjects = Simulation.getGameObjects();
			for (int i = 0; i < gameObjects.size(); ++i) {
				GameObject gameObject = gameObjects.get(i);
				if (gameObject instanceof Tank) {
					Tank tank = (Tank)gameObject;
					if (Util.circlesIntersect(this.pos, POWERUP_HALFDIMS.x, tank.pos, Tank.BODY_HALFSIZE.x)) {
						// React to it...
						tank.onCollect(this);
					}
				}
			}
		}
	}

	protected void drawShadow(Graphics2D g) {
		// Setup...
		double scale = calcDrawScale();
		Color colorShadow = Util.colorLerp(World.COLOR_BACKGROUND, World.COLOR_SHADOW, timeSinceBorn * 2.0f);

		// Body...
		Draw.drawRectShadow(g, this.pos, calcDrawHeight(POWERUP_HEIGHT, scale), POWERUP_HALFDIMS, scale, colorShadow, POWERUP_ROUNDED_SIZE);
	}

	protected void draw(Graphics2D g) {
		// Setup...
		double scale = calcDrawScale();
		double height = calcDrawHeight(POWERUP_HEIGHT, scale);
		Color colorFill = Util.colorLerp(World.COLOR_BACKGROUND, POWERUP_COLOR_FILL, timeSinceBorn * 2.0f);
		Color colorStroke = Util.colorLerp(World.COLOR_BACKGROUND, POWERUP_COLOR_STROKE, timeSinceBorn * 2.0f);
		Color colorText = Util.colorLerp(World.COLOR_BACKGROUND, POWERUP_COLOR_TEXT, timeSinceBorn * 2.0f);
		Color colorTextShadow = Util.colorLerp(World.COLOR_BACKGROUND, World.COLOR_SHADOW_TEXT, timeSinceBorn * 2.0f);
		Draw.FontSize fontSize = Draw.fontSizeFromScale(scale);

		// Body...
		Draw.drawRect(g, this.pos, height, POWERUP_HALFDIMS, scale, colorFill, colorStroke, POWERUP_STROKE_WIDTH, POWERUP_ROUNDED_SIZE);

		// Text...
		Draw.drawTextCentered(g, type, this.pos, height, 1, fontSize, colorText, colorTextShadow);
	}
}
