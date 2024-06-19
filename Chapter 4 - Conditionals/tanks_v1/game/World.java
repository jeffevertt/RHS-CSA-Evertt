package game;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
// import java.io.File;
// import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

//import javax.imageio.ImageIO;
import javax.swing.JPanel;
import javax.swing.Timer;

public class World extends JPanel implements ActionListener, MouseListener {
    // Singleton...
	private static World instance = null;
	public static synchronized World get()
    {
        if (instance == null)
            instance = new World();
  
        return instance;
    }

    // Constants...
    public static final int FIELD_BORDER = 10;
    public static final int FIELD_BORDER_TOP = 45;
    public static final Color COLOR_BACKGROUND = new Color(229, 195, 172);
    public static final Color COLOR_GRID = new Color(193, 154, 127);
    public static final Color COLOR_SHADOW = new Color(110, 110, 110);
    public static final Color COLOR_SHADOW_TEXT = new Color(60, 60, 60);
    public static final int RENDER_TIMER_PERIOD = 16;  // In milliseconds

    // Member variables...
    private Vec2 origin = new Vec2(0, 0);
    private Vec2 canvasSize = new Vec2(0, 0);
    private double pixelsPerUnit = 1;
    private BufferedImage bckGndImage = null;
    private Timer renderTimer;

    // Accessors...
    public Vec2 getOrigin() {
        return origin;
    }
    public Vec2 getCanvasSize() {
        return canvasSize;
    }
    public double getPixelsPerUnit() {
        return pixelsPerUnit;
    }

    // Member functions (methods)...
    protected World() {
        // Resize event...
        this.addComponentListener(new ComponentAdapter() {
            public void componentResized(ComponentEvent componentEvent) {
                // Set the origin and sizes...
                canvasSize = new Vec2(getWidth() - FIELD_BORDER * 2, getHeight() - FIELD_BORDER - FIELD_BORDER_TOP);
                pixelsPerUnit = Math.floor(Math.min(canvasSize.x, canvasSize.y) / 12);
                origin = new Vec2(FIELD_BORDER, getHeight() - FIELD_BORDER);

                // Setup the level...
                if (!Game.get().isGameActive()) {
                    Game.get().onLevelSetup();
                }
            }
        });

        // Defaults...
        setBackground(COLOR_BACKGROUND);

        // Background texture...
        // try {
        //     bckGndImage = ImageIO.read(new File("textures/ground.png"));
        // } 
        // catch (IOException e) {
        // }

        // Kick off render timer...
        if (renderTimer != null && renderTimer.isRunning()) {
            renderTimer.stop();
        }
        renderTimer = new Timer(RENDER_TIMER_PERIOD, this);
        renderTimer.start();

        // Mouse listener...
        this.addMouseListener(this);
    }

    // Render timer...
    public void actionPerformed(ActionEvent event) {
        if (event.getSource() == renderTimer) {
            repaint();
        }
    }

    // Mouse events...
    public void mousePressed(MouseEvent event) {
    }
    public void mouseReleased(MouseEvent event) {
    }
    public void mouseEntered(MouseEvent event) {
    }
    public void mouseExited(MouseEvent event) {
    }
    public void mouseClicked(MouseEvent event) {
        // Double click restarts it...
        if (event.getClickCount() == 2 && !event.isConsumed()) {
            Game.get().onLevelSetup();
        }
        else if ((event.getClickCount() == 1) && Game.get().isGameActive()) {
            // Pause/unpause the game...
            Game.get().setGamePause(!Game.get().isGamePaused());
        }
    }
    
    // Draw functions...
    private void drawGrid(Graphics2D g) {
        g.setTransform(Draw.getBaseTransform());
        g.setStroke(new BasicStroke());
        g.setColor(COLOR_GRID);
        for (int i = 0; i <= canvasSize.x; i += pixelsPerUnit) {
            int x = FIELD_BORDER + i;
            g.drawLine(x, FIELD_BORDER_TOP, x, FIELD_BORDER_TOP + (int)canvasSize.y);
		}
		for (int i = 0; i <= canvasSize.y; i += pixelsPerUnit) {
            int y = getHeight() - FIELD_BORDER - i;
            g.drawLine(FIELD_BORDER, y, FIELD_BORDER + (int)canvasSize.x, y);
		}
    }
    private void drawWorld(Graphics2D g) {
        // Setup...
        Draw.beginRender(g);

        // Set the base transform (this deals with HPI screen scaling properly)...
        Draw.setBaseTransform(g.getTransform());
        
        // Rendering hints...
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        Map<Object, Object> hints = new LinkedHashMap<Object, Object>();
        hints.put(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        hints.put(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        hints.put(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);
        g.addRenderingHints(hints);

        // Ful window clear...
        g.setColor(Color.LIGHT_GRAY);
        g.fillRect(0, 0, getWidth(), getHeight());

        // Background...
        if (bckGndImage != null) {
            Vec2 maxUnitsPixels = Util.toPixels(Util.maxCoordFrameUnits());
            g.drawImage(bckGndImage, (int)origin.x, (int)origin.y, (int)(maxUnitsPixels.x - origin.x), (int)(maxUnitsPixels.y - origin.y), null);
        } else {
            g.setColor(COLOR_BACKGROUND);
            g.fillRect(FIELD_BORDER, FIELD_BORDER_TOP, getWidth() - FIELD_BORDER * 2, getHeight() - FIELD_BORDER - FIELD_BORDER_TOP);
        }
        g.setColor(!Game.get().isGamePaused() ? (Game.get().isGameActive() ? Color.DARK_GRAY : Color.LIGHT_GRAY) : Color.RED);
        g.setStroke(new BasicStroke(2));
        g.drawRect(FIELD_BORDER, FIELD_BORDER_TOP, getWidth() - FIELD_BORDER * 2, getHeight() - FIELD_BORDER - FIELD_BORDER_TOP);

        // Loop twice, first for shadow, then for geometry...
        for (int pass = 0; pass < 2; ++pass) {
            // Grid...
            if (pass == 1) {
                drawGrid(g);
            }
            
            // PowerUps...
            ArrayList<GameObject> gameObjects = Simulation.getGameObjects();
            for (int i = 0; i < gameObjects.size(); ++i) {
                GameObject gameObject = gameObjects.get(i);
                if (gameObject instanceof PowerUp) {
                    if (pass == 0) gameObject.drawShadow(g); 
                    else gameObject.draw(g);
                }
            }

            // Targets...
            for (int i = 0; i < gameObjects.size(); ++i) {
                GameObject gameObject = gameObjects.get(i);
                if (gameObject instanceof Target) {
                    if (pass == 0) gameObject.drawShadow(g); 
                    else gameObject.draw(g);
                }
            }   
            
            // Tanks...
            for (int i = 0; i < gameObjects.size(); ++i) {
                GameObject gameObject = gameObjects.get(i);
                if (gameObject instanceof Tank) {
                    if (pass == 0) gameObject.drawShadow(g); 
                    else gameObject.draw(g);
                }
            }        

            // Ammo...
            for (int i = 0; i < gameObjects.size(); ++i) {
                GameObject gameObject = gameObjects.get(i);
                if (gameObject instanceof Ammo) {
                    if (pass == 0) gameObject.drawShadow(g); 
                    else gameObject.draw(g);
                }
            }
        }

        // Let the game do its drawing...
        Game.get().Draw(g);
    }
    public void paint(Graphics g) {
        if (Simulation.get() == null) {
            return;
        }
        drawWorld((Graphics2D)g);
    }
}
