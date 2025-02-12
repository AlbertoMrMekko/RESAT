package com.AlbertoMrMekko.resat.service;

import com.AlbertoMrMekko.resat.utils.ValidationResult;
import com.AlbertoMrMekko.resat.model.Employee;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class EmployeeService
{
    @Getter
    private List<Employee> employees;

    private final FileManager fileManager;

    private final AuthenticationService authenticationService;

    private final RecordService recordService;

    @Autowired
    public EmployeeService(final FileManager fileManager, final RecordService recordService,
                           final AuthenticationService authenticationService)
    {
        this.fileManager = fileManager;
        this.recordService = recordService;
        this.authenticationService = authenticationService;
        this.employees = loadEmployees();
    }

    private List<Employee> loadEmployees()
    {
        List<Employee> employees = this.fileManager.readEmployeesFromCsv();
        this.recordService.initEmployeesStatus(employees);
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
                return new ValidationResult(false, "Ya existe un empleado llamado " + name);
            }
            if (employee.getDni().equals(dni))
            {
                return new ValidationResult(false, "Ya existe el empleado con DNI " + dni);
            }
        }
        return new ValidationResult(true, null);
    }

    public void createEmployee(String name, String dni, String password)
    {
        String encodedPassword = this.authenticationService.encodePassword(password);
        Employee employee = new Employee(name, dni, encodedPassword, false);
        this.fileManager.addEmployee(employee);
        this.employees.add(employee);
    }

    public void deleteEmployee(Employee employee)
    {
        this.fileManager.deleteEmployee(employee.getDni());
        this.employees.remove(employee);
    }

    public List<Employee> getOnlineEmployees()
    {
        return employees.stream().filter(Employee::isOnline).collect(Collectors.toList());
    }
}
