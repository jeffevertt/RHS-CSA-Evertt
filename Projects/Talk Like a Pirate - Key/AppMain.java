
// TODO: Add instructions

/* In this lab, you will writing an app to converting text (stored in a file) from English to Pirate. 
 *   
 * There are some specific rules that your code needs to follow...
 *  1. Replace words as follows. 
 *      hello -> ahoy 
 *      hi -> yo-ho-ho
 *      my -> me
 *      friend -> bucko
 *      sir -> matey
 *      miss -> proud beauty
 *      stranger -> scurvy dog
 *      officer -> foul blaggart
 *      where -> whar; is -> be
 *      the ->th'
 *      you -> ye
 *      old -> barnacle covered
 *      happy -> grog-filled
 *      nearby -> broadside
 *      restroom -> head
 *      restaurant -> galley
 *      hotel -> fleabag inn
 *  2. If a word begins with a capital letter, then when you translate it, it should also begin with a 
 *      capital letter. Ignore the case of the rest of the word. 
 *      Examples...
 *          translate "Where" / "WHERE" / "WhErE" / "WhERE" / etc. as "Whar", 
 *              but translate "where" / "wHERE" / "wHeRe" / "wHere" / etc. as "whar". 
 *          You should only output "Whar" or "whar" as a translation for any form of "where",
 *              none of the other letters in "Whar" or "whar" should ever be capitalized apart from 
 *              possibly the first. 
 * 3. Pirates have a habit of saying "Arrr" a lot. Get your translator to randomly insert "Arrr." between 
 *      sentences. Anytime you encounter the end of a sentence (as denoted by a period, question mark, or 
 *      exclamation point), decide with a 50/50 chance whether to insert an "Arrr."
 */
public class AppMain {
    public static void main(String[] args) {
        // ...
    }

    public static String fileToPirateStr(String filename) {
        return "test";
    }
}
