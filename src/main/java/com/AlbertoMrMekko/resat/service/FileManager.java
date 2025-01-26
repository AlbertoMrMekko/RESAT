package com.AlbertoMrMekko.resat.service;

import com.AlbertoMrMekko.resat.model.Employee;
import org.springframework.stereotype.Component;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

@Component
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
        createFile(recordsFile, "DNI,Día,Horas\n");
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
}
