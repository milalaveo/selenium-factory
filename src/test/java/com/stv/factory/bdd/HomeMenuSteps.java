package com.stv.factory.bdd;

import io.cucumber.java.en.Then;
import org.junit.jupiter.api.Assertions;

public class HomeMenuSteps {

    private final ScenarioContext context;

    public HomeMenuSteps(ScenarioContext context) {
        this.context = context;
    }

    @Then("the main menu cart should be empty")
    public void theMainMenuCartShouldBeEmpty() {
        Assertions.assertTrue(
                context.getHomePage().isCartEmpty(),
                "Expected cart in main menu to be empty"
        );
    }

    @Then("the login tooltip should be {string}")
    public void theLoginTooltipShouldBe(String expected) {
        String actual = context.getHomePage().getLoginTooltipText();
        Assertions.assertTrue(
                actual != null && actual.toLowerCase().contains(expected.toLowerCase()),
                "Expected login tooltip to contain: " + expected + ", but was: " + actual
        );
    }
}
