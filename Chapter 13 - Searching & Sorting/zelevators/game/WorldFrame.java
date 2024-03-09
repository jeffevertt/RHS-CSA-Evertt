package game;

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
import java.util.LinkedHashMap;
import java.util.Map;

import javax.swing.JPanel;
import javax.swing.Timer;

public class WorldFrame extends JPanel implements ActionListener, MouseListener {
    // Singleton...
	private static WorldFrame instances = null;
	protected static synchronized WorldFrame get() {
        if (instances == null)
            instances = new WorldFrame();
  
        return instances;
    }

    // Constants...
    public static final Color COLOR_BACKGROUND = new Color(198, 181, 177);
    public static final int RENDER_TIMER_PERIOD = 16;  // In milliseconds

    // Member variables...
    private Timer renderTimer;

    // Accessors...
    public int getFrameWidth() {
        return getWidth();
    }
    public int getFrameHeight() {
        return getHeight();
    }

    // Member functions (methods)...
    protected WorldFrame() {
        // Resize event...
        this.addComponentListener(new ComponentAdapter() {
            public void componentResized(ComponentEvent componentEvent) {
                // Notify the World(s)...
                for (int i = 0; i < Game.get().getPlayerCount(); i++) {
                    World.get(i).onWorldFrameResized();
                }

                // Setup the level...
                if (!Game.get().isGameActive()) {
                    Game.get().onLevelSetup();
                }
            }
        });

        // Defaults...
        setBackground(COLOR_BACKGROUND);

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
    private void drawFrame(Graphics2D g) {
        // Wait till we're actually inialized...
        if (!Game.get().isInitialized()) {
            return;
        }

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

        // Full window clear...
        g.setColor(Color.LIGHT_GRAY);
        g.fillRect(0, 0, getFrameWidth(), getFrameHeight());

        // Let the world draw...
        for (int i = 0; i < Game.get().getPlayerCount(); i++) {
            // Reset state...
            Draw.resetRenderState(g);

            // Draw the world...
            World.get(i).drawWorld(g);
        }

        // Let the game do its drawing...
        Game.get().Draw(g);
    }
    public void paint(Graphics g) {
        drawFrame((Graphics2D)g);
    }
}
