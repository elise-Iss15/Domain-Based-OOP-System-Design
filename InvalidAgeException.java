class InvalidAgeException extends SchoolSystemException {
    public InvalidAgeException(int age) {
        super("Age " + age + " violates policy. Must be 5-100.");
    }
}