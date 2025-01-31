package com.AlbertoMrMekko.resat.service;

import com.AlbertoMrMekko.resat.model.Employee;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.io.input.ReversedLinesFileReader;
import org.springframework.stereotype.Service;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;

@Service
public class FileManager
{
    private final File appDir;

    private final File recordsFile;

    private final File employeesFile;

    public FileManager()
    {
        this.appDir = new File(System.getenv("LOCALAPPDATA"), "RESAT");
        this.recordsFile = new File(appDir, "registros.csv");
        this.employeesFile = new File(appDir, "empleados.csv");

        createDirectory(appDir);
        createFile(recordsFile, "DNI,Acción,Día,Hora\n");
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
            }
        }
    }

    public List<Employee> readEmployeesFromCsv()
    {
        List<Employee> employees = new ArrayList<>();
        String line;
        try (BufferedReader br = new BufferedReader(new FileReader(employeesFile.getAbsolutePath())))
        {
            // Discard header
            br.readLine();

            while ((line = br.readLine()) != null)
            {
                String[] values = line.split(",");
                if (values.length == 3)
                {
                    employees.add(new Employee(values[1], values[0], values[2], false));
                }
            }
        } catch (IOException e)
        {
            System.err.println("Error al leer el archivo CSV: " + e.getMessage());
        }
        return employees;
    }

    public void addEmployee(Employee employee)
    {
        try (FileWriter writer = new FileWriter(employeesFile, true))
        {
            String line = String.format("%s,%s,%s\n", employee.getName(), employee.getDni(), employee.getPassword());
            writer.write(line);
        } catch (IOException e)
        {
            System.err.println("Error al añadir empleado a empleados.csv: " + e.getMessage());
        }
    }

    public void deleteEmployee(String dni)
    {
        try
        {
            List<String> lines = new ArrayList<>();
            try (BufferedReader br = new BufferedReader(new FileReader(employeesFile)))
            {
                String line;
                while ((line = br.readLine()) != null)
                {
                    if (!line.split(",")[1].equals(dni))
                    {
                        lines.add(line);
                    }
                }
            }
            try (BufferedWriter bw = new BufferedWriter(new FileWriter(employeesFile)))
            {
                for (String line : lines)
                {
                    bw.write(line);
                    bw.newLine();
                }
            }
            System.out.println("Empleado eliminado: " + dni);
        } catch (IOException e)
        {
            System.err.println("Error al eliminar empleado: " + e.getMessage());
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
            int i = 0;
            while (!dniSet.isEmpty() && (line = reader.readLine()) != null)
            {
                CSVParser parser = CSVParser.parse(line, CSVFormat.DEFAULT);
                CSVRecord record = parser.iterator().next();

                if (record.size() == 4)
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

            return dniStatusMap;
        } catch (IOException e)
        {
            throw new RuntimeException(e);
        }
    }
}
