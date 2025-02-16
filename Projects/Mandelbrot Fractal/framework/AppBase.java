package framework;

import java.awt.event.*;
import java.awt.image.WritableRaster;

import javax.swing.Timer;

public class AppBase implements ActionListener {
    // constants
    private static final int UPDATE_TIMER_PERIOD    = 16;   // In milliseconds

    // singleton
    private static AppBase instance = null;
    public static synchronized AppBase get() {
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

        // app update
        onAppUpdate(deltaTime, false);
    }

    // methods to be overridden in lab implementation
    public void onAppSetup() {
        // let the board know
        AppBoard board = AppBoard.get();
        if (board != null) {
            board.setupBoard();
        }

        // do an initial update
        onAppUpdate(0, true);
    }

    public void onAppUpdate(double deltaTime, boolean firstUpdate) {
    }

    public void render(WritableRaster renderTarget) {
    }
}
