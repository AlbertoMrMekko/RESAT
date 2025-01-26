package com.AlbertoMrMekko.resat.model;

import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
public class EmployeeRecord
{
    private String employeeDni;

    private String action;

    private LocalDate date;

    private LocalTime time;

    public EmployeeRecord(String employeeDni, String action, LocalDate date,LocalTime time)
    {
        this.employeeDni = employeeDni;
        this.action = action;
        this.date = date;
        this.time = time;
    }
}
