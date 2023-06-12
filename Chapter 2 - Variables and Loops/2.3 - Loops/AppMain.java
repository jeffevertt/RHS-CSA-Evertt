public class AppMain {
    public static void main(String[] args) {
        loop1();
        loop2();
        loop3();
        loop4();
        loop5();
        loop6();
    }

    public static void loop1() {
        /* Update the print code to output the following sequence...
         *      2 7 12 17 22
         */
        for(int i = 0; i < 5; i++) {
	        System.out.print( i + " " );
        }
        System.out.println();
    }

    public static void loop2() {
        /* Update the print code to output the following sequence...
         *      4 7 10 13 16
         */
        for(int i = 0; i < 5; i++) {
	        System.out.print( i + " " );
        }
        System.out.println();
    }

    public static void loop3() {
        /* Update the code to output the following sequence...
         *      6 9 12 15 18 21
         * 
         * hint: you'll need to update the end condition.
         */
        for(int i = 0; i < 2; i++) {
	        System.out.print( i + " " );
        }
        System.out.println();
    }

    public static void loop4() {
        /* Update the code to output the following sequence...
         *      -15 -7 1 9 17 25
         */
        for(int i = 0; i < 2; i++) {
	        System.out.print( i + " " );
        }
        System.out.println();
    }

    public static void loop5() {
        /* Update the code to output the following sequence...
         *      10 9 8 7 6 5
         */
        for(int i = 0; i < 2; i++) {
	        System.out.print( i + " " );
        }
        System.out.println();
    }

    public static void loop6() {
        /* Update the code to output the following sequence...
         *      6 8 10
         * 
         * hint: you can modify all three parameters to the loop.
         */
        for(int i = 4; i < 10; i += 2) {
	        System.out.print( i + " " );
        }
        System.out.println();
    }
}
