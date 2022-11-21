package com.example.application.exception;

import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class FileNotSupportedException extends RuntimeException {

    String     message;
    String     statusText;
    HttpStatus status;

    public FileNotSupportedException(String message)
    {
        this.message = message;
    }
}

