import java.util.ArrayList;

public class Skyward {
    // Data
    private ArrayList<Student> students;
    
    // Constructor(s)
    public Skyward() {
        students = new ArrayList<Student>();
        addDefaultStudents();
    }

    // Methods
    private void addDefaultStudents() {
        students.add( new Student("Jacob Rodriguez", 10042983, 79.3) );
        students.add( new Student("Lily Patel", 10038891, 89.3) );
        students.add( new Student("Aiden Campbell", 10066823, 95.2) );
        students.add( new Student("Natalie Rivera", 10012956, 64.6) );
        students.add( new Student("Samuel Wilson", 10029474, 70.8) );
        students.add( new Student("Grace Thomas", 10092831, 99.0) );
        students.add( new Student("Ethan Davis", 10002674, 86.3) );
        students.add( new Student("Elizabeth Chang", 10082341, 93.8) );
        students.add( new Student("Michael Jones", 10034287, 59.1) );
        students.add( new Student("Emily Sharma", 10048216, 73.3) );
        students.add( new Student("Daniel Williams", 10023783, 80.2) );
        students.add( new Student("Abigail Kumar", 10019273, 89.9) );
        students.add( new Student("Henry Chang", 10062938, 81.7) );
        students.add( new Student("Evelyn Hernandez", 10034879, 78.4) );
        students.add( new Student("Alexander Singh", 10100235, 71.2) );
        students.add( new Student("Harper Lee", 10088292, 93.4) );
        students.add( new Student("Lucas Martinez", 10066298, 65.8) );
        students.add( new Student("Charlotte Lopez", 10021225, 92.6) );
        students.add( new Student("Benjamin Khan", 10031634, 90.1) );
        students.add( new Student("Amelia Gupta", 10029789, 86.0) );
        students.add( new Student("Oliver Smith", 10055289, 76.3) );
        students.add( new Student("Mia Ali", 10047833, 88.1) );
        students.add( new Student("James Kim", 10019283, 79.9) );
        students.add( new Student("Isabella Ramirez", 10022102, 89.1) );
        students.add( new Student("Elijah Brown", 10099018, 91.8) );
        students.add( new Student("Sophia Khan", 10032304, 98.2) );
        students.add( new Student("William Garcia", 10002373, 68.9) );
        students.add( new Student("Ava Nguyen", 10003923, 78.9) );
        students.add( new Student("Liam Rodriguez", 10092763, 82.6) );
        students.add( new Student("Emma Thompson", 10057893, 87.9) );
    }
    public ArrayList<Student> getStudents() {
        // Return a copy (note: Students are immutable)
        return new ArrayList<Student>(students);
    }
}
