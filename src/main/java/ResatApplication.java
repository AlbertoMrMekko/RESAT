import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
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

        showSideBarLayout();

        Scene scene = new Scene(this.root, 1125, 750);
        primaryStage.setTitle("RESAT");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void showSideBarLayout()
    {
        VBox sidebar = new VBox();
        sidebar.setStyle("-fx-border-color: black; -fx-border-width: 1px;");

        List<Employee> employees = getAllEmployees();
        for (Employee employee : employees)
        {
            HBox row = createSidebarRow(employee);
            if (selectedEmployee != null && employee.getDni().equals(selectedEmployee.getDni()))
            {
                row.setStyle("-fx-border-color: lightgray; -fx-border-width: 3px; -fx-background-color: lightgray;");
            }
            VBox.setVgrow(row, Priority.ALWAYS);
            sidebar.getChildren().add(row);
        }

        HBox newEmployeeButtonRow = createNewEmployeeButtonRow();
        VBox.setVgrow(newEmployeeButtonRow, Priority.ALWAYS);
        sidebar.getChildren().add(newEmployeeButtonRow);

        sidebar.setPrefWidth(225);

        this.root.setLeft(sidebar);
    }

    private List<Employee> getAllEmployees()
    {
        // TODO mock 3 employees
        Employee employee1 = new Employee("24682468H", "Employee1", false);
        Employee employee2 = new Employee("13571357L", "Employee2", true);
        Employee employee3 = new Employee("21436587X", "Employee3", false);
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
            showSideBarLayout();
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
        addButton.setOnAction(event -> {
            this.selectedEmployee = null;
            clearDynamicContent();
            showSideBarLayout();
            showCreateEmployeeForm();
        });
        addButton.setPrefSize(50, 30);

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
            System.out.println("Mostrar formulario para el registro manual");
            System.out.println("Tras rellenar formulario:" +
                                      "Mostrar ventana de autenticación " +
                                      "Registrar entrada/salida del empleado" +
                                      "Actualizar barra lateral" +
                                      "Actualizar acciones empleado");
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
            System.out.println("Mostrar ventana de confirmación");
            System.out.println("Mostrar ventana de autenticación");
            System.out.println("Eliminar el empleado");
            System.out.println("Actualizar barra lateral");
        });
        deleteEmployeeButton.setPrefSize(150, 40);

        deleteEmployeePane.getChildren().add(deleteEmployeeButton);
        BorderPane.setMargin(deleteEmployeePane, new Insets(10));

        return deleteEmployeePane;
    }

    private void showCreateEmployeeForm()
    {
        BorderPane formLayout = new BorderPane();
        Label title = new Label("Nuevo empleado");
        title.setStyle("-fx-font-size: 34px; -fx-font-weight: bold;");
        BorderPane.setMargin(title, new Insets(50, 0, 50, 0));
        BorderPane.setAlignment(title, Pos.CENTER);

        GridPane formFields = getFormFields();

        HBox formButtons = getFormButtons();
        formButtons.setPadding(new Insets(50));

        VBox centralPane = new VBox(formFields, formButtons);
        centralPane.setAlignment(Pos.TOP_CENTER);

        formLayout.setTop(title);
        formLayout.setCenter(centralPane);

        this.root.setCenter(formLayout);
    }

    private GridPane getFormFields()
    {
        GridPane formFields = new GridPane();
        formFields.setPadding(new Insets(20));
        formFields.setHgap(10);
        formFields.setVgap(10);
        formFields.setAlignment(Pos.CENTER);

        Label nameLabel = new Label("Nombre: ");
        TextField nameField = new TextField();
        Label dniLabel = new Label("DNI: ");
        TextField dniField = new TextField();
        Label password1Label = new Label("Contraseña: ");
        PasswordField password1Field = new PasswordField();
        Label password2Label = new Label("Repetir contraseña: ");
        PasswordField password2Field = new PasswordField();

        formFields.add(nameLabel, 0, 0);
        formFields.add(nameField, 1, 0);
        formFields.add(dniLabel, 0, 1);
        formFields.add(dniField, 1, 1);
        formFields.add(password1Label, 0, 2);
        formFields.add(password1Field, 1, 2);
        formFields.add(password2Label, 0, 3);
        formFields.add(password2Field, 1, 3);

        return formFields;
    }

    private HBox getFormButtons()
    {
        HBox buttons = new HBox(10);
        buttons.setPadding(new Insets(10));
        buttons.setAlignment(Pos.CENTER);
        buttons.setSpacing(5);

        Button cancelButton = new Button("Cancelar");
        Button createEmployeeButton = new Button("Crear empleado");

        HBox.setHgrow(createEmployeeButton, Priority.ALWAYS);
        buttons.getChildren().addAll(cancelButton, createEmployeeButton);

        cancelButton.setOnAction(event -> {
            clearDynamicContent();
        });
        createEmployeeButton.setOnAction(event -> {
            System.out.println("Mostrar ventana de confirmación");
            System.out.println("Crear nuevo empleado");
            System.out.println("Actualizar barra lateral");
        });

        return buttons;
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
