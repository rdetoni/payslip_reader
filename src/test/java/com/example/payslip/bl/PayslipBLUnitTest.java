package com.example.payslip.bl;

import com.example.payslip.model.entities.Payslip;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.util.ReflectionTestUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import static org.junit.Assert.*;

@RunWith(MockitoJUnitRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
public class PayslipBLUnitTest {
    @InjectMocks
    private PayslipReader payslipReader;

    @Before
    public void setup(){
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void getPayslipFromFileTest() throws IOException {
        ClassLoader classLoader = this.getClass().getClassLoader();
        File dummyPayslip = new File(classLoader.getResource("test_payslip.pdf").getFile());
        FileInputStream inputFile = new FileInputStream(dummyPayslip);
        MockMultipartFile file = new MockMultipartFile("file", "test_payslip.pdf", "application/pdf", inputFile);

        //values for regex are loaded from properties, so we need to set them manually for mocking purposes. Please adjust
        //the values according to your needs
        ReflectionTestUtils.setField(payslipReader, "dateRegex", "[0-9]{2}\\.[0-9]{2}\\.[0-9]{4}");
        ReflectionTestUtils.setField(payslipReader, "baseSalaryRegex", "[\\d]{2}\\s[\\d]{3},[\\d]{2}\\sPLN");
        ReflectionTestUtils.setField(payslipReader, "bonusRegex", "[\\d]{0,2}\\s[\\d]{3},[\\d]{2}Bonus");
        ReflectionTestUtils.setField(payslipReader, "netSalaryRegex", "[\\d]{1,2}\\s[\\d]{3},[\\d]{2}\\nROR");

        try{
            Payslip payslip = payslipReader.getPayslipFromFile(file, "85092321033");
            assertNotNull(payslip);
            assertFalse(payslipReader.getDate().isEmpty());
            assertFalse(payslipReader.getTotalYear().isEmpty());
            assertFalse(payslipReader.getBaseSalary().isEmpty());
            assertFalse(payslipReader.getBonus().isEmpty());
            assertFalse(payslipReader.getNetSalary().isEmpty());
        }catch(StringIndexOutOfBoundsException e){
            fail("Error trying to process info from payslip. Regex not working.");
        }
    }
}
