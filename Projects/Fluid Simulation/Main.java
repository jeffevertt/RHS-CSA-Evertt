import framework.App;
import simulation.FluidRenderer;
import simulation.FluidSimulation;

/* In this lab you will be completing the fluid simulation & rendering app.
 *  The majority of the simulation code is provided for you. 
 * The simulation has two fields - one is a scalar density field and the other
 *  is a 2D vector velocity field. A field is a 2D matrix where each cell
 *  represents a small rectangular section of the app's window. The first row 
 *  maps to the pixels at the bottom of the app's window and the last row of elements 
 *  is the top of the window screen.  
 * Together the two fields/2d-arrays contain all the information needed to simulate 
 *  and render. The simulation updates the physics of the system and rendering draws
 *  the current state of the simulation to an render target image.
 * The two fields contain two different bits of information...
 *   - Density field: Each cell is the amount of "smoke/dye" in that region of space.
 *        this is probably what you want to render. Think of it as little dust
 *        particles floating around in the air, they travel with the velocity stored
 *        in the other field.
 *   - Velocity field: Each cell contains a velocity vector (direction and magitude).
 *        So, for example, if each cell contained a (5,0) vector value, then all
 *        the dust particles represented by the density field would flow to the right.
 * 
 * You are adding a few key things to bring it to life. They include...
 *   - User input, responsible for two thing...
 *      - Add "smoke/dye" sources for the density field (without this, its all just black).
 *      - Add velocity sources/forces for the velocity field (without this, no motion).
 *      - Your code goes here: "simulation\FluidSimulation.java"
 *   - Rendering of the "smoke/dye" to the image renderTarget.
 *      - Your code goes here: "simulation\FluidRenderer.java"
 * 
 *       **** THE ONLY TWO FILES YOU SHOULD EDIT ARE THE TWO MENTIONED ABOVE *****
 * (though if you'd like to create new files/classes in the simulation folder, that's allowed)
 * 
 * If you'd like more background on how the simulation is implemented, it is based
 *  off of a paper written back in in 2003. The paper is referenced below & there is
 *  also a good video you can check out, if you'd like. Note that you don't need to 
 *  understand the math in this simulation, as a working implementation is provided.
 *   - YouTube video: https://www.youtube.com/watch?v=qsYE1wMEMPA 
 *   - Paper: http://graphics.cs.cmu.edu/nsp/course/15-464/Fall09/papers/StamFluidforGames.pdf  
 */

public class Main {
    public static void main(String[] args) {
        // Create up the app, it kicks off the whole process...
        if (!App.createApp(new FluidSimulation(), new FluidRenderer())) {
            throw new Error("Could not create the app :(");
        }
    }
}
