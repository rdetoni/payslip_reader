package com.example.payslip.bl;

import com.example.payslip.model.entities.Payslip;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.time.Month;
import java.time.Year;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class PayslipReader {

@Value("${pdf.password}")
private String pdfPassword;

@Value("${pdf.path}")
private String pdfFilePath;

public PDDocument getFileDecrypted() throws IOException {
    File originalPdf = null;
    PDDocument document = null;
    if(this.pdfFilePath != null && !this.pdfFilePath.isEmpty()){
        originalPdf = new File(this.pdfFilePath);
    }

    if(originalPdf != null){
        document = PDDocument.load(originalPdf);
        if(document.isEncrypted()) {
            if(this.pdfPassword != null){
                //TODO: password should be provided by controller, for the moment is loaded by properties file.
                document = PDDocument.load(originalPdf, pdfPassword);
            }else{
                document = PDDocument.load(originalPdf);
            }
        }
    }

    return document;
}

public Payslip getPayslipFromFile() throws IOException{
    Payslip payslip = new Payslip();
    PDDocument document = getFileDecrypted();
    PDFTextStripper pdfTextStripper = new PDFTextStripper();
    String payslipContent = pdfTextStripper.getText(document);

    //gets month and year
    String date = extractString("([0-9]{2}).([0-9]{2}).([0-9]{4})", payslipContent);
    if(date.isEmpty()){
        return null;
    }else{
        payslip.setMonth(Month.of(Integer.valueOf(date.substring(2, 4)) + 1));
        payslip.setYear(Year.of(Integer.valueOf(date.substring(5, 9))));
    }

    return payslip;
}

private String extractString(String regex, String text){
    Pattern pattern = Pattern.compile(regex);
    Matcher matcher = pattern.matcher(text);
    return matcher.find() ? matcher.group(1) : "";
}

}
