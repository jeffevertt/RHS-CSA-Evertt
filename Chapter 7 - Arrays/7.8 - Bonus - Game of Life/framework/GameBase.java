package framework;

import java.awt.Graphics2D;
import java.awt.event.*;

import javax.swing.Timer;

public class GameBase implements ActionListener {
    // constants
    private static final int UPDATE_TIMER_PERIOD    = 16;   // In milliseconds

    // singleton
    private static GameBase instance = null;
    public static synchronized GameBase get() {
        return instance;
    }

    // member variables
    private long milliSecondsLastUpdate = System.currentTimeMillis();
    private Timer updateTimer;

    // member functions (methods)
    public boolean create() {
        // save off the instance
        instance = this;

        // create the world
        if (!Window.create()) {
            return false;
        }

        // kick off update timer
        if (updateTimer != null && updateTimer.isRunning()) {
            updateTimer.stop();
        }
        updateTimer = new Timer(UPDATE_TIMER_PERIOD, this);
        updateTimer.start();

        return true;
    }

    public void actionPerformed(ActionEvent evt) {
        // calc deltaTime
        long milliSecondsNow = System.currentTimeMillis();
        double deltaTime = (double)(milliSecondsNow - milliSecondsLastUpdate) / 1000;
        milliSecondsLastUpdate = milliSecondsNow;

        // game update
        onLevelUpdate(deltaTime, false);
    }

    public void drawCell(Graphics2D g, int rowIdx, int colIdx) {
        Vec2 pos = new Vec2(colIdx * 1.0 + 0.5, rowIdx * 1.0 + 0.5);
        Vec2 halfDims = new Vec2(0.5, 0.5);
        Draw.drawRect(g, pos, halfDims);
    }

    // methods to be overridden in lab implementation
    public void onLevelSetup() {
        // do an initial update
        onLevelUpdate(0, true);
    }

    public void onLevelUpdate(double deltaTime, boolean firstUpdate) {
    }

    public void onMouseDrag(Vec2 start, Vec2 end) {
    }

    public void draw(Graphics2D g) {
    }
}
