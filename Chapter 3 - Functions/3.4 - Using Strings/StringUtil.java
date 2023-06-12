public class StringUtil {
    public static String firstHalf(String input) {
        // Returns a string containing the first half of 'input'
        // In the case of an odd number of characters, the extra goes in the second half (excluded here)
        //  Example: "0123456789" -> "01234"
        // 
        // Requirements...
        //  - Use a loop to build the return string.

        return input;
    }

    public static String beforeSpace(String input) {
        // Returns a string containing the portion of the string BEFORE the space
        // In the case of no space, the full string should be returned
        //  Example: "abcd ef" -> "abcd"
        // 
        // Requirements...
        //  - Use the string function substring.

        return input;
    }

    public static String afterSpace(String input) {
        // Returns a string containing the portion of the string AFTER the space
        // In the case of no space, an empty string should be returned
        //  Example: "abcd ef" -> "ef"
        // 
        // Requirements...
        //  - Use the string function substring.

        return input;
    }

    public static String swapAtSpace(String input) {
        // Returns a string that swaps the section before and after the space
        //  Example: "abcd ef" -> "ef abcd"
        // 
        // Requirements...
        //  - The other functions you created for this.

        return input;
    }

    public static char firstNonRepeatedChar(String input) {
        // Returns the first character that is not repeated later in the string (looking left to right)
        //  Example: "abcabcdef" -> 'd'

        return input.charAt(0);
    }
}
