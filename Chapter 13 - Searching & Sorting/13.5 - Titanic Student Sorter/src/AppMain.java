public class AppMain {
    /* Free Trip on the Titanic!
     * Just in case we hit an iceberg & there isn’t enough lifeboats, I’d like to be prepared.
     *  please write me a program to sort students based on a criteria that might change based 
     *  on my mood at the time. 
     * 
     * In this lab, you will complete the TitanicSimulator class. The only file you can modify
     *  is TitanicSimulator.java. Look for the todo comments in that file. You are writing
     *  the method: pickStudents(...)
     * The method is passed in the Skyward roster, which holds the full student list. The Student
     *  objects have accessors for first/last name, studentId, and grade in my class. 
     * I'm not certain how I'd like to pick students right now, so I will be passing in a parameter
     *  to pickStudents that tells you how to sort them: sortMethod. You can find details for this
     *  enum in TitanicSimulatorBase. I'll also pass in the number of students that will fit on 
     *  the lifeboat (and get to survive).
     * Your method returns an ArrayList of students. It should contain the first 'studentCount'
     *  students, determined by the ordering specified by 'sortMethod'
     * 
     * YOUR METHOD MUST IMPLEMENT EITHER AN INSERTION OR SELECTION SORT & DO A FULL SORT OF THE LIST
     *  then select the first 'studentCount' students. You can use the copyFirstN method to help you 
     *  with that last bit.
     */
    public static void main(String[] args) {
        TitanicSimulator titanic = new TitanicSimulator();
        titanic.runSimulations();
    }
}