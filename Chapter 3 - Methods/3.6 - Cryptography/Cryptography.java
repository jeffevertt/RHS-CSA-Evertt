import java.util.Scanner;

public class Cryptography {

    // a constant that represents the entire alphabet
    public static final String ALPHABET = "abcdefghijklmnopqrstuvwxyz";

    public static String createCypher(int n) {
        /*
         * --------------
         * createCypher 
         * --------------
         * n : int
         *  Specifies the seed for our Caesar cypher. We assume that 0 <= n < 26. 
         * 
         * returns String
         *  Return the Caesar cypher we create by "shifting" the alphabet over n times to the right.
         * --------------
         * Examples:
         *  n = 1
         *  >> zabc...vwxy (alphabet shifted over to the right 1 time)
         * 
         *  n = 25
         *  >> bcde...xyza (alphabet shifted over to the right 25 times)
         */

        // your code here:
        return "";
    }

    public static char encryptChar(String cypher, char c) {
        /*
         * --------------
         * encryptChar 
         * --------------
         * cypher : String
         *  Our cypher, a string with 26 lower-case characters that shuffle the order of the alphabet.
         * c : char
         *  The character we want to encrypt. We assume that c is lower-case.
         * 
         * returns char
         *  Returns the encrypted character using our cypher.
         * --------------
         * Example:
         *  c = a
         *  cypher = bcde...xyza
         *  >> b
         */
        
        // your code here:
        return ' ';
    }

    public static String encryptString(String cypher, String input) {
        /*
         * --------------
         * encryptString 
         * --------------
         * cypher : String
         *  Our cypher, a string with 26 lower-case characters that shuffle the order of the alphabet.
         * input : String
         *  The message that we want to encrypt.
         * 
         * returns String
         *  Returns the encrypted message using our cypher.
         * --------------
         */

        // your code here:
        return "";
    }

    public static char decryptChar(String cypher, char c) {
        /*
         * --------------
         * decryptChar 
         * --------------
         * cypher : String
         *  Our cypher, a string with 26 lower-case characters that shuffle the order of the alphabet.
         * c : char
         *  The character we want to decrypt. We assume that c is lower-case.
         * 
         * returns char
         *  Returns the decrypted character using our cypher.
         * --------------
         * Example:
         *  c = a
         *  cypher = bcde...xyza
         *  >> z
         */
        
        // your code here:
        return ' ';
    }

    public static String decryptString(String cypher, String input) {
        /*
         * --------------
         * decryptString 
         * --------------
         * cypher : String
         *  Our cypher, a string with 26 lower-case characters that shuffle the order of the alphabet.
         * input : String
         *  The message that we want to decrypt.
         * 
         * returns String
         *  Returns the decrypted message using our cypher.
         * --------------
         */
        
        // your code here:
        return "";
    }


    // ------------------------------------------------------------------------
    //  YOU DO NOT NEED TO EDIT, READ, OR WORRY ABOUT THE REST OF THE CODE!!!
    //
    // You are completely welcome to take a look! You will probably 
    // be pleased to see that you can understand a good amount of it!
    // But really, the heart of this entire program is the code that you
    // will write above :)
    // ------------------------------------------------------------------------

    public static void main(String[] args) {
        Scanner console = new Scanner(System.in);

        printWelcomeMessage();
        interactiveProgramConsole(console);

        console.close();
    }

    public static void interactiveProgramConsole(Scanner console) {
        System.out.println("Please type your selection below:");
        String input = console.nextLine().trim();
        while (true) {
            if (input.equals("1") || input.equals("e") || input.equals("encrypt")) {
                String cypher = interactiveCreateCypher(console);
                interactiveEncrypt(console, cypher);
                break;
            } else if (input.equals("2") || input.equals("d") || input.equals("decrypt")) {
                String cypher = interactiveCreateCypher(console);
                interactiveDecrypt(console, cypher);
                break;
            } else {
                System.out.println("Your selection is invalid. Please re-type your selection below:");
                input = console.nextLine().trim();
            }
        } 
    }

    public static String interactiveCreateCypher(Scanner console) {
        System.out.println("In order to get started, we first need to create a cypher!");
        System.out.println("Please input an integer number between 0 and 26 in order to create your cypher:");
        int n = console.nextInt();
        console.nextLine(); // nextInt doesn't read newline character
        while (n < 0 || n >= 26) {
            System.out.println("Your chosen number " + n + " is out of bounds. Please re-type a number between 0 and 26.");
            n = console.nextInt();
            console.nextLine(); // nextInt doesn't read newline character
        }
        String cypher = createCypher(n);
        System.out.println("Cypher created!");
        System.out.println();
        return cypher;
    }

    public static void interactiveDecrypt(Scanner console, String cypher) {
        System.out.println("Let's find out what the secret message says!");
        System.out.println("Please type your message below:");
        String input = console.nextLine().trim();
        while (!(input.equals("q") || input.equals("quit") || input.equals(""))) {
            String decrypted = decryptString(cypher, input);
            System.out.println("Here is your decrypted message:");
            System.out.println(decrypted);
            System.out.println();
            System.out.println("Let's decrypt another message!");
            System.out.println("Type in your message below; type q, quit, or press the Enter key to exit.");
            input = console.nextLine().trim();
        }
    }

    public static void interactiveEncrypt(Scanner console, String cypher) {
        System.out.println("Let's send your message in secret!");
        System.out.println("Please type your message below:");
        String input = console.nextLine().trim();
        while (!(input.equals("q") || input.equals("quit") || input.equals(""))) {
            String encrypted = encryptString(cypher, input);
            System.out.println("Here is your encrypted message:");
            System.out.println(encrypted);
            System.out.println();
            System.out.println("Let's encrypt another message!");
            System.out.println("Type in your message below; type q, quit, or press the Enter key to exit.");
            input = console.nextLine().trim();
        }
    }

    public static void printWelcomeMessage() {
        System.out.println("----------------------------------------------------------");
        System.out.println("|\t\tWELCOME TO OUR CYPHER PROGRAM!\t\t|");
        System.out.println("----------------------------------------------------------");
        System.out.println();
        System.out.println("Please choose one of the options below to get started:");
        System.out.println("1. Encrypt a message (type 1, e, or encrypt).");
        System.out.println("2. Decrypt a message (type 2, d, or decrypt).");    
    }
}
