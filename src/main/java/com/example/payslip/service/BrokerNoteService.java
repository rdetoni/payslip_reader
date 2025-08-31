package com.example.payslip.service;

import com.example.payslip.bl.RicoBrokerNoteReader;
import com.example.payslip.model.entities.RicoBrokerNote;
import com.example.payslip.model.repo.RicoBrokerNoteRepository;
import jakarta.transaction.Transactional;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
public class BrokerNoteService {
    private final RicoBrokerNoteReader ricoBrokerNoteReader;
    private final RicoBrokerNoteRepository ricoBrokerNoteRepository;

    @Autowired
    public BrokerNoteService(RicoBrokerNoteReader ricoBrokerNoteReader,
                             RicoBrokerNoteRepository ricoBrokerNoteRepository){
        this.ricoBrokerNoteReader = ricoBrokerNoteReader;
        this.ricoBrokerNoteRepository = ricoBrokerNoteRepository;
    }

    @Transactional
    public RicoBrokerNote createRicoBrokerNote(MultipartFile file) throws IOException {
        val ricoBrokerNote = ricoBrokerNoteReader.getBrokerNoteFromFile(file);
        ricoBrokerNoteRepository.save(ricoBrokerNote);
        return null;
    }
}
