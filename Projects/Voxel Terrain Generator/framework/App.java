package framework;

import org.lwjgl.glfw.GLFWErrorCallback;

import static org.lwjgl.glfw.GLFW.glfwInit;
import static org.lwjgl.glfw.GLFW.glfwSetErrorCallback;
import static org.lwjgl.glfw.GLFW.glfwTerminate;

public class App {
    // Singleton...
    private static App instance = null;
    public static synchronized App get()
    {
        if (instance == null)
            instance = new App();
  
        return instance;
    }

    // Constants
    public static final int TARGET_FPS = 60;
    public static final int TARGET_UPS = 30;

    // Member variables
    private GLFWErrorCallback errorCallback;
    protected Timer timer;

    // Methods
    public App() {
        timer = new Timer();
    }

    public boolean runApp(WorldBase world, RendererBase render) {
        if (!init()) {
            return false;
        }
        gameLoop();
        dispose();
        return true;
    }

    public void dispose() {
        RendererBase.get().dispose();
        Window.get().destroy();
        glfwTerminate();
        errorCallback.free();
    }

    public int getFPS() {
        return timer.getFPS();
    }

    /**
     * Initializes the game.
     */
    public boolean init() {
        /* Set error callback */
        errorCallback = GLFWErrorCallback.createPrint();
        glfwSetErrorCallback(errorCallback);

        /* Initialize GLFW */
        if (!glfwInit()) {
            System.out.println("Error: Unable to initialize GLFW!");
            return false;
        }

        /* Create GLFW window */
        Window window = new Window();
        if (!window.init()) {
            System.out.println("Error: Failed to create GLFW window!");
            return false;
        }

        /* Initialize renderer */
        if (!RendererBase.get().init()) {
            System.out.println("Error: Renderer initialization failed!");
            return false;
        }

        /* Initialize timer */
        timer.init();

        // Default scene
        if (!WorldBase.get().createDefaultScene()) {
            System.out.println("Error: Failed to create default scene!");
            return false;
        }


        return true;
    }

    /**
     * The game loop. <br>
     * For implementation take a look at <code>VariableDeltaGame</code> and
     * <code>FixedTimestepGame</code>.
     */
    public void gameLoop() {
        boolean closeRequested = false;
        while (!closeRequested) {
            /* Check if game should close */
            if (Window.get().isClosing()) {
                closeRequested = true;
            }

            /* Get delta time */
            double deltaTime = timer.getDelta();

            /* Update game and timer UPS */
            update(deltaTime);
            timer.updateUPS();

            /* Render game and update timer FPS */
            render();
            timer.updateFPS();

            /* Update timer */
            timer.update();

            /* Update window to show the new screen */
            Window window = Window.get();
            window.update();

            /* Synchronize if v-sync is disabled */
            if (!window.isVSyncEnabled()) {
                sync(TARGET_FPS);
            }
        }
    }

    public void update(double deltaTime) {
        // world
        WorldBase world = WorldBase.get();
        if (world != null) {
            world.update(deltaTime);
        }
    }

    public void render() {
        RendererBase renderer = RendererBase.get();

        // clear
        renderer.clear();

        // render the world
        renderer.renderWorld();
    }

    // Synchronizes the game at specified frames per second.
    public void sync(int fps) {
        double lastLoopTime = timer.getLastLoopTime();
        double now = timer.getTime();
        float targetTime = 1f / fps;

        while (now - lastLoopTime < targetTime) {
            Thread.yield();

            /* This is optional if you want your game to stop consuming too much
             * CPU but you will loose some accuracy because Thread.sleep(1)
             * could sleep longer than 1 millisecond */
            try {
                Thread.sleep(1);
            } catch (InterruptedException ex) {
                System.out.println("Error: Game.class.getName()");
            }

            now = timer.getTime();
        }
    }
}
