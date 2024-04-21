package framework;

import java.awt.event.*;
import javax.swing.Timer;

public class App implements ActionListener {
    // Constants...
    private static final int UPDATE_TIMER_PERIOD        = 16;   // In milliseconds
    private static final int DEFAULT_DOWNSCALE_FACTOR   = 2;    // Values larger than 1 reduce the backbuffer resolution to improve performance (quality vs performance tradeoff)

    // Nested classes...
    protected class AppStats {
        public boolean isPaused = false;

        public void reset() {
            isPaused = false;
        }
    }

    // Singleton...
    private static App instance = null;
    public static synchronized App get()
    {
        if (instance == null)
            instance = new App();
  
        return instance;
    }
    public static boolean createApp(RendererBase renderer) {
        return App.createApp(renderer, DEFAULT_DOWNSCALE_FACTOR);
    }
    public static boolean createApp(RendererBase renderer, int imageDownScaleFactor) {
        return App.get().create(renderer, imageDownScaleFactor);
    }

    // Accessors...
    public boolean isPaused() {
        return this.appStats.isPaused;
    }

    // Member variables...
    private AppStats appStats = new AppStats();
    private long milliSecondsLastUpdate = System.currentTimeMillis();
    private Timer updateTimer;

    // Member functions (methods)...
    protected boolean create(RendererBase renderer, int imageDownScaleFactor) {
        // Create the window (which creates the world)...
        if (!Window.create(renderer, imageDownScaleFactor)) {
            return false;
        }

        // Kick off update timer...
        if (updateTimer != null && updateTimer.isRunning()) {
            updateTimer.stop();
        }
        updateTimer = new Timer(UPDATE_TIMER_PERIOD, this);
        updateTimer.start();

        // App setup...
        onAppSetup();

        return true;
    }

    protected void onAppSetup() {
        // Reset app...
        appStats.reset();

        // Ask the world to create up a default scene...
        World.get().createDefaultScene();
    }

    protected void setPause(boolean isPaused) {
        appStats.isPaused = isPaused;
    }

    public void actionPerformed(ActionEvent evt) {
        App.get().updateEvent();
    }

    protected void updateEvent() {
        // Step the sim...
        long milliSecondsNow = System.currentTimeMillis();
        double deltaTime = (double)(milliSecondsNow - milliSecondsLastUpdate) / 1000;
        milliSecondsLastUpdate = milliSecondsNow;

        // Clamp deltaTime (slow down the sim, if needed)...
        deltaTime = Math.min(deltaTime, 0.1);

        // Deal with pause...
        if (isPaused()) {
            deltaTime = 0.0;
        }

        // Let the app do its update...
        updateApp(deltaTime);
    }

    private void updateApp(double deltaTime) {
        Camera camera = World.get().getCamera();
        if (camera != null) {
            camera.update(deltaTime);
        }
    }
}
