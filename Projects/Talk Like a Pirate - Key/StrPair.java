public class StrPair {
    public String word1;
    public String word2;

    public StrPair(String word1, String word2) {
        this.word1 = word1;
        this.word2 = word2;
    }
    public String firstWordCapitalized() {
        if (word1.length() < 1) {
            return word1;
        }
        return word1.substring(0, 1).toUpperCase() + word1.substring(1);
    }
    public String secondWordCapitalized() {
        if (word2.length() < 1) {
            return word2;
        }
        return word2.substring(0, 1).toUpperCase() + word2.substring(1);
    }
}
