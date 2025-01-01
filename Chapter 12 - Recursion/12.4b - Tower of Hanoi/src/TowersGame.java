// In this lab, you are writing a recursive method to solve the Tower of Hanoi problem.
//  First, go read about it: https://en.wikipedia.org/wiki/Tower_of_Hanoi
// Your code goes in TowersGame.java (this file...do not modify other files)

// TowersGame derives off of TowersGameBase. The base class implements everything except for the
//  solve logic. Your class, TowersGame, implements that logic. It must be a recursive solution.
// A working solution will show each step using ASCII animation. The majority of the animation 
//  is completed for you in the base class. You just need to call "drawFrame()" after each disk move.
// Speaking of moving disks, you do that with "moveDisk(int srcRod, int dstRod)", which moves
//  the top most disk on srcRod to dstRod (both are rod indices 0 to 2, from left to right).
//  Your recursive solution method should call "moveDisk" to move a disk and then "drawFrame" 
//  after that.

// The constructor to TowerGame takes a single parameter which specifices the number of disks
//  in the game. Note that this is done in AppMain.java and you will want to change that to test
//  that your code works for different disk counts. Also, you might want to start with 3 disks
//  as this is easier to picture mentally when you are thinking through the problem & debugging
//  your code.
//
// An example is show below for when 3 disks are specified. Here is the initial setup (all three 
//  disks on the left most rod/tower). Note that the draw code is provided for you, you don't 
//  need to write this yourself, you just call "drawFrame()"...
//      [=]      |        |     
//     [==]      |        |     
//     [===]     |        |     
//    -------  -------  ------- 
// Here is the final setup, after all moves have been made and a solution is found... 
//     |        |        [=]   
//     |        |       [==]   
//     |        |       [===]  
//   -------  -------  ------- 
// Your solution should animate properly & work for any number of disks (three or more).

// This class extends its base by implementing the solve() method. Your solution must use recursion.
//  And just to be extra clear, that method should call "moveDisk(...)" and "drawFrame()".
public class TowersGame extends TowersGameBase {
    
    public TowersGame(int numDisks) {
        super(numDisks);
    }

    public void solve() {
        // TODO
    }
}
