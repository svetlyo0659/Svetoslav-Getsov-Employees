package com.example.application.model;

import com.example.application.LocalDateFormatsEnum;
import com.opencsv.bean.AbstractBeanField;

import java.time.LocalDate;

public class LocalDateConverter extends AbstractBeanField {

    @Override
    protected Object convert(String jsonDate)
    {
        if (isEmpty(jsonDate)) {   // TODO MUST REDO THIS IS STUPID was using @Builder.Default
            jsonDate = LocalDate.now().toString();
        }
        return LocalDateFormatsEnum.getFormat(jsonDate);
    }


    //using this because String.Utils .isEmpty is deprecated
    private boolean isEmpty(String s)
    {
        return  s == null || "".equals(s);
    }
}
