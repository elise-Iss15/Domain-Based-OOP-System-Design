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
