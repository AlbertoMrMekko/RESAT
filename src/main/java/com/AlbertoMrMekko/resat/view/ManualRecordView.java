package com.AlbertoMrMekko.resat.view;

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
        dateField = new DatePicker();
        dateField.setPrefWidth(200);
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
            // TODO pasos para registro manual:
            //  1. obtener inputs
            //  2. verificar inputs
            //  3. confirmar
            //  4. autenticar
            //  5. registrar
            //  6. mostrar acciones empleado

            LocalDate date = dateField.getValue() != null ? LocalDate.parse(dateField.getValue().toString()) : null;
            String hour = hourComboBox.getValue() != null ? hourComboBox.getValue() : null;
            String minute = minuteComboBox.getValue() != null ? minuteComboBox.getValue() : null;
            RadioButton selectedAction = (RadioButton) actionGroup.getSelectedToggle();
            String action = selectedAction != null ? selectedAction.getText() : null;
            System.out.println(date + " , " + hour + " , " + minute + " , " + action);

            ValidationResult validationResult = this.recordService.validateManualRecord(date, hour, minute, action);
            if (validationResult.valid())
            {
                LocalDateTime datetime = LocalDateTime.of(date, LocalTime.of(Integer.parseInt(hour), Integer.parseInt(minute)));
                boolean confirmation = this.viewManager.showManualRecordConfirmationView(action, datetime);
                if (confirmation)
                {
                    boolean authenticated = this.viewManager.showAuthenticationView();
                    if (authenticated)
                    {
                        this.recordService.manualRecord();
                    }
                }

                // TODO => confirmacion, autenticacion, registro, actualizar barra lateral, mostrar acciones empleado
                // this.recordService.manualRecord();
                // this.viewManager.showSidebarView();
                // this.viewManager.clearDynamicContent();
                this.notificationService.showInfoAlert("Registro", "El registro se ha llevado a cabo con éxito");
            }
            else
            {
                this.notificationService.showErrorAlert("Error en el registro manual", validationResult.errorMsg());
            }
        });

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
