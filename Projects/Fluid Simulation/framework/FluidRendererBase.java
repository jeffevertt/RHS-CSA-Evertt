package framework;

import java.awt.image.WritableRaster;

public interface FluidRendererBase {
    public void render(WritableRaster renderTarget, FluidSimulationBase sim);
}
