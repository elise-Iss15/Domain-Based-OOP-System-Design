public class Main {
    public static void main(String[] args) {

        // Exceptional/error handling

        System.out.println("=== Test: Invalid Age ===");
        try {
            new Student("Tom", -5, "S99", 80.0);
        } catch (InvalidAgeException e) {
            System.out.println("Error: " + e.getMessage());
        }

        System.out.println("\n=== Test: Invalid Grade ===");
        try {
            new Student("Tom", 20, "S99", 150.0);
        } catch (InvalidGradeException e) {
            System.out.println("Error: " + e.getMessage());
        }

        System.out.println("\n=== Test: Empty Name ===");
        try {
            new Student("", 20, "S99", 80.0);
        } catch (IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage());
        }

        // creating objects and collections

        System.out.println("\n=== Creating Valid Objects ===");
        Teacher mrKagabo = new Teacher("Mr. Kagabo", 40, "Mathematics");
        Teacher msAmina = new Teacher("Ms. Amina", 35, "Science");

        Student bella = new Student("Bella", 20, "S01", 75.0);
        Student seth = new Student("Seth", 22, "S02", 60.0);
        Student annabeth = new Student("Annabeth", 21, "S03", 70.0);

        Course math = new Course("Advanced Mathematics", mrKagabo);
        Course science = new Course("Biology 101", msAmina);

        System.out.println("\n=== [List] Enrolling Students into Courses ===");
        math.enrollStudent(bella);
        math.enrollStudent(seth);
        math.enrollStudent(annabeth);

        science.enrollStudent(bella);
        science.enrollStudent(annabeth);

        // testing duplicates
        System.out.println("\n--- Duplicate Enrollment Test ---");
        math.enrollStudent(bella);

        // Removing a student from course List
        System.out.println("\n--- Unenrolling Seth from Math ---");
        math.unenrollStudent(seth);

        // showing the course info
        System.out.println("\n--- Current Math Enrollment ---");
        math.showInfo();

        // using set in enrolled courses in student class

        System.out.println("\n=== [Set] Student's Unique Course Memberships ===");
        System.out.println("Bella's courses : " + bella.getEnrolledCourseNames());
        System.out.println("Annabeth's courses: " + annabeth.getEnrolledCourseNames());

        // testing the duplicates in course (see how the set handles it)
        bella.addEnrolledCourse("Advanced Mathematics");

        // adding a student's achievements

        System.out.println("\n=== [Set] Awarding Unique Achievements ===");
        bella.addAchievement("Top Scorer");
        bella.addAchievement("Perfect Attendance");
        bella.addAchievement("Top Scorer");

        annabeth.addAchievement("Most Improved");
        annabeth.addAchievement("Top Scorer");

        // Remove an achievement
        bella.removeAchievement("Perfect Attendance");
        System.out.println("Bella's achievements after removal: " + bella.getAchievements());

        // teacher's grade's book

        System.out.println("\n=== [Map] Teacher Grade Book ===");
        mrKagabo.studentGrades(bella, 92.0);
        mrKagabo.studentGrades(annabeth, 78.5);

        // searching for student grade using student Id
        mrKagabo.lookupGrade("S01");
        mrKagabo.lookupGrade("S99");

        // Updating student's grade
        mrKagabo.studentGrades(bella, 95.0);

        // Show full grade book
        mrKagabo.showGradeBook();

        // Remove a record

        mrKagabo.removeGradeRecord("S03");
        mrKagabo.showGradeBook();

        // Attendance print out using Map and List

        System.out.println("\n=== [Map<String,List>] Attendance Records ===");
        math.markAttendance(bella, "2025-04-01");
        math.markAttendance(bella, "2025-04-03");
        math.markAttendance(annabeth, "2025-04-01");
        math.markAttendance(annabeth, "2025-04-03");
        math.markAttendance(annabeth, "2025-04-05");

        // show attendance for every student
        math.showAttendance(bella);
        math.showAttendance(annabeth);

        // remove an attendance record incase it was marked in error
        math.removeAttendance(bella, "2025-04-03");
        math.showAttendance(bella);

        // student profiles

        System.out.println("\n=== Full Student Profiles ===");
        bella.showProfile();
        System.out.println();
        annabeth.showProfile();

        // greetings or more of introductions
        System.out.println("\n=== Greetings ===");
        mrKagabo.greet();
        msAmina.greet();
        bella.greet();
        annabeth.greet();

        System.out.println("\nProgram finished successfully.");
    }
}
