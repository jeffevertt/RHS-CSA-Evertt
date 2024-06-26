public class Practice {
    /* Write a recurisve version of the method pow, which computes the value of
     *  b to the power of e. Preconditions: b >= 1 && e >= 1
     *  note: Do not modify pow, but do implement powR to have the same behavior,
     *        but impliment it with recursion, instead of a loop.
     * Example...
     *  input: powR(3, 2), output: 9
     *  input: powR(5, 3), output: 125
     */ 
    public int pow(int b, int e) {
        int result = 1;
        for (int i = 0; i < e; i++) {
            result *= b;
        }
        return result;
    }
    public int powR(int b, int e) {
        // TODO: Replace with recursive implementation
        return -1;
    }


    /* Write a recurisve version of the method createString, which returns a string of asterisks
     *  surrounded by a symmetric set of brackets. Preconditions: n >= 0
     *  note: Do not modify createString, but do implement createStringR to have the same behavior,
     *        but impliment it with recursion, instead of a loop.
     * Example...
     *  input: createStringR(3), return value: [[[*]]]
     *  input: createStringR(5), return value: [[[[[*]]]]]
     *  input: createStringR(0), return value: *
     */
    public String createString(int n) {
        String prefix = "";
        String postfix = "";
        for (int i = 0; i < n; i++) {
            prefix += "[";
            postfix += "]";
        }
        return prefix + "*" + postfix;
    }
    public String createStringR(int n) {
        // TODO: Replace with recursive implementation
        return "";
    }

    
    /* Write a method called printBinary that takes an integer and returns the number
     *  in binary (base 2). Your implementation must use recursion. 
     *  Preconditions: Integer.MAX_VALUE >= n >= 0
     * Examples...
     *  input: printBinary(5), console output: 0101 (or 101)
     *  input: printBinary(201), console output: 011001001 (or 11001001)
     */ 
    public void printBinary(int n) {
        // TODO: write the method printBinary
    }
}