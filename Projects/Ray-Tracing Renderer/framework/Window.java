package framework;

import java.awt.Dimension;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

public class Window extends JFrame implements KeyListener {
    // Singleton...
    private static Window instance = null;
    protected static synchronized Window get()
    {
        if (instance == null)
            instance = new Window();
  
        return instance;
    }

    // Constants...
    public static final String WINDOW_TITLE = "CS-A: Ray-Tracing Renderer";
    public static final int DEFAULT_WINDOW_WIDTH = 1024;
    public static final int DEFAULT_WINDOW_HEIGHT = 600;

    // Enums...
    public enum ArrowKey {
        Left(0),
        Right(1), 
        Up(2),
        Down(3);

        private final int value;
        private ArrowKey(int value) {
            this.value = value;
        }
        public int getValue() {
            return value;
        }
    }

    // Data...
    private boolean[] arrowPressed = new boolean[4]; // each of ArrowKey

    // Member functions (methods)...
    protected Window() {
        super(WINDOW_TITLE);

        // Setup the window...
        setSize(DEFAULT_WINDOW_WIDTH, DEFAULT_WINDOW_HEIGHT);
        setMinimumSize(new Dimension(DEFAULT_WINDOW_WIDTH / 2, DEFAULT_WINDOW_HEIGHT / 2));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        addKeyListener(this);
    }

    protected static boolean create(RendererBase renderer, int imageDownScaleFactor) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                World world = World.get();
                world.setRenderer(renderer, imageDownScaleFactor);
                get().add(world);
                get().setVisible(true);
            }
        });
        return true;
    }

    // Key press support...
    public boolean getArrowKeyPressed(ArrowKey key) {
        return arrowPressed[key.getValue()];
    }
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_LEFT) {
            arrowPressed[ArrowKey.Left.getValue()] = true;
        }
        else if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
            arrowPressed[ArrowKey.Right.getValue()] = true;
        }
        else if (e.getKeyCode() == KeyEvent.VK_UP) {
            arrowPressed[ArrowKey.Up.getValue()] = true;
        }
        else if (e.getKeyCode() == KeyEvent.VK_DOWN) {
            arrowPressed[ArrowKey.Down.getValue()] = true;
        }
    }
    public void keyReleased(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_LEFT) {
            arrowPressed[ArrowKey.Left.getValue()] = false;
        }
        else if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
            arrowPressed[ArrowKey.Right.getValue()] = false;
        }
        else if (e.getKeyCode() == KeyEvent.VK_UP) {
            arrowPressed[ArrowKey.Up.getValue()] = false;
        }
        else if (e.getKeyCode() == KeyEvent.VK_DOWN) {
            arrowPressed[ArrowKey.Down.getValue()] = false;
        }
    }
    public void keyTyped(KeyEvent e) {    
    }
}
