package com.selenium.emi_Utility;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.apache.commons.io.FileUtils;

public class ScreenshotUtil {
    public static void captureScreenshot(WebDriver driver, String name) {
        File src = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
        String timestamp = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
        File dest = new File("screenshots/" + name + "_" + timestamp + ".png");
        try {
            FileUtils.copyFile(src, dest);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

