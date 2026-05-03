package com.school.manager;

import com.school.exception.*;
import com.school.model.*;
import com.school.util.AppLogger;

import java.util.*;
import java.util.stream.Collectors;

public class SchoolManager {

    // Generics Layer — typed repositories
    private final Repository<Student> studentRepo = new Repository<>();
    private final Repository<Teacher> teacherRepo = new Repository<>();
    private final Repository<Course>  courseRepo  = new Repository<>();

    private final FileManager fileManager = new FileManager();

    public SchoolManager() {
        loadFromFiles();
    }

    // ─── STARTUP ─────────────────────────────────────────────────────────────

    private void loadFromFiles() {
        List<Teacher> teachers = fileManager.loadTeachers();
        List<Student> students = fileManager.loadStudents();
        List<Course>  courses  = fileManager.loadCourses(teachers, students);
        fileManager.loadMarks(students);
        fileManager.loadAttendance(courses);

        teacherRepo.setAll(teachers);
        studentRepo.setAll(students);
        courseRepo.setAll(courses);

        AppLogger.log("INFO", "Data loaded — " + teachers.size() + " teachers, "
                + students.size() + " students, " + courses.size() + " courses.");
    }

    public void saveAll() {
        fileManager.saveAll(teacherRepo.getAll(), studentRepo.getAll(), courseRepo.getAll());
    }

    // ─── STUDENT OPERATIONS ──────────────────────────────────────────────────


    public Student addStudent(String name, int age, String studentId) {
        if (age < 5 || age > 120)
            throw new InvalidAgeException(age);
        if (name == null || name.isBlank())
            throw new SchoolSystemException("Student name cannot be empty.");
        if (studentId == null || studentId.isBlank())
            throw new SchoolSystemException("Student ID cannot be empty.");

        boolean duplicate = studentRepo.getAll().stream()
                .anyMatch(s -> s.getStudentId().equalsIgnoreCase(studentId));
        if (duplicate)
            throw new SchoolSystemException("Student ID '" + studentId + "' already exists.");

        Student s = new Student(name, age, studentId);
        studentRepo.add(s);
        AppLogger.log("INFO", "Student added: " + s);
        return s;
    }

    /** Remove student by ID */
    public boolean removeStudent(String studentId) {
        Student s = findStudentById(studentId);
        // unenroll from all courses
        for (Course c : courseRepo.getAll()) {
            c.removeStudentFromRoster(s);
        }
        boolean removed = studentRepo.remove(s);
        if (removed) AppLogger.log("INFO", "Student removed: " + studentId);
        return removed;
    }

    /** Search students by name (case-insensitive partial match) */
    public List<Student> searchStudentsByName(String query) {
        return studentRepo.search(s ->
                s.getName().toLowerCase().contains(query.toLowerCase()));
    }

    /** Search student by exact ID */
    public Student findStudentById(String id) {
        return studentRepo.search(s -> s.getStudentId().equalsIgnoreCase(id))
                .stream().findFirst()
                .orElseThrow(() -> new StudentNotFoundException(id));
    }

    /** Sort students alphabetically by name */
    public List<Student> getStudentsSortedByName() {
        return studentRepo.sort(Comparator.comparing(Student::getName));
    }

    /** Sort students by GPA descending */
    public List<Student> getStudentsSortedByGPA() {
        return studentRepo.sort(Comparator.comparingDouble(
                (Student s) -> s.calculateGPA(0, 0)).reversed());
    }

    public List<Student> getAllStudents() {
        return studentRepo.getAll();
    }

    // ─── TEACHER OPERATIONS ──────────────────────────────────────────────────

    public Teacher addTeacher(String name, int age, String subject) {
        if (age < 18 || age > 120) throw new InvalidAgeException(age);
        if (name == null || name.isBlank())
            throw new SchoolSystemException("Teacher name cannot be empty.");
        if (subject == null || subject.isBlank())
            throw new SchoolSystemException("Subject cannot be empty.");

        Teacher t = new Teacher(name, age, subject);
        teacherRepo.add(t);
        AppLogger.log("INFO", "Teacher added: " + t);
        return t;
    }

    public boolean removeTeacher(String name) {
        Teacher t = findTeacherByName(name);
        boolean removed = teacherRepo.remove(t);
        if (removed) AppLogger.log("INFO", "Teacher removed: " + name);
        return removed;
    }

    public List<Teacher> searchTeachersByName(String query) {
        return teacherRepo.search(t ->
                t.getName().toLowerCase().contains(query.toLowerCase()));
    }

    public Teacher findTeacherByName(String name) {
        return teacherRepo.search(t -> t.getName().equalsIgnoreCase(name))
                .stream().findFirst()
                .orElseThrow(() -> new SchoolSystemException("Teacher '" + name + "' not found."));
    }

    public List<Teacher> getTeachersSortedByName() {
        return teacherRepo.sort(Comparator.comparing(Teacher::getName));
    }

    public List<Teacher> getAllTeachers() {
        return teacherRepo.getAll();
    }

    // ─── COURSE OPERATIONS ───────────────────────────────────────────────────

    public Course addCourse(String courseName, String teacherName) {
        if (courseName == null || courseName.isBlank())
            throw new SchoolSystemException("Course name cannot be empty.");

        boolean dup = courseRepo.getAll().stream()
                .anyMatch(c -> c.getCourseName().equalsIgnoreCase(courseName));
        if (dup) throw new SchoolSystemException("Course '" + courseName + "' already exists.");

        Teacher t = findTeacherByName(teacherName);
        Course c = new Course(courseName, t);
        courseRepo.add(c);
        AppLogger.log("INFO", "Course added: " + c);
        return c;
    }

    public boolean removeCourse(String courseName) {
        Course c = findCourseByName(courseName);
        boolean removed = courseRepo.remove(c);
        if (removed) AppLogger.log("INFO", "Course removed: " + courseName);
        return removed;
    }

    public List<Course> searchCoursesByName(String query) {
        return courseRepo.search(c ->
                c.getCourseName().toLowerCase().contains(query.toLowerCase()));
    }

    public Course findCourseByName(String name) {
        return courseRepo.search(c -> c.getCourseName().equalsIgnoreCase(name))
                .stream().findFirst()
                .orElseThrow(() -> new SchoolSystemException("Course '" + name + "' not found."));
    }

    public List<Course> getCoursesSortedByName() {
        return courseRepo.sort(Comparator.comparing(Course::getCourseName));
    }

    public List<Course> getAllCourses() {
        return courseRepo.getAll();
    }

    // ─── ENROLLMENT & GRADING ────────────────────────────────────────────────

    /** Enroll student into course — throws DuplicateEnrollmentException */
    public void enrollStudent(String studentId, String courseName) {
        Student s = findStudentById(studentId);
        Course  c = findCourseByName(courseName);
        s.enrollInCourse(c);
        AppLogger.log("INFO", "Enrolled " + studentId + " in " + courseName);
    }

    /** Remove student from course */
    public void unenrollStudent(String studentId, String courseName) {
        Student s = findStudentById(studentId);
        Course  c = findCourseByName(courseName);
        c.removeStudentFromRoster(s);
        AppLogger.log("INFO", "Unenrolled " + studentId + " from " + courseName);
    }

    /** Grade a student — throws InvalidGradeException */
    public void gradeStudent(String teacherName, String studentId,
                             String task, double score, double maxScore) {
        Teacher t = findTeacherByName(teacherName);
        Student s = findStudentById(studentId);
        t.grade(s, task, score, maxScore);
        AppLogger.log("INFO", "Graded " + studentId + ": " + task + " = " + score + "/" + maxScore);
    }

    /** Set summative (exam) score */
    public void setSummativeScore(String studentId, double score) {
        if (score < 0 || score > 100)
            throw new InvalidGradeException("Exam score must be between 0 and 100.");
        Student s = findStudentById(studentId);
        s.setSummative(score);
        AppLogger.log("INFO", "Set exam score for " + studentId + ": " + score);
    }

    /** Get student GPA display string */
    public String getStudentReport(String studentId) {
        Student s = findStudentById(studentId);
        // count how many sessions they've attended across all courses
        int total = 0, present = 0;
        for (Course c : courseRepo.getAll()) {
            if (c.getRoster().contains(s)) {
                total   += c.getTotalSessions();
                List<String> att = c.getAttendanceData().get(s.getStudentId());
                present += (att != null) ? att.size() : 0;
            }
        }
        double gpa = s.calculateGPA(total, present);
        String status = gpa >= 50 ? "PASSING ✓" : "AT RISK ⚠";
        return String.format("[%s] %s | GPA: %.1f%% | %s | Tasks: %s | Exam: %s",
                s.getStudentId(), s.getName(), gpa, status,
                s.getFormativeScores().isEmpty() ? "none" : s.getFormativeScores().toString(),
                s.getSummativeScore() == 0 ? "pending" : s.getSummativeScore() + "%");
    }
}
