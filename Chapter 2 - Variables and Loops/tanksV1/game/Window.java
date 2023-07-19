import java.awt.Dimension;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

public class Window extends JFrame implements KeyListener {
    // Singleton...
	private static Window instance = null;
	public static synchronized Window get()
    {
        if (instance == null)
            instance = new Window();
  
        return instance;
    }

    // Constants...
    public static final String WINDOW_TITLE = "CS-A: Tanks.v1";
    public static final int WINDOW_WIDTH = 1024;
    public static final int WINDOW_HEIGHT = 600;

    // Member functions (methods)...
    public Window() {
        super(WINDOW_TITLE);

        // Setup the window...
        setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
        setMinimumSize(new Dimension(WINDOW_WIDTH / 2, WINDOW_HEIGHT / 2));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        addKeyListener(this);     
    }

    public static boolean create() {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                get().add(World.get());
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
