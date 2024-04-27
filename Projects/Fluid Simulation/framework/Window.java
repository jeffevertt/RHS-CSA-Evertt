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
    public static final String WINDOW_TITLE = "CS-A: Fluid Simulation";
    public static final int DEFAULT_WINDOW_WIDTH = 1024;
    public static final int DEFAULT_WINDOW_HEIGHT = 600;

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

    protected static boolean create(FluidSimulationBase sim, FluidRendererBase renderer) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                World world = World.get();
                world.setSimulation(sim);
                world.setRenderer(renderer);
                get().add(world);
                get().setVisible(true);
            }
        });
        return true;
    }

    // Key press support...
    public void keyPressed(KeyEvent e) {
        World world = World.get();
        if (world != null) {
            FluidSimulationBase sim = world.getSimulation();
            if (sim != null) {
                sim.keyPressed(e);
            }
        }
    }
    public void keyReleased(KeyEvent e) {
        World world = World.get();
        if (world != null) {
            FluidSimulationBase sim = world.getSimulation();
            if (sim != null) {
                sim.keyReleased(e);
            }
        }
    }
    public void keyTyped(KeyEvent e) {    
    }
}
