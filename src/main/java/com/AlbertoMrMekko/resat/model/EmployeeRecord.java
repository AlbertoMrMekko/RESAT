package com.AlbertoMrMekko.resat.model;

import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Getter
public class EmployeeRecord
{
    private String employeeDni;

    private String action;

    private LocalDateTime datetime;

    public EmployeeRecord(String employeeDni, String action, LocalDateTime datetime)
    {
        this.employeeDni = employeeDni;
        this.action = action;
        this.datetime = datetime;
    }
}
