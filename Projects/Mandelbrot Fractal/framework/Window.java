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
    public static final String WINDOW_TITLE = "CS-A: Mandelbrot Fractal";
    private static final int WINDOW_APPROX_BORDER_X = 16;
    private static final int WINDOW_APPROX_BORDER_Y = 39;
    public static final int DEFAULT_WINDOW_WIDTH = 400 + Window.FIELD_BORDER * 2 + WINDOW_APPROX_BORDER_X;
    public static final int DEFAULT_WINDOW_HEIGHT = 400 + Window.FIELD_BORDER * 2 + WINDOW_APPROX_BORDER_Y;
    public static final int FIELD_BORDER = 10;

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

    protected static boolean create() {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                get().add(AppBoard.get());
                get().setVisible(true);
            }
        });
        return true;
    }

    // Key press support...
    public void keyPressed(KeyEvent e) {    
    }
    public void keyReleased(KeyEvent e) {    
    }
    public void keyTyped(KeyEvent e) {    
    }
}
