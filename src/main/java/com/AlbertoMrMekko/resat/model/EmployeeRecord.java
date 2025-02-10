package com.AlbertoMrMekko.resat.model;

import java.time.LocalDateTime;

public record EmployeeRecord(String employeeDni, String action, LocalDateTime datetime)
{
    @Override
    public String toString()
    {
        return String.format("%s,%s,%s\n", employeeDni, action, datetime.toString());
    }
}
