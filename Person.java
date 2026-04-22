
public abstract class Person {

    private String name;
    private int age;

    public Person(String name, int age) {

        if (name == null || name.isEmpty()) {
            throw new IllegalArgumentException("Name cannot be empty.");
        }
        if (age <= 0 || age > 100) {
            throw new InvalidAgeException("Age " + age + " is not valid. Age must be between 1 and 100.");
        }
        this.name = name;
        this.age = age;
    }

    public abstract void greet();

    public String getName() {
        return name;
    }

    public int getAge() {
        return age;
    }
}
