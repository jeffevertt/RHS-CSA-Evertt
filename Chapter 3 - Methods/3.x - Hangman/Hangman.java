import java.util.Scanner;

public class Hangman {

	private static String[] words = {"hangman", "banana", "computer", "dog", "rain", "water", "terminal", "midnight", "redmond", "science", "cat" };
	private static String word;
	private static String hiddenLetters;
	private static int incorrectCount = 0;

	public static void main(String[] args) {
		getWord();
		getGuesses();
	}

	public static void getGuesses() {
		Scanner scan = new Scanner(System.in);

		while (incorrectCount < 7 && hiddenLetters.contains("*")) {
			System.out.println("Guess a letter in the word:");
			System.out.println(hiddenLetters);
			String guess = scan.next();
			
    		while (guess.matches(".*[^a-z].*")) {
        		System.out.println("Please enter letters only, try again");
        		guess = scan.next();
    		}
			while (guess.length() > 1) {
        		System.out.println("Please enter a single letter only, try again");
        		guess = scan.next();
    		}
			hang(guess);
		}
		scan.close();
	}

	public static void hang(String guess) {
		String updatedLetters = "";
		for (int i = 0; i < word.length(); i++) {
			if (word.charAt(i) == guess.charAt(0)) {
				updatedLetters += guess.charAt(0);
			} else if (hiddenLetters.charAt(i) != '*') {
				updatedLetters += word.charAt(i);
			} else {
				updatedLetters += "*";
			}
		}

		if (hiddenLetters.equals(updatedLetters)) {
			incorrectCount++;
			printHangman();
		} else {
			hiddenLetters = updatedLetters;
		}
		if (hiddenLetters.equals(word)) {
			System.out.println("Correct! You win! The word was " + word);
		}
	}

	// Part 1
	// Prints out the game image to the terminal based on how many incorrect guesses have been made.
	// This game has 7 guesses. Be creative!
	public static void printHangman() {
		if (incorrectCount == 1) {
			System.out.println("Wrong guess, try again");
			// TODO: write code to print image when the player makes the first wrong guess
		}
		if (incorrectCount == 2) {
			System.out.println("Wrong guess, try again");
			// TODO: write code to print image when the player makes the second wrong guess
		}
		// TODO: write more code to print the images based on 3-7 incorrect guesses
	}

	// Part 2: 
	// Modify this function to get the word to guess from user input in the terminal
	public static void getWord(){
		word = words[(int) (Math.random() * words.length)];
		hiddenLetters = new String(new char[word.length()]).replace("\0", "*");
	}
}