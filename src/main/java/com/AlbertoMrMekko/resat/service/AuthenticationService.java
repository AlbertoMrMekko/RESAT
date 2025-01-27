package com.AlbertoMrMekko.resat.service;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationService
{
    private final BCryptPasswordEncoder passwordEncoder;

    public AuthenticationService()
    {
        passwordEncoder = new BCryptPasswordEncoder();
    }

    public String encodePassword(String password)
    {
        return passwordEncoder.encode(password);
    }

    public boolean validatePassword(String inputPassword, String employeePassword)
    {
        return passwordEncoder.matches(inputPassword, employeePassword);
    }
}
