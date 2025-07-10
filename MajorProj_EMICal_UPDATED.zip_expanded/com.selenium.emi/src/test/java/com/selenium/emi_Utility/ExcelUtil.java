package com.selenium.emi_Utility;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Iterator;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ExcelUtil {
    public static Object[][] readExcelData(String filePath, String sheetName) {
        List<Object[]> dataList = new ArrayList<>();
        try (FileInputStream fis = new FileInputStream(filePath);
             Workbook workbook = new XSSFWorkbook(fis)) {

            Sheet sheet = workbook.getSheet(sheetName);
            Iterator<Row> rows = sheet.iterator();

            // Skip header row
            if (rows.hasNext()) {
                rows.next();
            }

            while (rows.hasNext()) {
                Row row = rows.next();

                double loanAmount = row.getCell(0).getNumericCellValue();
                double interestRate = row.getCell(1).getNumericCellValue();
                String term = row.getCell(2).getStringCellValue();

                Object[] data = new Object[] { loanAmount, interestRate, term };
                dataList.add(data);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return dataList.toArray(new Object[0][]);
    }
}
