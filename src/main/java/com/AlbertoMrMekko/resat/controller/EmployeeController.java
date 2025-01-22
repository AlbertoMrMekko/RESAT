package com.AlbertoMrMekko.resat.controller;

import lombok.Getter;
import com.AlbertoMrMekko.resat.model.Employee;
import org.springframework.stereotype.Controller;

import java.util.List;

@Getter
@Controller
public class EmployeeController
{
    private List<Employee> employees;

    public EmployeeController()
    {
        this.employees = getEmployeesFromDB();
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
}
