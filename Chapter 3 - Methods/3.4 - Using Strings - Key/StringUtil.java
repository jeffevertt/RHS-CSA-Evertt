public class StringUtil {
    public static String firstHalf(String input) {
        // Returns a string containing the first half of 'input'
        // In the case of an odd number of characters, the extra goes in the second half (excluded here)
        //  Example: "0123456789" -> "01234"
        // 
        // Requirements...
        //  - Use a loop to build the return string.

        return input.substring(0, input.length() / 2);
    }

    public static String beforeSpace(String input) {
        // Returns a string containing the portion of the string BEFORE the space
        // In the case of no space, the full string should be returned
        //  Example: "abcd ef" -> "abcd"
        // 
        // Requirements...
        //  - Use the string function substring.

        return input.substring(0, input.indexOf(" "));
    }

    public static String afterSpace(String input) {
        // Returns a string containing the portion of the string AFTER the space
        // In the case of no space, an empty string should be returned
        //  Example: "abcd ef" -> "ef"
        // 
        // Requirements...
        //  - Use the string function substring.

        return input.substring(input.indexOf(" ") + 1, input.length());
    }

    public static String swapAtSpace(String input) {
        // Returns a string that swaps the section before and after the space
        //  Example: "abcd ef" -> "ef abcd"
        // 
        // Requirements...
        //  - The other functions you created for this.

        String before = beforeSpace(input);
        String after = afterSpace(input);

        return after + " " + before;
    }

    public static char firstNonRepeatedChar(String input) {
        // Returns the first character that is not repeated later in the string (looking left to right)
        //  Example: "abcabcdef" -> 'd'

        for (int i = 0; i < input.length(); ++i) {
            char ch = input.charAt(i);
            int count = 0;
            for (int j = 0; j < input.length(); ++j) {
                if (input.charAt(j) == ch) {
                    count++;
                }
            }
            if (count == 1) {
                return ch;
            }
        }

        // None found
        return '?';
    }
}
