package com.AlbertoMrMekko.resat.service;

import com.AlbertoMrMekko.resat.utils.ValidationResult;
import lombok.Getter;
import com.AlbertoMrMekko.resat.model.Employee;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Getter
@Service
public class EmployeeService
{
    private List<Employee> employees;

    private final FileManager fileManager;

    private final AuthenticationService authenticationService;

    @Autowired
    public EmployeeService(final FileManager fileManager, final AuthenticationService authenticationService)
    {
        this.fileManager = fileManager;
        //this.employees = getEmployeesFromDB();
        this.employees = loadEmployees();
        this.authenticationService = authenticationService;
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
        {
            return new ValidationResult(false, "Todos los campos son obligatorios");
        }
        if (!password.equals(password2))
        {
            return new ValidationResult(false, "Las contraseñas no coinciden");
        }
        String dniRegex = "^\\d{8}[A-Z]$";
        if (!dni.matches(dniRegex))
        {
            return new ValidationResult(false, "El formato del DNI es incorrecto. \nEjemplo de DNI correcto: " +
                    "12345678Z");
        }
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

    public void createEmployee(String dni, String name, String password)
    {
        String encodedPassword = this.authenticationService.encodePassword(password);
        Employee employee = new Employee(dni, name, encodedPassword, false);
        this.fileManager.addEmployee(employee);
        this.employees.add(employee);
    }
}
