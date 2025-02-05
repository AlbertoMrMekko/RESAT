package com.AlbertoMrMekko.resat.model;

import lombok.Getter;
import lombok.Setter;

@Getter
public class Employee
{
    private String name;

    private String dni;

    private String password;

    @Setter
    private boolean isOnline;

    public Employee(String name, String dni, String password, boolean isOnline)
    {
        this.name = name;
        this.dni = dni;
        this.password = password;
        this.isOnline = isOnline;
    }

    @Override
    public String toString()
    {
        return String.format("%s,%s,%s\n", name, dni, password);
    }
}
