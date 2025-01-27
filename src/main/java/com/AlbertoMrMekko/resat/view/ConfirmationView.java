package com.AlbertoMrMekko.resat.view;

import com.AlbertoMrMekko.resat.model.Employee;
import com.AlbertoMrMekko.resat.SelectedEmployeeManager;
import com.AlbertoMrMekko.resat.model.EmployeeRecord;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import lombok.Getter;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

@Component
public class ConfirmationView
{
    @Getter
    private boolean confirmed;

    private final SelectedEmployeeManager selectedEmployeeManager;

    private final ViewManager viewManager;

    public ConfirmationView(final SelectedEmployeeManager selectedEmployeeManager, @Lazy final ViewManager viewManager)
    {
        this.selectedEmployeeManager = selectedEmployeeManager;
        this.viewManager = viewManager;
    }

    public void showDeleteEmployeeConfirmationView()
    {
        Stage confirmationStage = new Stage();
        confirmationStage.initModality(Modality.APPLICATION_MODAL);

        Employee selectedEmployee = this.selectedEmployeeManager.getSelectedEmployee();

        BorderPane rootNode = new BorderPane();
        Label title = new Label("Eliminar empleado");
        Label text = new Label("¿Está seguro de que deseas eliminar el empleado " + selectedEmployee.getName() + "?");

        Button cancel = new Button("Cancelar");
        cancel.setOnAction(event -> {
            confirmed = false;
            confirmationStage.close();
        });

        Button accept = new Button("Sí, eliminar empleado " + selectedEmployee.getName());
        accept.setOnAction(event -> {
            confirmed = true;
            confirmationStage.close();

            this.viewManager.showAuthenticationView();
            System.out.println("Eliminar empleado");
            //clearDynamicContent(root);
            this.viewManager.showSidebarView();
        });

        HBox buttons = new HBox();
        buttons.getChildren().addAll(cancel, accept);

        rootNode.setTop(title);
        rootNode.setCenter(text);
        rootNode.setBottom(buttons);
        Scene scene = new Scene(rootNode, 400, 300);

        confirmationStage.setTitle("RESAT");
        confirmationStage.setScene(scene);
        confirmationStage.show();
    }

    public void showManualRecordConfirmationView(EmployeeRecord record)
    {
        Stage confirmationStage = new Stage();
        confirmationStage.initModality(Modality.APPLICATION_MODAL);

        Employee selectedEmployee = this.selectedEmployeeManager.getSelectedEmployee();

        BorderPane rootNode = new BorderPane();
        Label title = new Label("Registro manual");
        Label text = new Label("Se registrará la " + record.getAction() + " para el día " + record.getDate() +
                " a las " + record.getTime());

        Button cancel = new Button("Cancelar");
        cancel.setOnAction(event -> {
            confirmationStage.close();
        });

        Button accept = new Button("Aceptar");
        accept.setOnAction(event -> {
            this.viewManager.showAuthenticationView();
            System.out.println("Registrar");
            //clearDynamicContent(root);
            this.viewManager.showSidebarView();
            this.viewManager.showEmployeeActionsView();
        });

        HBox buttons = new HBox();
        buttons.getChildren().addAll(cancel, accept);

        rootNode.setTop(title);
        rootNode.setCenter(text);
        rootNode.setBottom(buttons);
        Scene scene = new Scene(rootNode, 400, 300);

        confirmationStage.setTitle("RESAT");
        confirmationStage.setScene(scene);
        confirmationStage.show();
    }
}
