package com.school.exception;

public class StudentNotFoundException extends SchoolSystemException {
    public StudentNotFoundException(String id) {
        super("Student with ID '" + id + "' was not found.");
    }
}
