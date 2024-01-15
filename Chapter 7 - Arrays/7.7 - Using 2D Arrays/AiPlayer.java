public class AiPlayer extends Player {
    public static final String marker = "O";

    public Move getNextMove(Board board) {
        int numEmpty = board.numberEmpty();
        int offset = (int)(Math.random() * numEmpty);
        for (int r = 0; r < board.rows(); ++r) {
            for (int c = 0; c < board.columns(); ++c) {
                if (board.isEmpty(r, c)) {
                    if (offset == 0) {
                        return new Move(r, c, marker);
                    }
                    --offset;
                }
            }
        }
        return null;
    }
}
