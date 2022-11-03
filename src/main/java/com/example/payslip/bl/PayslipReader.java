package com.example.payslip.bl;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;

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

}
