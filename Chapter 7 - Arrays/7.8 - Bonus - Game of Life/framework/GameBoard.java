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
import java.util.LinkedHashMap;
import java.util.Map;

import javax.swing.JPanel;
import javax.swing.Timer;

public class GameBoard extends JPanel implements ActionListener, MouseListener, MouseMotionListener {
    // Singleton...
	private static GameBoard instance = null;
	public static synchronized GameBoard get()
    {
        if (instance == null)
            instance = new GameBoard();
  
        return instance;
    }

    // Constants...
    public static final int CELL_COUNT_XY = 40;

    public static final Color COLOR_BACKGROUND = new Color(229, 220, 201);
    public static final Color COLOR_GRID = new Color(173, 173, 173);
    public static final int RENDER_TIMER_PERIOD = 16;  // In milliseconds

    // Member variables...
    private Vec2 origin = new Vec2(0, 0);
    private Vec2 canvasSize = new Vec2(0, 0);
    private double pixelsPerCell = 1;
    private Timer renderTimer;
    private Point dragStart;

    // Accessors...
    public Vec2 getOrigin() {
        return origin;
    }
    public Vec2 getCanvasSize() {
        return canvasSize;
    }
    public double getPixelsPerCell() {
        return pixelsPerCell;
    }

    // Member functions (methods)...
    protected GameBoard() {
        // Resize event...
        this.addComponentListener(new ComponentAdapter() {
            public void componentResized(ComponentEvent componentEvent) {
                // Set the origin and sizes...
                canvasSize = new Vec2(getWidth() - Window.FIELD_BORDER * 2, getHeight() - Window.FIELD_BORDER * 2);
                pixelsPerCell = Math.floor(Math.min(canvasSize.x, canvasSize.y) / CELL_COUNT_XY);
                origin = new Vec2(Window.FIELD_BORDER, getHeight() - Window.FIELD_BORDER);

                // Setup the level...
                GameBase.get().onLevelSetup();
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
        Vec2 start = new Vec2(Util.toCoordFrameX(dragStart.getX()), Util.toCoordFrameY(dragStart.getY()));
        Vec2 end = new Vec2(Util.toCoordFrameX(dragEnd.getX()), Util.toCoordFrameY(dragEnd.getY()));
        GameBase game = GameBase.get();
        if (game != null) {
            game.onMouseDrag(start, end);
        }
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
        // Double click restarts it...
        if (event.getClickCount() == 2 && !event.isConsumed()) {
            GameBase.get().onLevelSetup();
        }
    }
    
    // Draw functions...
    private void drawGrid(Graphics2D g) {
        g.setTransform(Draw.getBaseTransform());
        g.setStroke(new BasicStroke());
        g.setColor(COLOR_GRID);
        for (int i = 0; i <= canvasSize.x; i += pixelsPerCell) {
            int x = Window.FIELD_BORDER + i;
            g.drawLine(x, Window.FIELD_BORDER, x, Window.FIELD_BORDER + (int)canvasSize.y);
		}
		for (int i = 0; i <= canvasSize.y; i += pixelsPerCell) {
            int y = getHeight() - Window.FIELD_BORDER - i;
            g.drawLine(Window.FIELD_BORDER, y, Window.FIELD_BORDER + (int)canvasSize.x, y);
		}
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

        // full window clear
        g.setColor(Color.LIGHT_GRAY);
        g.fillRect(0, 0, getWidth(), getHeight());

        // background clear
        g.setColor(COLOR_BACKGROUND);
        g.fillRect(Window.FIELD_BORDER, Window.FIELD_BORDER, getWidth() - Window.FIELD_BORDER * 2, getHeight() - Window.FIELD_BORDER * 2);
        g.setColor(Color.LIGHT_GRAY);
        g.setStroke(new BasicStroke(2));
        g.drawRect(Window.FIELD_BORDER, Window.FIELD_BORDER, getWidth() - Window.FIELD_BORDER * 2, getHeight() - Window.FIELD_BORDER * 2);

        // grid
        drawGrid(g);

        // let the game do its drawing
        GameBase.get().draw(g);
    }
    public void paint(Graphics g) {
        drawWorld((Graphics2D)g);
    }
}
