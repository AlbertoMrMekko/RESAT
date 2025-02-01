package com.AlbertoMrMekko.resat.service;

import com.AlbertoMrMekko.resat.utils.ValidationResult;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Service
public class RecordService
{
    public RecordService()
    {

    }

    public ValidationResult validateManualRecord(LocalDate date, String hour, String minute, String action)
    {
        if (date == null || hour == null || minute == null || action == null)
        {
            return new ValidationResult(false, "Todos los campos son obligatorios");
        }
        LocalDateTime localDateTime = LocalDateTime.of(date, LocalTime.of(Integer.parseInt(hour), Integer.parseInt(minute)));
        System.out.println(localDateTime);
        if (localDateTime.isAfter(LocalDateTime.now()))
        {
            return new ValidationResult(false, "La fecha no debe ser posterior a la fecha actual");
        }

        return new ValidationResult(true, null);
    }

    public void manualRecord()
    {
        // TODO es necesario verificar que la acción anterior es contraria a la nueva, para evitar entrada sin salida y viceversa
        //  cuidado con el registro manual cuando el registro esté vacio para ese usuario. Si no hay registros el anterior cuenta como salida
    }
}
