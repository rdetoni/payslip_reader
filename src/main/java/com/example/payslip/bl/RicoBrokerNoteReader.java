package com.example.payslip.bl;

import com.example.payslip.model.entities.RicoBrokerNote;
import com.example.payslip.model.enums.OperationType;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Slf4j
@Component
public class RicoBrokerNoteReader {
    private static final String BROKER_NOTE_NAME = "NOTA_DE_CORRETAGEM_";

    @Value("${rico.brokerNote.operationType}")
    private String operationType;
    @Value("${rico.brokerNote.name}")
    private String name;
    @Value("${rico.brokerNote.ticker}")
    private String ticker;
    @Value("${rico.brokerNote.quantity}")
    private String quantity;
    @Value("${rico.brokerNote.price}")
    private String price;
    @Value("${rico.brokerNote.total}")
    private String total;
    @Value("${rico.brokerNote.totalWithTaxes}")
    private String totalWithTaxes;
    @Value("${rico.brokerNote.date}")
    private String date;

    public RicoBrokerNote getBrokerNoteFromFile(MultipartFile file) throws IOException {
        log.info("Starting process for RICO's broker note.");
        try (PDDocument document = PDDocument.load(
                ReaderUtils.getFile(file, BROKER_NOTE_NAME.concat(LocalDate.now().toString())))) {
            PDFTextStripper pdfTextStripper = new PDFTextStripper();
            String brokerNoteContent = pdfTextStripper.getText(document);

            val noteTotalWithTaxes = BigDecimal.valueOf(Double.parseDouble(ReaderUtils.extractString(this.totalWithTaxes, brokerNoteContent, 1).trim().replace(',', '.')));
            val noteTotal = BigDecimal.valueOf(Double.parseDouble(ReaderUtils.extractString(this.total, brokerNoteContent, 1).trim().replace(',', '.')));

            val ricoBrokerNote = RicoBrokerNote.builder()
                    .operationType(OperationType.fromCode(ReaderUtils.extractString(this.operationType, brokerNoteContent, 1)))
                    .name(ReaderUtils.extractString(this.name, brokerNoteContent, 1))
                    .ticker(ReaderUtils.extractString(this.ticker, brokerNoteContent, 1))
                    .quantity(Integer.valueOf(ReaderUtils.extractString(this.quantity, brokerNoteContent, 1)))
                    .price(BigDecimal.valueOf(Double.parseDouble(ReaderUtils.extractString(this.price, brokerNoteContent, 1).trim().replace(',', '.'))))
                    .total(noteTotal)
                    .fees(noteTotalWithTaxes.subtract(noteTotal))
                    .date(ReaderUtils.getDateFromString(ReaderUtils.extractString(this.date, brokerNoteContent, 1)))
                    .createdAt(LocalDateTime.now())
                    .updatedAt(LocalDateTime.now())
                    .build();

            return null;


        } catch (IOException e) {
            log.info("[RicoBrokerNoteReader] Exception caught processing RICO's broker note: {}", e.getMessage());
            throw e;
        }

    }
}
