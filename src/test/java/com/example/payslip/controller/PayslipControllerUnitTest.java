package com.example.payslip.controller;

import com.example.payslip.model.entities.Payslip;
import com.example.payslip.service.PayslipService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.web.multipart.MultipartFile;

import java.time.Month;
import java.time.Year;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@WebMvcTest
public class PayslipControllerUnitTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PayslipService payslipService;

    @InjectMocks
    private PayslipReaderController payslipReaderController;

    @Test
    public void successfullyCreatePayslipTest() throws Exception {
        MockMultipartFile dummyFile = new MockMultipartFile("file", "dummy_file.txt", MediaType.TEXT_PLAIN_VALUE, "Hello World".getBytes());
        Payslip payslip = new Payslip(Month.DECEMBER, Year.of(2002), 10000.00, 1000.00, 200.00, 700.00, new byte[]{});

        when(payslipService.createPayslip(any(MultipartFile.class), any(String.class))).thenReturn(payslip);

        mockMvc.perform(MockMvcRequestBuilders.multipart("/payslipReader/create").file(dummyFile))
               .andExpect(MockMvcResultMatchers.status().is(201));
    }

    @Test
    public void successfullyFindByMonthAndYearTest() throws Exception {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDispositionFormData("dummy_payslip.pdf", "dummy_payslip.pdf");
        headers.setCacheControl("must-revalidate, post-check=0, pre-check=0");
        ResponseEntity<byte[]> responseEntity = new ResponseEntity<>("Hello World".getBytes(), headers, HttpStatus.OK);

        when(payslipService.getPayslipByMonthAndYear(any(Integer.class), any(Integer.class))).thenReturn(responseEntity);

        mockMvc.perform(MockMvcRequestBuilders.get("/payslipReader/findByMonthAndYear")
                .param("month", "10").param("year", "2022"))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }
}
