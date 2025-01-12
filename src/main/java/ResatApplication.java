import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.util.List;

public class ResatApplication extends Application
{
    @Override
    public void start(Stage primaryStage)
    {
        VBox sidebar = sideBarLayout(primaryStage);
        VBox employeeActions = employeeActionsLayout();
        StackPane deleteEmployee = deleteEmployeeLayout();

        BorderPane root = new BorderPane();
        root.setLeft(sidebar);
        root.setCenter(employeeActions);
        root.setRight(deleteEmployee);

        Scene scene = new Scene(root, 800, 600);
        primaryStage.setTitle("RESAT");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private VBox sideBarLayout(Stage stage)
    {
        VBox sidebar = new VBox();
        sidebar.setStyle("-fx-border-color: black; -fx-border-width: 1px;");

        List<Employee> employees = getAllEmployees();
        for (Employee employee : employees)
        {
            HBox row = createSidebarRow(employee.getName(), employee.isOnline());
            VBox.setVgrow(row, Priority.ALWAYS);
            sidebar.getChildren().add(row);
        }

        HBox newEmployeeButtonRow = createNewEmployeeButtonRow(stage);
        VBox.setVgrow(newEmployeeButtonRow, Priority.ALWAYS);
        sidebar.getChildren().add(newEmployeeButtonRow);

        sidebar.setPrefWidth(200);

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

    private HBox createSidebarRow(String name, boolean isOnline)
    {
        HBox row = new HBox();
        row.setPadding(new Insets(5, 10, 5, 10));
        row.setStyle("-fx-border-color: lightgray; -fx-border-width: 1px;");
        row.setAlignment(Pos.CENTER);

        Region colorIndicator = new Region();
        colorIndicator.setPrefSize(20, 20);
        colorIndicator.setStyle("-fx-background-color: " + (isOnline ? "green" : "red") + ";");

        Label nameLabel = new Label(name);
        nameLabel.setStyle("-fx-font-size: 22px;");
        HBox.setHgrow(nameLabel, Priority.ALWAYS);
        nameLabel.setMaxWidth(Double.MAX_VALUE);
        nameLabel.setAlignment(Pos.CENTER);

        row.getChildren().addAll(colorIndicator, nameLabel);
        return row;
    }

    private HBox createNewEmployeeButtonRow(Stage stage)
    {
        HBox row = new HBox();
        row.setPadding(new Insets(5, 10, 5, 10));
        row.setStyle("-fx-border-color: lightgray; -fx-border-width: 1px;");
        row.setAlignment(Pos.CENTER);

        Button addButton = new Button("+");
        addButton.setPrefSize(50, 30);
        addButton.setOnAction(event -> showCreateEmployeeForm(stage));

        row.getChildren().add(addButton);
        return row;
    }

    private VBox employeeActionsLayout()
    {
        VBox mainButtons = new VBox(10);
        mainButtons.setPadding(new Insets(20));
        mainButtons.setAlignment(Pos.CENTER);

        Button btn1 = new Button("Entrar / Salir");
        btn1.setPrefHeight(100);
        btn1.setMaxWidth(Double.MAX_VALUE);

        Button btn2 = new Button("Registro manual");
        btn2.setPrefHeight(100);
        btn2.setMaxWidth(Double.MAX_VALUE);

        Button btn3 = new Button("Descargar registro");
        btn3.setPrefHeight(100);
        btn3.setMaxWidth(Double.MAX_VALUE);

        mainButtons.getChildren().addAll(btn1, btn2, btn3);

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

    private void showCreateEmployeeForm(Stage stage)
    {
        BorderPane mainPane = new BorderPane();
        Label title = new Label("Nuevo empleado");
        mainPane.setTop(title);

        VBox formLayout = new VBox(10);
        formLayout.setPadding(new Insets(20));

        // TODO create form with this info to fill
        Label nameLabel = new Label("Nombre: ");
        Label statusLabel = new Label("DNI: ");
        Label password1Label = new Label("Contraseña: ");
        Label password2Label = new Label("Repetir contraseña: ");

        formLayout.getChildren().addAll(nameLabel, statusLabel, password1Label, password2Label);

        mainPane.setCenter(formLayout);

        Scene createEmployeeScene = new Scene(mainPane, 800, 600);
        stage.setScene(createEmployeeScene);
        stage.show();

        /*
        Stage createEmployeeStage = new Stage();
        createEmployeeStage.setTitle("Nuevo empleado");

        VBox formLayout = new VBox(10);
        formLayout.setPadding(new Insets(20));

        Label nameLabel = new Label("Nombre: ");
        Label statusLabel = new Label("Estado: ");

        formLayout.getChildren().addAll(nameLabel, statusLabel);

        Button closeButton = new Button("Cerrar");
        closeButton.setOnAction(e -> createEmployeeStage.close());
        formLayout.getChildren().add(closeButton);

        Scene formScene = new Scene(formLayout, 300, 200);
        createEmployeeStage.setScene(formScene);
        createEmployeeStage.show();
        */
    }

    public static void main(String[] args) {
        launch(args);
    }
}
