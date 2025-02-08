package com.AlbertoMrMekko.resat.exceptions;

import lombok.Getter;

@Getter
public class ResatException extends RuntimeException
{
    private final String errorMessage;

    public ResatException(String errorMessage)
    {
        super();
        this.errorMessage = errorMessage;
    }
}
