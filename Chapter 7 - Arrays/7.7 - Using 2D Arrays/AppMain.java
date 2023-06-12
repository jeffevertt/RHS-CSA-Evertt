public class AppMain {
    // You are writing an interactive Tic-Tac-Toe game.
    //  If you have never played it before, do a google search and try it out.
    // In this game, the user plays against the computer (this program).
    // 
    // Your program must have the following elements...
    //  - A class called Board. 
    //      - This class should be responsible for keeping the board's state.
    //      - The board's state must be stored in a 2D array.
    //      - It should be able to render its current state into a string.
    //      - It should support placeMark(Move move) that marks a space for "X" or "O".
    //      - It should support calcWinner() that returns an int with 0:none, 1:human, 2:AI
    //  - A base class called Player.
    //      - A subclass called AiPlayer which is responsible for determining the computer's moves.
    //      - A subclass called HumanPlayer which accepts input from the user.
    //      - The players should support a getNextMove(Board board) function that returns a Move class.
    //          (note that the class Move is provided for you)
    //      - The AiPlayer should just pick an random empty square when it is its turn.
    //  - The program's main function ties it all together and loops until someone wins (or it is a draw).
    //      - The human goes first and is always "X". The AI is always "O".
    //  - A function in class Move is provided for converting user input into a Move.
    // 
    //  Example:
    //      |   |   
    //   -----------
    //      |   |
    //   -----------
    //      |   |
    //
    //   Your move (e.g. '0,2')? 1,0
    //
    //   AI's move: 0,2
    //
    //      |   | O
    //   -----------
    //    X |   |
    //   -----------
    //      |   |
    //
    //   Your move (e.g. '0,2')? 1,1
    //
    //   AI's move: 0,0
    //
    //    O |   | O
    //   -----------
    //    X | X |
    //   -----------
    //      |   |
    //
    //   Your move (e.g. '0,2')? 1,2
    //
    //    O |   | O
    //   -----------
    //    X | X | X
    //   -----------
    //      |   |
    // 
    //   You win!!!

    public static void main(String[] args) {
        // TODO
    }
}
