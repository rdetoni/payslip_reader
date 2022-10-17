package com.example.payslip.model.converters;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.time.Year;

@Converter(autoApply = true)
public class YearConverter implements AttributeConverter<Year, Integer> {
    @Override
    public Integer convertToDatabaseColumn(Year year) {
        int dbValue = year.getValue();

        return dbValue;
    }

    @Override
    public Year convertToEntityAttribute(Integer dbValue) {
        Year year = Year.of(dbValue);

        return year;
    }
}
