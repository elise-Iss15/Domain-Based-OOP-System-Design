import java.io.*;
import java.util.*;

public class fileManager {
    private static final String SEP = "\\|";
    private static final String WRITE_SEP = "|";

    public static void saveData(List<Student> students, List<Teacher> teachers, List<Course> courses) {
        BufferedWriter writer = null;
        try {

            writer = new BufferedWriter(new FileWriter("teachers.txt"));
            for (Teacher t : teachers) {
                writer.write(t.getName() + WRITE_SEP + t.getAge() + WRITE_SEP + t.getSubject());
                writer.newLine();
            }
            writer.close();

            writer = new BufferedWriter(new FileWriter("students.txt"));
            for (Student s : students) {
                writer.write(s.getName() + WRITE_SEP + s.getAge() + WRITE_SEP + s.getStudentId());
                writer.newLine();
            }
            writer.close();

            writer = new BufferedWriter(new FileWriter("courses.txt"));
            for (Course c : courses) {
                writer.write(
                        c.getCourseName() + WRITE_SEP + c.getTeacher().getName() + WRITE_SEP + c.getTotalSessions());
                writer.newLine();
            }
            writer.close();

            writer = new BufferedWriter(new FileWriter("marks.txt"));
            for (Student s : students) {
                for (Map.Entry<String, Double> entry : s.getFormativeScores().entrySet()) {
                    writer.write(s.getStudentId() + WRITE_SEP + entry.getKey() + WRITE_SEP + entry.getValue());
                    writer.newLine();
                }
            }
            writer.close();

        } catch (IOException e) {
            System.out.println(" Save failed: " + e.getMessage());
        }
    }

    public static void loadData(List<Student> students, List<Teacher> teachers, List<Course> courses,
            Map<String, Student> registry) {
        BufferedReader reader = null;
        try {

            File tFile = new File("teachers.txt");
            if (tFile.exists()) {
                reader = new BufferedReader(new FileReader(tFile));
                String line;
                while ((line = reader.readLine()) != null) {
                    String[] d = line.split(SEP);
                    teachers.add(new Teacher(d[0], Integer.parseInt(d[1]), d[2]));
                }
                reader.close();
            }

            File sFile = new File("students.txt");
            if (sFile.exists()) {
                reader = new BufferedReader(new FileReader(sFile));
                String line;
                while ((line = reader.readLine()) != null) {
                    String[] d = line.split(SEP);
                    Student s = new Student(d[0], Integer.parseInt(d[1]), d[2]);
                    students.add(s);
                    registry.put(s.getStudentId(), s);
                }
                reader.close();
            }

            File cFile = new File("courses.txt");
            if (cFile.exists()) {
                reader = new BufferedReader(new FileReader(cFile));
                String line;
                while ((line = reader.readLine()) != null) {
                    String[] d = line.split(SEP);
                    String cName = d[0];
                    String tName = d[1];
                    int sessions = Integer.parseInt(d[2]);

                    for (Teacher t : teachers) {
                        if (t.getName().equals(tName)) {

                            Course c = new Course(cName, t, true);
                            c.setTotalSessions(sessions);
                            courses.add(c);
                            break;
                        }
                    }
                }
                reader.close();
            }

            File mFile = new File("marks.txt");
            if (mFile.exists()) {
                reader = new BufferedReader(new FileReader(mFile));
                String line;
                while ((line = reader.readLine()) != null) {
                    String[] d = line.split(SEP);
                    Student s = registry.get(d[0]);
                    if (s != null)
                        s.addGrade(d[1], Double.parseDouble(d[2]));
                }
                reader.close();
            }

        } catch (Exception e) {
            System.out.println(" Load error: " + e.getMessage());
        }
    }
}