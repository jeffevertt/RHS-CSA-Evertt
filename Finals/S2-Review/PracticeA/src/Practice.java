public class Practice {
    /* strPatternA - prints out a pattern of dashes, driven by the
        givin input parameters. Use the examples to understand the pattern.
          strPatternA(3, 2)
             "--
              -----
              --------"
          strPatternA(2, 5)
             "-----
              -------"
          strPatternA(4, 1)
             "-
              -----
              ---------
              -------------"
     */
    public String strPatternA(int x, int y) {
        // TODO
        return "TODO";
    }

    /* strPatternB - prints a string multiple times, separated by commas. 
          strPatternB("abc", 3) -> "abc, abc, abc"
          strPatternB("x", 4) -> "x, x, x, x"
          strPatternB("ttfn", 1) -> "ttfn"
     */
    public String strPatternB(String str, int n) {
        // TODO
        return "TODO";
    }

    /* percToLetterGrade - Converts from a [0,100] to an N,D,C,B,A letter grade
          percToLetterGrade(89.9) -> "B"
          percToLetterGrade(61.8) -> "D"
          percToLetterGrade(55.5) -> "N"
     */
    public String percToLetterGrade(double perc) {
        // TODO
        return "TODO";
    }

    /* strToList - Converts a list of integers into an array. The string is 
        deliminated by a character given as a parameter. It returns null
        in an invalid chacter is found in the string. Note that the helper
        method tryParseInt is provided for you below.
          strToList("1,2,3,4", ',') -> { 1, 2, 3, 4 }
          strToList("5; 2", ';') -> { 5, 2 }
          strToList("5; 2", ',') -> null
     */
    public int[] strToList(String strList, char delim) {
        // TODO
        return null;
    }

    // Helper method - converts an integer contained in a string to an Integer
    //  type. If an integer is not contained in the string, it will return null.
    public Integer tryParseInt(String value) {
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            return null;
        }
    }
}
