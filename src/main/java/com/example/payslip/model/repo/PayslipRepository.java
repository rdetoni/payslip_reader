package com.example.payslip.model.repo;

import com.example.payslip.model.entities.Payslip;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RestResource;

import java.time.Month;
import java.time.Year;
import java.util.Optional;

public interface PayslipRepository extends CrudRepository<Payslip, Integer> {
    @Override
    @RestResource(exported = false)
    <S extends Payslip> S save(S entity);

    @Override
    @RestResource(exported = false)
    <S extends Payslip> Iterable<S> saveAll(Iterable<S> entities);

    @Override
    @RestResource(exported = false)
    void deleteById(Integer integer);

    @Override
    @RestResource(exported = false)
    void delete(Payslip entity);

    @Override
    @RestResource(exported = false)
    void deleteAllById(Iterable<? extends Integer> integers);

    @Override
    @RestResource(exported = false)
    void deleteAll(Iterable<? extends Payslip> entities);

    @Override
    @RestResource(exported = false)
    void deleteAll();

    /**
     * Lookup a Payslip by the Month and Year
     *
     * @param month - Month to be searched
     * @param year - Year to be searched
     * @return Optional of found Payslip
     */
    Optional<Payslip> findByMonthAndYear(Month month, Year year);
}
