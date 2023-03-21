package com.example.payslip.service;

import com.example.payslip.model.entities.Payslip;
import com.example.payslip.model.repo.PayslipRepository;
import org.junit.After;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.time.DateTimeException;
import java.time.Month;
import java.time.Year;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class PayslipServiceIntegrationTest {

    private int payslipId;

    @Autowired
    private PayslipService payslipService;

    @Autowired
    private PayslipRepository payslipRepository;

    @Test(expected = DateTimeException.class)
    public void testAGetPayslipByMonthAndYearTest() throws IOException{
        assertNotNull(payslipService.getPayslipByMonthAndYear(10, 2022));
        payslipService.getPayslipByMonthAndYear(000000, 12);
    }

    @Test
    public void testBCreatePayslipTest() throws IOException {
        ClassLoader classLoader = this.getClass().getClassLoader();
        File dummyPayslip = new File(classLoader.getResource("test_payslip.pdf").getFile());
        FileInputStream inputFile = new FileInputStream(dummyPayslip);
        MockMultipartFile file = new MockMultipartFile("file", "test_payslip.pdf", "application/pdf", inputFile);

        Payslip payslip = payslipService.createPayslip(file, "85092321033");

        assertNotNull(payslip);
        assertEquals(payslip.getMonth(), Month.OCTOBER);
        assertEquals(payslip.getYear(), Year.of(2022));
        assertEquals(payslip.getTotalYearSum(), 158941.38, 0.00);
        assertEquals(payslip.getBaseSalary(), 15000.00, 0.00);
        assertEquals(payslip.getBonus(), 2206.08, 0.00);
        assertEquals(payslip.getNetSalary(), 12398.2, 0.00);
        assertNotNull(payslip.getPayslipFile());

        this.payslipId = payslip.getId();
    }

    @After
    public void cleanUp(){
        if(payslipId > 0){
            payslipRepository.deleteById(payslipId);
        }
    }
}
