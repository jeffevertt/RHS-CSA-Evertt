package framework;

import java.awt.image.WritableRaster;

// Base class (interface) for student renderer
public interface RendererBase {
    public void render(WritableRaster renderTarget, World world, Camera camera);
}
