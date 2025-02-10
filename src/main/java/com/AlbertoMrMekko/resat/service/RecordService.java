package com.AlbertoMrMekko.resat.service;

import com.AlbertoMrMekko.resat.SelectedEmployeeManager;
import com.AlbertoMrMekko.resat.exceptions.ResatException;
import com.AlbertoMrMekko.resat.model.DailyEmployeeRecord;
import com.AlbertoMrMekko.resat.model.Employee;
import com.AlbertoMrMekko.resat.model.EmployeeRecord;
import com.AlbertoMrMekko.resat.utils.ValidationResult;
import org.springframework.stereotype.Service;

import java.io.File;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

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
            if (!record.datetime().isBefore(dateTime))
            {
                if (record.datetime().equals(dateTime) && record.employeeDni().equals(dni))
                {
                    System.err.println("Ya existe un registro para ese empleado con esa fecha");
                    throw new ResatException("Ya existe un registro para ese empleado con esa fecha");
                }
                else
                {
                    notFound = false;
                }
            }
            else
            {
                if (record.employeeDni().equals(dni))
                {
                    previousStatus = records.get(i).action();
                }
                i++;
            }
        }
        if (previousStatus.equals(action))
        {
            String oppositeAction = action.equals("Entrada") ? "Salida" : "Entrada";
            String errorMsg = "Este error ocurre cuando intentas registrar tu " + action + ", pero tu acción " +
                    "anterior" + " es también una " + action + ".\nCada " + action + " debe estar precedida por una " + oppositeAction;
            System.err.println(errorMsg);
            throw new ResatException(errorMsg);
        }
        records.add(i, new EmployeeRecord(dni, action, dateTime));

        String nextStatus = previousStatus;
        LocalDateTime nextDateTime = LocalDateTime.now();
        i++;
        notFound = true;
        while (i < records.size() && notFound)
        {
            EmployeeRecord record = records.get(i);
            if (record.employeeDni().equals(dni))
            {
                nextStatus = record.action();
                nextDateTime = LocalDateTime.parse(record.datetime().toString());
                notFound = false;
            }
            else
            {
                i++;
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
                // if next date is not of the same date, change to the same day at 23:59
                if (!dateTime.toLocalDate().isEqual(nextDateTime.toLocalDate()))
                {
                    nextDateTime = LocalDateTime.of(dateTime.toLocalDate(), LocalTime.of(23, 59));
                }
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
                String previousDate = dateTime.format(formatter);
                String nextDate = nextDateTime.format(formatter);
                String warningMessage;
                if (action.equals("Entrada"))
                {
                    warningMessage =
                            "Tras añadir la entrada, ha quedado una inconsistencia temporal. Esto ocurre " + "cuando "
                                    + "no existe un registro de salida entre la nueva entrada y la siguiente. Para " + "solucionarla, debes añadir un registro de salida con una fecha posterior a " + previousDate + " y " + "anterior a " + nextDate;
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

    public void downloadRecords(File outputFile)
    {
        List<EmployeeRecord> employeeRecords = this.fileManager.getRecords();
        Map<String, List<EmployeeRecord>> records =
                employeeRecords.stream().collect(Collectors.groupingBy(EmployeeRecord::employeeDni));
        ValidationResult validationResult = checkInconsistencies(records);
        if (validationResult.valid())
        {
            List<DailyEmployeeRecord> allEmployeesDailyRecords = new ArrayList<>();
            for (Map.Entry<String, List<EmployeeRecord>> entry : records.entrySet())
            {
                List<DailyEmployeeRecord> dailyEmployeeRecordList = toDailyEmployeeRecords(entry.getValue());
                allEmployeesDailyRecords.addAll(dailyEmployeeRecordList);
            }
            allEmployeesDailyRecords.sort(Comparator.comparing(DailyEmployeeRecord::date));
            this.fileManager.downloadRecords(allEmployeesDailyRecords, outputFile);
        }
        else
        {
            throw new ResatException(validationResult.errorMsg());
        }
    }

    private ValidationResult checkInconsistencies(Map<String, List<EmployeeRecord>> records)
    {
        for (Map.Entry<String, List<EmployeeRecord>> entry : records.entrySet())
        {
            EmployeeRecord previousRecord = new EmployeeRecord(entry.getKey(), "Salida", null);
            for (EmployeeRecord record : entry.getValue())
            {
                if (previousRecord.action().equals(record.action()))
                {
                    return new ValidationResult(false, buildInconsistencyMessage(previousRecord, record));
                }
                else
                {
                    previousRecord = record;
                }
            }
        }
        return new ValidationResult(true, null);
    }

    private String buildInconsistencyMessage(EmployeeRecord previousRecord, EmployeeRecord nextRecord)
    {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        String previousDate = previousRecord.datetime() != null ? previousRecord.datetime().format(formatter) :
                LocalDateTime.of(nextRecord.datetime().toLocalDate(), LocalTime.of(0, 0)).format(formatter);
        String nextDate = nextRecord.datetime().format(formatter);
        String oppositeAction = previousRecord.action().equals("Entrada") ? "Salida" : "Entrada";

        return ("Inconsistencia encontrada para el empleado con DNI " + previousRecord.employeeDni() + ".\nDebe " +
                "resolver la inconsistencia antes de poder descargar el registro.\nPara ello, debe crear un registro "
                + "de " + oppositeAction + " entre las fechas " + previousDate + " y " + nextDate);
    }

    private List<DailyEmployeeRecord> toDailyEmployeeRecords(List<EmployeeRecord> records)
    {
        List<DailyEmployeeRecord> dailyRecords = new ArrayList<>();

        List<List<EmployeeRecord>> recordsByDate =
                new ArrayList<>(records.stream().collect(Collectors.groupingBy(r -> r.datetime().toLocalDate())).values());

        for (List<EmployeeRecord> recordByDate : recordsByDate)
        {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
            List<String> hourList =
                    recordByDate.stream().map(r -> r.datetime().format(formatter)).collect(Collectors.toList());
            String hours = formatHours(hourList);
            dailyRecords.add(new DailyEmployeeRecord(recordByDate.get(0).employeeDni(),
                    recordByDate.get(0).datetime().toLocalDate(), hours));
        }

        return dailyRecords;
    }

    private String formatHours(List<String> hourList)
    {
        if (hourList.isEmpty())
        {
            return "";
        }
        else
        {
            boolean useDash = true;
            String hours = hourList.get(0);
            for (int i = 1; i < hourList.size(); i++, useDash = !useDash)
            {
                String delimiter;
                if (useDash)
                {
                    delimiter = "-";
                }
                else
                {
                    delimiter = ";";
                }
                hours = hours.concat(delimiter + hourList.get(i));
            }
            return hours;
        }
    }
}
