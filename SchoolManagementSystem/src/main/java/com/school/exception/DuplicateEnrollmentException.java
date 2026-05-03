package com.school.exception;

public class DuplicateEnrollmentException extends SchoolSystemException {
    public DuplicateEnrollmentException(String studentName, String courseName) {
        super("Student '" + studentName + "' is already enrolled in '" + courseName + "'.");
    }
}
