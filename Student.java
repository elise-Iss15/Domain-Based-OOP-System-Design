
public class Student extends Person {
    private String studentId;
    private double grade;

    public Student(String name, int age, String studentId, double grade) {
        super(name, age);
        this.studentId = studentId;
        this.grade = grade;
    }

    @Override
    public void greet() {
        System.out.println("Hi, I am student. My name is " + getName() + ", ID: " + studentId);
    }

    public void showGrade() {
        System.out.println(getName() + "'s grade: " + grade);
    }

    public String getStudentId() {
        return studentId;
    }

    public double getGrade() {
        return grade;
    }

    public void setGrade(double grade) {
        this.grade = grade;
    }
}
