package com.school.model;


public abstract class Person {
    private String name;
    private int age;

    public Person(String name, int age) {
        this.name = name;
        this.age = age;
    }

    public String getName() { return name; }
    public int getAge()     { return age; }

    public abstract void greet();

    @Override
    public String toString() {
        return name + " (age " + age + ")";
    }
}
