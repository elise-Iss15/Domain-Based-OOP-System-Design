
public class Teacher extends Person {

    private String subject;

    public Teacher(String name, int age, String subject) {
        super(name, age);
        if (subject == null || subject.isEmpty()) {
            throw new IllegalArgumentException("Subject cannot be empty.");
        }
        this.subject = subject;
    }

    @Override
    public void greet() {
        System.out.println("Hi, I am teacher. My name is " + getName() + ", I teach " + subject);
    }

    public void gradeStudent(Student student, double newGrade) {
        if (student == null) {
            throw new IllegalArgumentException("Student cannot be null.");
        }
        student.setGrade(newGrade);
        System.out.println(getName() + " gave " + student.getName() + " a grade of " + newGrade);
    }

    public String getSubject() { return subject; }
}
