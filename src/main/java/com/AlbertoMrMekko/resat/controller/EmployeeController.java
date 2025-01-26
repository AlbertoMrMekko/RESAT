package com.AlbertoMrMekko.resat.controller;

import com.AlbertoMrMekko.resat.service.FileManager;
import com.AlbertoMrMekko.resat.utils.ValidationResult;
import lombok.Getter;
import com.AlbertoMrMekko.resat.model.Employee;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.util.List;

@Getter
@Controller
public class EmployeeController
{
    private List<Employee> employees;

    private final FileManager fileManager;

    @Autowired
    public EmployeeController(final FileManager fileManager)
    {
        this.fileManager = fileManager;
        //this.employees = getEmployeesFromDB();
        this.employees = loadEmployees();
    }

    private List<Employee> getEmployeesFromDB()
    {
        // TODO get employees from csv file

        // fixme for the moment, mock 3 employees
        Employee employee1 = new Employee("24682468H", "Employee1", "wrongPassword", false);
        Employee employee2 = new Employee("13571357L", "Employee2", "wrongPassword", true);
        Employee employee3 = new Employee("21436587X", "Employee3", "wrongPassword", false);
        return List.of(employee1, employee2, employee3);
    }

    private List<Employee> loadEmployees()
    {
        List<Employee> employees = this.fileManager.readEmployeesFromCsv();
        // TODO falta status de cada empleado inicial
        return employees;
    }

    public ValidationResult validateCreateEmployee(String name, String dni, String password, String password2)
    {
        if (name.isBlank() || dni.isBlank() || password.isBlank() || password2.isBlank())
            return new ValidationResult(false, "Todos los campos son obligatorios");
        if (!password.equals(password2)) return new ValidationResult(false, "Las contraseñas no coinciden");
        String dniRegex = "^\\d{8}[A-Z]$";
        if (!dni.matches(dniRegex))
            return new ValidationResult(false, "El formato del DNI es incorrecto. \nEjemplo de DNI correcto: 12345678Z");
        for (Employee employee : employees)
        {
            if (employee.getName().equals(name))
            {
                return new ValidationResult(false, "Ya existe un empleado llamado" + name);
            }
            if (employee.getDni().equals(dni))
            {
                return new ValidationResult(false, "Ya existe el empleado con DNI " + dni);
            }
        }
        return new ValidationResult(true, null);
    }
}
