package com.school.component;

import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;


public class FormField extends VBox {

    private final TextField field;

    public FormField(String labelText, String promptText) {
        setSpacing(4);

        Label lbl = new Label(labelText.toUpperCase());
        lbl.getStyleClass().add("section-label");

        field = new TextField();
        field.setPromptText(promptText);
        field.setPrefWidth(200);

        getChildren().addAll(lbl, field);
    }

    public String getValue() {
        return field.getText().trim();
    }

    public void clear() {
        field.clear();
    }

    public TextField getTextField() {
        return field;
    }
}
