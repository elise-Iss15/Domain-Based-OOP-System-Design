package com.school.controller;

import com.school.component.StatusBar;
import com.school.exception.SchoolSystemException;
import com.school.manager.SchoolManager;
import com.school.model.*;
import com.school.model.SchoolUser.Role;
import com.school.util.AppLogger;
import com.school.util.InputValidator;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;



public class MainController {

    private final SchoolManager  manager;
    private final SchoolUser     currentUser;
    private final BorderPane     root = new BorderPane();

    // Sidebar nav buttons
    private Button btnDashboard, btnStudents, btnTeachers, btnCourses, btnEnroll, btnGrades;
    private Button activeBtn;

    public MainController(SchoolUser user) {
        this.currentUser = user;
        this.manager     = new SchoolManager();
    }

    public BorderPane buildUI() {
        root.setLeft(buildSidebar());
        showDashboard();
        return root;
    }

    // ═══════════════════════════════════════════════════════════
    // SIDEBAR
    // ═══════════════════════════════════════════════════════════

    private VBox buildSidebar() {
        VBox sidebar = new VBox();
        sidebar.getStyleClass().add("sidebar");
        sidebar.setPrefWidth(210);

        Label title = new Label("🏫 SchoolMS");
        title.getStyleClass().add("sidebar-title");

        Label sub = new Label("Management System");
        sub.getStyleClass().add("sidebar-subtitle");

        // Session info label
        Label sessionLabel = new Label(currentUser.getFullName());
        sessionLabel.setStyle("-fx-text-fill: #58a6ff; -fx-font-size: 12; -fx-padding: 0 16 4 16;");
        Label roleLabel = new Label("[" + currentUser.getRole() + "]");
        roleLabel.setStyle("-fx-text-fill: #8b949e; -fx-font-size: 11; -fx-padding: 0 16 12 16;");

        btnDashboard = navBtn("📊  Dashboard");
        btnDashboard.setOnAction(e -> { setActive(btnDashboard); showDashboard(); });

        // Role-based navigation
        if (currentUser.getRole() == Role.ADMIN) {
            btnStudents  = navBtn("🎓  Students");
            btnTeachers  = navBtn("👩‍🏫  Teachers");
            btnCourses   = navBtn("📚  Courses");
            btnEnroll    = navBtn("📋  Enrollment");
            btnGrades    = navBtn("📝  Grades");

            btnStudents .setOnAction(e -> { setActive(btnStudents);  showStudents(); });
            btnTeachers .setOnAction(e -> { setActive(btnTeachers);  showTeachers(); });
            btnCourses  .setOnAction(e -> { setActive(btnCourses);   showCourses(); });
            btnEnroll   .setOnAction(e -> { setActive(btnEnroll);    showEnrollment(); });
            btnGrades   .setOnAction(e -> { setActive(btnGrades);    showGrades(); });

        } else if (currentUser.getRole() == Role.TEACHER) {
            btnCourses  = navBtn("📚  My Courses");
            btnGrades   = navBtn("📝  Grade Students");

            btnCourses  .setOnAction(e -> { setActive(btnCourses);   showTeacherCourses(); });
            btnGrades   .setOnAction(e -> { setActive(btnGrades);    showGrades(); });

        } else { // STUDENT
            btnGrades   = navBtn("📝  My Report");
            btnGrades   .setOnAction(e -> { setActive(btnGrades);    showStudentReport(); });
        }

        Region spacer = new Region();
        VBox.setVgrow(spacer, Priority.ALWAYS);

        Button btnSave = new Button("💾  Save All Data");
        btnSave.getStyleClass().add("btn-secondary");
        btnSave.setMaxWidth(Double.MAX_VALUE);
        btnSave.setStyle("-fx-padding: 10 16;");
        btnSave.setOnAction(e -> saveAll());

        Button btnLogout = new Button("🚪  Logout");
        btnLogout.getStyleClass().add("btn-danger");
        btnLogout.setMaxWidth(Double.MAX_VALUE);
        btnLogout.setStyle("-fx-padding: 10 16;");
        btnLogout.setOnAction(e -> handleLogout());

        sidebar.getChildren().addAll(title, sub, sessionLabel, roleLabel, btnDashboard);

        if (currentUser.getRole() == Role.ADMIN) {
            sidebar.getChildren().addAll(btnStudents, btnTeachers, btnCourses, btnEnroll, btnGrades);
        } else if (currentUser.getRole() == Role.TEACHER) {
            sidebar.getChildren().addAll(btnCourses, btnGrades);
        } else {
            sidebar.getChildren().add(btnGrades);
        }

        sidebar.getChildren().addAll(spacer, btnSave, btnLogout);

        setActive(btnDashboard);
        return sidebar;
    }

    private Button navBtn(String text) {
        Button b = new Button(text);
        b.getStyleClass().add("nav-button");
        b.setMaxWidth(Double.MAX_VALUE);
        return b;
    }

    private void setActive(Button btn) {
        if (activeBtn != null) activeBtn.getStyleClass().remove("nav-button-active");
        btn.getStyleClass().add("nav-button-active");
        activeBtn = btn;
    }

    private void showDashboard() {
        VBox content = new VBox(20);
        content.getStyleClass().add("content-pane");

        Label title = new Label("Dashboard");
        title.getStyleClass().add("page-title");

        Label welcome = new Label("Welcome, " + currentUser.getFullName() + "  ·  Role: " + currentUser.getRole());
        welcome.setStyle("-fx-text-fill: #8b949e; -fx-font-size: 13;");

        // Stats row
        HBox stats = new HBox(16);
        stats.getChildren().addAll(
                statCard("👨‍🎓", String.valueOf(manager.getAllStudents().size()), "Students"),
                statCard("👩‍🏫", String.valueOf(manager.getAllTeachers().size()), "Teachers"),
                statCard("📚", String.valueOf(manager.getAllCourses().size()), "Courses")
        );

        // Recent students table (role-sensitive)
        Label recentLabel = new Label(currentUser.getRole() == Role.STUDENT ? "MY REPORT" : "ALL STUDENTS");
        recentLabel.getStyleClass().add("section-label");

        if (currentUser.getRole() == Role.STUDENT && currentUser.getLinkedStudentId() != null) {
            // Show only this student's report on dashboard
            try {
                String report = manager.getStudentReport(currentUser.getLinkedStudentId());
                Label reportLabel = new Label(report);
                reportLabel.getStyleClass().add("msg-info");
                reportLabel.setWrapText(true);
                content.getChildren().addAll(title, welcome, stats, recentLabel, reportLabel);
            } catch (Exception ex) {
                content.getChildren().addAll(title, welcome, stats,
                        new Label("Could not load student record: " + ex.getMessage()));
            }
        } else {
            TableView<Student> table = studentTable();
            table.setItems(FXCollections.observableArrayList(manager.getStudentsSortedByName()));
            VBox.setVgrow(table, Priority.ALWAYS);
            content.getChildren().addAll(title, welcome, stats, recentLabel, table);
        }

        root.setCenter(content);
    }

    private VBox statCard(String icon, String value, String label) {
        VBox card = new VBox(4);
        card.getStyleClass().add("card");
        card.setAlignment(Pos.CENTER);
        card.setPrefWidth(140);

        Label ico = new Label(icon);  ico.setStyle("-fx-font-size: 28px;");
        Label val = new Label(value); val.getStyleClass().add("stat-value");
        Label lbl = new Label(label); lbl.getStyleClass().add("stat-label");

        card.getChildren().addAll(ico, val, lbl);
        return card;
    }

    private void showStudents() {
        VBox content = new VBox(16);
        content.getStyleClass().add("content-pane");

        Label title = new Label("Students");
        title.getStyleClass().add("page-title");

        StatusBar status = new StatusBar();

        // Add form
        Label addLabel = new Label("ADD STUDENT");
        addLabel.getStyleClass().add("section-label");

        TextField fName = field("Full Name");
        TextField fAge  = field("Age");
        TextField fId   = field("Student ID");

        Button btnAdd = new Button("Add Student");
        btnAdd.getStyleClass().add("btn-primary");

        HBox addForm = new HBox(8, fName, fAge, fId, btnAdd);
        addForm.setAlignment(Pos.CENTER_LEFT);

        // Toolbar
        TextField fSearch   = field("Search by name…"); fSearch.setPrefWidth(220);
        Button btnSearch    = new Button("Search");    btnSearch.getStyleClass().add("btn-accent");
        Button btnSortName  = new Button("Sort A→Z");  btnSortName.getStyleClass().add("btn-secondary");
        Button btnSortGPA   = new Button("Sort GPA↓"); btnSortGPA.getStyleClass().add("btn-secondary");
        Button btnRemove    = new Button("Remove Selected"); btnRemove.getStyleClass().add("btn-danger");

        HBox toolbar = new HBox(8, fSearch, btnSearch, btnSortName, btnSortGPA, btnRemove);
        toolbar.setAlignment(Pos.CENTER_LEFT);

        // Table
        TableView<Student> table = studentTable();
        ObservableList<Student> data =
                FXCollections.observableArrayList(manager.getStudentsSortedByName());
        table.setItems(data);
        VBox.setVgrow(table, Priority.ALWAYS);

        // Wire — use InputValidator
        btnAdd.setOnAction(e -> {
            try {
                String name = InputValidator.validateName(fName.getText(), "Name");
                int    age  = InputValidator.validateAge(fAge.getText(), 5, 120);
                String id   = InputValidator.validateId(fId.getText(), "Student ID");

                manager.addStudent(name, age, id);
                refreshList(data, manager.getAllStudents());
                fName.clear(); fAge.clear(); fId.clear();
                status.showSuccess("Student '" + name + "' added.");
            } catch (IllegalArgumentException | SchoolSystemException ex) {
                status.showError(ex.getMessage());
            }
        });

        btnSearch.setOnAction(e -> {
            List<Student> result = manager.searchStudentsByName(fSearch.getText().trim());
            refreshList(data, result);
            status.showInfo(result.size() + " result(s) found.");
        });

        btnSortName.setOnAction(e -> {
            refreshList(data, manager.getStudentsSortedByName());
            status.showInfo("Sorted alphabetically.");
        });

        btnSortGPA.setOnAction(e -> {
            refreshList(data, manager.getStudentsSortedByGPA());
            status.showInfo("Sorted by GPA (highest first).");
        });

        btnRemove.setOnAction(e -> {
            Student sel = table.getSelectionModel().getSelectedItem();
            if (sel == null) { status.showError("Select a student first."); return; }
            try {
                manager.removeStudent(sel.getStudentId());
                refreshList(data, manager.getAllStudents());
                status.showSuccess("Student '" + sel.getName() + "' removed.");
            } catch (SchoolSystemException ex) {
                status.showError(ex.getMessage());
            }
        });

        fSearch.setOnAction(e -> btnSearch.fire());

        content.getChildren().addAll(title, addLabel, addForm, new Separator(), toolbar, table, status);
        root.setCenter(content);
    }

    // ═══════════════════════════════════════════════════════════
    // TEACHERS VIEW (ADMIN only)
    // ═══════════════════════════════════════════════════════════

    private void showTeachers() {
        VBox content = new VBox(16);
        content.getStyleClass().add("content-pane");

        Label title = new Label("Teachers");
        title.getStyleClass().add("page-title");

        StatusBar status = new StatusBar();

        Label addLabel = new Label("ADD TEACHER");
        addLabel.getStyleClass().add("section-label");

        TextField fName    = field("Full Name");
        TextField fAge     = field("Age");
        TextField fSubject = field("Subject / Department");

        Button btnAdd = new Button("Add Teacher");
        btnAdd.getStyleClass().add("btn-primary");

        HBox addForm = new HBox(8, fName, fAge, fSubject, btnAdd);
        addForm.setAlignment(Pos.CENTER_LEFT);

        TextField fSearch  = field("Search by name…"); fSearch.setPrefWidth(220);
        Button btnSearch   = new Button("Search");   btnSearch.getStyleClass().add("btn-accent");
        Button btnSort     = new Button("Sort A→Z"); btnSort.getStyleClass().add("btn-secondary");
        Button btnRemove   = new Button("Remove Selected"); btnRemove.getStyleClass().add("btn-danger");

        HBox toolbar = new HBox(8, fSearch, btnSearch, btnSort, btnRemove);
        toolbar.setAlignment(Pos.CENTER_LEFT);

        TableView<Teacher> table = teacherTable();
        ObservableList<Teacher> data =
                FXCollections.observableArrayList(manager.getTeachersSortedByName());
        table.setItems(data);
        VBox.setVgrow(table, Priority.ALWAYS);

        btnAdd.setOnAction(e -> {
            try {
                String name    = InputValidator.validateName(fName.getText(), "Name");
                int    age     = InputValidator.validateAge(fAge.getText(), 18, 120);
                String subject = InputValidator.validateText(fSubject.getText(), "Subject");
                manager.addTeacher(name, age, subject);
                refreshList(data, manager.getAllTeachers());
                fName.clear(); fAge.clear(); fSubject.clear();
                status.showSuccess("Teacher added.");
            } catch (IllegalArgumentException | SchoolSystemException ex) {
                status.showError(ex.getMessage());
            }
        });

        btnSearch.setOnAction(e -> {
            refreshList(data, manager.searchTeachersByName(fSearch.getText().trim()));
            status.showInfo("Search complete.");
        });

        btnSort.setOnAction(e -> {
            refreshList(data, manager.getTeachersSortedByName());
            status.showInfo("Sorted alphabetically.");
        });

        btnRemove.setOnAction(e -> {
            Teacher sel = table.getSelectionModel().getSelectedItem();
            if (sel == null) { status.showError("Select a teacher first."); return; }
            try {
                manager.removeTeacher(sel.getName());
                refreshList(data, manager.getAllTeachers());
                status.showSuccess("Teacher '" + sel.getName() + "' removed.");
            } catch (SchoolSystemException ex) {
                status.showError(ex.getMessage());
            }
        });

        fSearch.setOnAction(e -> btnSearch.fire());

        content.getChildren().addAll(title, addLabel, addForm, new Separator(), toolbar, table, status);
        root.setCenter(content);
    }

    // ═══════════════════════════════════════════════════════════
    // COURSES VIEW (ADMIN only)
    // ═══════════════════════════════════════════════════════════

    private void showCourses() {
        VBox content = new VBox(16);
        content.getStyleClass().add("content-pane");

        Label title = new Label("Courses");
        title.getStyleClass().add("page-title");

        StatusBar status = new StatusBar();

        Label addLabel = new Label("ADD COURSE");
        addLabel.getStyleClass().add("section-label");

        TextField fName = field("Course Name");
        ComboBox<String> cbTeacher = new ComboBox<>();
        cbTeacher.setPromptText("Select Teacher"); cbTeacher.setPrefWidth(180);
        refreshTeacherCombo(cbTeacher);

        Button btnAdd = new Button("Add Course");
        btnAdd.getStyleClass().add("btn-primary");

        HBox addForm = new HBox(8, fName, cbTeacher, btnAdd);
        addForm.setAlignment(Pos.CENTER_LEFT);

        TextField fSearch = field("Search courses…"); fSearch.setPrefWidth(220);
        Button btnSearch  = new Button("Search"); btnSearch.getStyleClass().add("btn-accent");
        Button btnSort    = new Button("Sort A→Z"); btnSort.getStyleClass().add("btn-secondary");
        Button btnRemove  = new Button("Remove Selected"); btnRemove.getStyleClass().add("btn-danger");

        HBox toolbar = new HBox(8, fSearch, btnSearch, btnSort, btnRemove);
        toolbar.setAlignment(Pos.CENTER_LEFT);

        TableView<Course> table = courseTable();
        ObservableList<Course> data =
                FXCollections.observableArrayList(manager.getCoursesSortedByName());
        table.setItems(data);
        VBox.setVgrow(table, Priority.ALWAYS);

        btnAdd.setOnAction(e -> {
            try {
                String course  = InputValidator.validateText(fName.getText(), "Course Name");
                String teacher = cbTeacher.getValue();
                if (teacher == null || teacher.isBlank()) { status.showError("Please select a teacher."); return; }
                manager.addCourse(course, teacher);
                refreshList(data, manager.getAllCourses());
                refreshTeacherCombo(cbTeacher);
                fName.clear(); cbTeacher.setValue(null);
                status.showSuccess("Course '" + course + "' added.");
            } catch (SchoolSystemException | IllegalArgumentException ex) {
                status.showError(ex.getMessage());
            }
        });

        btnSearch.setOnAction(e -> {
            refreshList(data, manager.searchCoursesByName(fSearch.getText().trim()));
            status.showInfo("Search complete.");
        });

        btnSort.setOnAction(e -> refreshList(data, manager.getCoursesSortedByName()));

        btnRemove.setOnAction(e -> {
            Course sel = table.getSelectionModel().getSelectedItem();
            if (sel == null) { status.showError("Select a course first."); return; }
            try {
                manager.removeCourse(sel.getCourseName());
                refreshList(data, manager.getAllCourses());
                status.showSuccess("Course removed.");
            } catch (SchoolSystemException ex) {
                status.showError(ex.getMessage());
            }
        });

        fSearch.setOnAction(e -> btnSearch.fire());

        content.getChildren().addAll(title, addLabel, addForm, new Separator(), toolbar, table, status);
        root.setCenter(content);
    }

    // ═══════════════════════════════════════════════════════════
    // ENROLLMENT VIEW (ADMIN only)
    // ═══════════════════════════════════════════════════════════

    private void showEnrollment() {
        VBox content = new VBox(16);
        content.getStyleClass().add("content-pane");

        Label title = new Label("Enrollment");
        title.getStyleClass().add("page-title");

        StatusBar status = new StatusBar();

        Label enrollLabel = new Label("ENROLL STUDENT IN COURSE");
        enrollLabel.getStyleClass().add("section-label");

        ComboBox<String> cbStudent = new ComboBox<>();
        cbStudent.setPromptText("Select Student"); cbStudent.setPrefWidth(200);
        refreshStudentCombo(cbStudent);

        ComboBox<String> cbCourse = new ComboBox<>();
        cbCourse.setPromptText("Select Course"); cbCourse.setPrefWidth(200);
        refreshCourseCombo(cbCourse);

        Button btnEnroll   = new Button("Enroll");   btnEnroll.getStyleClass().add("btn-primary");
        Button btnUnenroll = new Button("Unenroll"); btnUnenroll.getStyleClass().add("btn-danger");

        HBox enrollForm = new HBox(8, cbStudent, cbCourse, btnEnroll, btnUnenroll);
        enrollForm.setAlignment(Pos.CENTER_LEFT);

        Label rosterLabel = new Label("COURSE ROSTER");
        rosterLabel.getStyleClass().add("section-label");

        ComboBox<String> cbViewCourse = new ComboBox<>();
        cbViewCourse.setPromptText("Select Course to view roster"); cbViewCourse.setPrefWidth(220);
        refreshCourseCombo(cbViewCourse);

        Button btnView = new Button("View Roster"); btnView.getStyleClass().add("btn-accent");

        HBox rosterBar = new HBox(8, cbViewCourse, btnView);
        rosterBar.setAlignment(Pos.CENTER_LEFT);

        ListView<String> rosterList = new ListView<>();
        rosterList.setStyle("-fx-background-color: #161b22; -fx-control-inner-background: #161b22;");
        rosterList.setPrefHeight(220);

        btnEnroll.setOnAction(e -> {
            String sid = cbStudent.getValue(), cname = cbCourse.getValue();
            if (sid == null || cname == null) { status.showError("Please select both a student and a course."); return; }
            try {
                manager.enrollStudent(extractId(sid), cname);
                status.showSuccess(sid + " enrolled in " + cname + ".");
                refreshStudentCombo(cbStudent); refreshCourseCombo(cbCourse);
            } catch (SchoolSystemException ex) {
                status.showError(ex.getMessage());
            }
        });

        btnUnenroll.setOnAction(e -> {
            String sid = cbStudent.getValue(), cname = cbCourse.getValue();
            if (sid == null || cname == null) { status.showError("Please select both a student and a course."); return; }
            try {
                manager.unenrollStudent(extractId(sid), cname);
                status.showSuccess(sid + " unenrolled from " + cname + ".");
            } catch (SchoolSystemException ex) {
                status.showError(ex.getMessage());
            }
        });

        btnView.setOnAction(e -> {
            String cname = cbViewCourse.getValue();
            if (cname == null) { status.showError("Select a course."); return; }
            try {
                Course c = manager.findCourseByName(cname);
                ObservableList<String> roster = FXCollections.observableArrayList();
                if (c.getRoster().isEmpty()) {
                    roster.add("No students enrolled yet.");
                } else {
                    c.getRoster().forEach(s -> roster.add(
                            "[" + s.getStudentId() + "] " + s.getName() + " — age " + s.getAge()));
                }
                Platform.runLater(() -> rosterList.setItems(roster));
            } catch (SchoolSystemException ex) {
                status.showError(ex.getMessage());
            }
        });

        content.getChildren().addAll(title, enrollLabel, enrollForm,
                new Separator(), rosterLabel, rosterBar, rosterList, status);
        root.setCenter(content);
    }

    // ═══════════════════════════════════════════════════════════
    // GRADES VIEW (ADMIN + TEACHER)
    // ═══════════════════════════════════════════════════════════

    private void showGrades() {
        VBox content = new VBox(16);
        content.getStyleClass().add("content-pane");

        Label title = new Label("Grades");
        title.getStyleClass().add("page-title");

        StatusBar status = new StatusBar();

        // ── Add formative grade ───────────────────────────────
        Label fLabel = new Label("ADD FORMATIVE GRADE");
        fLabel.getStyleClass().add("section-label");

        ComboBox<String> cbTeacher = new ComboBox<>();
        cbTeacher.setPromptText("Teacher"); cbTeacher.setPrefWidth(160);

        // TEACHER role: lock to themselves
        if (currentUser.getRole() == Role.TEACHER && currentUser.getLinkedTeacherName() != null) {
            cbTeacher.setItems(FXCollections.observableArrayList(currentUser.getLinkedTeacherName()));
            cbTeacher.setValue(currentUser.getLinkedTeacherName());
            cbTeacher.setDisable(true);
        } else {
            refreshTeacherCombo(cbTeacher);
        }

        ComboBox<String> cbStudent = new ComboBox<>();
        cbStudent.setPromptText("Student"); cbStudent.setPrefWidth(160);
        refreshStudentCombo(cbStudent);

        TextField fTask  = field("Task name"); fTask.setPrefWidth(120);
        TextField fScore = field("Score");     fScore.setPrefWidth(70);
        TextField fMax   = field("Max");       fMax.setPrefWidth(70);

        Button btnGrade = new Button("Submit Grade"); btnGrade.getStyleClass().add("btn-primary");

        HBox gradeForm = new HBox(8, cbTeacher, cbStudent, fTask, fScore, fMax, btnGrade);
        gradeForm.setAlignment(Pos.CENTER_LEFT);

        // ── Set exam score ────────────────────────────────────
        Label eLabel = new Label("SET EXAM (SUMMATIVE) SCORE");
        eLabel.getStyleClass().add("section-label");

        ComboBox<String> cbExamStudent = new ComboBox<>();
        cbExamStudent.setPromptText("Student"); cbExamStudent.setPrefWidth(200);
        refreshStudentCombo(cbExamStudent);

        TextField fExam = field("Exam score (0–100)");
        Button btnExam  = new Button("Set Exam Score"); btnExam.getStyleClass().add("btn-accent");

        HBox examForm = new HBox(8, cbExamStudent, fExam, btnExam);
        examForm.setAlignment(Pos.CENTER_LEFT);

        // ── View student report ───────────────────────────────
        Label rLabel = new Label("VIEW STUDENT REPORT");
        rLabel.getStyleClass().add("section-label");

        ComboBox<String> cbReport = new ComboBox<>();
        cbReport.setPromptText("Student"); cbReport.setPrefWidth(200);
        refreshStudentCombo(cbReport);

        Button btnReport = new Button("View Report"); btnReport.getStyleClass().add("btn-secondary");

        Label reportOut = new Label();
        reportOut.getStyleClass().add("msg-info");
        reportOut.setWrapText(true);

        HBox reportForm = new HBox(8, cbReport, btnReport);
        reportForm.setAlignment(Pos.CENTER_LEFT);

        // Wire
        btnGrade.setOnAction(e -> {
            try {
                String tid  = cbTeacher.getValue();
                String sid  = cbStudent.getValue();
                String task = InputValidator.validateText(fTask.getText(), "Task name");
                if (tid == null || sid == null) throw new SchoolSystemException("Select teacher and student.");
                double score = InputValidator.validateScore(fScore.getText(), 0, Double.MAX_VALUE);
                double max   = InputValidator.validateScore(fMax.getText(), 1, Double.MAX_VALUE);
                manager.gradeStudent(tid, extractId(sid), task, score, max);
                fTask.clear(); fScore.clear(); fMax.clear();
                status.showSuccess("Grade recorded successfully.");
            } catch (IllegalArgumentException | SchoolSystemException ex) {
                status.showError(ex.getMessage());
            }
        });

        btnExam.setOnAction(e -> {
            try {
                String sid = cbExamStudent.getValue();
                if (sid == null) throw new SchoolSystemException("Select a student.");
                double score = InputValidator.validateScore(fExam.getText(), 0, 100);
                manager.setSummativeScore(extractId(sid), score);
                fExam.clear();
                status.showSuccess("Exam score set.");
            } catch (IllegalArgumentException | SchoolSystemException ex) {
                status.showError(ex.getMessage());
            }
        });

        btnReport.setOnAction(e -> {
            try {
                String sid = cbReport.getValue();
                if (sid == null) throw new SchoolSystemException("Select a student.");
                String report = manager.getStudentReport(extractId(sid));
                Platform.runLater(() -> reportOut.setText(report));
            } catch (SchoolSystemException ex) {
                status.showError(ex.getMessage());
            }
        });

        content.getChildren().addAll(title,
                fLabel, gradeForm, new Separator(),
                eLabel, examForm, new Separator(),
                rLabel, reportForm, reportOut, status);
        root.setCenter(content);
    }

    // ═══════════════════════════════════════════════════════════
    // TEACHER ROLE: MY COURSES VIEW
    // ═══════════════════════════════════════════════════════════

    private void showTeacherCourses() {
        VBox content = new VBox(16);
        content.getStyleClass().add("content-pane");

        Label title = new Label("My Courses");
        title.getStyleClass().add("page-title");

        StatusBar status = new StatusBar();

        if (currentUser.getLinkedTeacherName() == null) {
            content.getChildren().addAll(title,
                    new Label("Your account is not linked to a teacher record. Ask an admin to link you."));
            root.setCenter(content);
            return;
        }

        try {
            Teacher teacher = manager.findTeacherByName(currentUser.getLinkedTeacherName());
            Label info = new Label("Instructor: " + teacher.getName() + " | Department: " + teacher.getSubject());
            info.setStyle("-fx-text-fill: #8b949e;");

            TableView<Course> table = courseTable();
            ObservableList<Course> data = FXCollections.observableArrayList(teacher.getWorkload());
            table.setItems(data);
            VBox.setVgrow(table, Priority.ALWAYS);

            // Roster panel
            Label rosterLabel = new Label("COURSE ROSTER");
            rosterLabel.getStyleClass().add("section-label");

            ComboBox<String> cbCourse = new ComboBox<>();
            cbCourse.setPromptText("Select one of your courses"); cbCourse.setPrefWidth(240);
            teacher.getWorkload().forEach(c -> cbCourse.getItems().add(c.getCourseName()));

            Button btnView = new Button("View Roster"); btnView.getStyleClass().add("btn-accent");

            ListView<String> rosterList = new ListView<>();
            rosterList.setStyle("-fx-background-color: #161b22; -fx-control-inner-background: #161b22;");
            rosterList.setPrefHeight(160);

            btnView.setOnAction(e -> {
                String cname = cbCourse.getValue();
                if (cname == null) { status.showError("Select a course."); return; }
                try {
                    Course c = manager.findCourseByName(cname);
                    ObservableList<String> roster = FXCollections.observableArrayList();
                    if (c.getRoster().isEmpty()) {
                        roster.add("No students enrolled.");
                    } else {
                        c.getRoster().forEach(s -> roster.add(
                                "[" + s.getStudentId() + "] " + s.getName()));
                    }
                    Platform.runLater(() -> rosterList.setItems(roster));
                } catch (SchoolSystemException ex) {
                    status.showError(ex.getMessage());
                }
            });

            HBox rosterBar = new HBox(8, cbCourse, btnView);
            rosterBar.setAlignment(Pos.CENTER_LEFT);

            content.getChildren().addAll(title, info, table,
                    new Separator(), rosterLabel, rosterBar, rosterList, status);
        } catch (SchoolSystemException ex) {
            content.getChildren().addAll(title, new Label("Teacher record not found: " + ex.getMessage()));
        }

        root.setCenter(content);
    }

    // ═══════════════════════════════════════════════════════════
    // STUDENT ROLE: MY REPORT VIEW
    // ═══════════════════════════════════════════════════════════

    private void showStudentReport() {
        VBox content = new VBox(16);
        content.getStyleClass().add("content-pane");

        Label title = new Label("My Academic Report");
        title.getStyleClass().add("page-title");

        StatusBar status = new StatusBar();

        if (currentUser.getLinkedStudentId() == null) {
            content.getChildren().addAll(title,
                    new Label("Your account is not linked to a student record. Ask an admin to link you."));
            root.setCenter(content);
            return;
        }

        try {
            String report = manager.getStudentReport(currentUser.getLinkedStudentId());
            Student student = manager.findStudentById(currentUser.getLinkedStudentId());

            Label reportLabel = new Label(report);
            reportLabel.getStyleClass().add("msg-info");
            reportLabel.setWrapText(true);

            Label coursesLabel = new Label("ENROLLED COURSES");
            coursesLabel.getStyleClass().add("section-label");

            ListView<String> coursesList = new ListView<>();
            ObservableList<String> courses = FXCollections.observableArrayList();
            if (student.getEnrolledCourseNames().isEmpty()) {
                courses.add("You are not enrolled in any courses.");
            } else {
                courses.addAll(student.getEnrolledCourseNames());
            }
            coursesList.setItems(courses);
            coursesList.setPrefHeight(160);
            coursesList.setStyle("-fx-background-color: #161b22; -fx-control-inner-background: #161b22;");

            content.getChildren().addAll(title, reportLabel, coursesLabel, coursesList, status);
        } catch (SchoolSystemException ex) {
            content.getChildren().addAll(title, new Label("Error: " + ex.getMessage()));
        }

        root.setCenter(content);
    }

    // ═══════════════════════════════════════════════════════════
    // LOGOUT  (Inspired by HealthSystem's handleLogout)
    // ═══════════════════════════════════════════════════════════

    private void handleLogout() {
        try {
            manager.saveAll();
            AppLogger.log("INFO", "Logout: " + currentUser.getUsername());

            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/com/school/fxml/auth-view.fxml")
            );
            Scene scene = new Scene(loader.load(), 460, 480);
            String css = getClass().getResource("/com/school/css/styles.css").toExternalForm();
            scene.getStylesheets().add(css);

            Stage stage = (Stage) root.getScene().getWindow();
            stage.setTitle("🏫 School Management System — Login");
            stage.setScene(scene);
            stage.setResizable(false);
        } catch (IOException e) {
            AppLogger.log("ERROR", "Logout failed: " + e.getMessage());
        }
    }

    // ═══════════════════════════════════════════════════════════
    // SAVE ALL
    // ═══════════════════════════════════════════════════════════

    private void saveAll() {
        try {
            manager.saveAll();
            AppLogger.log("INFO", "Manual save triggered by: " + currentUser.getUsername());
        } catch (Exception e) {
            AppLogger.log("ERROR", "Save failed: " + e.getMessage());
        }
    }

    // ═══════════════════════════════════════════════════════════
    // TABLE BUILDERS
    // ═══════════════════════════════════════════════════════════

    private TableView<Student> studentTable() {
        TableView<Student> t = new TableView<>();
        t.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        t.setPlaceholder(new Label("No students found."));

        TableColumn<Student, String> colId   = col("ID",      150);
        TableColumn<Student, String> colName = col("Name",    200);
        TableColumn<Student, String> colAge  = col("Age",      80);
        TableColumn<Student, String> colGPA  = col("GPA",     120);
        TableColumn<Student, String> colCrs  = col("Courses", 200);

        colId  .setCellValueFactory(d -> new SimpleStringProperty(d.getValue().getStudentId()));
        colName.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().getName()));
        colAge .setCellValueFactory(d -> new SimpleStringProperty(String.valueOf(d.getValue().getAge())));
        colGPA .setCellValueFactory(d -> {
            double gpa = d.getValue().calculateGPA(0, 0);
            return new SimpleStringProperty(String.format("%.1f%% (%s)", gpa, gpa >= 50 ? "✓" : "⚠"));
        });
        colCrs .setCellValueFactory(d -> new SimpleStringProperty(
                d.getValue().getEnrolledCourseNames().isEmpty()
                        ? "None" : String.join(", ", d.getValue().getEnrolledCourseNames())));

        t.getColumns().addAll(colId, colName, colAge, colGPA, colCrs);
        return t;
    }

    private TableView<Teacher> teacherTable() {
        TableView<Teacher> t = new TableView<>();
        t.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        t.setPlaceholder(new Label("No teachers found."));

        TableColumn<Teacher, String> colName    = col("Name",    200);
        TableColumn<Teacher, String> colAge     = col("Age",      80);
        TableColumn<Teacher, String> colSubject = col("Subject", 200);
        TableColumn<Teacher, String> colCourses = col("Courses", 200);

        colName   .setCellValueFactory(d -> new SimpleStringProperty(d.getValue().getName()));
        colAge    .setCellValueFactory(d -> new SimpleStringProperty(String.valueOf(d.getValue().getAge())));
        colSubject.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().getSubject()));
        colCourses.setCellValueFactory(d -> new SimpleStringProperty(
                d.getValue().getWorkload().isEmpty() ? "None"
                : String.join(", ", d.getValue().getWorkload().stream().map(Course::getCourseName).toList())));

        t.getColumns().addAll(colName, colAge, colSubject, colCourses);
        return t;
    }

    private TableView<Course> courseTable() {
        TableView<Course> t = new TableView<>();
        t.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        t.setPlaceholder(new Label("No courses found."));

        TableColumn<Course, String> colName     = col("Course Name", 200);
        TableColumn<Course, String> colTeacher  = col("Teacher",     200);
        TableColumn<Course, String> colCount    = col("Students",    100);
        TableColumn<Course, String> colSessions = col("Sessions",    100);

        colName    .setCellValueFactory(d -> new SimpleStringProperty(d.getValue().getCourseName()));
        colTeacher .setCellValueFactory(d -> new SimpleStringProperty(d.getValue().getTeacher().getName()));
        colCount   .setCellValueFactory(d -> new SimpleStringProperty(String.valueOf(d.getValue().getRoster().size())));
        colSessions.setCellValueFactory(d -> new SimpleStringProperty(String.valueOf(d.getValue().getTotalSessions())));

        t.getColumns().addAll(colName, colTeacher, colCount, colSessions);
        return t;
    }

    private <T> TableColumn<T, String> col(String title, int minW) {
        TableColumn<T, String> c = new TableColumn<>(title);
        c.setMinWidth(minW);
        return c;
    }

    // ═══════════════════════════════════════════════════════════
    // HELPERS
    // ═══════════════════════════════════════════════════════════

    private TextField field(String prompt) {
        TextField tf = new TextField();
        tf.setPromptText(prompt);
        tf.setPrefWidth(160);
        return tf;
    }

    private <T> void refreshList(ObservableList<T> obs, List<T> fresh) {
        Platform.runLater(() -> {
            obs.clear();
            obs.addAll(fresh);
        });
    }

    private void refreshStudentCombo(ComboBox<String> cb) {
        ObservableList<String> items = FXCollections.observableArrayList();
        manager.getAllStudents().forEach(s -> items.add(s.getName() + " [" + s.getStudentId() + "]"));
        cb.setItems(items);
    }

    private void refreshTeacherCombo(ComboBox<String> cb) {
        ObservableList<String> items = FXCollections.observableArrayList();
        manager.getAllTeachers().forEach(t -> items.add(t.getName()));
        cb.setItems(items);
    }

    private void refreshCourseCombo(ComboBox<String> cb) {
        ObservableList<String> items = FXCollections.observableArrayList();
        manager.getAllCourses().forEach(c -> items.add(c.getCourseName()));
        cb.setItems(items);
    }

    /** Extract student ID from "Name [ID]" combo format */
    private String extractId(String combo) {
        int start = combo.lastIndexOf('[');
        int end   = combo.lastIndexOf(']');
        if (start >= 0 && end > start) return combo.substring(start + 1, end);
        return combo;
    }
}
