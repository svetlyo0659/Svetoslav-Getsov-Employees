package com.example.application.annotation;

import org.springframework.web.multipart.MultipartFile;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;


public class FileNotEmptyValidator implements ConstraintValidator<FileNotEmpty, MultipartFile>
{

    @Override
    public boolean isValid(MultipartFile multipartFile, ConstraintValidatorContext constraintValidatorContext)
    {
        return multipartFile != null && !multipartFile.isEmpty();
    }
}
