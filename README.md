# Student Management System

A simple Java project that demonstrates OOP principles.

## Classes

- **Person** - abstract base class
- **Student** - extends Person
- **Teacher** - extends Person
- **Course** - connects Teacher and Students

## OOP Concepts

- **Encapsulation** - all fields are private
- **Abstraction** - Person has abstract method greet()
- **Inheritance** - Student and Teacher extend Person
- **Polymorphism** - greet() behaves differently for each class

## Exception Handling

- **InvalidAgeException** - thrown when age is invalid
- **InvalidGradeException** - thrown when grade is out of range
- **CourseFullException** - thrown when course exceeds 3 students

## How to Run

```bash
javac *.java
java Main
```
