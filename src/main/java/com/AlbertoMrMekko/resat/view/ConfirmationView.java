package com.AlbertoMrMekko.resat.view;

import com.AlbertoMrMekko.resat.SelectedEmployeeManager;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import lombok.Getter;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

@Component
public class ConfirmationView
{
    @Getter
    private boolean confirmation;

    private final SelectedEmployeeManager selectedEmployeeManager;

    public ConfirmationView(final SelectedEmployeeManager selectedEmployeeManager)
    {
        this.selectedEmployeeManager = selectedEmployeeManager;
    }

    public void showDeleteEmployeeConfirmationView()
    {
        String selectedEmployeeName = this.selectedEmployeeManager.getSelectedEmployee().getName();
        show("Eliminar empleado", "¿Está seguro de que deseas eliminar el empleado " + selectedEmployeeName + "?");
    }

    public void showManualRecordConfirmationView(String action, LocalDateTime datetime)
    {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        String formattedDate = datetime.format(formatter);
        show("Registro manual", "Se registrará la " + action + " para el día " + formattedDate + " a " +
                "las " + datetime.toLocalTime());
    }

    private void show(String title, String text)
    {
        this.confirmation = false;
        Stage confirmationStage = new Stage();
        confirmationStage.initModality(Modality.APPLICATION_MODAL);

        BorderPane rootNode = new BorderPane();
        rootNode.getStyleClass().add("background");

        Label titleLabel = new Label(title);
        titleLabel.getStyleClass().add("emergent-window-title");
        HBox titleBox = new HBox(titleLabel);
        titleBox.getStyleClass().add("alignment-center");

        Label textLabel = new Label(text);
        textLabel.getStyleClass().add("emergent-window-text-2");
        HBox textBox = new HBox(textLabel);
        textBox.getStyleClass().add("alignment-center");

        Button cancel = new Button("Cancelar");
        cancel.getStyleClass().add("cancel-button");
        cancel.setOnAction(event -> {
            confirmation = false;
            confirmationStage.close();
        });

        Button accept = new Button("Aceptar");
        accept.getStyleClass().add("accept-button");
        accept.setOnAction(event -> {
            confirmation = true;
            confirmationStage.close();
        });

        HBox buttons = new HBox(20, cancel, accept);
        buttons.getStyleClass().add("buttons-box");

        rootNode.setTop(titleBox);
        rootNode.setCenter(textBox);
        rootNode.setBottom(buttons);
        Scene scene = new Scene(rootNode, 400, 300);

        String css = Objects.requireNonNull(getClass().getResource("/style.css")).toExternalForm();
        scene.getStylesheets().add(css);

        confirmationStage.setTitle("RESAT");
        confirmationStage.setScene(scene);
        confirmationStage.showAndWait();
    }
}
