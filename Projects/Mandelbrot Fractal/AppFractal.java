import framework.AppBase;

import java.awt.image.WritableRaster;

public class AppFractal extends AppBase {
    /******************* Mandelbrot Fractal - INSTRUCTIONS *******************/
    /* YouTube (nice overview): https://www.youtube.com/watch?v=NGMRB4O922I  */
    /*** The concept can take some time to wrap your head around, but the  ***/
    /*** basic implementation (without a lot of fancy optimizations), is   ***/
    /*** reasonably straightforward.                                       ***/
    /* What you need to implement is mostly just in render(...). You are     */
    /*** given a renderTarget (this is just a writable image). You need to ***/
    /*** set all the pixels in this image in render to RGB values.         ***/
    /* You do this imagining the complex plane in the window (x is the real  */
    /*** axis and y is the imaginary one). Each (x,y) point in the image   ***/
    /*** corresponds to a complex number (a + bi)...a point on the complex ***/
    /*** plane. Then use a method that you need to write to determine if   ***/
    /*** that complex number converges under the iterative formula         ***/
    /**************************  Zn = Zn-1^2 + C  ****************************/
    /* If the magnitude (distance from the origin) is ever greater than      */
    /*** 2 or so, it is guarenteed to diverge. Your method should          ***/
    /*** determine the number of iterations until escape. Then turn that   ***/
    /*** into a color and set it on the image/renderTarget.                ***/
    /* One more thing...you are going to need to write a class to store      */
    /*** a complex number & do the arithmetic (multiply, add).             ***/
    /*************************************************************************/

    public static void main(String[] args) {
        // Create up the app, it kicks off the whole process...
        AppFractal mandelbrotFractal = new AppFractal();
        if (!mandelbrotFractal.create()) {
            throw new Error("Could not create the app :(");
        }
    }

    // constants
    private static final int DEFAULT_MAX_ITERATIONS = 200;
    private static final double DIVERGENCE_ESCAPE_MAG = 2.001;

    // data
    //  TODO: add your data (if any...you might have none)

    // methods
    public void onAppSetup() {
        super.onAppSetup();

        // TODO: this is called once at the start, it is a good place to initialize your data
        //  note that you might have nothing to do here
    }

    public void render(WritableRaster renderTarget) {
        super.render(renderTarget);

        // TODO: this is where you write each pixel in render target
        //  Tip (1): Loop through each pixel in the renderTarget image. 
        //              You can use renderTarget.getWidth() & renderTarget.getHeight().
        //  Tip (2): I've written a couple of helpers to convert your texel x/y to 
        //              numbers on the complex plane. I'd recommend using these, they 
        //              also take care of pan/zoom using the mouse. 
        //                  AppUtils.texelCoordToComplexCoordinateX(...)
        //                  AppUtils.texelCoordToComplexCoordinateY(...)
        //  Tip (3): I've also written a convergeFactor to color method for you.
        //              It expectes an arguement in the range [0, 1].
        //              So, you'll need to normalize your number of iterations into this range.
        //                  AppUtils.getColorPaletteValueRGBA(...)
        //  Tip (4): Use renderTarget.setPixel(...) to set the pixel's color at the specified (x,y)
    }
}
