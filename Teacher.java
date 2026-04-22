import java.util.HashMap;
import java.util.Map;

public class Teacher extends Person {

    private String subject;

    private Map<String, Double> grades;

    public Teacher(String name, int age, String subject) {
        super(name, age);
        if (subject == null || subject.isEmpty()) {
            throw new IllegalArgumentException("Subject cannot be empty.");
        }
        this.subject = subject;
        this.grades = new HashMap<>();
    }

    public void studentGrades(Student student, double newGrade) {
        if (student == null) {
            throw new IllegalArgumentException("Student cannot be null.");
        }
        student.setGrade(newGrade);
        grades.put(student.getStudentId(), newGrade);
        System.out.println(getName() + " recorded grade " + newGrade
                + " for " + student.getName() + " (ID: " + student.getStudentId() + ")");
    }

    public Double lookupGrade(String studentId) {
        Double grade = grades.get(studentId);
        if (grade == null) {
            System.out.println("No grade found for student ID: " + studentId);
        } else {
            System.out.println("Grade for ID " + studentId + ": " + grade);
        }
        return grade;
    }

    public void removeGradeRecord(String studentId) {
        if (grades.remove(studentId) != null) {
            System.out.println("Grade record removed for student ID: " + studentId);
        } else {
            System.out.println("No record found for student ID: " + studentId);
        }
    }

    public void showGradeBook() {
        System.out.println("--- " + getName() + "'s Grade Book ---");
        if (grades.isEmpty()) {
            System.out.println("  (empty)");
        } else {
            for (Map.Entry<String, Double> entry : grades.entrySet()) {
                System.out.println("  Student ID: " + entry.getKey() + " → Grade: " + entry.getValue());
            }
        }
    }

    public Map<String, Double> getGrades() {
        return grades;
    }

    @Override
    public void greet() {
        System.out.println("Hi, I am a teacher. My name is " + getName() + ", I teach " + subject);
    }

    public String getSubject() {
        return subject;
    }
}
