package framework;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
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

public class World extends JPanel implements ActionListener, MouseListener {
    // Singleton...
    private static World instance = null;
    public static synchronized World get() {
        if (instance == null)
            instance = new World();
  
        return instance;
    }

    // Constants...
    public static final int FIELD_BORDER = 10;
    public static final int FIELD_BORDER_TOP = FIELD_BORDER;
    public static final Color COLOR_BACKGROUND = new Color(229, 195, 172);
    public static final int RENDER_TIMER_PERIOD = 16;   // In milliseconds

    // Member variables...
    private Vec2 origin = new Vec2(0, 0);
    private Vec2 canvasSize = new Vec2(0, 0);
    private double pixelsPerUnit = 1;
    private Timer renderTimer = null;
    private BufferedImage backBuffer = null;
    private FluidSimulationBase simulation = null;
    private FluidRendererBase renderer = null;

    // Get/Set accessors/mutators...
    public Vec2 getOrigin() {
        return origin;
    }
    public Vec2 getCanvasSize() {
        return canvasSize;
    }
    public double getPixelsPerUnit() {
        return pixelsPerUnit;
    }
    public FluidSimulationBase getSimulation() {
        return simulation;
    }
    public void setSimulation(FluidSimulationBase sim) {
        simulation = sim;
    }
    public FluidRendererBase getRenderer() {
        return renderer;
    }
    public void setRenderer(FluidRendererBase render) {
        renderer = render;
    }

    // Member functions (methods)...
    protected World() {
        // Resize event...
        this.addComponentListener(new ComponentAdapter() {
            public void componentResized(ComponentEvent componentEvent) {
                // Set the origin and sizes...
                canvasSize = new Vec2(getWidth() - FIELD_BORDER * 2, getHeight() - FIELD_BORDER - FIELD_BORDER_TOP);
                pixelsPerUnit = 1; //Math.floor(Math.min(canvasSize.x, canvasSize.y) / 1);
                origin = new Vec2(FIELD_BORDER, getHeight() - FIELD_BORDER);

                // Back buffer(s)...
                if (backBuffer != null) {
                    backBuffer = null; // no dispose required?
                }
                backBuffer = new BufferedImage((int)canvasSize.x, (int)canvasSize.y, BufferedImage.TYPE_INT_ARGB);
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
        if (simulation != null) {
            simulation.mousePressed(event);
        }
    }
    public void mouseReleased(MouseEvent event) {
        if (simulation != null) {
            simulation.mouseReleased(event);
        }
    }
    public void mouseEntered(MouseEvent event) {
    }
    public void mouseExited(MouseEvent event) {
    }
    public void mouseClicked(MouseEvent event) {
        // Click pauses it...
        // if (event.getClickCount() == 1) {
        //     // Pause/unpause the App...
        //     App.get().setPause(!App.get().isPaused());
        // }
    }
    protected Vec2 getMousePositionDrawRegionRelative() {
        // returns the position in the coordinates of the density/velocity fields
        //  the x,y value returned is 0,0 in the lower-left and (densityField.length-1),(densityField[0].length-1) at the upper right
        Point pt = this.getMousePosition();
        if (pt == null) {
            return null;
        }
        Vec2 ptClientRel = Util.toCoordFrame(new Vec2(pt.x, pt.y));
        return ptClientRel;
    }

    // Update...
    protected void update(double deltaTime) {
        // Simulation update...
        simulation.update(deltaTime);
    }
    
    // Draw functions...
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

        // Full window clear...
        g.setColor(Color.LIGHT_GRAY);
        g.fillRect(0, 0, getWidth(), getHeight());

        // Background...
        g.setColor(COLOR_BACKGROUND);
        g.fillRect(FIELD_BORDER, FIELD_BORDER_TOP, getWidth() - FIELD_BORDER * 2, getHeight() - FIELD_BORDER - FIELD_BORDER_TOP);
        g.setColor(!App.get().isPaused() ? Color.DARK_GRAY : Color.RED);
        g.setStroke(new BasicStroke(2));
        g.drawRect(FIELD_BORDER, FIELD_BORDER_TOP, getWidth() - FIELD_BORDER * 2, getHeight() - FIELD_BORDER - FIELD_BORDER_TOP);

        // Draw to backBuffer...
        if ((backBuffer != null) && (renderer != null)) {
            // Render simulation to the back buffer...
            WritableRaster renderTarget = backBuffer.getRaster();
            renderer.render(renderTarget, simulation);

            // Draw the back buffer onto the screen/panel...
            g.drawImage(backBuffer, 
                        (int)getOrigin().x, 
                        (int)getOrigin().y - (int)getCanvasSize().y, 
                        (int)getCanvasSize().x,
                        (int)getCanvasSize().y,
                        null);
        }
    }
    public void paint(Graphics g) {
        drawWorld((Graphics2D)g);
    }
}
