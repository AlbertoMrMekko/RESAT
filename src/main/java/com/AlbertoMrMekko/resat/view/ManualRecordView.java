package com.AlbertoMrMekko.resat.view;

import com.AlbertoMrMekko.resat.exceptions.ResatException;
import com.AlbertoMrMekko.resat.service.NotificationService;
import com.AlbertoMrMekko.resat.service.RecordService;
import com.AlbertoMrMekko.resat.utils.ValidationResult;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Component
public class ManualRecordView
{
    private final BorderPane root;

    private final ViewManager viewManager;

    private final RecordService recordService;

    private final NotificationService notificationService;

    private DatePicker dateField;

    private ComboBox<String> hourComboBox;

    private ComboBox<String> minuteComboBox;

    private ToggleGroup actionGroup;

    public ManualRecordView(final BorderPane root, @Lazy final ViewManager viewManager,
                            final RecordService recordService, final NotificationService notificationService)
    {
        this.root = root;
        this.viewManager = viewManager;
        this.recordService = recordService;
        this.notificationService = notificationService;
    }

    public void show()
    {
        BorderPane manualRecordLayout = new BorderPane();
        Label title = new Label("Registro manual");
        title.getStyleClass().add("title-label");
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
        formFields.getStyleClass().add("form-fields");

        Label dateLabel = new Label("Día: ");
        dateField = new DatePicker();
        dateField.getStyleClass().add("text-field");
        dateField.getEditor().setDisable(true);
        dateField.getEditor().setOpacity(1);

        Label timeLabel = new Label("Hora: ");
        hourComboBox = new ComboBox<>(FXCollections.observableArrayList(generateHours()));
        hourComboBox.setPromptText("Hora");
        minuteComboBox = new ComboBox<>(FXCollections.observableArrayList(generateMinutes()));
        minuteComboBox.setPromptText("Minuto");

        Label actionLabel = new Label("Acción: ");
        actionGroup = new ToggleGroup();
        RadioButton entryButton = new RadioButton("Entrar");
        entryButton.getStyleClass().add("radio-button");
        entryButton.setToggleGroup(actionGroup);
        RadioButton exitButton = new RadioButton("Salir");
        entryButton.getStyleClass().add("radio-button");
        exitButton.setToggleGroup(actionGroup);

        HBox timeBox = new HBox(45);
        timeBox.getChildren().add(0, hourComboBox);
        timeBox.getChildren().add(1, minuteComboBox);

        HBox actionBox = new HBox(45);
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
        Button cancelButton = new Button("Cancelar");
        cancelButton.getStyleClass().add("cancel-button");
        cancelButton.setOnAction(event -> {
            this.viewManager.clearDynamicContent();
            this.viewManager.showEmployeeActionsView();
        });

        Button acceptButton = new Button("Aceptar");
        HBox.setHgrow(acceptButton, Priority.ALWAYS);
        acceptButton.getStyleClass().add("accept-button");
        acceptButton.setOnAction(event -> {
            LocalDate date = dateField.getValue() != null ? LocalDate.parse(dateField.getValue().toString()) : null;
            String hour = hourComboBox.getValue() != null ? hourComboBox.getValue() : null;
            String minute = minuteComboBox.getValue() != null ? minuteComboBox.getValue() : null;
            RadioButton selectedAction = (RadioButton) actionGroup.getSelectedToggle();
            String action = selectedAction != null ? selectedAction.getText() : null;

            ValidationResult validationResult = this.recordService.validateManualRecord(date, hour, minute, action);
            if (validationResult.valid())
            {
                LocalDateTime datetime = LocalDateTime.of(date, LocalTime.of(Integer.parseInt(hour),
                        Integer.parseInt(minute)));
                String actionRecord = action.equals("Entrar") ? "Entrada" : "Salida";
                boolean confirmation = this.viewManager.showManualRecordConfirmationView(actionRecord, datetime);
                if (confirmation)
                {
                    boolean authenticated = this.viewManager.showAuthenticationView();
                    if (authenticated)
                    {
                        try
                        {
                            this.recordService.manualRecord(actionRecord, datetime);
                            this.viewManager.showSidebarView();
                            this.viewManager.showEmployeeActionsView();
                            this.notificationService.showInfoAlert("Registro", "El registro se realizado con éxito");
                        } catch (ResatException ex)
                        {
                            this.viewManager.showManualRecordView();
                            this.notificationService.showErrorAlert("Registro", "Error en el registro.\n" + ex.getErrorMessage());
                        }
                    }
                }
            }
            else
            {
                this.notificationService.showErrorAlert("Error en el registro manual", validationResult.errorMsg());
            }
        });

        HBox buttons = new HBox(20, cancelButton, acceptButton);
        buttons.getStyleClass().add("form-buttons");

        return buttons;
    }

    private String[] generateHours()
    {
        String[] hours = new String[24];
        for (int i = 0; i < 24; i++)
        {
            hours[i] = String.format("%02d", i);
        }
        return hours;
    }

    private String[] generateMinutes()
    {
        String[] minutes = new String[60];
        for (int i = 0; i < 60; i++)
        {
            minutes[i] = String.format("%02d", i);
        }
        return minutes;
    }
}
