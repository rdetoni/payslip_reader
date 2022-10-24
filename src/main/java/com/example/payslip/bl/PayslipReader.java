package com.example.payslip.bl;

import org.apache.pdfbox.pdmodel.PDDocument;

import java.io.File;
import java.io.IOException;

public class PayslipReader {

private void getFileDecrypted() throws IOException {
    File originalPdf = new File("src/main/resources/DETONI_MORAES_RICARDO_Kwitek_wypłaty.PDF");
    PDDocument document =  PDDocument.load(originalPdf);

    if(document.isEncrypted()){
//        document.
    }
}
}
