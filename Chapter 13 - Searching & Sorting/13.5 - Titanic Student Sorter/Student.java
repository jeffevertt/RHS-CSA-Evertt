public class Student {
    // Data
    private final String name;
    private final int studentId;
    private final double grade;

    // Constructor(s)
    public Student(String name, int studentId, double grade) {
        this.name = name;
        this.studentId = studentId;
        this.grade = grade;
    }

    // Accessors
    public String getFullName() {
        return name;
    }
    public String getFirstName() {
        return name.split(" ")[0];
    }
    public String getLastName() {
        return name.split(" ")[1];
    }
    public int getStudentId() {
        return studentId;
    }
    public double getGrade() {
        return grade;
    }

    // toString()
    public String toString() {
        return getFirstName();
    }
}
