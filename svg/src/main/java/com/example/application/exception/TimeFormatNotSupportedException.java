package com.example.application.exception;

public class TimeFormatNotSupportedException extends RuntimeException {
    final String message;

    public TimeFormatNotSupportedException(String message)
    {
        this.message = message;
    }
}
