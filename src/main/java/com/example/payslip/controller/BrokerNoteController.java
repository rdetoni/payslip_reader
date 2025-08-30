package com.example.payslip.controller;

import com.example.payslip.service.BrokerNoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping(path = "/brokerNoteReader")
public class BrokerNoteController {
    private BrokerNoteService brokerNoteService;

    @Autowired
    public BrokerNoteController (BrokerNoteService brokerNoteService){
        this.brokerNoteService = brokerNoteService;
    }

    @PostMapping("/createRicoNote")
    @ResponseStatus(HttpStatus.CREATED)
    public void createRicoNote(@RequestParam MultipartFile file) throws IOException {
        this.brokerNoteService.createRicoBrokerNote(file);
    }
}
