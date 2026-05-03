class DuplicateEnrollmentException extends SchoolSystemException {
    public DuplicateEnrollmentException(String student, String course) {
        super(student + " is already enrolled in " + course);
    }
}