import framework.App;
import renderer.Renderer;

public class Main {
    public static void main(String[] args) {
        // Create up the app, it kicks off the whole process...
        if (!App.createApp(new Renderer())) {
            throw new Error("Could not create the game :(");
        }
    }
}
