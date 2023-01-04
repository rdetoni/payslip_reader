package com.example.payslip.model.entities;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Month;
import java.time.Year;
@Entity
@Getter @Setter @NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Payslip {

    public Payslip(Month month, Year year, Double totalYearSum, Double baseSalary, Double bonus,
                   Double netSalary, byte[] payslipFile){
        this.month = month;
        this.year = year;
        this.totalYearSum = totalYearSum;
        this.baseSalary = baseSalary;
        this.bonus = bonus;
        this.netSalary = netSalary;
        this.payslipFile = payslipFile;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
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

    @Lob
    @Column(name="payslip_file", columnDefinition="MEDIUMBLOB")
    private byte[] payslipFile;
}
