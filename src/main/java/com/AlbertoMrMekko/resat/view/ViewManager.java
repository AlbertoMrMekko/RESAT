package com.AlbertoMrMekko.resat.view;

import javafx.scene.layout.BorderPane;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ViewManager
{
    private final BorderPane root;

    private final SidebarView sidebarView;

    private final EmployeeActionsView employeeActionsView;

    private final CreateEmployeeView createEmployeeView;

    private final ManualRecordView manualRecordView;

    private final ConfirmationView confirmationView;

    private final AuthenticationView authenticationView;

    @Autowired
    public ViewManager(final BorderPane root, final SidebarView sidebarView,
                       final EmployeeActionsView employeeActionsView, final CreateEmployeeView createEmployeeView,
                       final ManualRecordView manualRecordView, final ConfirmationView confirmationView,
                       final AuthenticationView authenticationView)
    {
        this.root = root;
        this.sidebarView = sidebarView;
        this.employeeActionsView = employeeActionsView;
        this.createEmployeeView = createEmployeeView;
        this.manualRecordView = manualRecordView;
        this.confirmationView = confirmationView;
        this.authenticationView = authenticationView;
    }

    public void showSidebarView()
    {
        root.setLeft(sidebarView.getView());
        root.setCenter(null);
        root.setRight(null);
    }

    public void showEmployeeActionsView()
    {
        root.setCenter(employeeActionsView.getView());
    }

    public void showConfirmationView()
    {
        root.setRight(confirmationView.getView());
    }

    // TODO crear clear para cada parte del root, averiguar desde dónde llamar a cada uno
    public void clearDynamicContent(BorderPane root)
    {
        root.setCenter(null);
        root.setRight(null);
    }
}
