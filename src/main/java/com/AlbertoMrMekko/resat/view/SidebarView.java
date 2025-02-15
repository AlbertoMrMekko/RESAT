package com.AlbertoMrMekko.resat.view;

import com.AlbertoMrMekko.resat.service.EmployeeService;
import com.AlbertoMrMekko.resat.model.Employee;
import com.AlbertoMrMekko.resat.SelectedEmployeeManager;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class SidebarView
{
    private final BorderPane root;

    private final SelectedEmployeeManager selectedEmployeeManager;

    private final EmployeeService employeeService;

    private final ViewManager viewManager;

    @Autowired
    public SidebarView(final BorderPane root, final SelectedEmployeeManager selectedEmployeeManager,
                       final EmployeeService employeeService, @Lazy final ViewManager viewManager)
    {
        this.root = root;
        this.selectedEmployeeManager = selectedEmployeeManager;
        this.employeeService = employeeService;
        this.viewManager = viewManager;
    }

    public void show()
    {
        VBox sidebar = new VBox();
        sidebar.getStyleClass().add("sidebar-background");

        List<Employee> employees = this.employeeService.getEmployees();
        Employee selectedEmployee = this.selectedEmployeeManager.getSelectedEmployee();
        for (Employee employee : employees)
        {
            HBox row = createSidebarRow(employee);
            if (selectedEmployee != null && employee.getDni().equals(selectedEmployee.getDni()))
            {
                row.getStyleClass().add("sidebar-selected-row");
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
        row.getStyleClass().add("sidebar-row");

        Region colorIndicator = new Region();
        colorIndicator.setPrefSize(20, 20);
        colorIndicator.getStyleClass().add(employee.isOnline() ? "online-indicator" : "offline-indicator");

        Label nameLabel = new Label(employee.getName());
        nameLabel.getStyleClass().add("sidebar-name");
        HBox.setHgrow(nameLabel, Priority.ALWAYS);
        nameLabel.setMaxWidth(Double.MAX_VALUE);
        nameLabel.setAlignment(Pos.CENTER);

        row.setOnMouseClicked(event -> {
            selectedEmployeeManager.setSelectedEmployee(employee);
            this.show();
            this.viewManager.showEmployeeActionsView();
        });

        row.getChildren().addAll(colorIndicator, nameLabel);
        return row;
    }

    private HBox createNewEmployeeButtonRow()
    {
        HBox row = new HBox();
        row.setPadding(new Insets(5, 10, 5, 10));
        row.getStyleClass().add("sidebar-row");
        row.setAlignment(Pos.CENTER);

        Button addButton = new Button("+");
        addButton.setOnAction(event -> {
            selectedEmployeeManager.setSelectedEmployee(null);
            this.show();
            this.viewManager.showCreateEmployeeView();
        });
        addButton.setPrefSize(50, 30);

        row.getChildren().add(addButton);
        return row;
    }
}
