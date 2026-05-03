package com.school.controller;

import com.school.manager.AuthService;
import com.school.manager.PersistenceService;
import com.school.model.SchoolUser;
import com.school.model.SchoolUser.Role;
import com.school.util.AppLogger;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;


public class AuthController {

    private final PersistenceService persistence = new PersistenceService();
    private final AuthService authService        = new AuthService(persistence);

    @FXML private Label statusLabel;

    // Login fields
    @FXML private TextField   loginUsernameField;
    @FXML private PasswordField loginPasswordField;

    // Register fields
    @FXML private TextField    regUsername;
    @FXML private PasswordField regPassword;
    @FXML private TextField    regFullName;
    @FXML private ComboBox<String> regRole;
    @FXML private TextField    regLinkedId;

    @FXML
    private void toggleLinkedField() {
        String selected = regRole.getValue();
        boolean show = "TEACHER".equals(selected) || "STUDENT".equals(selected);
        regLinkedId.setVisible(show);
        regLinkedId.setManaged(show);
        if ("STUDENT".equals(selected)) {
            regLinkedId.setPromptText("Student ID (to link this account to a student)");
        } else if ("TEACHER".equals(selected)) {
            regLinkedId.setPromptText("Teacher Name (to link this account to a teacher record)");
        }
    }

    @FXML
    private void handleLogin() {
        try {
            SchoolUser user = authService.login(
                    loginUsernameField.getText(),
                    loginPasswordField.getText()
            );
            openMainView(user);
        } catch (Exception e) {
            showError(e.getMessage());
        }
    }

    @FXML
    private void handleSignup() {
        try {
            String roleStr = regRole.getValue();
            if (roleStr == null) throw new IllegalArgumentException("Please select an account type.");

            Role role = Role.valueOf(roleStr);
            SchoolUser user = authService.signup(
                    regUsername.getText(),
                    regPassword.getText(),
                    role,
                    regFullName.getText()
            );

            // Set linked domain reference if provided
            String linked = regLinkedId.getText().trim();
            if (!linked.isEmpty()) {
                if (role == Role.STUDENT) user.setLinkedStudentId(linked);
                if (role == Role.TEACHER) user.setLinkedTeacherName(linked);
                persistence.saveUsers(authService.getUsers());
            }

            showSuccess("Account created! Logging you in…");
            openMainView(user);
        } catch (Exception e) {
            showError(e.getMessage());
        }
    }

    private void openMainView(SchoolUser user) {
        MainController controller = new MainController(user);
        Scene scene = new Scene(controller.buildUI(), 1140, 700);

        String css = getClass().getResource("/com/school/css/styles.css").toExternalForm();
        scene.getStylesheets().add(css);

        Stage stage = (Stage) loginUsernameField.getScene().getWindow();
        stage.setTitle("🏫 School Management System — " + user.getFullName() + " [" + user.getRole() + "]");
        stage.setScene(scene);
        stage.setMinWidth(900);
        stage.setMinHeight(600);
        stage.centerOnScreen();

        AppLogger.log("INFO", "Dashboard opened for: " + user.getUsername());
    }

    private void showSuccess(String msg) {
        statusLabel.setText("✓ " + msg);
        statusLabel.setStyle("-fx-text-fill: #3fb950;");
    }

    private void showError(String msg) {
        statusLabel.setText("✗ " + msg);
        statusLabel.setStyle("-fx-text-fill: #f85149;");
    }
}
