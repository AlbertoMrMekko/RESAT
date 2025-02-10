package com.AlbertoMrMekko.resat.service;

import com.AlbertoMrMekko.resat.exceptions.ResatException;
import com.AlbertoMrMekko.resat.model.DailyEmployeeRecord;
import com.AlbertoMrMekko.resat.model.Employee;
import com.AlbertoMrMekko.resat.model.EmployeeRecord;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.io.input.ReversedLinesFileReader;
import org.springframework.stereotype.Service;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.*;

@Service
public class FileManager
{
    private final File appDir;

    private final File recordsFile;

    private final File employeesFile;

    private final NotificationService notificationService;

    public FileManager(final NotificationService notificationService)
    {
        this.notificationService = notificationService;

        this.appDir = new File(System.getenv("LOCALAPPDATA"), "RESAT");
        this.recordsFile = new File(appDir, "registros.csv");
        this.employeesFile = new File(appDir, "empleados.csv");

        createDirectory(appDir);
        createFile(recordsFile, "DNI,Acción,Fecha\n");
        createFile(employeesFile, "Nombre,DNI,Contraseña\n");
    }

    private void createDirectory(File dir)
    {
        if (!dir.exists() && dir.mkdirs())
        {
            System.out.println("Directorio creado: " + dir.getAbsolutePath());
        }
    }

    private void createFile(File file, String header)
    {
        if (!file.exists())
        {
            try (FileWriter writer = new FileWriter(file))
            {
                writer.write(header);
                System.out.println("Archivo creado: " + file.getAbsolutePath());
            } catch (IOException e)
            {
                System.err.println("Error al crear el archivo " + file.getAbsolutePath() + ": " + e.getMessage());
                this.notificationService.showCriticalErrorAlert("Error al crear el archivo " + file.getAbsolutePath() + ": " + e.getMessage());
            }
        }
    }

    public List<Employee> readEmployeesFromCsv()
    {
        List<Employee> employees = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(employeesFile)))
        {
            // Discard header
            reader.readLine();

            String line;
            while ((line = reader.readLine()) != null)
            {
                String[] data = line.split(",");
                employees.add(new Employee(data[0], data[1], data[2], false));
            }
        } catch (IOException e)
        {
            System.err.println("Error al cargar los empleados: " + e.getMessage());
            this.notificationService.showCriticalErrorAlert("Error al cargar los empleados: " + e.getMessage());
        }

        return employees;
    }

    public List<EmployeeRecord> getRecords()
    {
        List<EmployeeRecord> records = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(recordsFile)))
        {
            // Discard header
            reader.readLine();

            String line;
            while ((line = reader.readLine()) != null)
            {
                String[] data = line.split(",");
                records.add(new EmployeeRecord(data[0], data[1], LocalDateTime.parse(data[2])));
            }
        } catch (IOException e)
        {
            System.err.println("Error al cargar los registros: " + e.getMessage());
            throw new ResatException("Error al cargar los registros: " + e.getMessage());
        }

        return records;
    }

    public void addEmployee(Employee employee)
    {
        try (FileWriter writer = new FileWriter(employeesFile, true))
        {
            writer.write(employee.toString());
        } catch (IOException e)
        {
            System.err.println("Error al añadir un nuevo empleado: " + e.getMessage());
            throw new ResatException("Error al añadir un nuevo empleado: " + e.getMessage());
        }
    }

    public void deleteEmployee(String dni)
    {
        try
        {
            List<String> lines = new ArrayList<>();
            try (BufferedReader reader = new BufferedReader(new FileReader(employeesFile)))
            {
                String line;
                while ((line = reader.readLine()) != null)
                {
                    if (!line.split(",")[1].equals(dni))
                    {
                        lines.add(line);
                    }
                }
            }
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(employeesFile)))
            {
                for (String line : lines)
                {
                    writer.write(line);
                    writer.newLine();
                }
            }
            System.out.println("Empleado con DNI " + dni + " eliminado");
        } catch (IOException e)
        {
            System.err.println("Error al eliminar empleado: " + e.getMessage());
            throw new ResatException("Error al eliminar empleado: " + e.getMessage());
        }
    }

    public void initEmployeesStatus(List<Employee> employees)
    {
        Set<String> dniSet = new HashSet<>();
        for (Employee e : employees)
        {
            dniSet.add(e.getDni());
        }
        Map<String, String> dniStatusMap = findLatestStatus(dniSet);
        String entrada = "Entrada";
        for (Employee employee : employees)
        {
            String status = dniStatusMap.get(employee.getDni());
            boolean online = entrada.equals(status);
            employee.setOnline(online);
        }
    }

    private Map<String, String> findLatestStatus(Set<String> dniSet)
    {
        Map<String, String> dniStatusMap = new HashMap<>();
        try (ReversedLinesFileReader reader = new ReversedLinesFileReader(recordsFile, StandardCharsets.UTF_8))
        {
            String line;
            while (!dniSet.isEmpty() && (line = reader.readLine()) != null)
            {
                CSVParser parser = CSVParser.parse(line, CSVFormat.DEFAULT);
                CSVRecord record = parser.iterator().next();

                if (record.size() == 3)
                {
                    String dni = record.get(0);
                    String action = record.get(1);
                    if (dniSet.contains(dni))
                    {
                        dniStatusMap.put(dni, action);
                        dniSet.remove(dni);
                    }
                }
            }
            for (String remainingDni : dniSet)
            {
                dniStatusMap.put(remainingDni, "Salida");
            }
        } catch (IOException e)
        {
            System.err.println("Error al buscar el estado anterior del empleado: " + e.getMessage());
            this.notificationService.showCriticalErrorAlert("Error al buscar el estado anterior del empleado: " + e.getMessage());
        }

        return dniStatusMap;
    }

    public void addRecord(EmployeeRecord record)
    {
        try (FileWriter writer = new FileWriter(recordsFile, true))
        {
            writer.write(record.toString());
        } catch (IOException e)
        {
            System.err.println("Error al añadir un nuevo registro: " + e.getMessage());
            throw new ResatException("Error al añadir un nuevo registro: " + e.getMessage());
        }
    }

    public void saveRecords(List<EmployeeRecord> records)
    {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(recordsFile)))
        {
            writer.write("DNI,Acción,Fecha\n");
            for (EmployeeRecord record : records)
            {
                writer.write(record.toString());
            }
        } catch (IOException e)
        {
            System.err.println("Error al guardar los registros: " + e.getMessage());
            throw new ResatException("Error al guardar los registros: " + e.getMessage());
        }
    }

    public void downloadRecords(List<DailyEmployeeRecord> records, File outputFile)
    {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(outputFile)))
        {
            writer.write("DNI,Día,Horas\n");
            for (DailyEmployeeRecord record : records)
            {
                writer.write(record.toString());
            }
        } catch (IOException e)
        {
            System.err.println("Error al descargar los registros: " + e.getMessage());
            throw new ResatException("Error al descargar los registros: " + e.getMessage());
        }
    }
}
