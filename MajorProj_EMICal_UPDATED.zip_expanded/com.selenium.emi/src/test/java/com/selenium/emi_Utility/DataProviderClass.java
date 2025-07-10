package com.selenium.emi_Utility;

import org.testng.annotations.DataProvider;

public class DataProviderClass {

    @DataProvider(name = "validEMIData")
    public static Object[][] getValidEMIData() {
        String filePath = "src/test/java/com/selenium/emi_Utility/testdataEMI.xlsx";
        return ExcelUtil.readExcelData(filePath, "Sheet1");
    }

//    @DataProvider(name = "invalidEMIData")
//    public static Object[][] getInvalidEMIData() {
//        String filePath = "src/test/java/com/selenium/emi_Utility/testdataEMI.xlsx";
//        return ExcelUtil.readExcelData(filePath, "Sheet1", "invalid");
//    }
}
