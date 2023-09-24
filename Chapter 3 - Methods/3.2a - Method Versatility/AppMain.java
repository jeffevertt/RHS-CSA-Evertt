public class AppMain {
    public static void main(String[] args) {
        drawBox('$', 6, 4);
    }
    public static void drawBox(int size) {
        drawBox('*', size);
    }
    public static void drawBox(char ch, int size) {
        drawBox(ch, size * 2, size);
    }
    public static void drawBox(char ch, int width, int height) {
        printTopBot(ch, width);
        for (int i = 0; i < height - 2; i++) {
            printMid(ch, width);
        }
        printTopBot(ch, width);
    }
    public static void printTopBot(char ch, int length) {
        for (int i = 0; i < length; i++) {
            System.out.print(ch);
        }
        System.out.println();
    }
    public static void printMid(char ch, int length) {
        System.out.print(ch);
        for (int i = 0; i < length - 2; i++) {
            System.out.print(' ');
        }
        System.out.println(ch);
    }
}
