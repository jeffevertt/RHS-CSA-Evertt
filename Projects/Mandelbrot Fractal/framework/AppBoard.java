package framework;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.swing.JPanel;
import javax.swing.Timer;

public class AppBoard extends JPanel implements ActionListener, MouseListener, MouseMotionListener, MouseWheelListener {
    // Singleton...
	private static AppBoard instance = null;
	public static synchronized AppBoard get()
    {
        if (instance == null)
            instance = new AppBoard();
  
        return instance;
    }

    // Constants...
    public static final double DEFAULT_PIXEL_PER_UNIT = 200.0;
    public static final int SUPERSAMPLE_TEXELS_PER_PIXEL_XY = 1;
    public static final Color COLOR_BACKGROUND = new Color(229, 220, 201);
    public static final Color COLOR_GRID = new Color(173, 173, 173);
    public static final int RENDER_TIMER_PERIOD = 16;  // In milliseconds

    // Member variables...
    private boolean isFirstRender = true;
    private boolean cameraHasMoved = true;
    private Vec2 origin = new Vec2(0, 0);
    private double pixelsPerUnit = DEFAULT_PIXEL_PER_UNIT;
    private Vec2 canvasSize = new Vec2(0, 0);
    private Timer renderTimer;
    private Point dragStart;
    private BufferedImage backBuffer = null;

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
    public double getZoomFactor() {
        return pixelsPerUnit / DEFAULT_PIXEL_PER_UNIT;
    }

    // Member functions (methods)...
    protected AppBoard() {
        // Resize event...
        this.addComponentListener(new ComponentAdapter() {
            public void componentResized(ComponentEvent componentEvent) {
                // canvas size
                canvasSize = new Vec2(getWidth() - Window.FIELD_BORDER * 2, getHeight() - Window.FIELD_BORDER * 2);

                // create backbuffer
                backBuffer = new BufferedImage((int)canvasSize.x * SUPERSAMPLE_TEXELS_PER_PIXEL_XY, 
                                               (int)canvasSize.y * SUPERSAMPLE_TEXELS_PER_PIXEL_XY, BufferedImage.TYPE_INT_ARGB);

                // setup the level
                AppBase.get().onAppSetup();
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
        this.addMouseMotionListener(this);
        this.addMouseWheelListener(this);
    }
    public void setupBoard() {
        // origin & scale reset
        origin = new Vec2(Window.FIELD_BORDER + (getWidth()  - Window.FIELD_BORDER * 2) / 2, 
                          Window.FIELD_BORDER + (getHeight() - Window.FIELD_BORDER * 2) / 2);
        pixelsPerUnit = DEFAULT_PIXEL_PER_UNIT;

        // need to clear next frame
        isFirstRender = true;
        cameraHasMoved = true;
    }

    // Render timer...
    public void actionPerformed(ActionEvent event) {
        if (event.getSource() == renderTimer) {
            repaint();
        }
    }

    // Mouse events...
    public void mouseMoved(MouseEvent event) {
    }
    public void mouseDragged(MouseEvent event) {
        if (dragStart == null) {
            dragStart = event.getPoint();
            return;
        }
        Point dragEnd = event.getPoint();

        // move origin (TODO: deal with zoom, keep coord on mouse)
        Vec2 deltaPixels = Vec2.subtract(new Vec2(dragEnd.getX(), dragEnd.getY()), new Vec2(dragStart.getX(), dragStart.getY()));
        origin.add(deltaPixels);

        // flag camera motion
        cameraHasMoved = true;

        // reset the start
        dragStart = dragEnd;
    }
    public void mousePressed(MouseEvent event) {
        dragStart = event.getPoint();
    }
    public void mouseReleased(MouseEvent event) {
    }
    public void mouseEntered(MouseEvent event) {
    }
    public void mouseExited(MouseEvent event) {
    }
    public void mouseClicked(MouseEvent event) {
        // double click restarts it
        if (event.getClickCount() == 2 && !event.isConsumed()) {
            AppBase.get().onAppSetup();
        }
    }
    public void mouseWheelMoved(MouseWheelEvent event) {
        Vec2 zoomCenterPixels = new Vec2(event.getPoint().getX(), event.getPoint().getY());
        double zoomFactor = Math.pow(1.1, -event.getWheelRotation());

        // zoom in/out (around center point)
        Vec2 zoomCenter = Util.toCoordFrame(zoomCenterPixels);
        pixelsPerUnit *= zoomFactor;
        origin = new Vec2(zoomCenterPixels.x - zoomCenter.x * pixelsPerUnit, zoomCenterPixels.y - zoomCenter.y * -pixelsPerUnit);

        // flag camera motion
        cameraHasMoved = true;
    }
    
    // draw functions
    private void clearScreen(Graphics2D g) {
        // full window clear
        g.setColor(Color.LIGHT_GRAY);
        g.fillRect(0, 0, getWidth(), getHeight());

        // client rect clear
        g.setColor(COLOR_BACKGROUND);
        g.fillRect(Window.FIELD_BORDER, Window.FIELD_BORDER, getWidth() - Window.FIELD_BORDER * 2, getHeight() - Window.FIELD_BORDER * 2);
        g.setColor(Color.LIGHT_GRAY);
        g.setStroke(new BasicStroke(2));
        g.drawRect(Window.FIELD_BORDER, Window.FIELD_BORDER, getWidth() - Window.FIELD_BORDER * 2, getHeight() - Window.FIELD_BORDER * 2);
    }
    private void drawWorld(Graphics2D g) {
        // setup...
        Draw.beginRender(g);

        // set the base transform (this deals with HPI screen scaling properly)
        Draw.setBaseTransform(g.getTransform());

        // rendering hints
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        Map<Object, Object> hints = new LinkedHashMap<Object, Object>();
        hints.put(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        hints.put(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        hints.put(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);
        g.addRenderingHints(hints);

        // first render, do a clear
        if (isFirstRender) {
            clearScreen(g);
            isFirstRender = false;
        }

        // let the renderer do its drawing
        if (backBuffer != null) {
            // let the renderer draw to the render buffer
            WritableRaster renderTarget = backBuffer.getRaster();

            // let the app do its drawing
            if (cameraHasMoved) {
                AppBase.get().render(renderTarget);
                cameraHasMoved = false;
            }

            // draw the back buffer onto the screen/panel
            g.drawImage(backBuffer, 
                        Window.FIELD_BORDER, Window.FIELD_BORDER, 
                        (int)getCanvasSize().x, (int)getCanvasSize().y,
                        null);
        }
    }
    public void paint(Graphics g) {
        drawWorld((Graphics2D)g);
    }
}
