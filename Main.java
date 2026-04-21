public class Main {
    public static void main(String[] args) {

        // Invalid age( we had set the range in which age falls into)

        System.out.println("=== Test: Invalid Age ===");
        try {
            new Student("Tom", -5, "3", 80.0);
        } catch (InvalidAgeException e) {
            System.out.println("Error: " + e.getMessage());
        }

        // Invalid grades(Same to the age we had also estimated the range where grade
        // must fall into to be valid)
        System.out.println("\n=== Test: Invalid Grade ===");
        try {
            new Student("Tom", 20, "3", 150.0);
        } catch (InvalidGradeException e) {
            System.out.println("Error: " + e.getMessage());
        }

        // Empty Name textbox...
        System.out.println("\n=== Test: Empty Name ===");
        try {
            new Student("", 20, "3", 80.0);
        } catch (IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage());
        }

        System.out.println("\n=== Creating Valid Objects ===");
        Teacher teacher = new Teacher("Mr. Kagabo", 40, "Mathematics");
        Student bella = new Student("Bella", 20, "1", 75.0);
        Student Seth = new Student("Seth", 22, "2", 60.0);
        Course math = new Course("Advanced Mathematics", teacher);

        System.out.println("\n=== Enrollment ===");
        math.enrollStudent(bella);
        math.enrollStudent(Seth);

        System.out.println("\n=== Test: Course Full ===");
        try {
            Student extra1 = new Student("Annabeth", 21, "3", 70.0);
            Student extra2 = new Student("Isaac", 23, "4", 65.0);
            math.enrollStudent(extra1);
            math.enrollStudent(extra2);
        } catch (CourseFullException e) {
            System.out.println("Error: " + e.getMessage());
        }

        System.out.println("\n=== Greetings ===");
        teacher.greet();
        bella.greet();
        Seth.greet();

        System.out.println("\n=== Course ===");
        math.showInfo();

        System.out.println("\n=== Grading ===");
        teacher.gradeStudent(bella, 88.5);
        bella.showGrade();
        Seth.showGrade();

        System.out.println("\n=== Test: Invalid Grade via Teacher ===");
        try {
            teacher.gradeStudent(bella, 200.0);
        } catch (InvalidGradeException e) {
            System.out.println("Error: " + e.getMessage());
        }

        System.out.println("\nProgram finished without crashing.");
    }
}
