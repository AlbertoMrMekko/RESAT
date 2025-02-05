package com.AlbertoMrMekko.resat.model;

import lombok.Getter;

import java.time.LocalDateTime;

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

    @Override
    public String toString()
    {
        return String.format("%s,%s,%s\n", employeeDni, action, datetime.toString());
    }
}
