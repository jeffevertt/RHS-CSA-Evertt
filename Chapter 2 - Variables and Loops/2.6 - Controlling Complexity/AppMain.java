public class AppMain {
    // Desired output (two different examples, depending on what the constant is set to)...
    // SIZE = 3
    //   #============#
    //   |    <><>    |
    //   |  <>....<>  |
    //   |<>........<>|
    //   |<>........<>|
    //   |  <>....<>  |
    //   |    <><>    |
    //   #============#
    // SIZE = 4
    //   #================#
    //   |      <><>      |
    //   |    <>....<>    |
    //   |  <>........<>  |
    //   |<>............<>|
    //   |<>............<>|
    //   |  <>........<>  |
    //   |    <>....<>    |
    //   |      <><>      |
    //   #================#
    
    public static void main(String[] args) {
        drawTopBottom();
        drawMiddle();
        drawTopBottom();
    }

    public static void drawTopBottom() {
        System.out.println("#==========#");
    }
    public static void drawMiddle() {
        for (int i = 0; i < 5; ++i) {
            System.out.print("|");
            System.out.print("   <..>   ");
            System.out.println("|");
        }
    }
}
