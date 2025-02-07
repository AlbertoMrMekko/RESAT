package com.AlbertoMrMekko.resat.exceptions;

import lombok.Getter;

public class ResatException extends RuntimeException
{
    @Getter
    private String errorMessage;

    public ResatException(String errorMessage)
    {
        super();
        this.errorMessage = errorMessage;
    }
}
