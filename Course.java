import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Course {

    // changing the maximum num of students that can enroll in a course (here we
    // changed from 3 to 30 to make it realistic enough)
    private static final int MAX_STUDENTS = 30;

    private String courseName;
    private Teacher teacher;

    // using List to get a list of students enrolled in a course
    private List<Student> students;

    // using a Map to track attendance
    private Map<String, List<String>> attendanceRecord;

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
        this.attendanceRecord = new HashMap<>();
    }

    // Enrolling a student into a course
    public void enrollStudent(Student student) {
        if (student == null) {
            throw new IllegalArgumentException("Student cannot be null.");
        }
        if (students.size() >= MAX_STUDENTS) {
            throw new CourseFullException("Course " + courseName + " is full. Max "
                    + MAX_STUDENTS + " students allowed.");
        }

        for (Student s : students) {
            if (s.getStudentId().equals(student.getStudentId())) {
                System.out.println(student.getName() + " is already enrolled in " + courseName);
                return;
            }
        }
        students.add(student);
        student.addEnrolledCourse(courseName);
        attendanceRecord.put(student.getStudentId(), new ArrayList<>());
        System.out.println(student.getName() + " enrolled in " + courseName);
    }

    // Remove Unenroll a student
    public void unenrollStudent(Student student) {
        if (students.remove(student)) {
            attendanceRecord.remove(student.getStudentId());
            student.dropCourse(courseName);
            System.out.println(student.getName() + " unenrolled from " + courseName);
        } else {
            System.out.println(student.getName() + " was not enrolled in " + courseName);
        }
    }

    // getting the list of students
    public List<Student> getStudents() {
        return students;
    }

    // Adding student's attendance or Mark a student as presennt
    public void markAttendance(Student student, String date) {
        List<String> dates = attendanceRecord.get(student.getStudentId());
        if (dates == null) {
            System.out.println(student.getName() + " is not enrolled in " + courseName);
            return;
        }
        dates.add(date);
        System.out.println("Attendance marked for " + student.getName() + " on " + date);
    }

    // Removing a specific attendance date for a student incase it was marked in
    // error)
    public void removeAttendance(Student student, String date) {
        List<String> dates = attendanceRecord.get(student.getStudentId());
        if (dates != null && dates.remove(date)) {
            System.out.println("Attendance for " + date + " removed for " + student.getName());
        } else {
            System.out.println("No attendance record found for " + student.getName() + " on " + date);
        }
    }

    // Showing attendance for a student
    public void showAttendance(Student student) {
        List<String> dates = attendanceRecord.get(student.getStudentId());
        if (dates == null) {
            System.out.println(student.getName() + " is not enrolled.");
        } else {
            System.out.println(student.getName() + "'s attendance: "
                    + (dates.isEmpty() ? "No records yet" : dates));
        }
    }

    // Showing all course info
    public void showInfo() {
        System.out.println("Course: " + courseName);
        System.out.println("Teacher: " + teacher.getName() + " | Subject: " + teacher.getSubject());
        System.out.println("Students enrolled (" + students.size() + "):");
        if (students.isEmpty()) {
            System.out.println("  (none)");
        } else {
            for (Student s : students) {
                System.out.println("  - " + s.getName() + " (ID: " + s.getStudentId() + ")");
            }
        }
    }

    public String getCourseName() {
        return courseName;
    }

    public Teacher getTeacher() {
        return teacher;
    }
}
