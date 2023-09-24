public class AppMain {
    public static void main(String[] args) {
        // Test a set of strings...
        System.out.println("--------- firstHalf ---------");
        System.out.println("'" + StringUtil.firstHalf("abcdef") + "'");
        System.out.println("'" + StringUtil.firstHalf("this is is this") + "'");

        System.out.println("-------- beforeSpace --------");
        System.out.println("'" + StringUtil.beforeSpace("abc def") + "'");
        System.out.println("'" + StringUtil.beforeSpace("this is a test") + "'");

        System.out.println("--------- afterSpace --------");
        System.out.println("'" + StringUtil.afterSpace("abc def") + "'");
        System.out.println("'" + StringUtil.afterSpace("this is a test") + "'");

        System.out.println("-------- swapAtSpace --------");
        System.out.println("'" + StringUtil.swapAtSpace("abc def") + "'");
        System.out.println("'" + StringUtil.swapAtSpace("this is a test") + "'");

        System.out.println("---- firstNonRepeatedChar ---");
        System.out.println("'" + StringUtil.firstNonRepeatedChar("aabbcddefg") + "'");
        System.out.println("'" + StringUtil.firstNonRepeatedChar("abcabcdef") + "'");
    }
}
