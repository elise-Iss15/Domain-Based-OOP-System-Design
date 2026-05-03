package com.school.component;

import javafx.animation.PauseTransition;
import javafx.scene.control.Label;
import javafx.util.Duration;


public class StatusBar extends Label {

    public StatusBar() {
        setWrapText(true);
        setMinHeight(20);
    }

    public void showSuccess(String msg) {
        setText("✓ " + msg);
        getStyleClass().removeAll("msg-error", "msg-info");
        getStyleClass().add("msg-success");
        autoClear();
    }

    public void showError(String msg) {
        setText("✗ " + msg);
        getStyleClass().removeAll("msg-success", "msg-info");
        getStyleClass().add("msg-error");
        autoClear();
    }

    public void showInfo(String msg) {
        setText("ℹ " + msg);
        getStyleClass().removeAll("msg-success", "msg-error");
        getStyleClass().add("msg-info");
    }

    public void clear() {
        setText("");
        getStyleClass().removeAll("msg-success", "msg-error", "msg-info");
    }

    private void autoClear() {
        PauseTransition pause = new PauseTransition(Duration.seconds(5));
        pause.setOnFinished(e -> clear());
        pause.play();
    }
}
