import framework.App;
import renderer.Renderer;

public class Main {
    // In this lab you will learn about how a raytraced rendering.
    //  - Do some research (there are some great youtube videos that introduce the topic).
    //  - Look through the code to get a feel for how it works.
    //  - Think up some kind of extension for this renderer, maybe it's a new
    //      type of object in the scene or maybe it's some new renderer feature.
    //      Implementing it will earn any offered extra credit. You are welcome
    //      to check with the teacher to verify that your idea will qualify.

    public static void main(String[] args) {
        // Create up the app, it kicks off the whole process...
        if (!App.createApp(new Renderer())) {
            throw new Error("Could not create the app :(");
        }
    }
}
