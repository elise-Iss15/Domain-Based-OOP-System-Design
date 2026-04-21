import java.util.ArrayList;

public class Course {

    private static final int MAX_STUDENTS = 3;

    private String courseName;
    private Teacher teacher;
    private ArrayList<Student> students;

    public Course(String courseName, Teacher teacher) {
        if (courseName == null || courseName.isEmpty()) {
            throw new IllegalArgumentException("Course name cannot be empty.");
        }
        if (teacher == null) {
            throw new IllegalArgumentException("Course must have a teacher.");
        }
        this.courseName = courseName;
        this.teacher = teacher;
        this.students = new ArrayList<>();
    }

    public void enrollStudent(Student student) {
        if (student == null) {
            throw new IllegalArgumentException("Student cannot be null.");
        }
        if (students.size() >= MAX_STUDENTS) {
            throw new CourseFullException("Course " + courseName + " is full. Max " + MAX_STUDENTS + " students allowed.");
        }
        students.add(student);
        System.out.println(student.getName() + " enrolled in " + courseName);
    }

    public void showInfo() {
        System.out.println("Course: " + courseName);
        System.out.println("Teacher: " + teacher.getName() + " | Subject: " + teacher.getSubject());
        System.out.println("Students enrolled:");
        for (Student s : students) {
            System.out.println("  - " + s.getName() + " (ID: " + s.getStudentId() + ")");
        }
    }

    public String getCourseName(){
        return courseName;
    }
    public Teacher getTeacher(){
        return teacher;
    }
    public ArrayList<Student> getStudents() {
        return students;
    }
}
