import java.util.HashSet;
import java.util.Set;

public class Student extends Person {

    private String studentId;
    private double grade;

    private Set<String> enrolledCourseNames;

    // using set to track stusdent's achivements
    private Set<String> achievements;

    public Student(String name, int age, String studentId, double grade) {
        super(name, age);
        if (studentId == null || studentId.isEmpty()) {
            throw new IllegalArgumentException("Student ID cannot be empty.");
        }
        if (grade < 0 || grade > 100) {
            throw new InvalidGradeException("Grade " + grade + " is not valid. Grade must be between 0 and 100.");
        }
        this.studentId = studentId;
        this.grade = grade;
        this.enrolledCourseNames = new HashSet<>();
        this.achievements = new HashSet<>();
    }

    public void addEnrolledCourse(String courseName) {
        if (enrolledCourseNames.add(courseName)) {
            System.out.println(getName() + " is now enrolled in course: " + courseName);
        } else {
            System.out.println(getName() + " is already enrolled in: " + courseName);
        }
    }

    public void dropCourse(String courseName) {
        if (enrolledCourseNames.remove(courseName)) {
            System.out.println(getName() + " dropped course: " + courseName);
        } else {
            System.out.println(getName() + " was not enrolled in: " + courseName);
        }
    }

    // Check all enrolled courses
    public Set<String> getEnrolledCourseNames() {
        return enrolledCourseNames;
    }

    // Award a unique badge/achievement
    public void addAchievement(String achievement) {
        if (achievements.add(achievement)) {
            System.out.println("🏅 " + getName() + " earned achievement: " + achievement);
        } else {
            System.out.println(getName() + " already has achievement: " + achievement);
        }
    }

    // Removing an achievement
    public void removeAchievement(String achievement) {
        achievements.remove(achievement);
    }

    // Get all achievements
    public Set<String> getAchievements() {
        return achievements;
    }

    @Override
    public void greet() {
        System.out.println("Hi, I am a student. My name is " + getName() + ", ID: " + studentId);
    }

    public void showGrade() {
        System.out.println(getName() + "'s grade: " + grade);
    }

    public void showProfile() {
        System.out.println("--- Student Profile ---");
        System.out.println("Name     : " + getName());
        System.out.println("ID       : " + studentId);
        System.out.println("Grade    : " + grade);
        System.out.println("Courses  : " + (enrolledCourseNames.isEmpty() ? "None" : enrolledCourseNames));
        System.out.println("Badges   : " + (achievements.isEmpty() ? "None" : achievements));
    }

    public String getStudentId() {
        return studentId;
    }

    public double getGrade() {
        return grade;
    }

    public void setGrade(double grade) {
        if (grade < 0 || grade > 100) {
            throw new InvalidGradeException("Grade " + grade + " is not valid. Grade must be between 0 and 100.");
        }
        this.grade = grade;
    }
}
