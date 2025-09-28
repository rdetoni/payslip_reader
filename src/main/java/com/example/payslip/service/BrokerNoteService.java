package com.example.payslip.service;

import com.example.payslip.bl.AssetConverter;
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
    private final AssetPublisher assetPublisher;

    @Autowired
    public BrokerNoteService(RicoBrokerNoteReader ricoBrokerNoteReader,
                             RicoBrokerNoteRepository ricoBrokerNoteRepository,
                             AssetPublisher assetPublisher){
        this.ricoBrokerNoteReader = ricoBrokerNoteReader;
        this.ricoBrokerNoteRepository = ricoBrokerNoteRepository;
        this.assetPublisher = assetPublisher;
    }

    @Transactional
    public void createRicoBrokerNote(MultipartFile file) throws IOException {
        val ricoBrokerNotes = ricoBrokerNoteReader.getBrokerNoteFromFile(file);
        ricoBrokerNotes.forEach(note -> {
                ricoBrokerNoteRepository.save(note);
                assetPublisher.publishAsset(AssetConverter.convertNoteToMessage(note));
            }
        );
    }
}
