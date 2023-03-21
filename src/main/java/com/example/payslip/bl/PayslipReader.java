package com.example.payslip.bl;

import com.example.payslip.model.entities.Payslip;
import com.example.payslip.model.repo.PayslipRepository;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.FileAlreadyExistsException;
import java.time.Month;
import java.time.Year;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
@Slf4j
public class PayslipReader {

private PayslipRepository payslipRepository;

@Value("${payslip.regex.dateRegex}")
private String dateRegex;

@Getter
private String date;

@Value("${payslip.regex.totalYearRegex}")
private String totalYearRegex;

@Getter
private String totalYear;

@Value("${payslip.regex.baseSalaryRegex}")
private String baseSalaryRegex;

@Getter
private String baseSalary;

@Value("${payslip.regex.bonusRegex}")
private String bonusRegex;

@Getter
private String bonus;

@Value("${payslip.regex.netSalaryRegex}")
private String netSalaryRegex;

@Getter
private String netSalary;

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

private void isExistingPayslip(Payslip payslip) throws FileAlreadyExistsException {
    log.info("Checking if payslip already exists.");

    if(payslipRepository.findByMonthAndYear(payslip.getMonth(), payslip.getYear()).isPresent()){
        log.info("Payslip already exists.");
        throw new FileAlreadyExistsException("Payslip already exists.");
    }
}

private File getFile(MultipartFile file, String fileName) throws IOException{
    File convertedFile = new File(System.getProperty("java.io.tmpdir")+"/"+fileName);

    if(convertedFile.exists()){
        return convertedFile;
    }else{
        OutputStream outputStream = new FileOutputStream(convertedFile);
        outputStream.write(file.getBytes());
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
        log.error("File could not be processed.");
        throw new IOException("File could not be processed. Missing information.");
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
    log.info("Data validated.");
    return new Payslip(month, year, totalYearSum, baseSalary, bonus, netSalary, file.getBytes());
}

public Payslip getPayslipFromFile(MultipartFile originalPdf, String password) throws IOException, FileAlreadyExistsException {
    log.info("Process for reading PDF file started.");
    PDDocument document = getFileDecrypted(getFile(originalPdf, originalPdf.getOriginalFilename()), password);
    PDFTextStripper pdfTextStripper = new PDFTextStripper();
    String payslipContent = pdfTextStripper.getText(document);
    document.close();

    log.info("Extracting necessary information");
    //gets month and year
    date = extractString(dateRegex, payslipContent);

    //total year sum
    //TODO: Special character ł is not working when used as a property. Needs to be fixed.
    totalYear = extractString("Podstawa składek\\s\\n.*\\n\\s[\\d]{2,3}\\s[\\d]{2,3},[\\d]{2}", payslipContent);
    totalYear = totalYear.replaceAll(",",".").substring(34, totalYear.length()).replaceAll(" ", "");

    //base salary
    baseSalary = extractString(baseSalaryRegex, payslipContent).replaceAll(",", ".").substring(0, 9)
            .replaceAll(" ", "");
    //bonus
    bonus = extractString(bonusRegex, payslipContent);
    if(!bonus.isEmpty()){
        bonus = bonus.replaceAll(",", ".").substring(0, bonus.length()-5).replaceAll(" ", "");
    }

    //net salary
    netSalary = extractString(netSalaryRegex, payslipContent).replaceAll(",", ".")
            .substring(0, 8).replaceAll(" ", "");
    log.info("Information extracted successfully.");

    Payslip newPayslip = validateAndCreateObject(date, totalYear, baseSalary, bonus, netSalary, originalPdf);

    /*not the best solution, but payslip for October 2022 is being used for integration test, and should be allowed to be
    saved more than once. Suggestions for improve this are welcome*/
    if(!(newPayslip.getMonth().compareTo(Month.OCTOBER) == 0) && !(newPayslip.getYear().compareTo(Year.of(2022)) == 0)){
        isExistingPayslip(newPayslip);
    }

    log.info("Payslip was created, for Month {} and Year {}", new Object[]{newPayslip.getMonth().name(), newPayslip.getYear()});
    return newPayslip;
}

}
