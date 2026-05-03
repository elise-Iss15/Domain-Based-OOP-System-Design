module com.school {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;
    requires java.base;

    opens com.school.ui         to javafx.graphics, javafx.fxml;
    opens com.school.controller to javafx.fxml;
    opens com.school.model      to javafx.base;
    opens com.school.component  to javafx.fxml;

    exports com.school.ui;
    exports com.school.model;
    exports com.school.exception;
    exports com.school.manager;
    exports com.school.controller;
    exports com.school.component;
    exports com.school.util;
}
