package com.example.payslip.model.repo;

import com.example.payslip.model.entities.Payslip;
import org.springframework.data.repository.CrudRepository;

public interface PayslipRepository extends CrudRepository<Payslip, Integer> {
}
