import java.util.ArrayList;

public class TitanicSimulatorBase {
    // Enums
    public enum SortMethod {
        FIRST_NAME,     // Alphebetical (A to Z)
        LAST_NAME,      // Alphebetical (A to Z)
        STUDENT_ID,     // Students with the lowest ID come first
        GRADE           // Students with the highest grade come first
    }

    // Data
    private Skyward skyward;

    // Construtor(s)
    public TitanicSimulatorBase() {
        skyward = new Skyward();
    }

    // Methods
    public ArrayList<Student> pickStudents(Skyward skyward, int studentCount, SortMethod sortMethod) {
        return null;
    }
    public void runSimulations() {
        System.out.println("\n**** TITANIC SIMULATION ****");

        // Sort by first name...
        ArrayList<Student> sortName = pickStudents(skyward, 6, SortMethod.FIRST_NAME);
        System.out.println("** (by firstname) Survivors: " + sortName);

        // Sort by student ID...
        ArrayList<Student> sortId = pickStudents(skyward, 8, SortMethod.STUDENT_ID);
        System.out.println("** (by studentId) Survivors: " + sortId);

        // Sort by grade...
        ArrayList<Student> sortGrade = pickStudents(skyward, 9, SortMethod.GRADE);
        System.out.println("****** (by grade) Survivors: " + sortGrade);

        System.out.println("****************************\n");
    }
}
