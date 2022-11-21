package com.example.application;

import com.example.application.common.ErrorBean;
import com.example.application.exception.EmptyOrMissingFileException;
import com.example.application.exception.FileNotSupportedException;
import com.example.application.exception.TimeFormatNotSupportedException;
import lombok.RequiredArgsConstructor;
import lombok.extern.apachecommons.CommonsLog;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.io.FileNotFoundException;

@RestControllerAdvice(basePackageClasses = CustomExceptionHandler.class)
@CommonsLog
@RequiredArgsConstructor
public class CustomExceptionHandler extends ResponseEntityExceptionHandler
{

    private static final String ERROR_CODE_FILE_NOT_SUPPORTED    = "ERROR_FILE_TYPE_NOT_SUPPORTED";
    public  static final String ERROR_CODE_FORMAT_NOT_SUPPORTED  = "ERROR_TIME_FORMAT_NOT_SUPPORTED";
    public  static final String ERROR_CODE_FILE_EMPTY_OR_MISSING = "ERROR_FILE_EMPTY_OR_MISSING";

    @ExceptionHandler(FileNotSupportedException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorBean handleFileNotSupportedException(WebRequest request,
                                                     FileNotSupportedException exception)
    {
        return ErrorBean.builder()
                   .errorCode(ERROR_CODE_FILE_NOT_SUPPORTED)
                   .message(exception.getMessage())
                   .path(request.getContextPath())
                .build();
    }

    @ExceptionHandler(TimeFormatNotSupportedException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorBean handleTimeFormatNotSupportedException(WebRequest request,
                                                           TimeFormatNotSupportedException exception)
    {
        return ErrorBean.builder()
                   .errorCode(ERROR_CODE_FORMAT_NOT_SUPPORTED)
                   .message(exception.getMessage())
                   .path(request.getContextPath())
                .build();
    }

    @ExceptionHandler(EmptyOrMissingFileException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorBean handleEmptyOrMissingFileException(WebRequest request,
                                                       EmptyOrMissingFileException exception)
    {
        return ErrorBean.builder()
                .errorCode(ERROR_CODE_FILE_EMPTY_OR_MISSING)
                .message(isEmpty(exception.getMessage()) ? "File is empty or missing" : exception.getMessage())
                .path(request.getContextPath())
                .build();
    }

    private boolean isEmpty(String s)
    {
        return  s == null || "".equals(s);
    }

}
