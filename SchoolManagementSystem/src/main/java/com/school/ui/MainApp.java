package com.school.ui;

import com.school.util.AppLogger;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;


public class MainApp extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader loader = new FXMLLoader(
                getClass().getResource("/com/school/fxml/auth-view.fxml")
        );
        Scene scene = new Scene(loader.load(), 460, 480);

        String css = getClass().getResource("/com/school/css/styles.css").toExternalForm();
        scene.getStylesheets().add(css);

        stage.setTitle("🏫 School Management System — Login");
        stage.setScene(scene);
        stage.setResizable(false);

        stage.setOnCloseRequest(e -> {
            AppLogger.log("INFO", "Application closed.");
            Platform.exit();
        });

        stage.show();
        AppLogger.log("INFO", "School Management System started.");
    }

    public static void main(String[] args) {
        launch(args);
    }
}
