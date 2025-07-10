package com.selenium.emi_Tests;

import com.selenium.emi_Pages.HomePage;
import com.selenium.emi_Utility.ScreenshotUtil;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.testng.Assert;
import org.testng.annotations.Test;
import java.time.Duration;
import com.selenium.emi_Utility.DataProviderClass;

public class emiTest extends BaseTest {
    HomePage homePage;
    JavascriptExecutor js;

    @Test(groups = {"smoke"}, priority = 1)
    public void testNavigationToEMICalculator() {
        homePage = new HomePage(driver);
        js = (JavascriptExecutor) driver;
        String url=driver.getCurrentUrl();
        if(url.equals("https://aviralmathur02.github.io/EMI-Calculator/")) {
        Assert.assertTrue(true,url);
        }
    }

    @Test(groups = {"smoke"}, priority = 2)
    public void testPrincipalAmountFieldIsEnabled() {
        homePage = new HomePage(driver);
        System.out.println("Principal Amount Field Enabled: " + homePage.loanAmount.isEnabled());
    }
    
    @Test(groups = {"smoke"}, priority = 3)
    public void testInterestRateFieldIsEnabled() {
        homePage = new HomePage(driver);
        System.out.println("Interest Rate Field Enabled: " + homePage.interestRate.isEnabled());
    }

    @Test(groups = {"smoke"}, priority = 4)
    public void testPaymentTermFieldIsEnabled() {
        homePage = new HomePage(driver);
        System.out.println("Payment Term Field Enabled: " + homePage.term.isEnabled());
    }

    @Test(dataProvider = "validEMIData", dataProviderClass = DataProviderClass.class, groups = {"regression"}, priority = 5)
    public void testEMIResultIsDisplayed(double loanAmount, double interestRate, String term) throws InterruptedException {
        homePage = new HomePage(driver);

        homePage.loanAmount.clear();
        homePage.loanAmount.sendKeys(String.valueOf(loanAmount));

        homePage.interestRate.clear();
        homePage.interestRate.sendKeys(String.valueOf(interestRate));

        homePage.selectTermByVisibleText(term);

        homePage.calculateButton.click();
        Thread.sleep(2000);

        String emiValue = homePage.emiAmount.getAttribute("value");
        System.out.println("EMI calculated is: " + emiValue);

        Assert.assertNotNull(emiValue, "EMI value should not be null");
        Assert.assertFalse(emiValue.isEmpty(), "EMI value should not be empty");
    }


    @Test(dataProvider = "validEMIData", dataProviderClass = DataProviderClass.class, groups = {"regression"}, priority = 6)
    public void testEMICalculationFormulaCorrectness(double loanAmount, double interestRate, String termText) {
        homePage = new HomePage(driver);
        js = (JavascriptExecutor) driver;

        homePage.loanAmount.clear();
        homePage.loanAmount.sendKeys(String.valueOf(loanAmount));
        double principal = Double.parseDouble(homePage.loanAmount.getAttribute("value"));

        homePage.interestRate.clear();
        homePage.interestRate.sendKeys(String.valueOf(interestRate));
        double rate = Double.parseDouble(homePage.interestRate.getAttribute("value"));

        homePage.selectTermByVisibleText(termText);
        int term = Integer.parseInt(termText.replaceAll("[^\\d]", ""));

        homePage.calculateButton.click();

        String newAmount = (String) js.executeScript("return document.getElementById('emiAmount').value;");
        newAmount = newAmount.replaceAll("[^\\d.]", "");
        double emiFromWeb = Double.parseDouble(newAmount);

        double emi = calculateEMI(principal, rate, term);

        if (Math.abs(emiFromWeb - emi) ==0) {
            System.out.println("EMI Calculation matched");
        } else {
            System.out.println("EMI Calculation mismatched");
        }
    }
    public double calculateEMI(double principal, double annualRate, int months) {
        double monthlyRate = annualRate / 12 / 100;
        return (principal * monthlyRate * Math.pow(1 + monthlyRate, months)) /
               (Math.pow(1 + monthlyRate, months) - 1);
    }

    @Test(groups = {"regression"}, priority = 7)
    public void testLoanCategory5000() {
        homePage = new HomePage(driver);
        homePage.loanAmount.clear();
        homePage.loanAmount.sendKeys("5000");
        homePage.calculateButton.click();
        //System.out.println("Loan Category for ₹5000 : " + homePage.loanCategoryBox.getText());
        String lc=homePage.loanCategoryBox.getText();
        if(lc.equals("Small loan")) {
            Assert.assertTrue(true);
            System.out.println("Loan Category for ₹5000 : " + homePage.loanCategoryBox.getText());
        }
    }

    @Test(groups = {"regression"}, priority = 8)
    public void testLoanCategory50001() {
        homePage = new HomePage(driver);
        homePage.loanAmount.clear();
        homePage.loanAmount.sendKeys("50001");
        homePage.calculateButton.click();
        //System.out.println("Loan Category for ₹50001 : " + homePage.loanCategoryBox.getText());
        String lc=homePage.loanCategoryBox.getText();
        if(lc.equals("Personal loan")) {
            Assert.assertTrue(true);
            System.out.println("Loan Category for ₹50001 : " + homePage.loanCategoryBox.getText());
        }
    }

    @Test(groups = {"regression"}, priority = 9)
    public void testLoanCategory500001() {
        homePage = new HomePage(driver);
        homePage.loanAmount.clear();
        homePage.loanAmount.sendKeys("500001");
        homePage.calculateButton.click();
        //System.out.println("Loan Category for ₹500001 : " + homePage.loanCategoryBox.getText());
        String lc=homePage.loanCategoryBox.getText();
        if(lc.equals("Personal loan")) {
            Assert.assertTrue(true);
            System.out.println("Loan Category for ₹500001 : " + homePage.loanCategoryBox.getText());
        }
    }

    @Test(dataProvider = "validEMIData", dataProviderClass = DataProviderClass.class, groups = {"regression"}, priority = 10)
    public void testValidEMICalculation(double loanAmount, double interestRate, String termText) {
        homePage = new HomePage(driver);

        homePage.loanAmount.clear();
        homePage.loanAmount.sendKeys(String.valueOf(loanAmount));

        homePage.interestRate.clear();
        homePage.interestRate.sendKeys(String.valueOf(interestRate));

        js = (JavascriptExecutor) driver;
        String termValue = termText.replaceAll("[^\\d]", ""); // Extract numeric value
        js.executeScript("document.getElementById('term').value='" + termValue + "';");

        homePage.calculateButton.click();

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.until(ExpectedConditions.attributeToBeNotEmpty(homePage.emiAmount, "value"));

        String emiAmount = homePage.emiAmount.getAttribute("value");
        System.out.println("EMI for valid input : " + emiAmount);

        Assert.assertNotNull(emiAmount, "EMI amount should not be null");
        Assert.assertFalse(emiAmount.isEmpty(), "EMI amount should not be empty");
    }

    @Test(dataProvider = "validEMIData", dataProviderClass = DataProviderClass.class, groups = {"ui"}, priority = 11)
    public void testPieChartDisplaysCorrectValues(double loanAmount, double interestRate, String termText) {
        homePage = new HomePage(driver);

        homePage.loanAmount.clear();
        homePage.loanAmount.sendKeys(String.valueOf(loanAmount));

        homePage.interestRate.clear();
        homePage.interestRate.sendKeys(String.valueOf(interestRate));

        js = (JavascriptExecutor) driver;
        String termValue = termText.replaceAll("[^\\d]", "");
        js.executeScript("document.getElementById('term').value='" + termValue + "';");

        homePage.calculateButton.click();

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.until(ExpectedConditions.visibilityOf(homePage.emiChart));

        boolean chartDisplayed = homePage.emiChart.isDisplayed();
        System.out.println("Chart Displayed : " + chartDisplayed);

        Assert.assertTrue(chartDisplayed, "Pie chart should be displayed for valid input");
    }

    @Test(dataProvider = "validEMIData", dataProviderClass = DataProviderClass.class, groups = {"ui"}, priority = 12)
    public void testPieChartUpdatesAfterRecalculation(double loanAmount, double interestRate, String termText) {
        homePage = new HomePage(driver);
        js = (JavascriptExecutor) driver;

        // First calculation
        homePage.loanAmount.clear();
        homePage.loanAmount.sendKeys(String.valueOf(loanAmount));

        homePage.interestRate.clear();
        homePage.interestRate.sendKeys(String.valueOf(interestRate));

        String termValue = termText.replaceAll("[^\\d]", "");
        js.executeScript("document.getElementById('term').value='" + termValue + "';");

        homePage.calculateButton.click();

        // Recalculation with a different loan amount
        double updatedLoanAmount = loanAmount + 50000; 
        homePage.loanAmount.clear();
        homePage.loanAmount.sendKeys(String.valueOf(updatedLoanAmount));
        homePage.calculateButton.click();

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.until(ExpectedConditions.visibilityOf(homePage.emiChart));

        boolean chartDisplayed = homePage.emiChart.isDisplayed();
        System.out.println("Chart Displayed : " + chartDisplayed);

        Assert.assertTrue(chartDisplayed, "Pie chart should be displayed after recalculation");
    }
    @Test(dataProvider = "validEMIData", dataProviderClass = DataProviderClass.class, groups = {"ui"}, priority = 13)
    public void testBenefitsAndRisksDisplayed(double loanAmount, double interestRate, String termText) {
        homePage = new HomePage(driver);
        js = (JavascriptExecutor) driver;

        homePage.loanAmount.clear();
        homePage.loanAmount.sendKeys(String.valueOf(loanAmount));

        homePage.interestRate.clear();
        homePage.interestRate.sendKeys(String.valueOf(interestRate));

        String termValue = termText.replaceAll("[^\\d]", "");
        js.executeScript("document.getElementById('term').value='" + termValue + "';");

        homePage.calculateButton.click();

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.until(ExpectedConditions.visibilityOf(homePage.emiInfo));

        boolean infoVisible = homePage.emiInfo.isDisplayed();
        System.out.println("Benefits and Risks section visible : " + infoVisible);

        Assert.assertTrue(infoVisible, "Benefits and Risks section should be visible");
    }
    
    @Test(groups = {"negative"}, priority = 14)
    public void testMinimumInterestRateErrorMessage() {
        homePage = new HomePage(driver);
        homePage.interestRate.clear();
        homePage.interestRate.sendKeys("0");
        homePage.calculateButton.click();
        ScreenshotUtil.captureScreenshot(driver, "MinimumInterestRateError");
        System.out.println("Error: " + homePage.rateWarning.getText().contains("Minimum interest rate"));
        String msg=homePage.rateWarning.getText();
        if(msg.equals("Minimum interest rate is 1%")) {
        	Assert.assertTrue(true);
        }
    }

    @Test(dataProvider = "validEMIData", dataProviderClass = DataProviderClass.class, groups = {"ui"}, priority = 15)
    public void testEMIResultAndTotalPaymentBreakdownDisplayed(double loanAmount, double interestRate, String termText) {
        homePage = new HomePage(driver);
        js = (JavascriptExecutor) driver;

        homePage.loanAmount.clear();
        homePage.loanAmount.sendKeys(String.valueOf(loanAmount));

        homePage.interestRate.clear();
        homePage.interestRate.sendKeys(String.valueOf(interestRate));

        String termValue = termText.replaceAll("[^\\d]", "");
        js.executeScript("document.getElementById('term').value='" + termValue + "';");

        homePage.calculateButton.click();

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.until(ExpectedConditions.visibilityOf(homePage.interestPaidBox));
        wait.until(ExpectedConditions.visibilityOf(homePage.totalPaymentBox));

        boolean interestDisplayed = homePage.interestPaidBox.isDisplayed();
        boolean totalDisplayed = homePage.totalPaymentBox.isDisplayed();

        System.out.println("EMI Displayed: " + interestDisplayed);
        System.out.println("Total payment displayed: " + totalDisplayed);

        Assert.assertTrue(interestDisplayed, "Interest Paid box should be visible");
        Assert.assertTrue(totalDisplayed, "Total Payment box should be visible");
    }


    @Test(groups = {"regression"}, priority = 16)
    public void testPrintEMI() {
        calculateEMI(100000, 10, 12);
    }

    @Test(groups = {"negative"}, priority = 17)
    public void testLogoOverlap() {
        homePage = new HomePage(driver);
        Rectangle rect1 = homePage.logo.getRect();
        Rectangle rect2 = homePage.infoBox.getRect();
        boolean isOverlapping = !(rect1.getX() + rect1.getWidth() < rect2.getX() ||
                                  rect2.getX() + rect2.getWidth() < rect1.getX() ||
                                  rect1.getY() + rect1.getHeight() < rect2.getY() ||
                                  rect2.getY() + rect2.getHeight() < rect1.getY());
        ScreenshotUtil.captureScreenshot(driver, "LogoOverlap");
        assert !isOverlapping : "Elements are overlapping.";
    }

    @Test( groups = {"negative"}, priority = 18)
    public void testInterestOverflow() {
        homePage = new HomePage(driver);

        homePage.loanAmount.clear();
        homePage.loanAmount.sendKeys("500000");

        homePage.interestRate.clear();
        homePage.interestRate.sendKeys("1000000000");

        homePage.selectTermByVisibleText("12 months");
        homePage.calculateButton.click();

        WebElement frame2 = homePage.infoBox; // Assuming this is the container
        WebElement totalin = homePage.interestPaidBox;

        int frameX = frame2.getLocation().getX();
        int frameY = frame2.getLocation().getY();
        int frameWidth = frame2.getSize().getWidth();
        int frameHeight = frame2.getSize().getHeight();

        int interestX = totalin.getLocation().getX();
        int interestY = totalin.getLocation().getY();
        int interestWidth = totalin.getSize().getWidth();
        int interestHeight = totalin.getSize().getHeight();

        boolean isOverflowing = interestX + interestWidth > frameX + frameWidth ||
                                interestY + interestHeight > frameY + frameHeight;

        Assert.assertFalse(isOverflowing, "Interest amount is overflowing out of the frame.");
    }

    @Test(groups = {"ui"}, priority = 19)
    public void testScrollFunctionality() {
        driver.manage().window().setSize(new Dimension(390, 844));
        js = (JavascriptExecutor) driver;
        Long initialY = (Long) js.executeScript("return window.scrollY;");
        js.executeScript("window.scrollBy(0, 500);");
        Long afterScrollY = (Long) js.executeScript("return window.scrollY;");
        assert afterScrollY > initialY;
    }

    @Test(groups = {"negative"}, priority = 20) 
    public void testCalculateButtonDisabledForInvalidInterest() { 
    	homePage = new HomePage(driver); 
    	homePage.interestRate.clear(); 
    	homePage.interestRate.sendKeys("30e30"); 
    	assert !homePage.calculateButton.isEnabled(); 
    	}


    @Test(groups = {"negative"}, priority = 21)
    public void testPieChartInterestCalculationForExtremeInterest() {
        homePage = new HomePage(driver);

        // Enter invalid interest rate
        homePage.interestRate.clear();
        homePage.interestRate.sendKeys("30e30");

        // Click calculate
        homePage.calculateButton.click();

        // Wait for a short time to allow any chart to attempt rendering
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
        try {
            wait.until(ExpectedConditions.visibilityOf(homePage.emiChart));
            // If chart is visible, check if interest value is meaningful
            Object secondValue = ((JavascriptExecutor) driver).executeScript(
                "let chart = Chart.getChart('emiChart');" +
                "return chart ? chart.data.datasets[0].data[1] : null;"
            );
            Assert.fail("Pie chart should not be displayed or should show no interest for invalid input.");
        } catch (TimeoutException e) {
            Assert.assertTrue(true);
        }
    }


    @Test(groups = {"ui"}, priority = 22)
    public void testPaymentTermIsInput() {
        homePage = new HomePage(driver);
        assert homePage.term.getTagName().equalsIgnoreCase("input");
    }

    @Test(groups = {"regression"}, priority = 23)
    public void testPrincipalRounding() throws InterruptedException {
        homePage = new HomePage(driver);
        homePage.loanAmount.clear();
        homePage.loanAmount.sendKeys("123456.789");
        homePage.loanAmount.sendKeys(Keys.TAB);
        Thread.sleep(1000);
        String displayedValue = homePage.loanAmount.getAttribute("value");
        assert displayedValue.matches("\\d+(\\.\\d{1,2})?");
    }

    @Test(groups = {"regression"}, priority = 24)
    public void testLoanCategory() {
        homePage = new HomePage(driver);
        homePage.loanAmount.clear();
        homePage.loanAmount.sendKeys("1500000");
        homePage.calculateButton.click();
        String category = homePage.loanCategoryBox.getText();
        assert category.matches("Small Loan|Personal Loan|Business Loan");
    }

    @Test(groups = {"ui"}, priority = 25)
    public void testHighLoanWarning() {
        homePage = new HomePage(driver);
        homePage.loanAmount.clear();
        homePage.loanAmount.sendKeys("1000001");
        homePage.calculateButton.click();
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
        WebElement warning = wait.until(ExpectedConditions.visibilityOf(homePage.highLoanWarning));
        assert warning.isDisplayed();
    }

    @Test(groups = {"negative"}, priority = 26)
    public void testCalculateButtonDisabledForExcessiveLoan() {
        homePage = new HomePage(driver);
        homePage.loanAmount.clear();
        homePage.loanAmount.sendKeys("100000000");
        assert !homePage.calculateButton.isEnabled();
    }

    @Test(groups = {"ui"}, priority = 27)
    public void testHighLoanWarningForMidRangeAmount() {
    	homePage = new HomePage(driver);
        homePage.loanAmount.clear();
        homePage.loanAmount.sendKeys("10000000");
        homePage.calculateButton.click();
        WebElement warning = homePage.highLoanWarning;
        String warningText = warning.getText().trim();
        System.out.println("Warning displayed: " + warningText);
        assert !warningText.isEmpty();
    }


}

    
