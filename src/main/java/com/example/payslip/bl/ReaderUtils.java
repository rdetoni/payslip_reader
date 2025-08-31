package com.example.payslip.bl;

import lombok.experimental.UtilityClass;
import lombok.val;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@UtilityClass
public class ReaderUtils {
    public File getFile(MultipartFile file, String fileName) throws IOException {
        File convertedFile = new File(System.getProperty("java.io.tmpdir")+"/"+fileName);

        if(convertedFile.exists()){
            return convertedFile;
        }else{
            OutputStream outputStream = new FileOutputStream(convertedFile);
            outputStream.write(file.getBytes());
            return convertedFile;
        }
    }

    public String extractString(String regex, String text, int groupIndex){
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(text);
        return matcher.find() ? matcher.group(groupIndex) : "";
    }

    public LocalDate getDateFromString(String date){
        return LocalDate.parse(date, DateTimeFormatter.ofPattern("dd/MM/yyyy"));
    }
}
