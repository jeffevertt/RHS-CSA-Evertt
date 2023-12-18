public class AppMain {
    /* You are writing code that guesses the number I am thinking of
     *  (in as few guesses as possible). 
     * 
     * Here are the rules of my game.
     *   - I'll think of a number between 1 and 1000 (inclusive)
     *   - Your code guesses a number and I'll tell you (in a return code)...
     *        0 : You are correct (you got it!)
     *       -1 : My number is smaller than your guess
     *        1 : My number is larger than your guess
     *   - You can guess as many times as you'd like, but I am going
     *       to keep count of how many guesses it took you (and I'm judgmental).
     * 
     * You will write your code in the NumberGuesser class that has already been
     *  stubbed out for you. I'd like you to make TWO implementations...
     *    - guessNumberBasic() should be a very simple linear/sequential
     *        guesser (i.e. it should guess 1, 2, 3, ... till it finds it).
     *        This method should not attempt to minimize guesses, it is purely 
     *        a linear / sequential guesser. Keep it simple.
     *    - guessNumberFast() should try to guess the number with the minimum
     *        number of guesses. This is the method I will judge you on. 
     *        Unlike the sequential guesser, this method should attempt to 
     *        minimize the number of guesses it takes to guess the answer.
     * 
     *  Other rules...
     *    - You may not modify NumberGuesserBase. All your code must go in the
     *        NumberGuesser.java. No modifications in any other files.
     *    - You may add instance variables to your class, helper methods, etc.
     */
    public static void main(String[] args) {
        NumberGuesser game = new NumberGuesser();
        game.runGames(500);
    }
}