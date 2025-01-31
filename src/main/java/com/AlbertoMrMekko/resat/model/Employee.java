package com.AlbertoMrMekko.resat.model;

import lombok.Getter;
import lombok.Setter;

@Getter
public class Employee
{
    private String dni;

    private String name;

    private String password;

    @Setter
    private boolean isOnline;

    public Employee(String dni, String name, String password, boolean isOnline)
    {
        this.dni = dni;
        this.name = name;
        this.password = password;
        this.isOnline = isOnline;
    }
}
