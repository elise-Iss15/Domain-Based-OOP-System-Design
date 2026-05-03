package com.school.model;

public class SchoolUser {

    public enum Role { ADMIN, TEACHER, STUDENT }

    private final String username;
    private String hashedPassword;
    private final Role role;
    private final String fullName;

    // Optional link to domain objects (nullable)
    private String linkedStudentId;   // set if role == STUDENT
    private String linkedTeacherName; // set if role == TEACHER

    public SchoolUser(String username, String hashedPassword, Role role, String fullName) {
        this.username       = username;
        this.hashedPassword = hashedPassword;
        this.role           = role;
        this.fullName       = fullName;
    }

    // Getters
    public String getUsername()         { return username; }
    public String getHashedPassword()   { return hashedPassword; }
    public Role   getRole()             { return role; }
    public String getFullName()         { return fullName; }
    public String getLinkedStudentId()  { return linkedStudentId; }
    public String getLinkedTeacherName(){ return linkedTeacherName; }

    public void setLinkedStudentId(String id)    { this.linkedStudentId = id; }
    public void setLinkedTeacherName(String name){ this.linkedTeacherName = name; }

    @Override
    public String toString() {
        return fullName + " [" + role + "]";
    }
}
