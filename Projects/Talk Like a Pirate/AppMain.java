
/* In this lab, you will writing an app to converting text (stored in a file) from English to Pirate. 
 *  The app should print the text contained in file "english-text.txt" to the console, after it has 
 *      been translated to pirate. Note that your program must actually read the text from the file.
 *      Do not just copy and paste it into the code. 
 * 
 * A big part of this lab is writing well structured code. Avoid copy/paste coding. Consider what 
 *  needs to happen and how to best write the code to do that. 
 * 
 * See PirateConverter.java for details on how to convert to pirate talk.
 */

public class AppMain {
    private static final String ENGLISH_TEXT_FILENAME = "english-text.txt";

    public static void main(String[] args) {
        // Do not modify the below code. Your class should adhere to the methods
        //  and structure provided here.
        PirateConverter pirateConverter = new PirateConverter();
        if (pirateConverter.fileToPirateTalk(ENGLISH_TEXT_FILENAME)) {
            System.out.println("-------------- ENGLISH --------------");
            System.out.println(pirateConverter.getEnglishText());
            System.out.println("-------------- PIRATE! --------------");
            System.out.println(pirateConverter.getPirateText());
            System.out.println("-------------------------------------");
        }
        else {
            System.out.println("Error! Failed to parse text file: " + ENGLISH_TEXT_FILENAME);
        }
    }
}
