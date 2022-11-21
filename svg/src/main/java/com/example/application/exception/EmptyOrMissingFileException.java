package com.example.application.exception;

public class EmptyOrMissingFileException extends RuntimeException {

    String     message;

    public EmptyOrMissingFileException(String message)
    {
        this.message = message;
    }
}
