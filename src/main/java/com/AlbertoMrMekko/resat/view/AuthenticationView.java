package com.AlbertoMrMekko.resat.view;

import com.AlbertoMrMekko.resat.SelectedEmployeeManager;
import com.AlbertoMrMekko.resat.service.AuthenticationService;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class AuthenticationView
{
    private final SelectedEmployeeManager selectedEmployeeManager;

    private final AuthenticationService authenticationService;

    public AuthenticationView(final SelectedEmployeeManager selectedEmployeeManager,
                              final AuthenticationService authenticationService)
    {
        this.selectedEmployeeManager = selectedEmployeeManager;
        this.authenticationService = authenticationService;
    }

    public void show()
    {
        Stage authenticationStage = new Stage();
        authenticationStage.initModality(Modality.APPLICATION_MODAL);

        BorderPane rootNode = new BorderPane();

        Label title = new Label("Identifícate");

        VBox content = new VBox(10);
        Label text = new Label("Introduce tu contraseña para validar la acción");
        PasswordField passwordField = new PasswordField();
        passwordField.setOnAction(event -> {
            if (this.authenticationService.validatePassword(passwordField.getText(), this.selectedEmployeeManager.getSelectedEmployee().getPassword()))
            {
                System.out.println("Accion validada, continuar");
            }
            else
            {
                System.out.println("Mostrar mensaje contraseña errónea");
            }
        });

        content.getChildren().addAll(text, passwordField);

        HBox buttons = new HBox();

        Button back = new Button("Atrás");
        back.setOnAction(event -> {
            authenticationStage.close();
        });

        Button accept = new Button("Aceptar");

        // TODO código duplicado, crear método para unificar acción con botón Aceptar y pulsar Enter
        accept.setOnAction(event -> {
            System.out.println("Hacer lo mismo que en passwordField.setOnAction");
        });

        buttons.getChildren().addAll(back, accept);

        rootNode.setTop(title);
        rootNode.setCenter(content);
        rootNode.setBottom(buttons);
        Scene scene = new Scene(rootNode, 400, 300);

        authenticationStage.setTitle("RESAT");
        authenticationStage.setScene(scene);
        authenticationStage.show();
    }
}
