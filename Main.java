import java.util.*;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        Map<String, Student> studentRegistry = new HashMap<>();
        List<Course> courses = new ArrayList<>();
        List<Teacher> teachers = new ArrayList<>();
        List<Student> studentList = new ArrayList<>();

        fileManager.loadData(studentList, teachers, courses, studentRegistry);

        while (true) {
            try {
                System.out.println("\n========================================");
                System.out.println("      ACADEMIC MANAGEMENT SYSTEM      ");
                System.out.println("========================================");
                System.out.println("1. Register Teacher");
                System.out.println("2. Create Course");
                System.out.println("3. Enroll Student");
                System.out.println("4. Take Attendance");
                System.out.println("5. Grade Student");
                System.out.println("6. View Faculty Dashboard");
                System.out.println("7. View Course Reports");
                System.out.println("8. Exit");
                System.out.print("\nAction Selection > ");

                String choice = sc.nextLine();

                if (choice.equals("8")) {
                    System.out.println("Saving progress to files...");

                    fileManager.saveData(studentList, teachers, courses);
                    break;
                }

                switch (choice) {
                    case "1":
                        System.out.println("\n--- Teacher Registration ---");
                        System.out.print("Name: ");
                        String tn = sc.nextLine();
                        System.out.print("Age: ");
                        int ta = Integer.parseInt(sc.nextLine());
                        System.out.print("Department/Subject: ");
                        String ts = sc.nextLine();

                        Teacher newTeacher = new Teacher(tn, ta, ts);
                        teachers.add(newTeacher);
                        System.out.println("Teacher " + tn + " registered successfully.");
                        break;

                    case "2":
                        if (teachers.isEmpty())
                            throw new SchoolSystemException("No teachers available. Register a teacher first.");

                        System.out.println("\n--- Create New Course ---");
                        System.out.print("Course Name: ");
                        String cn = sc.nextLine();

                        System.out.println("Select Instructor:");
                        for (int i = 0; i < teachers.size(); i++) {
                            System.out.println(
                                    i + ". " + teachers.get(i).getName() + " (" + teachers.get(i).getSubject() + ")");
                        }
                        int tIdx = Integer.parseInt(sc.nextLine());

                        courses.add(new Course(cn, teachers.get(tIdx)));
                        System.out.println("Course '" + cn + "' created under " + teachers.get(tIdx).getName());
                        break;

                    case "3":
                        if (courses.isEmpty())
                            throw new SchoolSystemException("No courses available. Create a course first.");

                        System.out.println("\n--- Student Enrollment ---");
                        System.out.print("Name: ");
                        String sn = sc.nextLine();
                        System.out.print("Age: ");
                        int sa = Integer.parseInt(sc.nextLine());
                        System.out.print("Student ID: ");
                        String sid = sc.nextLine();

                        Student newS = new Student(sn, sa, sid);

                        System.out.println("Select Course to Join:");
                        for (int i = 0; i < courses.size(); i++) {
                            System.out.println(i + ". " + courses.get(i).getCourseName());
                        }
                        int cIdx = Integer.parseInt(sc.nextLine());

                        newS.enrollInCourse(courses.get(cIdx));

                        studentList.add(newS);
                        studentRegistry.put(sid, newS);
                        System.out.println(sn + " enrolled successfully.");
                        break;

                    case "4":
                        if (courses.isEmpty())
                            throw new SchoolSystemException("No courses exist.");

                        System.out.println("\n--- Session Attendance ---");
                        for (int i = 0; i < courses.size(); i++) {
                            System.out.println(i + ". " + courses.get(i).getCourseName());
                        }
                        System.out.print("Select Course Index: ");
                        int attIdx = Integer.parseInt(sc.nextLine());

                        courses.get(attIdx).takeAttendance(sc);
                        break;

                    case "5":
                        if (studentRegistry.isEmpty())
                            throw new SchoolSystemException("No students in registry.");

                        System.out.println("\n--- Grading Dashboard ---");
                        System.out.print("Enter Student ID: ");
                        String id = sc.nextLine();
                        Student target = studentRegistry.get(id);

                        if (target == null)
                            throw new StudentNotFoundException(id);

                        System.out.println("Select Course for Grading:");
                        for (int i = 0; i < courses.size(); i++) {
                            System.out.println(i + ". " + courses.get(i).getCourseName());
                        }
                        int gIdx = Integer.parseInt(sc.nextLine());
                        Course selectedCourse = courses.get(gIdx);

                        System.out.print("Task Title: ");
                        String task = sc.nextLine();
                        System.out.print("Points Earned: ");
                        double score = Double.parseDouble(sc.nextLine());
                        System.out.print("Total Possible: ");
                        double max = Double.parseDouble(sc.nextLine());

                        selectedCourse.getTeacher().grade(target, task, score, max);
                        System.out.println("Grade recorded for " + target.getName());
                        break;

                    case "6":
                        System.out.println("\n--- Faculty Workload Dashboard ---");
                        if (teachers.isEmpty())
                            System.out.println("No faculty registered.");
                        for (Teacher t : teachers)
                            t.showWorkload();
                        break;

                    case "7":
                        System.out.println("\n--- Course Summary Reports ---");
                        if (courses.isEmpty())
                            System.out.println("No courses currently active.");
                        for (Course c : courses)
                            c.showReport();
                        break;

                    default:
                        System.out.println("Invalid choice. Please select 1-8.");
                }
            } catch (NumberFormatException e) {
                System.out.println("INPUT ERROR: Numeric data required (Choice/Age/Score/Index).");
            } catch (IndexOutOfBoundsException e) {
                System.out.println("SELECTION ERROR: That index does not exist in the list.");
            } catch (SchoolSystemException e) {
                System.out.println("POLICY ERROR: " + e.getMessage());
            } catch (Exception e) {
                System.out.println("UNEXPECTED SYSTEM ERROR: " + e.getMessage());
            }
        }
        System.out.println("\nLogging out... System shutdown complete. Goodbye!");
    }
}