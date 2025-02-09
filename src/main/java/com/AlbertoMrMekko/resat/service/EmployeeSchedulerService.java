package com.AlbertoMrMekko.resat.service;

import com.AlbertoMrMekko.resat.SelectedEmployeeManager;
import com.AlbertoMrMekko.resat.exceptions.ResatException;
import com.AlbertoMrMekko.resat.model.Employee;
import com.AlbertoMrMekko.resat.model.EmployeeRecord;
import com.AlbertoMrMekko.resat.view.ViewManager;
import javafx.application.Platform;
import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
public class EmployeeSchedulerService
{
    private final EmployeeService employeeService;

    private final FileManager fileManager;

    private final NotificationService notificationService;

    private final ViewManager viewManager;

    private final SelectedEmployeeManager selectedEmployeeManager;

    public EmployeeSchedulerService(final EmployeeService employeeService, final FileManager fileManager,
                                    final NotificationService notificationService, @Lazy final ViewManager viewManager, final SelectedEmployeeManager selectedEmployeeManager)
    {
        this.employeeService = employeeService;
        this.fileManager = fileManager;
        this.notificationService = notificationService;
        this.viewManager = viewManager;
        this.selectedEmployeeManager = selectedEmployeeManager;
    }

    /**
     * Register the exit of all the online employees every weekday at 14:00 and 20:00, and Saturdays at 13:00
     */
    @Scheduled(cron = "0 0 14,20 * * MON-FRI")
    @Scheduled(cron = "0 0 13 * * SAT")
    // fixme TESTING SCHEDULER (segundo minuto hora * * *)
    @Scheduled(cron = "0 31 16 * * *")
    public void scheduledExit()
    {
        List<Employee> onlineEmployees = employeeService.getOnlineEmployees();

        if (!onlineEmployees.isEmpty())
        {
            try
            {
                for (Employee employee : onlineEmployees)
                {
                    this.fileManager.addRecord(new EmployeeRecord(employee.getDni(), "Salida",
                            LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES)));
                    employee.setOnline(false);
                }
                Platform.runLater(() -> {
                    this.selectedEmployeeManager.setSelectedEmployee(null);
                    this.viewManager.clearDynamicContent();
                    this.viewManager.showSidebarView();
                    this.notificationService.showInfoAlert("Registro", "El registro programado se ha realizado con " +
                            "éxito");
                });
            } catch (ResatException ex)
            {
                Platform.runLater(() -> {
                    this.selectedEmployeeManager.setSelectedEmployee(null);
                    this.viewManager.clearDynamicContent();
                    this.viewManager.showSidebarView();
                    this.notificationService.showErrorAlert("Registro",
                            "Error en el registro.\n" + ex.getErrorMessage());
                });
            }
        }
    }
}
