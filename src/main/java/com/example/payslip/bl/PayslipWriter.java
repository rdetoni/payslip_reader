package com.example.payslip.bl;

import com.example.payslip.model.entities.Payslip;
import com.example.payslip.model.repo.PayslipRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

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

    private MultipartFile convertArrayOfBytesIntoFile(byte[] data, String month, String year) throws IOException{
        String fileName = "Payslip_" + month + "_" + year + ".pdf";
        CustomMultipartFile customMultipartFile = new CustomMultipartFile(data, fileName);

        log.debug("Attempting to create file from data.");
        try{
            customMultipartFile.transferTo(customMultipartFile.getFile());
        }finally{
            customMultipartFile.clearOutStreams();
        }
        log.debug("Successfully inserted bytes into file.");

        return customMultipartFile;
    }

    public MultipartFile getFileFromPayslip(int month, int year) throws FileNotFoundException, IOException{
        Month convertedMonth = Month.of(month);
        Year convertedYear = Year.of(year);
        log.info("Retrieving payslip file for {0} {1}", new Object[]{convertedMonth.name(), convertedYear.toString()});

        Payslip payslip = payslipRepository.findByMonthAndYear(convertedMonth, convertedYear)
                .orElseThrow(FileNotFoundException::new);

        log.info("Payslip found.");

        return convertArrayOfBytesIntoFile(payslip.getPayslipFile(), convertedMonth.name(), convertedYear.toString());
    }
}
