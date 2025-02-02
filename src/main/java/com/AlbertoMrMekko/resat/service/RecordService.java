package com.AlbertoMrMekko.resat.service;

import com.AlbertoMrMekko.resat.SelectedEmployeeManager;
import com.AlbertoMrMekko.resat.model.Employee;
import com.AlbertoMrMekko.resat.model.EmployeeRecord;
import com.AlbertoMrMekko.resat.utils.ValidationResult;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;

@Service
public class RecordService
{
    private final FileManager fileManager;

    private final SelectedEmployeeManager selectedEmployeeManager;

    public RecordService(final FileManager fileManager, final SelectedEmployeeManager selectedEmployeeManager)
    {
        this.fileManager = fileManager;
        this.selectedEmployeeManager = selectedEmployeeManager;
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
        EmployeeRecord record = new EmployeeRecord(dni, action, datetime);
        this.fileManager.automaticRecord(record);
        boolean updatedStatus = action.equals("Entrada");
        employee.setOnline(updatedStatus);
    }

    public void manualRecord()
    {
        // TODO es necesario verificar que la acción anterior es contraria a la nueva, para evitar entrada sin salida y viceversa
        //  cuidado con el registro manual cuando el registro esté vacio para ese usuario. Si no hay registros el anterior cuenta como salida

    }
}
