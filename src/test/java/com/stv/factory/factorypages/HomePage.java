package com.stv.factory.factorypages;

import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.FindBys;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.time.Duration;
import java.util.List;

/**
 * Page Object for InMotion Hosting Home page.
 * Implements Page Object + Page Factory pattern.
 */
public class HomePage extends BasePage {

    private static final String URL = "https://www.inmotionhosting.com/";

    /**
     * CTA links appear multiple times on the page
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

    @FindBy(id = "onetrust-pc-btn-handler")
    private WebElement cookieSettingsButton;

    public HomePage(WebDriver driver) {
        super(driver);
        PageFactory.initElements(driver, this);
    }

    /**
     * Opens the Home page
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
                wait.until(ExpectedConditions.invisibilityOf(cookiesBanner));
            }
        } catch (Exception ignored) {
        }
        return this;
    }

    /**
     * Opens the Login page via the header/login link.
     */
    public LoginPage openLoginPageFromHeader() {
        WebElement login = findLoginTrigger();
        String currentWindow = driver.getWindowHandle();
        int windowCount = driver.getWindowHandles().size();
        login.click();
        wait.until(d -> d.getWindowHandles().size() >= windowCount);
        if (driver.getWindowHandles().size() > windowCount) {
            for (String handle : driver.getWindowHandles()) {
                if (!handle.equals(currentWindow)) {
                    driver.switchTo().window(handle);
                    break;
                }
            }
        }
        return new LoginPage(driver);
    }

    /**
     * Reads the tooltip text associated with the Login control.
     */
    public String getLoginTooltipText() {
        WebElement login = findLoginTrigger();
        String tooltip = readTooltipAttribute(login);
        if (!tooltip.isBlank()) {
            return tooltip;
        }

        new Actions(driver).moveToElement(login).perform();
        return readVisibleTooltip();
    }

    /**
     * Returns true if the cart count in the main menu indicates empty (0).
     */
    public boolean isCartEmpty() {
        String cartText = resolveCartText();
        if (cartText == null || cartText.isBlank()) {
            throw new NoSuchElementException("Cart text not found in the main menu");
        }
        String digits = cartText.replaceAll("\\D+", "");
        if (!digits.isEmpty()) {
            return Integer.parseInt(digits) == 0;
        }
        return cartText.toLowerCase().contains("empty");
    }

    /**
     * Opens the OneTrust Privacy Preference Center from the cookie banner.
     */
    public CookiePreferencesCenter openCookiePreferencesCenter() {
        wait.until(ExpectedConditions.elementToBeClickable(cookieSettingsButton)).click();
        return new CookiePreferencesCenter(driver);
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

    private WebElement findLoginTrigger() {
        List<WebElement> candidates = driver.findElements(org.openqa.selenium.By.xpath(
                "//header//*[self::a or self::button][" +
                        "normalize-space()='Login' or " +
                        "contains(@aria-label,'Login') or " +
                        "contains(@title,'Login') or " +
                        "contains(@href,'login')" +
                        "]"
        ));
        for (WebElement el : candidates) {
            if (el.isDisplayed() && el.isEnabled()) {
                return el;
            }
        }
        candidates = driver.findElements(org.openqa.selenium.By.xpath(
                "//*[self::a or self::button][" +
                        "normalize-space()='Login' or " +
                        "contains(@aria-label,'Login') or " +
                        "contains(@title,'Login') or " +
                        "contains(@href,'login')" +
                        "]"
        ));
        for (WebElement el : candidates) {
            if (el.isDisplayed() && el.isEnabled()) {
                return el;
            }
        }
        throw new NoSuchElementException("Login control not found in header");
    }

    private String readTooltipAttribute(WebElement element) {
        String[] attrs = {"title", "data-tooltip", "data-title", "data-original-title", "aria-label"};
        for (String attr : attrs) {
            String value = element.getAttribute(attr);
            if (value != null && !value.isBlank()) {
                return value.trim();
            }
        }
        return "";
    }

    private String readVisibleTooltip() {
        List<WebElement> tooltips = driver.findElements(org.openqa.selenium.By.cssSelector(
                "[role='tooltip'], .tooltip, .tippy-content, .ui-tooltip"
        ));
        for (WebElement tooltip : tooltips) {
            if (tooltip.isDisplayed()) {
                String text = tooltip.getText().trim();
                if (!text.isEmpty()) {
                    return text;
                }
            }
        }
        return "";
    }

    private String resolveCartText() {
        List<org.openqa.selenium.By> selectors = List.of(
                org.openqa.selenium.By.cssSelector(".cart-contents-count, .cart-count, .mini-cart__count, .cart-number, .cart-count-number"),
                org.openqa.selenium.By.cssSelector("header a[href*='cart'], header a[aria-label*='Cart' i]"),
                org.openqa.selenium.By.cssSelector("nav a[href*='cart'], nav a[aria-label*='Cart' i]")
        );
        for (org.openqa.selenium.By selector : selectors) {
            List<WebElement> elements = driver.findElements(selector);
            for (WebElement el : elements) {
                if (!el.isDisplayed()) {
                    continue;
                }
                String text = el.getText().trim();
                if (!text.isEmpty()) {
                    return text;
                }
                String aria = el.getAttribute("aria-label");
                if (aria != null && !aria.isBlank()) {
                    return aria.trim();
                }
            }
        }
        return null;
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
                if (el.isDisplayed() && el.isEnabled()) {
                    wait.until(ExpectedConditions.elementToBeClickable(el));
                    return el;
                }
            } catch (Exception ignored) {
            }
        }

        try {
            return new org.openqa.selenium.support.ui.WebDriverWait(driver, Duration.ofSeconds(2))
                    .until(d -> {
                        for (WebElement el : elements) {
                            if (el.isDisplayed() && el.isEnabled()) {
                                return el;
                            }
                        }
                        return null;
                    });
        } catch (Exception ignored) {
        }

        return elements.get(0);
    }
}
