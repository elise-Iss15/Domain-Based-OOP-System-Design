public class Course {

    private String courseName;
    private String teacherName;

    public Course(String courseName, String teacherName) {
        this.courseName = courseName;
        this.teacherName = teacherName;
    }

    public void showInfo() {
        System.out.println("Course: " + courseName + " | Teacher: " + teacherName);
    }

    public String getCourseName() {
        return courseName;
    }

    public String getTeacherName() {
        return teacherName;
    }
}