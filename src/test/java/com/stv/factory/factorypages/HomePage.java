package com.stv.factory.factorypages;

import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.FindBys;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

/**
 * Page Object for InMotion Hosting Home page.
 * Implements Page Object + Page Factory pattern.
 */
public class HomePage {

    private static final String URL = "https://www.inmotionhosting.com/";

    private final WebDriver driver;
    private final WebDriverWait wait;

    /**
     * CTA links appear multiple times on the page,
     * therefore we store them as a list and click the first available one.
     */
    @FindBys({
            @FindBy(xpath = "//a[normalize-space()='Explore WordPress Plans']")
    })
    private List<WebElement> exploreWordPressPlansLinks;

    @FindBys({
            @FindBy(xpath = "//a[normalize-space()='View VPS Options']")
    })
    private List<WebElement> viewVpsOptionsLinks;

    /**
     * Cookie consent elements (OneTrust).
     * They may block clicks if not handled.
     */
    @FindBy(id = "onetrust-accept-btn-handler")
    private WebElement acceptCookiesButton;

    @FindBy(id = "onetrust-group-container")
    private WebElement cookiesBanner;

    public HomePage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        PageFactory.initElements(driver, this);
    }

    /**
     * Opens the Home page.
     */
    public HomePage open() {
        driver.get(URL);
        return this;
    }

    /**
     * Accepts cookies if the consent banner is displayed.
     * This prevents ElementClickInterceptedException.
     */
    public HomePage acceptCookiesIfPresent() {
        try {
            wait.until(ExpectedConditions.visibilityOf(acceptCookiesButton));
            if (acceptCookiesButton.isDisplayed()) {
                acceptCookiesButton.click();
                // wait until banner disappears so it does not block clicks
                wait.until(ExpectedConditions.invisibilityOf(cookiesBanner));
            }
        } catch (Exception ignored) {
            // Cookie banner may not appear – this is fine
        }
        return this;
    }

    /**
     * Navigates to WordPress Hosting page via CTA button.
     * Returns new Page Object instance.
     */
    public WordPressHostingPage goToWordPressHosting() {
        firstClickable(exploreWordPressPlansLinks).click();
        return new WordPressHostingPage(driver);
    }

    /**
     * Navigates to VPS Hosting page via CTA button.
     * Returns new Page Object instance.
     */
    public VpsHostingPage goToVpsHosting() {
        firstClickable(viewVpsOptionsLinks).click();
        return new VpsHostingPage(driver);
    }

    /**
     * Utility method:
     * Finds the first clickable element from the list.
     */
    private WebElement firstClickable(List<WebElement> elements) {
        if (elements == null || elements.isEmpty()) {
            throw new NoSuchElementException("Expected elements not found on HomePage");
        }

        for (WebElement el : elements) {
            try {
                wait.until(ExpectedConditions.elementToBeClickable(el));
                if (el.isDisplayed() && el.isEnabled()) {
                    return el;
                }
            } catch (Exception ignored) {
            }
        }
        return elements.get(0);
    }
}
