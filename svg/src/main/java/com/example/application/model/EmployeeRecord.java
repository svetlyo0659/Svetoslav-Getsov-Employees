package com.example.application.model;

import com.opencsv.bean.CsvBindByPosition;
import com.opencsv.bean.CsvCustomBindByPosition;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder(toBuilder = true)
public class EmployeeRecord
{
    /***
     *  If the CSV file containing header info, we also can use
     *  '@CsvBindByName' to map the CSV file to a Java object.
     *  Example : '@CsvBindByName(column = "EmpID")'
     *            '@CsvBindByName(column = "ProjectId")'
     *            '@CsvBindByName(column = "DateFrom")'
     *            '@CsvBindByName(column = "DateTo")'
     */

    @CsvBindByPosition(position = 0)
    String    empId;

    @CsvBindByPosition(position = 1)
    String    projectId;

    @CsvCustomBindByPosition(position = 2, converter = LocalDateConverter.class)
    LocalDate dateFrom;

    @CsvCustomBindByPosition(position = 3, converter = LocalDateConverter.class)
    @Builder.Default // TODO This worked OK, before the ENUM entered the show
    LocalDate dateTo = LocalDate.now();
}
