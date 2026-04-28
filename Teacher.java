import java.util.*;

public class Teacher extends Person {
    private String subject;

    private List<Course> workload = new ArrayList<>();

    public Teacher(String name, int age, String subject) {
        super(name, age);
        this.subject = subject;
    }

    @Override
    public void greet() {
        System.out.println("Faculty: " + getName() + " (Dept: " + subject + ")");
    }

    public void assignToCourse(Course c) {
        workload.add(c);
    }

    public void showWorkload() {
        System.out.println("\n--- Faculty Dashboard: " + getName() + " ---");
        if (workload.isEmpty()) {
            System.out.println(" No courses assigned yet.");
        } else {
            for (Course c : workload) {
                System.out.println(" - " + c.getCourseName() + " (" + c.getRoster().size() + " Students)");
            }
        }
    }

    public void grade(Student s, String task, double score, double max) {
        if (score < 0 || score > max)
            throw new InvalidGradeException("Score " + score + " is out of bounds.");

        s.addGrade(task, (score / max) * 100);
    }

    public String getSubject() {
        return this.subject;
    }

    public List<Course> getWorkload() {
        return workload;
    }
}