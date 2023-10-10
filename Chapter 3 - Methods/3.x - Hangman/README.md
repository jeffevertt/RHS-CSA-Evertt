# Hangman - Chapter 3/4 Extension
Take a look at the starter code in `Hangman.java`. You will be implementing parts of the game, and adding new functionality.
The game will randomly choose a word from a predefined list. Hidden letters are marked with an asterisk (it's easier to see individual letters with an asterisk than with an underscore). Once a correct letter is guessed, that letter will be revealed everywhere it is in the word, and the remaining letters will each remain hidden with an asterisk.

Your tasks:

1. Write code that prints out the game based on how many incorrect guesses the player has made. Be creative! You do not have to draw out the typical hangman game. The game is set up to end when the player makes 7 incorrect guesses - if you want to add more, feel free to increase this number.

2. Update the method `getWord()` to get the word to guess from user input. Hint: take a look at `getGuesses()`

3. Update the game so that when the game ends (win or lose), it will ask if the player wants to play again. If the player says "yes", start a new game. If the player says "no", end the game with a "Thank you for playing!" message. Hint: consider conditionals, if statements, and a while loop.

4. Implement a method that keeps track of which letters have been guessed. Each turn, show the letters that have not been guessed and use an asterisk to show the letters that have already been guessed. For example, if a player has made three guesses: "e", "n", and "a", show: "Letters you can guess: \*bcd\*fghijklm\*opqrstuvwxyz"

5. Implement your own new feature of the game. Maybe the player has to guess a phrase or sentence. Or maybe it keeps track of score between two players for a number of games. Maybe players can choose a "hard" or "easy" mode - whatever you'd like those to be. Have fun with it!