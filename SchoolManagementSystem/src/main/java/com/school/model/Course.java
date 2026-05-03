package com.school.model;

import java.util.*;

public class Course {

    private String courseName;
    private Teacher teacher;
    private List<Student> roster      = new ArrayList<>();
    private Map<String, List<String>> attendance = new HashMap<>();
    private int totalSessions = 0;

    public Course(String courseName, Teacher teacher, boolean isRestoring) {
        this.courseName = courseName;
        this.teacher    = teacher;
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
            attendance.putIfAbsent(s.getStudentId(), new ArrayList<>());
            s.getEnrolledCourseNames().add(this.courseName);
        }
    }

    public void removeStudentFromRoster(Student s) {
        roster.remove(s);
        attendance.remove(s.getStudentId());
        s.getEnrolledCourseNames().remove(this.courseName);
    }

    public void recordAttendance(String studentId, String date) {
        attendance.putIfAbsent(studentId, new ArrayList<>());
        attendance.get(studentId).add(date);
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

    // --- Getters & Setters ---
    public String getCourseName()                        { return courseName; }
    public Teacher getTeacher()                          { return teacher; }
    public List<Student> getRoster()                     { return roster; }
    public int getTotalSessions()                        { return totalSessions; }
    public void setTotalSessions(int s)                  { this.totalSessions = s; }
    public Map<String, List<String>> getAttendanceData() { return attendance; }

    @Override
    public String toString() {
        return courseName + " — " + teacher.getName() + " (" + roster.size() + " students)";
    }
}
