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
        String accion = action.equals("Entrar") ? "entrada" : "salida";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        String formattedDate = datetime.format(formatter);
        show("Registro manual", "Se registrará la " + accion + " para el día " + formattedDate + " a " +
                "las " + datetime.toLocalTime());
    }

    private void show(String title, String text)
    {
        this.confirmation = false;
        Stage confirmationStage = new Stage();
        confirmationStage.initModality(Modality.APPLICATION_MODAL);

        BorderPane rootNode = new BorderPane();

        Label titleLabel = new Label(title);
        Label textLabel = new Label(text);

        Button cancel = new Button("Cancelar");
        cancel.setOnAction(event -> {
            confirmation = false;
            confirmationStage.close();
        });

        Button accept = new Button("Aceptar");
        accept.setOnAction(event -> {
            confirmation = true;
            confirmationStage.close();
        });

        HBox buttons = new HBox();
        buttons.getChildren().addAll(cancel, accept);

        rootNode.setTop(titleLabel);
        rootNode.setCenter(textLabel);
        rootNode.setBottom(buttons);
        Scene scene = new Scene(rootNode, 400, 300);

        confirmationStage.setTitle("RESAT");
        confirmationStage.setScene(scene);
        confirmationStage.showAndWait();
    }
}
