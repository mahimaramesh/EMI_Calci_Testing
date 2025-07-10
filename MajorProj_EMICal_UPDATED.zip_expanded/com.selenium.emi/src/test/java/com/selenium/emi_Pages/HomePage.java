package com.selenium.emi_Pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.Select;

public class HomePage {
    WebDriver driver;

    @FindBy(id = "loanAmount")
    public WebElement loanAmount;

    @FindBy(id = "interestRate")
    public WebElement interestRate;

    @FindBy(id = "term")
    public WebElement term;

    @FindBy(id = "calculateButton")
    public WebElement calculateButton;

    @FindBy(id = "emiAmount")
    public WebElement emiAmount;

    @FindBy(id = "loanCategoryBox")
    public WebElement loanCategoryBox;

    @FindBy(id = "emiChart")
    public WebElement emiChart;    

    @FindBy(id = "emiInfo")
    public WebElement emiInfo;

    @FindBy(id = "rateWarning")
    public WebElement rateWarning;

    @FindBy(id = "interestPaidBox")
    public WebElement interestPaidBox;

    @FindBy(id = "totalPaymentBox")
    public WebElement totalPaymentBox;

    @FindBy(id = "highLoanWarning")
    public WebElement highLoanWarning;

    @FindBy(className = "logo")
    public WebElement logo;

    @FindBy(className = "info-box")
    public WebElement infoBox;

    public HomePage(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    public void selectTermByVisibleText(String text) {
        new Select(term).selectByVisibleText(text);
    }
}