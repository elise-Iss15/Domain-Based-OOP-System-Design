package com.school.model;

import com.school.exception.InvalidGradeException;
import java.util.*;

/**
 * Teacher entity — extends Person.
 * OOP Layer: Inheritance, Encapsulation
 * Collections Layer: ArrayList for workload
 */
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
            System.out.println("  No courses assigned yet.");
        } else {
            for (Course c : workload) {
                System.out.println("  - " + c.getCourseName() + " (" + c.getRoster().size() + " Students)");
            }
        }
    }

    /** Grade a student — throws InvalidGradeException if score is out of range */
    public void grade(Student s, String task, double score, double max) {
        if (score < 0 || score > max)
            throw new InvalidGradeException("Score " + score + " is out of bounds [0–" + max + "].");
        s.addGrade(task, (score / max) * 100);
    }

    // --- Getters ---
    public String getSubject()        { return subject; }
    public List<Course> getWorkload() { return workload; }

    @Override
    public String toString() {
        return getName() + " | " + subject + " (age " + getAge() + ")";
    }
}
