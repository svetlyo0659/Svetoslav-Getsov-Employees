package com.example.application.impl;

import com.example.application.CSVReaderService;
import com.example.application.exception.EmptyOrMissingFileException;
import com.example.application.exception.FileNotSupportedException;
import com.example.application.model.EmployeeRecord;
import com.example.application.model.PairedEmployeesPerProject;
import com.opencsv.bean.CsvToBeanBuilder;
import lombok.extern.apachecommons.CommonsLog;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service("cSVReaderService")
@CommonsLog
public class CSVReaderServiceImpl implements CSVReaderService {

    private static final String HELL_WORLD = "Hello World!";
    private static final List<String> supportedFiles = Arrays.asList("csv");
    @Override
    public String printHelloWorld()
    {
        return HELL_WORLD;
    }


    /**
     *   Sample data:
     *   143, 12, 2013-11-01, 2014-01-05
     *   218, 10, 2012-05-16, NULL
     *   143, 10, 2009-01-01, 2011-04-27
     *
     *   Sample output:
     *   143, 218, 8
     */

    @Override
    public List<PairedEmployeesPerProject> readCvsFile(MultipartFile file) throws IOException
    {

        fileValidation(file); //Didn't have the time to implement the javax annotation for @FileNotEmpty so this.

        Reader reader = new BufferedReader(
                new InputStreamReader(file.getInputStream()));

        List<EmployeeRecord> records = new CsvToBeanBuilder(reader)
                    .withSeparator(',')
                    .withType(EmployeeRecord.class)
                    .build()
                .parse();

        List<PairedEmployeesPerProject> employeesResult = new ArrayList<>();

        for (int i = 0; i < records.size(); i++) {
            for (int j = i + 1; j < records.size(); j++) {

                EmployeeRecord firstEmployee = records.get(i);
                EmployeeRecord secondEmployee = records.get(j);

                if (firstEmployee.getProjectId().equals(secondEmployee.getProjectId()) &&
                        hasTimeOverlaps(firstEmployee, secondEmployee)) {

                    long overlapDays = getOverlapDays(firstEmployee, secondEmployee);
                    if (overlapDays > 0) {
                   //TODO Check if the two employees worked again on the same project and add the time
                        employeesResult.add(
                                PairedEmployeesPerProject
                                        .builder()
                                          .firstEmployeeId(firstEmployee.getEmpId())
                                          .secondEmployeeId(secondEmployee.getEmpId())
                                          .projectId(firstEmployee.getProjectId())
                                          .daysWorked(overlapDays)
                                        .build());
                    }
                }
            }
        }

        // user collections sort for results to be shown in Descending order
        // -- use Lambda instead
        employeesResult.sort((o1, o2) -> o2.getDaysWorked()
                .compareTo(o1.getDaysWorked()));

        return employeesResult;
    }

    /***
     * @param firstEmployee
     * @param secondEmployee
     * @return true if
     *
     *  DF = Date-From:*
     *  DT = Date-To:*
     *
     *  DF1 = DF2 && DT1 = DT2
     *  OR
     *  DF1 before DF2 && DT1 after DT2
     *  OR
     *  DF1 before DF2 && DT1 before DT2
     *  OR
     *  DF1 after DF2 && DT1 after DF2 && DT1 before DF2
     *  OR
     *  DF1 after DF2 && DT1 before DT2
     *
     *  Found this discussion on stackoverflow about time range / overlap
     *  https://stackoverflow.com/questions/325933/determine-whether-two-date-ranges-overlap/325964#325964
     *  Now one of De Morgan's laws says that:
     *  Not (A Or B) <=> Not A And Not B
     *  Which translates to: (StartDate1 <= EndDate2) and (EndDate1 >= StartDate2)
     *  (DF1 <= DT2)  and  (DT1 >= DF2)
     *
     *  checks & scenarios
     *  ((df1.equals(df2)  && dt1.equals(dt2))   ||
     *  (df1.isBefore(df2) && dt1.isAfter(dt2))  ||
     *  (df1.isBefore(df2) && dt1.isBefore(dt2)  ||
     *  (df1.isAfter(df2)  && dt1.isAfter(df2)
     *                     && dt1.isBefore(df2)) ||
     *  (df1.isAfter(df2)  && dt1.isBefore(dt2))));
     *
     *  if (!(comparableEnd.isBefore(toCompareInit) || comparableInit.isAfter(toCompareEnd)))
     */

    private boolean hasTimeOverlaps(EmployeeRecord firstEmployee, EmployeeRecord secondEmployee)
    {
        LocalDate startDate1 = firstEmployee.getDateFrom();
        LocalDate startDate2 = secondEmployee.getDateFrom();
        LocalDate endDate1   = firstEmployee.getDateTo();
        LocalDate endDate2   = secondEmployee.getDateTo();

        return (startDate1.isBefore(endDate2) || startDate1.isEqual(endDate2)) &&
               (endDate1.isAfter(startDate2)  || endDate1.equals(startDate2));
    }


    /**
     * helpful info about time overlaps and calculation of it.
     * https://blogs.sas.com/content/sgf/2022/01/13/calculating-the-overlap-of-date-time-intervals/
     */

    private long getOverlapDays(EmployeeRecord firstEmployee,
                                EmployeeRecord secondEmployee)
    {
        LocalDate startDateInclusive = firstEmployee.getDateFrom().isBefore(secondEmployee.getDateFrom())
                ? secondEmployee.getDateFrom() : firstEmployee.getDateFrom();

        LocalDate endDateInclusive = firstEmployee.getDateTo().isBefore(secondEmployee.getDateTo())
                ? firstEmployee.getDateTo() : secondEmployee.getDateTo();

        //Use java class Period to extract working days between the paired employees
        Period period = Period.between(startDateInclusive,endDateInclusive);

        return period.getDays();
    }

    private void fileValidation(MultipartFile file)
    {
        if(file == null || file.isEmpty() ){
            throw new EmptyOrMissingFileException("File is empty or missing");
        }
        for (String format: supportedFiles) {
            if (file.getContentType() != null  && !file.getContentType().contains(format)) {
                throw new FileNotSupportedException("File type not-supported");
            }
        }

    }

}
