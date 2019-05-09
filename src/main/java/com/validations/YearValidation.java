package com.validations;

import com.exceptions.BadRequestEXception;

import org.springframework.stereotype.Component;

@Component
public class YearValidation {

    public void validation(String year) throws BadRequestEXception{
        if (year == null || year.isEmpty())
            throw new BadRequestEXception("year can't be null");
        if (!year.matches("^[0-9]{4}$"))
            throw new BadRequestEXception("bad format");




    }
}
