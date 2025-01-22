package com.AlbertoMrMekko.resat.view;

import com.AlbertoMrMekko.resat.controller.EmployeeController;
import com.AlbertoMrMekko.resat.model.Employee;
import com.AlbertoMrMekko.resat.SelectedEmployeeManager;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class SidebarView
{
    private final BorderPane root;

    private final SelectedEmployeeManager selectedEmployeeManager;

    private final EmployeeController employeeController;

    private final EmployeeActionsView employeeActionsView;

    private final CreateEmployeeView createEmployeeView;

    @Autowired
    public SidebarView(final BorderPane root, final SelectedEmployeeManager selectedEmployeeManager,
                       final EmployeeController employeeController, final EmployeeActionsView employeeActionsView,
                       final CreateEmployeeView createEmployeeView)
    {
        this.root = root;
        this.selectedEmployeeManager = selectedEmployeeManager;
        this.employeeController = employeeController;
        this.employeeActionsView = employeeActionsView;
        this.createEmployeeView = createEmployeeView;
    }

    public void show()
    {
        VBox sidebar = new VBox();
        sidebar.setStyle("-fx-border-color: black; -fx-border-width: 1px;");

        List<Employee> employees = this.employeeController.getEmployees();
        Employee selectedEmployee = this.selectedEmployeeManager.getSelectedEmployee();
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
            selectedEmployeeManager.setSelectedEmployee(employee);
            //clearDynamicContent(root);
            this.show();
            this.employeeActionsView.show();
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
            selectedEmployeeManager.setSelectedEmployee(null);
            //clearDynamicContent(root);
            this.show();
            this.createEmployeeView.show();
        });
        addButton.setPrefSize(50, 30);

        row.getChildren().add(addButton);
        return row;
    }
}
