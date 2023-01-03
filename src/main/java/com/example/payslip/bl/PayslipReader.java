package com.example.payslip.bl;

import com.example.payslip.model.entities.Payslip;
import com.example.payslip.model.repo.PayslipRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileAlreadyExistsException;
import java.time.Month;
import java.time.Year;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Component
@Slf4j
public class PayslipReader {

private PayslipRepository payslipRepository;

@Autowired
public PayslipReader(PayslipRepository payslipRepository){
    this.payslipRepository = payslipRepository;
}

protected PayslipReader(){}

private PDDocument getFileDecrypted(File originalPdf, String password) throws IOException {
    PDDocument document = null;

    if(originalPdf != null){
        if(password != null){
            log.info("Trying to decrypt PDF with password provided.");
            document = PDDocument.load(originalPdf, password);
        }else{
            document = PDDocument.load(originalPdf);
        }
    }

    return document;
}

private String extractString(String regex, String text){
    Pattern pattern = Pattern.compile(regex);
    Matcher matcher = pattern.matcher(text);
    return matcher.find() ? matcher.group(0) : "";
}

private void isExistingPayslip(Payslip payslip) throws FileAlreadyExistsException{
    log.info("Checking if payslip already exists.");
    List<Payslip> payslipList =  StreamSupport.stream(payslipRepository.findAll().spliterator(), false)
            .filter(p -> (p.getMonth() == payslip.getMonth()) && (p.getYear().compareTo(payslip.getYear()) == 0))
            .collect(Collectors.toList());

    if(!payslipList.isEmpty()){
        log.error("Payslip already exists.");
        throw new FileAlreadyExistsException("Payslip already exists.");
    }
}

private File getFile(MultipartFile file, String fileName) throws IOException{
    File convertedFile = new File(System.getProperty("java.io.tmpdir")+"/"+fileName);

    if(convertedFile.exists()){
        return convertedFile;
    }else{
        file.transferTo(convertedFile);
        return convertedFile;
    }
}

private Payslip validateAndCreateObject(String date, String totalYear, String baseSal, String bonusText,
                                        String netSalaryText, MultipartFile file) throws IOException{
    log.debug("Creating payslip object.");
    Double bonus = null;
    Month month;
    Year year;
    Double totalYearSum;
    Double baseSalary;
    Double netSalary;

    //validates all data
    if(date.isEmpty() || totalYear.isEmpty() || baseSal.isEmpty() || netSalaryText.isEmpty()){
        log.error("Error validating data. Missing information.");
        return null;
    }else{
        month = Month.of(Integer.valueOf(date.substring(3, 5)));
        log.debug("Month: {}", month.name());
        year = Year.of(Integer.valueOf(date.substring(6, 10)));
        log.debug("Year: {}", year.getValue());
        totalYearSum = Double.valueOf(totalYear);
        baseSalary = Double.valueOf(baseSal);
        netSalary = Double.valueOf(netSalaryText);
        if(!bonusText.isEmpty()){
            bonus = Double.valueOf(bonusText);
        }
    }

    //populates and return new object
    log.info("Data valildated.");
    return new Payslip(month, year, totalYearSum, baseSalary, bonus, netSalary, file.getBytes());
}

public Payslip getPayslipFromFile(MultipartFile originalPdf, String password) throws IOException, FileAlreadyExistsException {
    log.info("Process for reading PDF file started.");
    PDDocument document = getFileDecrypted(getFile(originalPdf, originalPdf.getOriginalFilename()), password);
    PDFTextStripper pdfTextStripper = new PDFTextStripper();
    String payslipContent = pdfTextStripper.getText(document);
    document.close();

    log.info("Extracting necessary information");
    //TODO: Regex should be properties on application.properties file, and not hardcoded strings.
    //gets month and year
    String date = extractString("[0-9]{2}\\.[0-9]{2}\\.[0-9]{4}", payslipContent);

    //total year sum
    String totalYear = extractString("Podstawa składek\\s\\r\\n.*\\r\\n\\s[\\d]{2,3}\\s[\\d]{2,3},[\\d]{2}", payslipContent)
            .replaceAll(",",".").substring(34, 44).replaceAll(" ", "");

    //base salary
    String base = extractString("[\\d]{2}\\s[\\d]{3},[\\d]{2}\\sPLN", payslipContent)
            .replaceAll(",", ".").substring(0, 9).replaceAll(" ", "");
    //bonus
    String bonus = extractString("Bonus\\s[\\d]{3},[\\d]{2}", payslipContent);
    if(!bonus.isEmpty()){
        bonus = bonus.replaceAll(",", ".").substring(6, 12).replaceAll(" ", "");
    }

    //net salary
    String netSalary = extractString("[\\d]{1,2}\\s[\\d]{3},[\\d]{2}\\r\\nROR", payslipContent)
            .replaceAll(",", ".").substring(0, 8).replaceAll(" ", "");
    log.info("Information extracted successfully.");

    Payslip newPayslip = validateAndCreateObject(date, totalYear, base, bonus, netSalary, originalPdf);
    isExistingPayslip(newPayslip);

    if(newPayslip == null){
        log.error("File could not be processed.");
        throw new IOException("File could not be processed");
    }else{
        log.info("Payslip was created, for Month {0} and Year {1}", new Object[]{newPayslip.getMonth().name(), newPayslip.getYear()});
        return newPayslip;
    }
}

}
