package com.example.payslip.model.converters;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.time.Month;

@Converter(autoApply = true)
public class MonthConverter implements AttributeConverter<Month, Integer> {
    @Override
    public Integer convertToDatabaseColumn(Month month) {
        return month.getValue();
    }

    @Override
    public Month convertToEntityAttribute(Integer dbValue) {
        return Month.of(dbValue);
    }
}
