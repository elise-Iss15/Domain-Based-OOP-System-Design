public class Main {
    public static void main(String[] args) {

        Teacher teacher = new Teacher("Mr. Kagabo", 40, "Mathematics");
        Student Bella = new Student("Bella", 20, "1", 75.0);
        Student Seth = new Student("Seth", 22, "2", 60.0);
        Course math = new Course("Advanced Mathematics", "Mr. Kagabo");

        System.out.println("=== Greetings ===");
        teacher.greet();
        bella.greet();
        Seth.greet();

        System.out.println("\n=== Course ===");
        math.showInfo();

        System.out.println("\n=== Grading ===");
        teacher.gradeStudent(bella, 88.5);
        bella.showGrade();
        Seth.showGrade();
    }
}
