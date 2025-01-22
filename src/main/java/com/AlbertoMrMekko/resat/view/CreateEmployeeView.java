package com.AlbertoMrMekko.resat.view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import org.springframework.stereotype.Component;

@Component
public class CreateEmployeeView
{
    private final BorderPane root;

    public CreateEmployeeView(BorderPane root)
    {
        this.root = root;
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

        Label nameLabel = new Label("Nombre: ");
        TextField nameField = new TextField();
        nameField.setPrefWidth(200);
        nameField.setPromptText("Introduce tu nombre");

        Label dniLabel = new Label("DNI: ");
        TextField dniField = new TextField();
        dniField.setPrefWidth(200);
        dniField.setPromptText("Introduce tu DNI");

        Label password1Label = new Label("Contraseña: ");
        PasswordField password1Field = new PasswordField();
        password1Field.setPrefWidth(200);
        password1Field.setPromptText("Introduce tu nueva contraseña");

        Label password2Label = new Label("Repetir contraseña: ");
        PasswordField password2Field = new PasswordField();
        password2Field.setPrefWidth(200);
        password2Field.setPromptText("Repite la nueva contraseña");

        formFields.add(nameLabel, 0, 0);
        formFields.add(nameField, 1, 0);
        formFields.add(dniLabel, 0, 1);
        formFields.add(dniField, 1, 1);
        formFields.add(password1Label, 0, 2);
        formFields.add(password1Field, 1, 2);
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
            //clearDynamicContent(root);
        });
        createEmployeeButton.setOnAction(event -> {
            System.out.println("Crear nuevo empleado");
            System.out.println("Actualizar barra lateral");
            /*if (validateNewEmployeeInputs())
            {
                System.out.println("Crear nuevo empleado");
                System.out.println("Actualizar barra lateral");
                System.out.println("Clear dynamic content");
            }
            else
            {
                System.out.println("Mostrar mensaje con problema");
            }*/
        });

        return buttons;
    }
}
