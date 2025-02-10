package com.AlbertoMrMekko.resat.model;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public record DailyEmployeeRecord(String dni, LocalDate date, String hours)
{
    @Override
    public String toString()
    {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        String formattedDate = date.format(formatter);
        return String.format("%s,%s,%s\n", dni, formattedDate, hours);
    }
}
