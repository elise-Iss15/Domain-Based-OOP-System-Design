package com.school.exception;

/** Base exception for all school system errors. */
public class SchoolSystemException extends RuntimeException {
    public SchoolSystemException(String message) {
        super(message);
    }
}
