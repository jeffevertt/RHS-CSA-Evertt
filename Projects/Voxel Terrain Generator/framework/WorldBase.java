package framework;

public class WorldBase {
    // Singletons...
    private static WorldBase instance = null;
    public static synchronized WorldBase get() {
        return instance;
    }

    // Constants...
    public static final double AMBIENT_LIGHT = 0.0;     // 0 to 1 scale

    // Member variables...
    private RendererBase renderer = null;
    private CameraBase camera = null;

    // Get/Set accessors/mutators...
    public RendererBase getRenderer() {
        return renderer;
    }
    protected void setRenderer(RendererBase renderer) {
        this.renderer = renderer;
    }

    // Member functions (methods)...
    protected WorldBase() {
        // Instance...
        if (instance != null) {
            System.out.println("WARNING: Two worlds created. Don't do that.");
        }
        instance = this;
    }

    public double findHeightAtPosXZ(int x, int z) {
        return 0;
    }

    // Scene support methods...
    public void resetScene() {
        camera = null;
    }
    public CameraBase getCamera() {
        return camera;
    }
    public void setCamera(CameraBase camera) {
        this.camera = camera;
    }
    public boolean createDefaultScene() {
        // reset, just in case...
        resetScene();

        return true;
    }

    // Update...
    public void update(double deltaTime) {
        // Scene objects...
        if (camera != null) {
            camera.update(deltaTime);
        }
    }
}
