package com.AlbertoMrMekko.resat.view;

import com.AlbertoMrMekko.resat.SelectedEmployeeManager;
import com.AlbertoMrMekko.resat.service.AuthenticationService;
import com.AlbertoMrMekko.resat.service.NotificationService;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import lombok.Getter;
import org.springframework.stereotype.Component;

@Component
public class AuthenticationView
{
    @Getter
    private boolean authenticated;

    private final SelectedEmployeeManager selectedEmployeeManager;

    private final AuthenticationService authenticationService;

    private final NotificationService notificationService;

    public AuthenticationView(final SelectedEmployeeManager selectedEmployeeManager,
                              final AuthenticationService authenticationService,
                              final NotificationService notificationService)
    {
        this.selectedEmployeeManager = selectedEmployeeManager;
        this.authenticationService = authenticationService;
        this.notificationService = notificationService;
    }

    public void show()
    {
        this.authenticated = false;
        Stage authenticationStage = new Stage();
        authenticationStage.initModality(Modality.APPLICATION_MODAL);

        BorderPane rootNode = new BorderPane();

        Label title = new Label("Identifícate");

        VBox content = new VBox(10);
        Label text = new Label("Introduce tu contraseña para validar la acción");
        PasswordField passwordField = new PasswordField();
        passwordField.setOnAction(event -> handleAuthentication(passwordField, authenticationStage));

        content.getChildren().addAll(text, passwordField);

        HBox buttons = new HBox();

        Button back = new Button("Atrás");
        back.setOnAction(event -> {
            authenticated = false;
            authenticationStage.close();
        });

        Button accept = new Button("Aceptar");

        accept.setOnAction(event -> handleAuthentication(passwordField, authenticationStage));

        buttons.getChildren().addAll(back, accept);

        rootNode.setTop(title);
        rootNode.setCenter(content);
        rootNode.setBottom(buttons);
        Scene scene = new Scene(rootNode, 400, 300);

        authenticationStage.setTitle("RESAT");
        authenticationStage.setScene(scene);
        authenticationStage.showAndWait();
    }

    private void handleAuthentication(PasswordField passwordField, Stage authenticationStage)
    {
        boolean correctPassword = this.authenticationService.validatePassword(passwordField.getText(),
                this.selectedEmployeeManager.getSelectedEmployee().getPassword());
        if (correctPassword)
        {
            authenticated = true;
            authenticationStage.close();
        }
        else
        {
            this.notificationService.showErrorAlert("Error de autenticación", "La contraseña introducida no es " +
                    "correcta");
            passwordField.clear();
            passwordField.requestFocus();
        }
    }
}
