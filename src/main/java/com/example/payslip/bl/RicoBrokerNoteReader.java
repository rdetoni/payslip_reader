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
import java.util.Arrays;
import java.util.List;

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
    @Value("${rico.brokerNote.bovespa}")
    private String bovespa;

    public List<RicoBrokerNote> getBrokerNoteFromFile(MultipartFile file) throws IOException {
        log.info("Starting process for RICO's broker note.");
        try (PDDocument document = PDDocument.load(
                ReaderUtils.getFile(file, BROKER_NOTE_NAME.concat(LocalDate.now().toString())))) {
            PDFTextStripper pdfTextStripper = new PDFTextStripper();
            String brokerNoteContent = pdfTextStripper.getText(document);
            List<String> lines = Arrays.asList(brokerNoteContent.split("\\r?\\n"));

            val noteTotalWithTaxes = BigDecimal.valueOf(Double.parseDouble(ReaderUtils.extractString(
                    this.totalWithTaxes, brokerNoteContent, 1).trim().replace(',', '.')));
            val noteTotal = BigDecimal.valueOf(Double.parseDouble(ReaderUtils.extractString(
                    this.total, brokerNoteContent, 1).substring(1).trim()
                    .replace(',', '.')));
            val totalFee = noteTotalWithTaxes.subtract(noteTotal);
            val date = ReaderUtils.getDateFromString(ReaderUtils.extractString(
                    this.date, brokerNoteContent, 1));
            val brokerNote = file.getBytes();

            return  lines.stream()
                        .filter(line -> line.contains(this.bovespa))
                        .map(line -> {
                            val quantity = Integer.valueOf(ReaderUtils.extractString(this.quantity, line, 1));
                            val price = BigDecimal.valueOf(Double.parseDouble(ReaderUtils.extractString(
                                    this.price, line, 1).trim().replace(',', '.')));
                            return  RicoBrokerNote.builder()
                                        .operationType(OperationType.fromCode(ReaderUtils.extractString(
                                                this.operationType, line, 1)))
                                        .name(ReaderUtils.extractString(this.name, line, 1))
                                        .ticker(ReaderUtils.extractString(this.ticker, line, 1))
                                        .quantity(quantity)
                                        .price(price)
                                        .total(noteTotal)
                                        .fees(calculateSingleFee(totalFee,quantity, price, noteTotal))
                                        .date(date)
                                        .createdAt(LocalDateTime.now())
                                        .updatedAt(LocalDateTime.now())
                                        .brokerNote(brokerNote)
                                        .build();
                        })
                        .toList();
        } catch (IOException e) {
            log.info("[RicoBrokerNoteReader] Exception caught processing RICO's broker note: {}", e.getMessage());
            throw e;
        }

    }

    private BigDecimal calculateSingleFee(BigDecimal totalFee, int quantity, BigDecimal unityValue,
                                          BigDecimal totalValue) {
        BigDecimal percentage = BigDecimal.valueOf(quantity).multiply(unityValue).divide(totalValue)
                .multiply(BigDecimal.valueOf(100.0));
        return totalFee.multiply(percentage);
    }
}
