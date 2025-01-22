package com.AlbertoMrMekko.resat;

import com.AlbertoMrMekko.resat.model.Employee;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
public class SelectedEmployeeManager
{
    private Employee selectedEmployee;

    public SelectedEmployeeManager()
    {
        this.selectedEmployee = null;
    }
}
