package com.AlbertoMrMekko.resat.view;

import com.AlbertoMrMekko.resat.model.EmployeeRecord;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalTime;

@Component
public class ManualRecordView
{
    private final BorderPane root;

    private final ViewManager viewManager;

    public ManualRecordView(final BorderPane root, @Lazy final ViewManager viewManager)
    {
        this.root = root;
        this.viewManager = viewManager;
    }

    public void show()
    {
        BorderPane manualRecordLayout = new BorderPane();
        Label title = new Label("Registro manual");
        title.setStyle("-fx-font-size: 34px; -fx-font-weight: bold;");
        BorderPane.setMargin(title, new Insets(50, 0, 50, 0));
        BorderPane.setAlignment(title, Pos.CENTER);

        GridPane formFields = getFormFields();

        HBox formButtons = getFormButtons();
        formButtons.setPadding(new Insets(50));

        VBox centralPane = new VBox(formFields, formButtons);
        centralPane.setAlignment(Pos.TOP_CENTER);

        manualRecordLayout.setTop(title);
        manualRecordLayout.setCenter(centralPane);

        this.root.setCenter(manualRecordLayout);
    }

    private GridPane getFormFields()
    {
        GridPane formFields = new GridPane();
        formFields.setPadding(new Insets(20));
        formFields.setHgap(10);
        formFields.setVgap(10);
        formFields.setAlignment(Pos.CENTER);

        Label dateLabel = new Label("Día: ");
        DatePicker dateField = new DatePicker();
        dateField.setPrefWidth(200);
        dateField.setPromptText("dd/mm/aaaa");

        Label timeLabel = new Label("Hora: ");
        ComboBox<String> hourComboBox = new ComboBox<>(FXCollections.observableArrayList(generateHours()));
        ComboBox<String> minuteComboBox = new ComboBox<>(FXCollections.observableArrayList(generateMinutes()));
        hourComboBox.setPromptText("Hora");
        minuteComboBox.setPromptText("Minuto");

        Label actionLabel = new Label("Acción: ");
        ToggleGroup actionGroup = new ToggleGroup();
        RadioButton entryButton = new RadioButton("Entrar");
        entryButton.setToggleGroup(actionGroup);
        RadioButton exitButton = new RadioButton("Salir");
        exitButton.setToggleGroup(actionGroup);

        HBox timeBox = new HBox();
        timeBox.getChildren().add(0, hourComboBox);
        timeBox.getChildren().add(1, minuteComboBox);

        HBox actionBox = new HBox();
        actionBox.getChildren().add(0, entryButton);
        actionBox.getChildren().add(1, exitButton);

        formFields.add(dateLabel, 0, 0);
        formFields.add(dateField, 1, 0);
        formFields.add(timeLabel, 0, 3);
        formFields.add(timeBox, 1, 3);
        formFields.add(actionLabel, 0, 6);
        formFields.add(actionBox, 1, 6);

        return formFields;
    }

    private HBox getFormButtons()
    {
        HBox buttons = new HBox(10);
        buttons.setPadding(new Insets(10));
        buttons.setAlignment(Pos.CENTER);
        buttons.setSpacing(5);

        Button cancelButton = new Button("Cancelar");
        Button acceptButton = new Button("Aceptar");

        HBox.setHgrow(acceptButton, Priority.ALWAYS);
        buttons.getChildren().addAll(cancelButton, acceptButton);

        cancelButton.setOnAction(event -> {
            this.viewManager.clearDynamicContent();
            this.viewManager.showEmployeeActionsView();
        });
        acceptButton.setOnAction(event -> {
            // fixme with inputs
            String employeeDni = "";
            String action = "";
            LocalDate date = LocalDate.now();
            LocalTime time = LocalTime.now();
            EmployeeRecord record = new EmployeeRecord(employeeDni, action, date, time);
            this.viewManager.showManualRecordConfirmationView(record);
        });

        return buttons;
    }

    private String[] generateHours() {
        String[] hours = new String[24];
        for (int i = 0; i < 24; i++) {
            hours[i] = String.format("%02d", i);
        }
        return hours;
    }

    private String[] generateMinutes() {
        String[] minutes = new String[60];
        for (int i = 0; i < 60; i++) {
            minutes[i] = String.format("%02d", i);
        }
        return minutes;
    }
}
