package com.stv.factory.bdd;

import io.cucumber.java.After;
import io.cucumber.java.Before;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.time.Duration;

public class BddHooks {

    private final ScenarioContext context;

    public BddHooks(ScenarioContext context) {
        this.context = context;
    }

    @Before
    public void setUp() {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--start-maximized");
        options.setPageLoadStrategy(org.openqa.selenium.PageLoadStrategy.EAGER);

        WebDriver driver = new ChromeDriver(options);
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(0));
        driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(30));

        context.setDriver(driver);
    }

    @After
    public void tearDown() {
        WebDriver driver = context.getDriver();
        if (driver != null) {
            driver.quit();
        }
    }
}
