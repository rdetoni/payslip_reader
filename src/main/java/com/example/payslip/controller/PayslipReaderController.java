package com.example.payslip.controller;

import com.example.payslip.service.PayslipService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.FileAlreadyExistsException;

@RestController
@RequestMapping(path="/payslipReader")
public class PayslipReaderController {
    //TODO: Take a look at Spring Actuator to log request and response calls for this controller
    private PayslipService payslipService;

    @Autowired
    public PayslipReaderController(PayslipService payslipService){
        this.payslipService = payslipService;
    }

    protected PayslipReaderController(){}

    @PostMapping("/create")
    @ResponseStatus(HttpStatus.CREATED)
    public void create(@RequestParam MultipartFile file,
                       @RequestParam(name = "password", required = false) String password) throws IOException, FileAlreadyExistsException {
        payslipService.createPayslip(file, password);
    }

    @GetMapping("/findByMonthAndYear")
    @ResponseStatus(HttpStatus.OK)
    public MultipartFile findByMonthAndYear(@RequestParam(name = "month") int month,
                                            @RequestParam(name = "year") int year) throws FileNotFoundException, IOException{
        return payslipService.getPayslipByMonthAndYear(month, year);
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(IOException.class)
    public String return500(IOException e){
        return "Error processing PDF file. Exception is: " + e.getMessage();
    }

    @ResponseStatus(HttpStatus.CONFLICT)
    @ExceptionHandler(FileAlreadyExistsException.class)
    public String return409(FileAlreadyExistsException e){
        return e.getMessage();
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(FileAlreadyExistsException.class)
    public String return409(FileNotFoundException e){
        return "Payslip not found.";
    }
}
