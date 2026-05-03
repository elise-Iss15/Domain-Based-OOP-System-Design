import java.util.*;

public class Course {
    private String courseName;
    private Teacher teacher;
    private List<Student> roster = new ArrayList<>();
    private Map<String, List<String>> attendance = new HashMap<>();
    private int totalSessions = 0;

    public Course(String courseName, Teacher teacher, boolean isRestoring) {
        this.courseName = courseName;
        this.teacher = teacher;

        if (!isRestoring) {
            teacher.assignToCourse(this);
        }
    }

    public Course(String courseName, Teacher teacher) {
        this(courseName, teacher, false);
    }

    public void addStudentToRoster(Student s) {
        if (!roster.contains(s)) {
            roster.add(s);

            if (!attendance.containsKey(s.getStudentId())) {
                attendance.put(s.getStudentId(), new ArrayList<>());
            }

            if (!s.getEnrolledCourseNames().contains(this.courseName)) {
                s.getEnrolledCourseNames().add(this.courseName);
            }
        }
    }

    public void takeAttendance(Scanner sc) {
        totalSessions++;
        System.out.print("Enter Date (YYYY-MM-DD): ");
        String date = sc.nextLine();
        for (Student s : roster) {
            System.out.print("Is " + s.getName() + " present? (y/n): ");
            if (sc.nextLine().toLowerCase().startsWith("y")) {
                attendance.get(s.getStudentId()).add(date);
            }
        }
    }

    public void showReport() {
        System.out.println("\n========================================");
        System.out.println("COURSE: " + courseName.toUpperCase());
        System.out.println("INSTRUCTOR: " + teacher.getName());
        System.out.println("----------------------------------------");
        if (roster.isEmpty()) {
            System.out.println("No students enrolled.");
        } else {
            for (Student s : roster) {

                List<String> records = attendance.get(s.getStudentId());
                int present = (records != null) ? records.size() : 0;

                s.displayReport(totalSessions, present);
            }
        }
        System.out.println("========================================");
    }

    // --- GETTERS & SETTERS ---

    public Teacher getTeacher() {
        return this.teacher;
    }

    public String getCourseName() {
        return this.courseName;
    }

    public List<Student> getRoster() {
        return roster;
    }

    public int getTotalSessions() {
        return totalSessions;
    }

    public void setTotalSessions(int totalSessions) {
        this.totalSessions = totalSessions;
    }

    public Map<String, List<String>> getAttendanceData() {
        return attendance;
    }

    public void recordAttendance(String studentId, String date) {

        attendance.putIfAbsent(studentId, new ArrayList<>());
        attendance.get(studentId).add(date);
    }
}