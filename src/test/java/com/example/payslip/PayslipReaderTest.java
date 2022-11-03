package com.example.payslip;

import com.example.payslip.bl.PayslipReader;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = PayslipReader.class)
@TestPropertySource
public class PayslipReaderTest {
    @Autowired
    PayslipReader payslipReader;

    @Test
    @DisplayName("File cannot be null")
    void testFileNotNull() throws IOException {
        assertNotNull(payslipReader.getFileDecrypted());
    }

    @Test
    @DisplayName("File cannot be encrypted")
    void testFileNotEncrypted() throws IOException {
        assertFalse(payslipReader.getFileDecrypted().isEncrypted());
    }

}
