package com.AlbertoMrMekko.resat.service;

import com.AlbertoMrMekko.resat.SelectedEmployeeManager;
import com.AlbertoMrMekko.resat.model.Employee;
import com.AlbertoMrMekko.resat.model.EmployeeRecord;
import com.AlbertoMrMekko.resat.utils.ValidationResult;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
public class RecordService
{
    private final FileManager fileManager;

    private final SelectedEmployeeManager selectedEmployeeManager;

    private final NotificationService notificationService;

    public RecordService(final FileManager fileManager, final SelectedEmployeeManager selectedEmployeeManager,
                         final NotificationService notificationService)
    {
        this.fileManager = fileManager;
        this.selectedEmployeeManager = selectedEmployeeManager;
        this.notificationService = notificationService;
    }

    public ValidationResult validateManualRecord(LocalDate date, String hour, String minute, String action)
    {
        if (date == null || hour == null || minute == null || action == null)
        {
            return new ValidationResult(false, "Todos los campos son obligatorios");
        }
        LocalDateTime localDateTime = LocalDateTime.of(date, LocalTime.of(Integer.parseInt(hour),
                Integer.parseInt(minute)));
        System.out.println(localDateTime);
        if (localDateTime.isAfter(LocalDateTime.now()))
        {
            return new ValidationResult(false, "La fecha no debe ser posterior a la fecha actual");
        }

        return new ValidationResult(true, null);
    }

    public void automaticRecord()
    {
        Employee employee = this.selectedEmployeeManager.getSelectedEmployee();
        String dni = employee.getDni();
        String action = employee.isOnline() ? "Salida" : "Entrada";
        LocalDateTime datetime = LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES);
        this.fileManager.addRecord(new EmployeeRecord(dni, action, datetime));
        boolean updatedStatus = action.equals("Entrada");
        employee.setOnline(updatedStatus);
    }

    public void manualRecord(String action, LocalDateTime dateTime)
    {
        List<EmployeeRecord> records = this.fileManager.getRecords();
        addRecord(action, dateTime, records);
        this.fileManager.saveRecords(records);
    }

    private void addRecord(String action, LocalDateTime dateTime, List<EmployeeRecord> records)
    {
        String dni = this.selectedEmployeeManager.getSelectedEmployee().getDni();
        String previousStatus = "Salida";
        int i = 0;
        boolean notFound = true;
        while (i < records.size() && notFound)
        {
            EmployeeRecord record = records.get(i);
            if (!record.getDatetime().isBefore(dateTime))
            {
                if (record.getDatetime().equals(dateTime) && record.getEmployeeDni().equals(dni))
                {
                    // ERROR
                }
                else
                {
                    notFound = false;
                }
            }
            else
            {
                if (record.getEmployeeDni().equals(dni))
                {
                    previousStatus = records.get(i).getAction();
                }
            }
            i++;
        }
        if (previousStatus.equals(action))
        {
            // ERROR
        }
        records.add(i, new EmployeeRecord(dni, action, dateTime));

        String nextStatus = previousStatus;
        LocalDateTime nextDateTime = LocalDateTime.now();
        i++;
        notFound = true;
        while (i < records.size() && notFound)
        {
            EmployeeRecord record = records.get(i);
            if (record.getEmployeeDni().equals(dni))
            {
                nextStatus = record.getAction();
                nextDateTime = LocalDateTime.parse(record.getDatetime().toString());
                notFound = false;
            }
        }
        if (notFound)
        {
            // new record is the most recent => set current status
            selectedEmployeeManager.getSelectedEmployee().setOnline(!selectedEmployeeManager.getSelectedEmployee().isOnline());
        }
        else
        {
            if (nextStatus.equals(action))
            {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
                String previousDate = dateTime.format(formatter);
                String nextDate = nextDateTime.format(formatter);
                String warningMessage;
                if (action.equals("Entrada"))
                {
                    warningMessage = "Tras añadir la entrada, ha quedado una inconsistencia temporal. Esto ocurre " +
                            "cuando " + "no existe un registro de salida entre la nueva entrada y la siguiente. Para "
                            + "solucionarla, debes añadir un registro de salida con una fecha posterior a " + previousDate + " y " + "anterior a " + nextDate;
                }
                else
                {
                    warningMessage = "Tras añadir la salida, ha quedado una inconsistencia temporal. Esto ocurre " +
                            "cuando no existe un registro de entrada entre la nueva salida y la siguiente. Para " +
                            "solucionarla, debes añadir un registro de entrada con una fecha posterior a " + previousDate + " y " + "anterior a " + nextDate;
                }
                this.notificationService.showWarningAlert("Aviso de inconsistencia", warningMessage);
            }
        }
    }
}
