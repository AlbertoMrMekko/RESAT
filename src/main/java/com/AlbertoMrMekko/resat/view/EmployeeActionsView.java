package com.AlbertoMrMekko.resat.view;

import com.AlbertoMrMekko.resat.SelectedEmployeeManager;
import com.AlbertoMrMekko.resat.exceptions.ResatException;
import com.AlbertoMrMekko.resat.service.EmployeeService;
import com.AlbertoMrMekko.resat.service.NotificationService;
import com.AlbertoMrMekko.resat.service.RecordService;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.Objects;

@Component
public class EmployeeActionsView
{
    private final BorderPane root;

    private final SelectedEmployeeManager selectedEmployeeManager;

    private final ViewManager viewManager;

    private final EmployeeService employeeService;

    private final RecordService recordService;

    private final NotificationService notificationService;

    public EmployeeActionsView(final BorderPane root, final SelectedEmployeeManager selectedEmployeeManager,
                               @Lazy final ViewManager viewManager, final EmployeeService employeeService,
                               final RecordService recordService, final NotificationService notificationService)
    {
        this.root = root;
        this.selectedEmployeeManager = selectedEmployeeManager;
        this.viewManager = viewManager;
        this.employeeService = employeeService;
        this.recordService = recordService;
        this.notificationService = notificationService;
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
            boolean authenticated = this.viewManager.showAuthenticationView();
            if (authenticated)
            {
                try
                {
                    this.recordService.automaticRecord();
                    this.viewManager.showSidebarView();
                    this.viewManager.showEmployeeActionsView();
                    this.notificationService.showInfoAlert("Registro", "El registro se realizado con éxito");
                } catch (ResatException ex)
                {
                    this.viewManager.showEmployeeActionsView();
                    this.notificationService.showErrorAlert("Registro",
                            "Error en el registro.\n" + ex.getErrorMessage());
                }
            }
        });
        recordButton.setPrefHeight(100);
        recordButton.setMaxWidth(Double.MAX_VALUE);

        Button manualInputButton = new Button("Registro manual");
        manualInputButton.setOnAction(event -> this.viewManager.showManualRecordView());
        manualInputButton.setPrefHeight(100);
        manualInputButton.setMaxWidth(Double.MAX_VALUE);

        Button downloadRecordsButton = new Button("Descargar registro");
        downloadRecordsButton.setOnAction(event -> {
            boolean authenticated = this.viewManager.showAuthenticationView();
            if (authenticated)
            {
                try
                {
                    File selectedFolder = selectFolder();
                    File outputFile = new File(selectedFolder, "registroTotal.csv");
                    this.recordService.downloadRecords(outputFile);
                    this.notificationService.showInfoAlert("Descarga de registros", "La descarga se ha realizado con " +
                            "éxito. Puedes encontrar el archivo en " + outputFile);
                } catch (ResatException ex)
                {
                    this.notificationService.showErrorAlert("Descarga de registros", "Error en la descarga de " +
                            "registros.\n" + ex.getErrorMessage());
                }
            }
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
        deleteEmployeeButton.getStyleClass().add("delete-employee-button");
        deleteEmployeeButton.setOnAction(event -> {
            boolean confirmation = this.viewManager.showDeleteEmployeeConfirmationView();
            if (confirmation)
            {
                try
                {
                    if (selectedEmployeeManager.getSelectedEmployee().isOnline())
                    {
                        this.recordService.automaticRecord();
                    }
                    this.employeeService.deleteEmployee(selectedEmployeeManager.getSelectedEmployee());
                    this.viewManager.clearDynamicContent();
                    this.viewManager.showSidebarView();
                    this.notificationService.showInfoAlert("Empleado eliminado", "El empleado se ha eliminado con " +
                            "éxito");
                } catch (ResatException ex)
                {
                    this.viewManager.showEmployeeActionsView();
                    this.notificationService.showErrorAlert("Eliminar empleado",
                            "Error al eliminar el empleado: \n" + ex.getErrorMessage());
                }
            }
        });
        deleteEmployeeButton.setPrefSize(150, 40);

        deleteEmployeePane.getChildren().add(deleteEmployeeButton);
        BorderPane.setMargin(deleteEmployeePane, new Insets(10));

        return deleteEmployeePane;
    }

    private File selectFolder()
    {
        DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setTitle("Seleccionar carpeta de destino");

        Stage folderStage = new Stage();
        File selectedFolder = directoryChooser.showDialog(folderStage);
        return Objects.requireNonNullElseGet(selectedFolder, this::defaultFolder);
    }

    private File defaultFolder()
    {
        return new File(System.getenv("LOCALAPPDATA"), "RESAT");
    }
}
