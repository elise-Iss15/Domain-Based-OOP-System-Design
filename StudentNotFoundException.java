class StudentNotFoundException extends SchoolSystemException {
    public StudentNotFoundException(String id) {
        super("Student ID [" + id + "] not found.");
    }
}