public class Utils {
    public static void printArray(int[] list) {
        String result = "{ ";
        for (int i = 0; i < list.length; i++) {
            result += (list[i] + ((i + 1 < list.length) ? ", " : " "));
        }
        result += "}";
        System.out.println(result);
    }

    public static void printArray(String[] list) {
        String result = "{ ";
        for (int i = 0; i < list.length; i++) {
            result += (list[i] + ((i + 1 < list.length) ? ", " : " "));
        }
        result += "}";
        System.out.println(result);
    }    
}