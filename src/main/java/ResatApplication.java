import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.util.List;

public class ResatApplication extends Application
{
    private BorderPane root;

    private Employee selectedEmployee;

    @Override
    public void start(Stage primaryStage)
    {
        this.root = new BorderPane();

        VBox sidebar = sideBarLayout();
        this.root.setLeft(sidebar);

        Scene scene = new Scene(this.root, 1125, 750);
        primaryStage.setTitle("RESAT");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private VBox sideBarLayout()
    {
        VBox sidebar = new VBox();
        sidebar.setStyle("-fx-border-color: black; -fx-border-width: 1px;");

        List<Employee> employees = getAllEmployees();
        for (Employee employee : employees)
        {
            HBox row = createSidebarRow(employee);
            VBox.setVgrow(row, Priority.ALWAYS);
            sidebar.getChildren().add(row);
        }

        HBox newEmployeeButtonRow = createNewEmployeeButtonRow();
        VBox.setVgrow(newEmployeeButtonRow, Priority.ALWAYS);
        sidebar.getChildren().add(newEmployeeButtonRow);

        sidebar.setPrefWidth(225);

        return sidebar;
    }

    private List<Employee> getAllEmployees()
    {
        // TODO mock 3 employees
        Employee employee1 = new Employee("Employee1", false);
        Employee employee2 = new Employee("Employee2", true);
        Employee employee3 = new Employee("Employee3", false);
        return List.of(employee1, employee2, employee3);
    }

    private HBox createSidebarRow(Employee employee)
    {
        HBox row = new HBox();
        row.setPadding(new Insets(5, 10, 5, 10));
        row.setStyle("-fx-border-color: lightgray; -fx-border-width: 1px;");
        row.setAlignment(Pos.CENTER);

        Region colorIndicator = new Region();
        colorIndicator.setPrefSize(20, 20);
        colorIndicator.setStyle("-fx-background-color: " + (employee.isOnline() ? "green" : "red") + ";");

        Label nameLabel = new Label(employee.getName());
        nameLabel.setStyle("-fx-font-size: 22px;");
        HBox.setHgrow(nameLabel, Priority.ALWAYS);
        nameLabel.setMaxWidth(Double.MAX_VALUE);
        nameLabel.setAlignment(Pos.CENTER);

        row.setOnMouseClicked(event -> {
            this.selectedEmployee = employee;
            clearDynamicContent();
            showEmployeeActions();
        });

        row.getChildren().addAll(colorIndicator, nameLabel);
        return row;
    }

    private HBox createNewEmployeeButtonRow()
    {
        HBox row = new HBox();
        row.setPadding(new Insets(5, 10, 5, 10));
        row.setStyle("-fx-border-color: lightgray; -fx-border-width: 1px;");
        row.setAlignment(Pos.CENTER);

        Button addButton = new Button("+");
        addButton.setPrefSize(50, 30);
        addButton.setOnAction(event -> {
            clearDynamicContent();
            showCreateEmployeeForm();
        });

        row.getChildren().add(addButton);
        return row;
    }

    private VBox employeeActionsLayout()
    {
        VBox mainButtons = new VBox(10);
        mainButtons.setPadding(new Insets(20));
        mainButtons.setAlignment(Pos.CENTER);

        String recordButtonText = this.selectedEmployee.isOnline() ? "Salir" : "Entrar";
        Button recordButton = new Button(recordButtonText);
        recordButton.setPrefHeight(100);
        recordButton.setMaxWidth(Double.MAX_VALUE);

        Button manualInputButton = new Button("Registro manual");
        manualInputButton.setPrefHeight(100);
        manualInputButton.setMaxWidth(Double.MAX_VALUE);

        Button downloadRecordsButton = new Button("Descargar registro");
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
        deleteEmployeeButton.setPrefSize(150, 40);

        deleteEmployeePane.getChildren().add(deleteEmployeeButton);
        BorderPane.setMargin(deleteEmployeePane, new Insets(10));

        return deleteEmployeePane;
    }

    private void showCreateEmployeeForm()
    {
        BorderPane formLayout = new BorderPane();
        Label title = new Label("Nuevo empleado");

        VBox formFields = new VBox(10);
        formLayout.setPadding(new Insets(20));

        // TODO create form with this info to fill
        Label nameLabel = new Label("Nombre: ");
        Label statusLabel = new Label("DNI: ");
        Label password1Label = new Label("Contraseña: ");
        Label password2Label = new Label("Repetir contraseña: ");
        formFields.getChildren().addAll(nameLabel, statusLabel, password1Label, password2Label);

        formLayout.setTop(title);
        formLayout.setCenter(formFields);

        this.root.setCenter(formLayout);
    }

    private void showEmployeeActions()
    {
        VBox employeeActions = employeeActionsLayout();
        StackPane deleteEmployee = deleteEmployeeLayout();

        root.setCenter(employeeActions);
        root.setRight(deleteEmployee);
    }

    /**
     * Remove all nodes associated with root except the sidebar
     */
    private void clearDynamicContent()
    {
        this.root.setCenter(null);
        this.root.setRight(null);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
