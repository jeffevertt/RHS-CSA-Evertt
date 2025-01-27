
import app.Renderer;
import app.World;
import framework.App;

public class Main {

    // In this lab you will add camera functionallity to this voxel based terrain generator.
    //  Open app\Camera.java for details on the lab/project.
    // 
    // After implementing the camera, there are other opportunities to extend the app. 
    //  To be able to earn any extra credit that is offered, you must completed one of these
    // or come up with your own of a similar scale of complexity. 
    //  Here are a few...
    //    - Tweaking how the world is build, look at the app\World.java code to start.
    //    - Adding a light on the player/camera. This would require adding code to the
    //          shaders used for the world (res\worldDefault.vert & res\worldDefault.frag).
    //          You might select this if you would like to learn more about shaders.
    //          I would only suggest this if you already have an idea what a shader is
    //          and are familar with how lighting works in games.
    public static void main(String[] args) {
        // Create up the app, it kicks off the whole process...
        if (!App.get().runApp(new World(), new Renderer())) {
            throw new Error("Could not create the app :(");
        }
    }
}
