public class AppMain {
    public static void main(String[] args) {
        // Line 1
        String line1 = "";
        for (int i = 0; i < 5; i++) {
            line1 += randValue(100) + " ";
        }
        System.out.println(line1);

        // Line 2
        String line2 = "";
        for (int i = 0; i < 5; i++) {
            line2 += randValue(100, 500) + " ";
        }
        System.out.println(line2);
    }
    
    // Returns a value with a maximum of a provided value, [0,𝑚𝑎𝑥𝑉𝑎𝑙𝑢𝑒).
    public static int randValue(int maxValue) {
        return (int)(Math.random() * 10);
    }

    // Return a value between two provided doubles, [𝑚𝑖𝑛𝑉𝑎𝑙𝑢𝑒,𝑚𝑎𝑥𝑉𝑎𝑙𝑢𝑒).
    public static int randValue(int minValue, int maxValue) {
        return minValue + (int)(Math.random() * (maxValue - minValue));
    }
}
