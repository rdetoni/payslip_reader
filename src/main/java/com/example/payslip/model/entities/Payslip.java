package com.example.payslip.model.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

import java.time.Month;
import java.time.Year;
@Entity
public class Payslip {

    @Id
    @GeneratedValue
    private Integer id;

    @Column
    private Month month;

    @Column
    private Year year;

    @Column
    private Double totalYearSum;

    @Column
    private Double baseSalary;

    @Column
    private Double bonus;

    @Column
    private Double netSalary;

    @Column
    private byte[] payslipFile;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Month getMonth() {
        return month;
    }

    public void setMonth(Month month) {
        this.month = month;
    }

    public Year getYear() {
        return year;
    }

    public void setYear(Year year) {
        this.year = year;
    }

    public Double getTotalYearSum() {
        return totalYearSum;
    }

    public void setTotalYearSum(Double totalYearSum) {
        this.totalYearSum = totalYearSum;
    }

    public Double getBaseSalary() {
        return baseSalary;
    }

    public void setBaseSalary(Double baseSalary) {
        this.baseSalary = baseSalary;
    }

    public Double getBonus() {
        return bonus;
    }

    public void setBonus(Double bonus) {
        this.bonus = bonus;
    }

    public Double getNetSalary() {
        return netSalary;
    }

    public void setNetSalary(Double netSalary) {
        this.netSalary = netSalary;
    }

    public byte[] getPayslipFile() {
        return payslipFile;
    }

    public void setPayslipFile(byte[] payslipFile) {
        this.payslipFile = payslipFile;
    }
}
