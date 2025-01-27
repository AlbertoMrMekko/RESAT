package com.AlbertoMrMekko.resat.view;

import com.AlbertoMrMekko.resat.service.EmployeeService;
import com.AlbertoMrMekko.resat.service.NotificationService;
import com.AlbertoMrMekko.resat.utils.ValidationResult;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

@Component
public class CreateEmployeeView
{
    private final BorderPane root;

    private final ViewManager viewManager;

    private final EmployeeService employeeService;

    private final NotificationService notificationService;

    private TextField nameField;

    private TextField dniField;

    private PasswordField passwordField;

    private PasswordField password2Field;

    public CreateEmployeeView(final BorderPane root, @Lazy final ViewManager viewManager,
                              final EmployeeService employeeService,
                              final NotificationService notificationService)
    {
        this.root = root;
        this.viewManager = viewManager;
        this.employeeService = employeeService;
        this.notificationService = notificationService;
    }

    public void show()
    {
        BorderPane formLayout = new BorderPane();
        Label title = new Label("Nuevo empleado");
        title.setStyle("-fx-font-size: 34px; -fx-font-weight: bold;");
        BorderPane.setMargin(title, new Insets(50, 0, 50, 0));
        BorderPane.setAlignment(title, Pos.CENTER);

        GridPane formFields = getFormFields();

        HBox formButtons = getFormButtons();
        formButtons.setPadding(new Insets(50));

        VBox centralPane = new VBox(formFields, formButtons);
        centralPane.setAlignment(Pos.TOP_CENTER);

        formLayout.setTop(title);
        formLayout.setCenter(centralPane);

        this.root.setCenter(formLayout);
    }

    private GridPane getFormFields()
    {
        GridPane formFields = new GridPane();
        formFields.setPadding(new Insets(20));
        formFields.setHgap(10);
        formFields.setVgap(10);
        formFields.setAlignment(Pos.CENTER);

        Label nameLabel = new Label("Nombre ");
        nameField = new TextField();
        nameField.setPrefWidth(200);
        nameField.setPromptText("Ej. Daniel");

        Label dniLabel = new Label("DNI ");
        dniField = new TextField();
        dniField.setPrefWidth(200);
        dniField.setPromptText("Ej. 12345678Z");

        Label password1Label = new Label("Contraseña: ");
        passwordField = new PasswordField();
        passwordField.setPrefWidth(200);
        passwordField.setPromptText("Introduce tu nueva contraseña");

        Label password2Label = new Label("Repetir contraseña: ");
        password2Field = new PasswordField();
        password2Field.setPrefWidth(200);
        password2Field.setPromptText("Repite la nueva contraseña");

        formFields.add(nameLabel, 0, 0);
        formFields.add(nameField, 1, 0);
        formFields.add(dniLabel, 0, 1);
        formFields.add(dniField, 1, 1);
        formFields.add(password1Label, 0, 2);
        formFields.add(passwordField, 1, 2);
        formFields.add(password2Label, 0, 3);
        formFields.add(password2Field, 1, 3);

        return formFields;
    }

    private HBox getFormButtons()
    {
        HBox buttons = new HBox(10);
        buttons.setPadding(new Insets(10));
        buttons.setAlignment(Pos.CENTER);
        buttons.setSpacing(5);

        Button cancelButton = new Button("Cancelar");
        Button createEmployeeButton = new Button("Crear empleado");

        HBox.setHgrow(createEmployeeButton, Priority.ALWAYS);
        buttons.getChildren().addAll(cancelButton, createEmployeeButton);

        cancelButton.setOnAction(event -> {
            this.viewManager.clearDynamicContent();
        });
        createEmployeeButton.setOnAction(event -> {
            String name = nameField.getText();
            String dni = dniField.getText();
            String password = passwordField.getText();
            String password2 = password2Field.getText();
            ValidationResult validationResult = this.employeeService.validateCreateEmployee(name, dni, password,
                    password2);
            if (validationResult.valid())
            {
                this.employeeService.createEmployee(dni, name, password);
                this.viewManager.showSidebarView();
                this.viewManager.clearDynamicContent();
                this.notificationService.showInfoAlert("Creación de empleado", "El empleado " + name + " se ha creado" +
                        " correctamente");
            }
            else
            {
                this.notificationService.showErrorAlert("Error en la creación de empleado",
                        validationResult.errorMsg());
            }

        });

        return buttons;
    }


}
