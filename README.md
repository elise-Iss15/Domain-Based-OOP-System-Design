# Student Management System

A Java project demonstrating OOP principles and the Java Collections Framework.

## Classes

- **Person** — abstract base class
- **Student** — extends Person
- **Teacher** — extends Person
- **Course** — connects Teacher and Students

## OOP Concepts

- **Encapsulation** — all fields are private
- **Abstraction** — Person has abstract method greet()
- **Inheritance** — Student and Teacher extend Person
- **Polymorphism** — greet() behaves differently for each class

## Collections Used

| Collection | Location | Relationship | Reason |
|---|---|---|---|
| `List<Student>` | `Course` | Course → many Students | Ordered enrollment, one-to-many |
| `Set<String>` | `Student` | Student → unique course names | Prevents duplicate enrollment |
| `Set<String>` | `Student` | Student → unique achievements | Each badge earned only once |
| `Map<String, Double>` | `Teacher` | studentId → grade | Fast lookup by key (ID) |
| `Map<String, List<String>>` | `Course` | studentId → attendance dates | Key-value + ordered |

## Exception Handling

- **InvalidAgeException** — thrown when age is invalid
- **InvalidGradeException** — thrown when grade is out of range
- **CourseFullException** — thrown when course exceeds capacity

## How to Run

```bash
javac *.java
java Main
```
