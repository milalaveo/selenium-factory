package com.stv.factory.factorypages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;

public class VpsHostingPage extends BasePage {

    @FindBy(css = "h1")
    private WebElement h1;

    public VpsHostingPage(WebDriver driver) {
        super(driver);
        PageFactory.initElements(driver, this);
        wait.until(ExpectedConditions.visibilityOf(h1));
    }

    public String getH1Text() {
        return h1.getText().trim();
    }
}
