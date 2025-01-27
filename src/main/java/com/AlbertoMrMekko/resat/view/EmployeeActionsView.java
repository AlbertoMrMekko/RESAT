package com.AlbertoMrMekko.resat.view;

import com.AlbertoMrMekko.resat.SelectedEmployeeManager;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

@Component
public class EmployeeActionsView
{
    private final BorderPane root;

    private final SelectedEmployeeManager selectedEmployeeManager;

    private final ViewManager viewManager;

    public EmployeeActionsView(final BorderPane root, final SelectedEmployeeManager selectedEmployeeManager,
                               @Lazy final ViewManager viewManager)
    {
        this.root = root;
        this.selectedEmployeeManager = selectedEmployeeManager;
        this.viewManager = viewManager;
    }

    public void show()
    {
        VBox employeeActions = employeeActionsLayout();
        StackPane deleteEmployee = deleteEmployeeLayout();

        root.setCenter(employeeActions);
        root.setRight(deleteEmployee);
    }

    private VBox employeeActionsLayout()
    {
        VBox mainButtons = new VBox(10);
        mainButtons.setPadding(new Insets(20));
        mainButtons.setAlignment(Pos.CENTER);

        String recordButtonText = this.selectedEmployeeManager.getSelectedEmployee().isOnline() ? "Salir" : "Entrar";
        Button recordButton = new Button(recordButtonText);
        recordButton.setOnAction(event -> {
            System.out.println("Mostrar ventana de autenticación");
            System.out.println("Registrar entrada/salida del empleado");
            System.out.println("Actualizar barra lateral");
            System.out.println("Actualizar acciones empleado");
        });
        recordButton.setPrefHeight(100);
        recordButton.setMaxWidth(Double.MAX_VALUE);

        Button manualInputButton = new Button("Registro manual");
        manualInputButton.setOnAction(event -> {
            this.viewManager.showManualRecordView();
            System.out.println("Tras rellenar formulario:" + "Mostrar ventana de autenticación " + "Registrar " +
                    "entrada/salida del empleado" + "Actualizar barra lateral" + "Actualizar acciones empleado");
        });
        manualInputButton.setPrefHeight(100);
        manualInputButton.setMaxWidth(Double.MAX_VALUE);

        Button downloadRecordsButton = new Button("Descargar registro");
        downloadRecordsButton.setOnAction(event -> {
            System.out.println("Mostrar ventana de autenticación");
            System.out.println("Descargar el registro completo (y guardarlo en descargas o pedir path??)");
        });

        downloadRecordsButton.setPrefHeight(100);
        downloadRecordsButton.setMaxWidth(Double.MAX_VALUE);

        mainButtons.getChildren().addAll(recordButton, manualInputButton, downloadRecordsButton);

        return mainButtons;
    }

    private StackPane deleteEmployeeLayout()
    {
        StackPane deleteEmployeePane = new StackPane();
        deleteEmployeePane.setAlignment(Pos.TOP_RIGHT);
        Button deleteEmployeeButton = new Button("Eliminar empleado");
        deleteEmployeeButton.setOnAction(event -> {
            // TODO mover a un método aparte (?)
            this.viewManager.showDeleteEmployeeConfirmationView();
            // TODO
            //  if Cancelar => showEmployeeActions
            //  if Aceptar:
            //  showAuthenticationView
            //  if Atrás => showEmployeeActions
            //  if Contra incorrecta => Reintentar y mostrar alerta error
            //  if Contra correcta:
            //  Eliminar empleado de list employees y de empleados.csv
            //  Actualizar barra lateral y clearDynamicContent
        });
        deleteEmployeeButton.setPrefSize(150, 40);

        deleteEmployeePane.getChildren().add(deleteEmployeeButton);
        BorderPane.setMargin(deleteEmployeePane, new Insets(10));

        return deleteEmployeePane;
    }
}
