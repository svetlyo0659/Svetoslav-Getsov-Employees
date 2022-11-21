package com.example.application.model;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder(toBuilder = true)
public class PairedEmployeesPerProject
{
    String  firstEmployeeId;
    String  secondEmployeeId;
    String  projectId;
    Long    daysWorked;

    public void addOverlapDuration (long overlapDays)
    {
        this.daysWorked += overlapDays;
    }
}
