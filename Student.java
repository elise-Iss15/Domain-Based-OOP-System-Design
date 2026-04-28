import java.util.*;

public class Student extends Person {
    private String studentId;
    private Map<String, Double> formativeScores = new HashMap<>();
    private Set<String> enrolledCourseNames = new HashSet<>();
    private double summativeScore = 0.0;

    public Student(String name, int age, String studentId) {
        super(name, age);
        this.studentId = studentId;
    }

    @Override
    public void greet() {
        System.out.println("Student: " + getName() + " (ID: " + studentId + ")");
    }

    public void enrollInCourse(Course course) {

        if (enrolledCourseNames.contains(course.getCourseName())) {
            throw new DuplicateEnrollmentException(getName(), course.getCourseName());
        }

        course.addStudentToRoster(this);
    }

    public void addGrade(String task, double pct) {
        formativeScores.put(task, pct);
    }

    public void setSummative(double score) {
        this.summativeScore = score;
    }

    public double calculateGPA(int total, int present) {
        double fAvg = 0;
        if (!formativeScores.isEmpty()) {
            for (double s : formativeScores.values())
                fAvg += s;
            fAvg /= formativeScores.size();
        }
        double att = (total == 0) ? 0 : ((double) present / total) * 100;

        return (summativeScore == 0) ? (fAvg * 0.8 + att * 0.2) : (fAvg * 0.4 + summativeScore * 0.5 + att * 0.1);
    }

    public void displayReport(int total, int present) {
        double gpa = calculateGPA(total, present);
        System.out.println("  > [" + studentId + "] " + getName());
        System.out.println("    Tasks: " + (formativeScores.isEmpty() ? "No grades" : formativeScores));
        System.out.println("    Exam:  " + (summativeScore == 0 ? "Pending" : summativeScore + "%"));
        System.out.printf("    GPA:   %.2f%% | Status: %s\n", gpa, (gpa >= 50 ? "PASSING" : "AT RISK"));
    }

    public String getStudentId() {
        return studentId;
    }

    public Map<String, Double> getFormativeScores() {
        return formativeScores;
    }

    public Set<String> getEnrolledCourseNames() {
        return enrolledCourseNames;
    }

    public double getSummativeScore() {
        return summativeScore;
    }
}