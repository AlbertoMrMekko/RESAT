package com.AlbertoMrMekko.resat.view;

import com.AlbertoMrMekko.resat.model.Employee;
import com.AlbertoMrMekko.resat.SelectedEmployeeManager;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.springframework.stereotype.Component;

@Component
public class ConfirmationView
{
    private final BorderPane root;

    private final SelectedEmployeeManager selectedEmployeeManager;

    private final AuthenticationView authenticationView;

    private final SidebarView sidebarView;

    public ConfirmationView(final BorderPane root, final SelectedEmployeeManager selectedEmployeeManager,
                            final AuthenticationView authenticationView, final SidebarView sidebarView)
    {
        this.root = root;
        this.selectedEmployeeManager = selectedEmployeeManager;
        this.authenticationView = authenticationView;
        this.sidebarView = sidebarView;
    }

    public void show()
    {
        Stage confirmationStage = new Stage();
        confirmationStage.initModality(Modality.APPLICATION_MODAL);

        Employee selectedEmployee = this.selectedEmployeeManager.getSelectedEmployee();

        BorderPane rootNode = new BorderPane();
        Label title = new Label("Eliminar empleado");
        Label text = new Label("¿Está seguro de que deseas eliminar el empleado " + selectedEmployee.getName() + "?");
        HBox buttons = new HBox();

        Button cancel = new Button("Cancelar");
        cancel.setOnAction(event -> {
            confirmationStage.close();
        });

        Button accept = new Button("Sí, eliminar empleado " + selectedEmployee.getName());
        accept.setOnAction(event -> {
            this.authenticationView.show();
            System.out.println("Eliminar empleado");
            //clearDynamicContent(root);
            sidebarView.show();
        });

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
