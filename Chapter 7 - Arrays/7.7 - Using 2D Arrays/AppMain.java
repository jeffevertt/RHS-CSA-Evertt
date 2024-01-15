public class AppMain {
    // You are writing an interactive Tic-Tac-Toe game.
    //  If you have never played it before, do a google search and try it out.
    // In this game, the user plays against the computer (this program).
    // 
    // The class declarations are given to you. You will need to complete the
    //  Board class. Most of its methods have signatures & comments that spec
    //  out their behavior. You must implement those methods. To get started,
    //  I suggest doing the following.
    //    - If you don't know Tic-Tac-Toe, watch a YouTube video on it.
    //    - Take a look over the main method. Try to understand...
    //        - The object abstractions: Board, Move, & HumanPlayer/AiPlayer
    //    - Look over the Board class and start implementing each method.
    //        - Use the comments for each method to understand the their purpose.
    //        - I'd recommend starting at the top of the file and working 
    //          your way down. Once you implement renderToString you should be
    //          able to start testing (running the program, looking at the output).
    // 
    // Example output...
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
        // Setup
        Board board = new Board();
        HumanPlayer human = new HumanPlayer();
        AiPlayer ai = new AiPlayer();

        // Print the empty board
        System.out.println();
        System.out.println(board.renderToString());

        // Loop till the game is done
        while ((board.calcWinner() == 0) && (board.numberEmpty() != 0)) {
            // Player move
            Move move = human.getNextMove(board);
            board.placeMark(move);
            System.out.println();

            // Ai, if the game isn't done
            if ((board.calcWinner() == 0) && (board.numberEmpty() != 0)) {
                move = ai.getNextMove(board);
                board.placeMark(move);
                System.out.println("AI's move: " + move + "\n");
            }

            // Print the board
            System.out.println(board.renderToString());
        }

        // Report the winner...
        if (board.calcWinner() == 1) {
            System.out.println("You win!!!");
        } else if (board.calcWinner() == 2) {
            System.out.println("AI wins - better luck next time.");
        } else {
            System.out.println("It's a draw...no winners");
        }
    }
}
