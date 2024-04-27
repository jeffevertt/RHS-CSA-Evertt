package simulation;

import framework.FluidRendererBase;
import framework.FluidSimulationBase;

import java.awt.image.WritableRaster;

/* FluidRenderer is responsible for rendering/drawing the simulation.
 *  You are implementing the method shown below. The renderTarget is an image. You
 *    can write individual pixels of the image using something like this...
 *        renderTarget.setPixel(x, y, new int[] { 0, 255, 0, 255 });
 *    the above line would write green to the pixel location at location (x,y). 
 *  Note that the 3rd argument is in the format Red,Green,Blue,Alpha. Alpha is
 *    used for transparency. You probably want to keep that as 255 all the time.
 *    Each R/G/B/A value is in the range of [0,255].
 * 
 * Tips...
 *   - You probably want to draw the density field and ignore the velocity field, 
 *      as this is generally the only thing that would be visible (though, using
 *      the velocity field as well might be a fun thing to play with).
 *   - You can get the density field with: double[][] densityField = sim.getDensityField();
 *   - Until you add sources in the FluidSimulation, your density field will be
 *      all zeros, so don't expect to be able to rendering anything interesting until 
 *      you have at least some test code adding a source there.
 */
public class FluidRenderer implements FluidRendererBase {
    public void render(WritableRaster renderTarget, FluidSimulationBase sim) {
        // TODO

        // *** Test code (remove me) ***
        for (int x = 0; x < renderTarget.getWidth(); x++) {
            renderTarget.setPixel(x, renderTarget.getHeight() / 2, 
                                  new int[] { 0, 0, 0, 255 });
        }
        // *** Test code (remove me) ***
    }
}
