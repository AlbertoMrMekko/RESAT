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

import java.util.Objects;

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
        rootNode.getStyleClass().add("background");

        Label title = new Label("Identifícate");
        title.getStyleClass().add("emergent-window-title");
        HBox titleBox = new HBox(title);
        titleBox.getStyleClass().add("alignment-center");

        VBox content = new VBox(10);
        content.getStyleClass().add("alignment-center");
        Label text = new Label("Introduce tu contraseña para validar la acción");
        text.getStyleClass().add("emergent-window-text");
        PasswordField passwordField = new PasswordField();
        passwordField.getStyleClass().add("password-field");
        passwordField.setOnAction(event -> handleAuthentication(passwordField, authenticationStage));

        content.getChildren().addAll(text, passwordField);

        Button back = new Button("Atrás");
        back.getStyleClass().add("cancel-button");
        back.setOnAction(event -> {
            authenticated = false;
            authenticationStage.close();
        });

        Button accept = new Button("Aceptar");
        accept.getStyleClass().add("accept-button");
        accept.setOnAction(event -> handleAuthentication(passwordField, authenticationStage));

        HBox buttons = new HBox(20, back, accept);
        buttons.getStyleClass().add("buttons-box");

        rootNode.setTop(titleBox);
        rootNode.setCenter(content);
        rootNode.setBottom(buttons);
        Scene scene = new Scene(rootNode, 400, 300);

        String css = Objects.requireNonNull(getClass().getResource("/style.css")).toExternalForm();
        scene.getStylesheets().add(css);

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
