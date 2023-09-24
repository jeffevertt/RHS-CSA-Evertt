import java.util.Scanner;

public class AppMain {
    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);

        // Asks the user to type their age, the current year, a future year, and their name.
        System.out.print("What is your age? ");
        int age = input.nextInt();
        System.out.print("What is the current year? ");
        int currentYear = input.nextInt();
        System.out.print("Which future year? ");
        int futureYear = input.nextInt();
        input.nextLine(); // Eat the CR
        System.out.print("What is your name? ");
        String name = input.nextLine();
        
        // Print out "<Name> will be <computed age> years old in <future year>".
        int futureAge = age + (futureYear - currentYear);
        System.out.println(name + " will be " + futureAge + " years old in " + futureYear);
    }
}
