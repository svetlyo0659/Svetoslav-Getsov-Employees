package com.example.application;

import com.example.application.exception.TimeFormatNotSupportedException;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public enum LocalDateFormatsEnum
{

     DD_MM_YYYY( "dd-MM-yyyy"),
    DD_MM_YYYY2( "dd/MM/yyyy"),
     MM_DD_YYYY( "mm-DD-yyyy"),
    MM_DD_YYYY2( "mm/DD/yyyy"),
     YYYY_MM_DD( "yyyy-MM-dd"),
    YYYY_MM_DD2( "yyyy/MM/dd"),
     YYYY_DD_MM( "yyyy-DD-mm"),
    YYYY_DD_MM2( "yyyy/DD/mm");

    public final String supportedFormat;

    LocalDateFormatsEnum(String supportedFormat)
    {
        this.supportedFormat = supportedFormat;
    }

    public String getValue() {
        return supportedFormat;
    }

    public static LocalDate getFormat(String date)
    {
        for (LocalDateFormatsEnum supportedFormat : values()) {
            try
            {
                DateTimeFormatter formatter = DateTimeFormatter
                        .ofPattern(supportedFormat.getValue());

                return LocalDate.parse(date, formatter);
            }
            catch (DateTimeParseException ignored) {

            }
        }
        throw new TimeFormatNotSupportedException("Time format not supported.");
    }

}
