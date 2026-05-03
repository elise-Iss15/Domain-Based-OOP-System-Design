package com.school.manager;

import com.school.model.*;
import com.school.util.AppLogger;

import java.io.*;
import java.util.*;

public class FileManager {

    private static final String DATA_DIR      = "data/";
    private static final String TEACHERS_FILE = DATA_DIR + "teachers.txt";
    private static final String STUDENTS_FILE = DATA_DIR + "students.txt";
    private static final String COURSES_FILE  = DATA_DIR + "courses.txt";
    private static final String MARKS_FILE    = DATA_DIR + "marks.txt";
    private static final String ATTENDANCE_FILE = DATA_DIR + "attendance.txt";

    public FileManager() {
        new File(DATA_DIR).mkdirs();
    }

    // ─── SAVE ────────────────────────────────────────────────────────────────

    public void saveAll(List<Teacher> teachers, List<Student> students, List<Course> courses) {
        saveTeachers(teachers);
        saveStudents(students);
        saveCourses(courses);
        saveMarks(students);
        saveAttendance(courses);
        AppLogger.log("INFO", "All data saved to files.");
    }

    private void saveTeachers(List<Teacher> teachers) {
        try (PrintWriter pw = new PrintWriter(new FileWriter(TEACHERS_FILE))) {
            for (Teacher t : teachers) {
                pw.println(t.getName() + "," + t.getAge() + "," + t.getSubject());
            }
        } catch (IOException e) {
            AppLogger.log("ERROR", "Failed to save teachers: " + e.getMessage());
        }
    }

    private void saveStudents(List<Student> students) {
        try (PrintWriter pw = new PrintWriter(new FileWriter(STUDENTS_FILE))) {
            for (Student s : students) {
                pw.println(s.getName() + "," + s.getAge() + "," + s.getStudentId());
            }
        } catch (IOException e) {
            AppLogger.log("ERROR", "Failed to save students: " + e.getMessage());
        }
    }

    private void saveCourses(List<Course> courses) {
        try (PrintWriter pw = new PrintWriter(new FileWriter(COURSES_FILE))) {
            for (Course c : courses) {
                StringBuilder sb = new StringBuilder();
                sb.append(c.getCourseName()).append(",")
                  .append(c.getTeacher().getName()).append(",")
                  .append(c.getTotalSessions());
                for (Student s : c.getRoster()) {
                    sb.append(",").append(s.getStudentId());
                }
                pw.println(sb);
            }
        } catch (IOException e) {
            AppLogger.log("ERROR", "Failed to save courses: " + e.getMessage());
        }
    }

    private void saveMarks(List<Student> students) {
        try (PrintWriter pw = new PrintWriter(new FileWriter(MARKS_FILE))) {
            for (Student s : students) {
                // summative
                pw.println(s.getStudentId() + ",SUMMATIVE," + s.getSummativeScore());
                // formative tasks
                for (Map.Entry<String, Double> e : s.getFormativeScores().entrySet()) {
                    pw.println(s.getStudentId() + "," + e.getKey() + "," + e.getValue());
                }
            }
        } catch (IOException e) {
            AppLogger.log("ERROR", "Failed to save marks: " + e.getMessage());
        }
    }

    private void saveAttendance(List<Course> courses) {
        try (PrintWriter pw = new PrintWriter(new FileWriter(ATTENDANCE_FILE))) {
            for (Course c : courses) {
                for (Map.Entry<String, List<String>> entry : c.getAttendanceData().entrySet()) {
                    for (String date : entry.getValue()) {
                        pw.println(c.getCourseName() + "," + entry.getKey() + "," + date);
                    }
                }
            }
        } catch (IOException e) {
            AppLogger.log("ERROR", "Failed to save attendance: " + e.getMessage());
        }
    }

    // ─── LOAD ────────────────────────────────────────────────────────────────

    public List<Teacher> loadTeachers() {
        List<Teacher> teachers = new ArrayList<>();
        File f = new File(TEACHERS_FILE);
        if (!f.exists()) return teachers;
        try (BufferedReader br = new BufferedReader(new FileReader(f))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (line.isBlank()) continue;
                String[] parts = line.split(",");
                if (parts.length >= 3) {
                    teachers.add(new Teacher(parts[0], Integer.parseInt(parts[1].trim()), parts[2]));
                }
            }
        } catch (IOException e) {
            AppLogger.log("ERROR", "Failed to load teachers: " + e.getMessage());
        }
        return teachers;
    }

    public List<Student> loadStudents() {
        List<Student> students = new ArrayList<>();
        File f = new File(STUDENTS_FILE);
        if (!f.exists()) return students;
        try (BufferedReader br = new BufferedReader(new FileReader(f))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (line.isBlank()) continue;
                String[] parts = line.split(",");
                if (parts.length >= 3) {
                    students.add(new Student(parts[0], Integer.parseInt(parts[1].trim()), parts[2].trim()));
                }
            }
        } catch (IOException e) {
            AppLogger.log("ERROR", "Failed to load students: " + e.getMessage());
        }
        return students;
    }

    public List<Course> loadCourses(List<Teacher> teachers, List<Student> students) {
        List<Course> courses = new ArrayList<>();
        File f = new File(COURSES_FILE);
        if (!f.exists()) return courses;
        try (BufferedReader br = new BufferedReader(new FileReader(f))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (line.isBlank()) continue;
                String[] parts = line.split(",");
                if (parts.length < 3) continue;

                String courseName  = parts[0];
                String teacherName = parts[1];
                int sessions       = Integer.parseInt(parts[2].trim());

                Teacher t = teachers.stream()
                        .filter(x -> x.getName().equals(teacherName))
                        .findFirst().orElse(null);
                if (t == null) continue;

                Course course = new Course(courseName, t, true);
                t.assignToCourse(course);
                course.setTotalSessions(sessions);

                // enroll students
                for (int i = 3; i < parts.length; i++) {
                    String sid = parts[i].trim();
                    students.stream()
                            .filter(s -> s.getStudentId().equals(sid))
                            .findFirst()
                            .ifPresent(s -> {
                                course.addStudentToRoster(s);
                            });
                }
                courses.add(course);
            }
        } catch (IOException e) {
            AppLogger.log("ERROR", "Failed to load courses: " + e.getMessage());
        }
        return courses;
    }

    public void loadMarks(List<Student> students) {
        File f = new File(MARKS_FILE);
        if (!f.exists()) return;
        try (BufferedReader br = new BufferedReader(new FileReader(f))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (line.isBlank()) continue;
                String[] parts = line.split(",");
                if (parts.length < 3) continue;
                String sid   = parts[0].trim();
                String task  = parts[1].trim();
                double score = Double.parseDouble(parts[2].trim());

                students.stream()
                        .filter(s -> s.getStudentId().equals(sid))
                        .findFirst()
                        .ifPresent(s -> {
                            if (task.equals("SUMMATIVE")) s.setSummative(score);
                            else                          s.addGrade(task, score);
                        });
            }
        } catch (IOException e) {
            AppLogger.log("ERROR", "Failed to load marks: " + e.getMessage());
        }
    }

    public void loadAttendance(List<Course> courses) {
        File f = new File(ATTENDANCE_FILE);
        if (!f.exists()) return;
        try (BufferedReader br = new BufferedReader(new FileReader(f))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (line.isBlank()) continue;
                String[] parts = line.split(",");
                if (parts.length < 3) continue;
                String courseName = parts[0].trim();
                String studentId  = parts[1].trim();
                String date       = parts[2].trim();

                courses.stream()
                        .filter(c -> c.getCourseName().equals(courseName))
                        .findFirst()
                        .ifPresent(c -> c.recordAttendance(studentId, date));
            }
        } catch (IOException e) {
            AppLogger.log("ERROR", "Failed to load attendance: " + e.getMessage());
        }
    }
}
