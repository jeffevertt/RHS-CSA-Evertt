// Lesson 7.2 - Using Arrays

public class AppMain {
    // In this lab, you will need to complete each of the static methods in this class.
    //  Follow the comments/specifications provided above each methods.
    public static void main(String[] args) {
        // Add your own test cases here to prove to yourself that your implemenations are correct.
        System.out.println("--------- createArrayOfLengthTen ---------");
        Utils.printArray( createArrayOfLengthTen() );

        System.out.println("\n--------- setAllElements ---------");
        int[] listA = new int[] { 1, 2, 3, 4 };
        Utils.printArray( setAllElements(listA, 5) );

        System.out.println("\n--------- setElementToZero ---------");
        // TODO

        System.out.println("\n--------- createAbcArray ---------");
        // TODO
    }

    // Creates and returns an array with 10 integer elements in it.
    //  All elements should all be set to a value of -1.
    public static int[] createArrayOfLengthTen() {
        // TODO
    }

    // Set all elements in the provided integer array to the provided value.
    //  Example: 
    //      int[] list = new int[] { 1, 2, 3, 4 };
    //      Utils.print( setAllElements(list, 5) ); -> { 5, 5, 5, 5 }
    public static int[] setAllElements(int[] list, int toValue) {
        // TODO
        return list;
    }

    // Set the specified element in the provided array to zero.
    //  If the provided element index specified an invaid index, then the method
    //      does nothing.
    //  Example: 
    //      int[] list = new int[] { 1, 2, 3, 4 };
    //      setElementToZero(list, 1);
    //      Utils.print(list); -> { 1, 0, 3, 4 }
    public static void setElementToZero(int[] list, int elemIdx) {
        // TODO
    }    

    // Creates an array of Strings of the specificed length, defaulting all
    //  elements to "abc"
    //  Example:
    //      createAbcArray(3) -> { "abc", "abc", "abc" }
    public static String[] createAbcArray(int elemCount) {
        // TODO
        return new String[0];
    }
}
