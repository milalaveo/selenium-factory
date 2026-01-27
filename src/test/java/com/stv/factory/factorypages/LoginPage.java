package com.stv.factory.factorypages;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.util.List;

public class LoginPage extends BasePage {

    private static final By EMAIL_INPUT = By.cssSelector("input#username");
    private static final By LOGIN_BUTTON = By.xpath(
            "//button[(@type='submit' or contains(@class,'login') or contains(@class,'submit')) and normalize-space()] | " +
                    "//input[@type='submit' and (contains(@value,'Login') or contains(@value,'Sign in') or contains(@value,'Sign In'))]"
    );
    private static final By LOGO_LINK = By.cssSelector(
            "a[rel='home'], a[href='https://www.inmotionhosting.com/'], a[href='https://www.inmotionhosting.com'], " +
                    "header a[class*='logo' i], header a[aria-label*='InMotion' i]"
    );

    public LoginPage(WebDriver driver) {
        super(driver);
        wait.until(ExpectedConditions.presenceOfElementLocated(EMAIL_INPUT));
    }

    public boolean isLoaded() {
        try {
            wait.until(ExpectedConditions.presenceOfElementLocated(EMAIL_INPUT));
            return true;
        } catch (Exception ignored) {
        }
        String url = driver.getCurrentUrl().toLowerCase();
        if (url.contains("login")) {
            return true;
        }
        String title = driver.getTitle();
        return title != null && title.toLowerCase().contains("login");
    }

    public String getEmailPlaceholder() {
        return getEmailInput().getAttribute("placeholder");
    }

    public void enterEmail(String value) {
        WebElement input = getEmailInput();
        input.clear();
        input.sendKeys(value);
    }

    public String getEmailValue() {
        return getEmailInput().getAttribute("value");
    }

    public void scrollToLoginAndSubmit() {
        WebElement button = getLoginButton();
        ((JavascriptExecutor) driver).executeScript(
                "arguments[0].scrollIntoView({block: 'center', inline: 'nearest'});",
                button
        );
        wait.until(ExpectedConditions.elementToBeClickable(button)).click();
    }

    public HomePage clickLogoToReturnHome() {
        WebElement logo = getLogoLink();
        logo.click();
        return new HomePage(driver);
    }

    private WebElement getEmailInput() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(EMAIL_INPUT));
    }

    private WebElement getLoginButton() {
        List<WebElement> candidates = driver.findElements(LOGIN_BUTTON);
        for (WebElement el : candidates) {
            if (el.isDisplayed() && el.isEnabled()) {
                return el;
            }
        }
        return wait.until(ExpectedConditions.elementToBeClickable(LOGIN_BUTTON));
    }

    private WebElement getLogoLink() {
        List<WebElement> candidates = driver.findElements(LOGO_LINK);
        for (WebElement el : candidates) {
            if (el.isDisplayed() && el.isEnabled()) {
                return el;
            }
        }
        return wait.until(ExpectedConditions.elementToBeClickable(LOGO_LINK));
    }
}
