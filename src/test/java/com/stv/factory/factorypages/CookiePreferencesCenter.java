package com.stv.factory.factorypages;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;
import java.util.Map;

public class CookiePreferencesCenter {

    private static final By MODAL = By.id("onetrust-pc-sdk");
    private static final By FALLBACK_CONTENT = By.cssSelector("#onetrust-pc-sdk .ot-tab-content, #onetrust-pc-sdk #ot-pc-content");
    private static final By CONTENT_CONTAINER = By.cssSelector("#ot-pc-content > div.ot-sdk-container.ot-grps-cntr.ot-sdk-column");
    private static final By ACTIVE_DESCRIPTION = By.cssSelector("#onetrust-pc-sdk #ot-pc-desc, " +
            "#onetrust-pc-sdk .ot-desc-cntr:not(.ot-hide) .ot-grp-desc");
    private static final By FUNCTIONAL_TAB = By.cssSelector("#ot-pc-content > div.ot-sdk-container.ot-grps-cntr.ot-sdk-column " +
            "> div.ot-sdk-four.ot-sdk-columns.ot-tab-list > ul > li:nth-child(3)");

    private final WebDriver driver;
    private final WebDriverWait wait;

    public CookiePreferencesCenter(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.until(ExpectedConditions.visibilityOfElementLocated(MODAL));
    }

    public String getCategoryDescriptionText(String categoryName) {
        WebElement tab = findCategoryTab(categoryName);
        tab.click();

        String panelId = tab.getAttribute("aria-controls");
        if (panelId != null && !panelId.isBlank()) {
            return waitForNonEmptyText(By.id(panelId));
        }

        return waitForNonEmptyText(FALLBACK_CONTENT);
    }

    public boolean isCategoryContentPresentButNotVisible(String categoryName) {
        WebElement tab = findCategoryTab(categoryName);
        tab.click();

        WebElement description = waitForDescriptionElement(tab);
        String text = readText(description);
        if (text.isEmpty()) {
            return false;
        }

        WebElement container = resolveScrollContainer(description);
        return isNotVisibleToUser(description, container);
    }

    private WebElement findCategoryTab(String categoryName) {
        if ("Functional Cookies".equalsIgnoreCase(categoryName)) {
            WebElement tabItem = wait.until(ExpectedConditions.presenceOfElementLocated(FUNCTIONAL_TAB));
            List<WebElement> clickTargets = tabItem.findElements(By.cssSelector("button, a, [role='tab']"));
            if (!clickTargets.isEmpty()) {
                return wait.until(ExpectedConditions.elementToBeClickable(clickTargets.get(0)));
            }
            return wait.until(ExpectedConditions.elementToBeClickable(tabItem));
        }
        String tabXpath = String.format(
                "//*[self::button or self::a or self::div][normalize-space()='%s' and " +
                        "(@role='tab' or @aria-controls or contains(@class,'ot-cat-item') or " +
                        "contains(@class,'ot-pc-tab') or contains(@class,'category-menu-switch-handler'))]",
                categoryName
        );
        By by = By.xpath("//div[@id='onetrust-pc-sdk']" + tabXpath);
        return wait.until(ExpectedConditions.elementToBeClickable(by));
    }

    private WebElement waitForDescriptionElement(WebElement tab) {
        String panelId = tab.getAttribute("aria-controls");
        if (panelId != null && !panelId.isBlank()) {
            By panelBy = By.id(panelId);
            wait.until(ExpectedConditions.presenceOfElementLocated(panelBy));
            return wait.until(driver -> {
                WebElement panel = driver.findElement(panelBy);
                List<WebElement> candidates = panel.findElements(By.cssSelector(".ot-grp-desc, #ot-pc-desc, p"));
                for (WebElement el : candidates) {
                    if (!readText(el).isEmpty()) {
                        return el;
                    }
                }
                return null;
            });
        }

        return wait.until(driver -> {
            List<WebElement> elements = driver.findElements(ACTIVE_DESCRIPTION);
            for (WebElement el : elements) {
                if (!readText(el).isEmpty()) {
                    return el;
                }
            }
            return null;
        });
    }

    private String readText(WebElement element) {
        String textContent = element.getAttribute("textContent");
        if (textContent != null && !textContent.trim().isEmpty()) {
            return textContent.trim();
        }
        return element.getText().trim();
    }

    private WebElement resolveScrollContainer(WebElement description) {
        WebElement container = wait.until(ExpectedConditions.visibilityOfElementLocated(CONTENT_CONTAINER));
        WebElement scrollable = (WebElement) ((JavascriptExecutor) driver).executeScript(
                "let el = arguments[0];" +
                        "const root = arguments[1];" +
                        "while (el && el !== root) {" +
                        "  const style = window.getComputedStyle(el);" +
                        "  const overflowY = style.overflowY;" +
                        "  const overflowX = style.overflowX;" +
                        "  const canScrollY = (overflowY === 'auto' || overflowY === 'scroll') && el.scrollHeight > el.clientHeight;" +
                        "  const canScrollX = (overflowX === 'auto' || overflowX === 'scroll') && el.scrollWidth > el.clientWidth;" +
                        "  if (canScrollY || canScrollX) return el;" +
                        "  el = el.parentElement;" +
                        "}" +
                        "return null;",
                description,
                container
        );
        return scrollable != null ? scrollable : container;
    }

    private boolean isNotVisibleToUser(WebElement element, WebElement container) {
        Map<String, Number> rects = (Map<String, Number>) ((JavascriptExecutor) driver).executeScript(
                "const el = arguments[0], c = arguments[1];" +
                        "const er = el.getBoundingClientRect();" +
                        "const cr = c.getBoundingClientRect();" +
                        "return {" +
                        "elTop: er.top, elBottom: er.bottom, elLeft: er.left, elRight: er.right," +
                        "cTop: cr.top, cBottom: cr.bottom, cLeft: cr.left, cRight: cr.right};",
                element,
                container
        );

        double elTop = rects.get("elTop").doubleValue();
        double elBottom = rects.get("elBottom").doubleValue();
        double elLeft = rects.get("elLeft").doubleValue();
        double elRight = rects.get("elRight").doubleValue();
        double cTop = rects.get("cTop").doubleValue();
        double cBottom = rects.get("cBottom").doubleValue();
        double cLeft = rects.get("cLeft").doubleValue();
        double cRight = rects.get("cRight").doubleValue();

        boolean fullyInside = elTop >= cTop && elBottom <= cBottom && elLeft >= cLeft && elRight <= cRight;
        if (!fullyInside) {
            return true;
        }

        double centerX = elLeft + ((elRight - elLeft) / 2);
        double centerY = elTop + ((elBottom - elTop) / 2);
        return isOccludedAtPoint(element, centerX, centerY);
    }

    private boolean isOccludedAtPoint(WebElement element, double x, double y) {
        Object hit = ((JavascriptExecutor) driver).executeScript(
                "const el = arguments[0];" +
                        "const x = arguments[1];" +
                        "const y = arguments[2];" +
                        "const hit = document.elementFromPoint(x, y);" +
                        "if (!hit) return true;" +
                        "return !el.contains(hit);",
                element,
                x,
                y
        );
        return Boolean.TRUE.equals(hit);
    }

    private String waitForNonEmptyText(By by) {
        return wait.until(driver -> {
            List<WebElement> elements = driver.findElements(by);
            for (WebElement el : elements) {
                String text = readText(el);
                if (!text.isEmpty()) {
                    return text;
                }
            }
            return null;
        });
    }
}
