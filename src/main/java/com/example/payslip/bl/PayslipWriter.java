package com.example.payslip.bl;

import com.example.payslip.model.entities.Payslip;
import com.example.payslip.model.repo.PayslipRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.time.Month;
import java.time.Year;

@Component
@Slf4j
public class PayslipWriter {
    private PayslipRepository payslipRepository;

    @Autowired
    public PayslipWriter(PayslipRepository payslipRepository){
        this.payslipRepository = payslipRepository;
    }

    private ResponseEntity<byte[]> convertArrayOfBytesIntoFile(byte[] data, String month, String year) throws IOException{
        String fileName = "Payslip_" + month + "_" + year + ".pdf";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDispositionFormData(fileName, fileName);
        headers.setCacheControl("must-revalidate, post-check=0, pre-check=0");

        return new ResponseEntity<byte[]>(data, headers, HttpStatus.OK);
    }

    public ResponseEntity<byte[]> getFileFromPayslip(int month, int year) throws FileNotFoundException, IOException{
        Month convertedMonth = Month.of(month);
        Year convertedYear = Year.of(year);
        log.info("Retrieving payslip file for {} {}", new String[]{convertedMonth.name(), convertedYear.toString()});

        Payslip payslip = payslipRepository.findByMonthAndYear(convertedMonth, convertedYear)
                .orElseThrow(FileNotFoundException::new);

        log.info("Payslip found.");

        return convertArrayOfBytesIntoFile(payslip.getPayslipFile(), convertedMonth.name(), convertedYear.toString());
    }
}
