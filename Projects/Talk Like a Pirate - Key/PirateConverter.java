
/* There are some specific rules that your code needs to follow...
 *  1. Replace words as follows. 
 *      hello -> ahoy 
 *      my -> me
 *      friend -> bucko
 *      sir -> matey
 *      stranger -> scurvy dog
 *      where -> whar
 *      is -> be
 *      the -> th'
 *      you -> ye
 *      old -> barnacle covered
 *  2. If a word begins with a capital letter, then when you translate it, it should also begin with a 
 *      capital letter. Ignore the case of the rest of the word. 
 *      Examples...
 *          translate "Where" / "WHERE" / "WhErE" / "WhERE" / etc. as "Whar", 
 *              but translate "where" / "wHERE" / "wHeRe" / "wHere" / etc. as "whar". 
 *          You should only output "Whar" or "whar" as a translation for any form of "where",
 *              none of the other letters in "Whar" or "whar" should ever be capitalized apart from 
 *              possibly the first. 
 * 3. Pirates have a habit of saying "Arrr" a lot. Start your pirate text with "Arrr! ".
 */

import java.io.File;
import java.util.ArrayList;
import java.util.Scanner;

public class PirateConverter {
    private ArrayList<StrPair> dictionary = new ArrayList<StrPair>();
    private ArrayList<String> delims = new ArrayList<String>();
    private String englishText, pirateText;

    public String getEnglishText() {
        return englishText;
    }
    public String getPirateText() {
        return pirateText;
    }

    public boolean fileToPirateTalk(String filename) {
        // build our translation dictionary
        buildPirateDictionary();

        // read in the file & translate
        englishText = fileToPirateStr(filename);
        if (englishText == null) {
            return false;
        }

        // translate & print
        pirateText = toPirate(englishText);

        return true;
    }

    private String fileToPirateStr(String filename) {
        String pirateText = "";

        // Read the file in
        try {
            File file = new File(filename);
            Scanner scan = new Scanner(file);
            while (scan.hasNext()) {
                if (pirateText.length() > 0) {
                    pirateText += "\n";
                }
                String line = scan.nextLine();
                pirateText += line;
            }
            scan.close();
        }
        catch (Exception ex) {
            System.out.println("Error: " + ex);
            return null;
        }

        return pirateText;
    }

    private void buildPirateDictionary() {
        // dictionary
        dictionary.clear();
        dictionary.add(new StrPair("hello", "ahoy"));
        dictionary.add(new StrPair("my", "me"));
        dictionary.add(new StrPair("friend", "bucko"));
        dictionary.add(new StrPair("sir", "matey"));
        dictionary.add(new StrPair("stranger", "scurvy dog"));
        dictionary.add(new StrPair("where", "whar"));
        dictionary.add(new StrPair("is", "be"));
        dictionary.add(new StrPair("the", "th'"));
        dictionary.add(new StrPair("you", "ye"));
        dictionary.add(new StrPair("old", "barnacle covered"));

        // delimiters
        delims.clear();
        delims.add(" ");
        delims.add(".");
        delims.add(",");
        delims.add("!");
        delims.add("?");
        delims.add("\n");
    }

    private String wordToPirate(String word) {
        for (int i = 0; i < dictionary.size(); i++) {
            if (word.equals(dictionary.get(i).firstWordCapitalized())) {
                return dictionary.get(i).secondWordCapitalized();
            }
            else if (word.equals(dictionary.get(i).word1)) {
                return dictionary.get(i).word2;
            }
        }
        return word;
    }

    private String toPirate(String english) {
        String pirate = "Arrr! ";

        // go word by word, converting
        int idx = 0;
        while (idx < english.length()) {
            // find the end of the word
            int idxEnd = idx + 1;
            while (idxEnd < english.length() && 
                   !delims.contains(english.substring(idxEnd, idxEnd + 1))) {
                idxEnd++;
            }

            // here's our word, convert it
            String englishWord = english.substring(idx, idxEnd);
            String pirateWord = wordToPirate(englishWord);
            pirate += pirateWord;

            // skip the delimeter
            while (idxEnd < english.length() &&
                   delims.contains(english.substring(idxEnd, idxEnd + 1))) {
                pirate += english.substring(idxEnd, idxEnd + 1);
                idxEnd++;
            }

            // next
            idx = idxEnd;
        }

        return pirate;
    }
}
