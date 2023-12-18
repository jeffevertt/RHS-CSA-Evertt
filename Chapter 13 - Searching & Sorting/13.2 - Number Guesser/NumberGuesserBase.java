
/******************************************/
/******************************************/
/******** DO NOT MODIFY THIS CLASS  *******/
/******************************************/
/******************************************/

import java.text.DecimalFormat;

public class NumberGuesserBase {
    // Consts (numbers are inclusive)
    public static final int MIN_NUMBER = 1;
    public static final int MAX_NUMBER = 1000;
    private static final int MAX_GUESS_COUNT = 10000;

    // Stats
    private class Stats {
        private int gameCount;
        private int guessCount;
        public Stats() {
            gameCount = 0;
            guessCount = 0;
        }
        public int getGameCount() {
            return gameCount;
        }
        public void incGameCount() {
            gameCount++;
        }
        public void incGuessCount(int count) {
            guessCount += count;
        }
        public double calcAvgGuessesPerGame() {
            if (gameCount <= 0) {
                return -1.0;
            }
            return (double)guessCount / gameCount;
        }
    }

    // Data
    private Stats statsBasic;
    private Stats statsFast;
    private int currentNumber;
    private int guessCountThisGame;

    // Constructor(s)
    public NumberGuesserBase() {
        reset();
    }

    // Methods
    private void reset() {
        statsBasic = new Stats();
        statsFast = new Stats();
        currentNumber = -1;
        guessCountThisGame = 0;
    }
    private int[] generateNumbers(int gameCount) {
        int[] numbers = new int[gameCount];
        for (int i = 0; i < numbers.length; i++) {
            numbers[i] = (int)(Math.random() * (MAX_NUMBER - MIN_NUMBER + 1)) + MIN_NUMBER;
        }
        return numbers;
    }
    public void runGames(int gameCount) {
        // Reset (just in case)
        reset();

        // Print header
        System.out.println("\n********************** NUMBER GUESSER **********************");
        DecimalFormat formatter = new DecimalFormat("0000.00");

        // Run the games (basic guesser)
        if (!runGame(true, gameCount)) {
            return;
        }

        // Print stats (basic)
        String strGuessesPerGameBasic = formatter.format(statsBasic.calcAvgGuessesPerGame()).replaceAll("\\G0", " ");
        System.out.println("** (BASIC) Guesses per Game (avg): " + strGuessesPerGameBasic + " (" + statsBasic.getGameCount() + " in games) **");

        // Run the games (fast guesser)
        if (!runGame(false, gameCount)) {
            return;
        }

        // Print stats (fast)
        String strGuessesPerGameFast = formatter.format(statsFast.calcAvgGuessesPerGame()).replaceAll("\\G0", " ");
        System.out.println("**  (FAST) Guesses per Game (avg): " + strGuessesPerGameFast + " (" + statsFast.getGameCount() + " in games) **");
        System.out.println("************************************************************\n");
    }
    private boolean runGame(boolean basic, int gameCount) {
        int[] numbers = generateNumbers(gameCount);
        Stats stats = basic ? statsBasic : statsFast;
        for (int i = 0; i < numbers.length; i++) {
            // Save off the current number (for the guess method)
            currentNumber = numbers[i];

            // Guard against infinite loops (in cooperation with guess(...))
            guessCountThisGame = 0;
            int guessedNumber = basic ? guessNumberBasic() : guessNumberFast();
            stats.incGuessCount(guessCountThisGame);
            boolean numberFound = (guessedNumber == currentNumber);

            // Deal with not found
            if (!numberFound) {
                System.out.println(" !!! Your " + (basic ? "guessNumberBasic" : "guessNumberFast") + " method failed to guess it in " + MAX_GUESS_COUNT + " guesses !!!\n");
                return false;
            }

            // Update stats, then on to next game
            stats.incGameCount();
        }

        return true;
    }
    public int guess(int number) {
        guessCountThisGame++;
        if (currentNumber == number) {
            return 0;
        }

        // Guard against inf loop (fake count, if too many guesses)
        if (guessCountThisGame >= MAX_GUESS_COUNT) {
            return 0;
        }

        // Return -1 or 1, depending on which side the guess is on
        if (currentNumber < number) {
            return -1;
        }
        return 1;
    }

    // Guesser methods (to be implemented by child class)
    public int guessNumberBasic() {
        return -1;
    }
    public int guessNumberFast() {
        return -1;
    }
}
