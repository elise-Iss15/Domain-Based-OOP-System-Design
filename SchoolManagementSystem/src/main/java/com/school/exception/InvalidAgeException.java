package com.school.exception;

public class InvalidAgeException extends SchoolSystemException {
    public InvalidAgeException(int age) {
        super("Invalid age: " + age + ". Age must be between 5 and 120.");
    }
}
