/* The Board stores the state of the Tic-Tac-Toe board */
public class Board {
    public static final String empty = " ";

    // 3x3 array of single character Strings. This stores the current
    //  state of the board. After construction, it is guarenteed to
    //  contain nine String elements. 
    // Each element stores one of the following...
    //    Board.empty, HumanPlayer.marker, or AiPlayer.marker
    private String[][] board; 

    public Board() {
        reset();
    }

    // Reset board so that each element is a an empty string (use Board.empty)
    //  postcondition: board is a 3x3 array. all elements are Board.empty.
    public void reset() {
        // TODO
    }

    // Returns the number of rows in the board
    public int rows() {
        // TODO (don't hard code this, use board)
        return 0;
    }

    // Returns the number of columns in the board
    public int columns() {
        // TODO (don't hard code this, use board)
        return 0;
    }

    // Returns true if the specified row/column space is empty 
    //  (does not already store an X or O)
    //  You can assume r & c are valid values.
    public boolean isEmpty(int r, int c) {
        // TODO
        return true;
    }

    // Returns the String marker at the specified row/column (e.g. "X")
    //  You can assume r & c are valid values.
    public String getMarkerAt(int r, int c) {
        // TODO
        return null;
    }

    // Creates and returns a String that can be printed display the board.
    //  Example return value: " X |   | O \n-----------\n O | X |   \n-----------\n   |   | O "
    public String renderToString() {
        // TODO
        return " X |   | O \n-----------\n O | X |   \n-----------\n   |   | O ";
    }

    // Returns the number of empty spaces on the board. 
    //  An empty board (at the start of the game) would return 9.
    //  After both players have places one mark, it would return 7.
    public int numberEmpty() {
        // TODO (don't store this in a variable, actually count it here)
        return 9;
    }

    // Update the board with the specified move applied.
    //  You can assume it is a valid move.
    public void placeMark(Move move) {
        // TODO
    }

    // Determines if there is a winner, based on the current board state.
    //  Note that in the case that no one has won, it returns 0.
    //  Returns: 0:none, 1:human(HumanPlayer.marker), 2:AI(AiPlayer.marker)
    public int calcWinner() {
        // TODO
        return 2;
    }
}
