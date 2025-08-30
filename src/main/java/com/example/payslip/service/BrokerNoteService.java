package com.example.payslip.service;

import com.example.payslip.bl.RicoBrokerNoteReader;
import com.example.payslip.model.entities.RicoBrokerNote;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
public class BrokerNoteService {
    private RicoBrokerNoteReader ricoBrokerNoteReader;

    @Autowired
    public BrokerNoteService(RicoBrokerNoteReader ricoBrokerNoteReader){
        this.ricoBrokerNoteReader = ricoBrokerNoteReader;
    }

    public RicoBrokerNote createRicoBrokerNote(MultipartFile file) throws IOException {
        return this.ricoBrokerNoteReader.getBrokerNoteFromFile(file);
    }
}
