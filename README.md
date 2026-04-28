# 🏫 School Management System — Summary

**Assignment Phase:** Data Persistence via File I/O
**Branch:** `file-io` | **Language:** Java

---

## What This Project Is

A Java application that manages the relationships between **Teachers**, **Students**, and **Courses** in a school environment. This phase of the project focuses on making data **persistent** — meaning information is saved to files and can be reloaded the next time the program runs, so nothing is lost between sessions.

---

## How the Classes Relate

- **Person** is the abstract base for everything — it holds the shared properties of name and age.
- **Teacher** extends Person and manages a list of courses they are assigned to teach.
- **Student** extends Person and tracks their grades, exam scores, attendance, and course enrollments.
- **Course** is the central hub — it links one Teacher to many Students, and records attendance for each session.

---

## What File I/O Adds

Five text files are used to save and restore all system data:

- **teachers.txt** — stores each teacher's name, age, and subject department.
- **students.txt** — stores each student's name, age, and ID number.
- **courses.txt** — stores which teacher runs each course and which students are enrolled.
- **marks.txt** — stores each student's assignment scores and exam result.
- **attendance.txt** — stores the dates each student was present in each course.

A new **FileManager** class is responsible for writing all of this data when the program exits, and reading it back in the correct order when the program starts again.

---

## Loading Order Matters

When reading from files, the system must load data in a specific sequence — Teachers and Students must be loaded first, because Courses depend on them already being in memory to link everything together. Marks and Attendance are loaded last.

---

## Code Analysis — What's Good

- The class hierarchy is clean and well-structured with proper use of inheritance and encapsulation.
- All the necessary getters were already added with comments indicating they were intended for file persistence — showing good forward planning.
- Exceptions like `InvalidGradeException` and `DuplicateEnrollmentException` are properly handled and thrown before any bad data can be saved.

---

## Code Analysis — What Needs Improvement

1. **FileManager is missing** — the getters exist but the actual class that reads and writes files still needs to be built. This is the main task.

2. **Course constructor has a hidden side effect** — creating a Course automatically assigns it to a Teacher. When loading from file, this would accidentally duplicate the teacher's workload. A separate loading method is needed.

3. **Session count can go out of sync** — the total number of class sessions should be calculated from the actual saved dates rather than stored as a separate number, to keep data consistent.

4. **GPA formula uses unexplained numbers** — the weights used to calculate a student's grade (40% formative, 50% exam, 10% attendance) are buried in the code without labels, making them hard to understand or change.

5. **Date input is not validated** — the attendance feature accepts any text as a date. A format check should be added to prevent bad data from being saved to file.

author: Elyse Ishimwe
github: Elise-Iss15
