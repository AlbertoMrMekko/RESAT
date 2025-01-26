package com.AlbertoMrMekko.resat.view;

import com.AlbertoMrMekko.resat.model.EmployeeRecord;
import javafx.scene.layout.BorderPane;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ViewManager
{
    private final SidebarView sidebarView;

    private final EmployeeActionsView employeeActionsView;

    private final CreateEmployeeView createEmployeeView;

    private final ManualRecordView manualRecordView;

    private final ConfirmationView confirmationView;

    private final AuthenticationView authenticationView;

    private final BorderPane root;

    @Autowired
    public ViewManager(final SidebarView sidebarView, final EmployeeActionsView employeeActionsView,
                       final CreateEmployeeView createEmployeeView, final ManualRecordView manualRecordView,
                       final ConfirmationView confirmationView, final AuthenticationView authenticationView,
                       final BorderPane root)
    {
        this.sidebarView = sidebarView;
        this.employeeActionsView = employeeActionsView;
        this.createEmployeeView = createEmployeeView;
        this.manualRecordView = manualRecordView;
        this.confirmationView = confirmationView;
        this.authenticationView = authenticationView;
        this.root = root;
    }

    public void showSidebarView()
    {
        this.sidebarView.show();
    }

    public void showEmployeeActionsView()
    {
        clearDynamicContent();
        this.employeeActionsView.show();
    }

    public void showCreateEmployeeView()
    {
        clearDynamicContent();
        this.createEmployeeView.show();
    }

    public void showManualRecordView()
    {
        clearDynamicContent();
        this.manualRecordView.show();
    }

    public void showDeleteEmployeeConfirmationView()
    {
        this.confirmationView.showDeleteEmployeeConfirmationView();
    }

    public void showManualRecordConfirmationView(EmployeeRecord record)
    {
        this.confirmationView.showManualRecordConfirmationView(record);
    }

    public void showAuthenticationView()
    {
        this.authenticationView.show();
    }

    // TODO crear clear para cada parte del root, averiguar desde dónde llamar a cada uno
    public void clearDynamicContent()
    {
        root.setCenter(null);
        root.setRight(null);
    }
}
