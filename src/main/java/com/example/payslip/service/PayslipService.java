package com.example.payslip.service;

import com.example.payslip.bl.PayslipReader;
import com.example.payslip.bl.PayslipWriter;
import com.example.payslip.model.entities.Payslip;
import com.example.payslip.model.repo.PayslipRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.FileAlreadyExistsException;

@Service
public class PayslipService {
    private PayslipRepository payslipRepository;

    private PayslipReader payslipReader;

    private PayslipWriter payslipWriter;

    @Autowired
    public PayslipService(PayslipRepository payslipRepository, PayslipReader payslipReader, PayslipWriter payslipWriter){
        this.payslipRepository = payslipRepository;
        this.payslipReader = payslipReader;
        this.payslipWriter = payslipWriter;
    }

    protected PayslipService(){}

    public Payslip createPayslip(MultipartFile payslipPdf, String password) throws IOException, FileAlreadyExistsException {
        Payslip payslip = payslipReader.getPayslipFromFile(payslipPdf, password);
        if(payslip.getId() == null){
            return payslipRepository.save(payslip);
        }else{
            return payslip;
        }
    }

    public ResponseEntity<byte[]> getPayslipByMonthAndYear(int month, int year) throws FileNotFoundException, IOException {
        return payslipWriter.getFileFromPayslip(month, year);
    }

    public Iterable<Payslip> lookup(){
        return payslipRepository.findAll();
    }

    public long total(){
        return payslipRepository.count();
    }
}
