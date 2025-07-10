package com.selenium.emi_Utility;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
 
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;
 
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;
 
public class Listener implements ITestListener{
	public static ExtentReports extent;
	public static ExtentTest test;
    public ExtentSparkReporter htmlReporter;
    private static Logger logger=LogManager.getLogger();
    @Override
    public void onStart(ITestContext context) {
    	
        htmlReporter = new ExtentSparkReporter("report/" + timestamp() + "myreport.html");
        htmlReporter.config().setTheme(Theme.STANDARD);
        htmlReporter.config().setDocumentTitle("Automation Report");
        htmlReporter.config().setReportName("Test Report");
 
        extent = new ExtentReports();
        extent.attachReporter(htmlReporter);
        extent.setSystemInfo("Environment", "QA");
        extent.setSystemInfo("User", "Tester");
    }
 
    @Override
    public void onTestStart(ITestResult result) {
    	logger.info("Test started: "+result.getName());
        test = extent.createTest(result.getMethod().getMethodName());
    }
 
    @Override
    public void onTestSuccess(ITestResult result) {
        test.pass("Test Passed");
        logger.info("Test case PASSED is: " + result.getName());
    }
 
    @Override
    public void onTestFailure(ITestResult result) {
        test.fail(result.getThrowable());
        logger.error("Test case FAILED is: " + result.getName());
    }
 
    @Override
    public void onTestSkipped(ITestResult result) {
        test.skip("Test Skipped");
        logger.info("Test case SKIPPED is: " + result.getName());
    }
 
    @Override
    public void onFinish(ITestContext context) {
        extent.flush();
        logger.info("TEST FINISH");
    }
    public static String timestamp() {
        return new SimpleDateFormat("yyyy-MM-dd HH-mm-ss").format(new Date());
    }
}
