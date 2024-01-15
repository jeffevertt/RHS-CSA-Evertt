import java.util.Scanner;

public class HumanPlayer extends Player {
    public static final String marker = "X";
    Scanner scanner = new Scanner(System.in);
    
    public Move getNextMove(Board board) {
        // User input
        Move move = null;
        while (move == null) {
            System.out.print("Your move (e.g. '0,2')? ");
            String input = scanner.nextLine();  // Read user input
            move = Move.StringToMove(input, marker);
            if ((move == null) || !board.isEmpty(move.r, move.c)) {
                move = null;
            }
        }
        return move;   
    }
}
