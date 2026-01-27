package com.stv.factory.bdd;

import io.cucumber.java.en.Then;
import org.junit.jupiter.api.Assertions;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class PageStateSteps {

    private final ScenarioContext context;

    public PageStateSteps(ScenarioContext context) {
        this.context = context;
    }

    @Then("the page should be loaded")
    public void thePageShouldBeLoaded() {
        WebDriver driver = context.getDriver();
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        String readyState = wait.until(d ->
                (String) ((JavascriptExecutor) d).executeScript("return document.readyState")
        );
        Assertions.assertTrue(
                "complete".equals(readyState) || "interactive".equals(readyState),
                "Expected document.readyState to be interactive or complete, but was: " + readyState
        );
    }
}
