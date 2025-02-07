import framework.GameBase;
import framework.GameBoard;
import framework.Vec2;

import java.awt.Graphics2D;

public class GameOfLife extends GameBase {
    // You are implementing Conway's game of life using the provided framework.
    //  - The only changes you'll need to make are in this file & are marked with TODO
    //  - First, go do a bit of reading on it: https://en.wikipedia.org/wiki/Conway%27s_Game_of_Life
    //  - You can find the rules you need to implement on that same page. 

    public static void main(String[] args) {
        // Create up the game, it kicks off the whole process...
        GameOfLife gameOfLife = new GameOfLife();
        if (!gameOfLife.create()) {
            throw new Error("Could not create the game :(");
        }
    }

    // data
    //  TODO: add your data (state of all the cells, maybe)

    // methods
    public void onLevelSetup() {
        super.onLevelSetup();

        // print out some useful game configuration information
        System.out.println("\nGrid size is " + GameBoard.CELL_COUNT_XY + " x " + GameBoard.CELL_COUNT_XY);

        // TODO: this is called once at the start, it is a good place to initialize your data
    }

    public void onMouseDrag(Vec2 start, Vec2 end) {
        super.onMouseDrag(start, end);

        // remove this line, it's just showing you that you get an event call for this
        System.out.println("mouseDrag: " + start + " to " + end);

        // TODO: called when the mouse is dragged (with button down). you should fill in the cells
        //  covered by the path (from start to end). you are probably writing into your data here
    }
    
    public void onLevelUpdate(double deltaTime, boolean firstUpdate) {
        super.onLevelUpdate(deltaTime, firstUpdate);

        // TODO: here is where the rules go. update your cell data based on the rules.
        //  (hint) you are going to need a second 2d array to write into, then swap them after.
        //           you shouldn't update your cell data in place, update a second 2d array just 
        //           like the first, then swap which one is the current state of the world/cells.
    }
    
    public void draw(Graphics2D g) {
        super.draw(g);

        // example drawing (remove these lines, they are intended to show you how to draw a cell)
        drawCell(g, 10, 3);
        drawCell(g, 10, 4);
        drawCell(g, 11, 4);

        // TODO: loop through your cells, drawing those that should be draw.
        //  Use "drawCells(...)" implemented in the parent class to draw to the screen.
    }
}
