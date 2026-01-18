package com.stv.factory.bdd;

import com.stv.factory.factorypages.HomePage;
import com.stv.factory.factorypages.VpsHostingPage;
import com.stv.factory.factorypages.WordPressHostingPage;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.junit.jupiter.api.Assertions;

public class NavigationSteps {

    private final ScenarioContext context;

    public NavigationSteps(ScenarioContext context) {
        this.context = context;
    }

    @Given("I am on the InMotion Hosting home page")
    public void iAmOnHomePage() {
        HomePage homePage = new HomePage(context.getDriver()).open();
        context.setHomePage(homePage);
    }

    @And("I accept cookies if present")
    public void iAcceptCookiesIfPresent() {
        context.getHomePage().acceptCookiesIfPresent();
    }

    @When("I navigate to the WordPress Hosting page")
    public void iNavigateToWordPressHostingPage() {
        WordPressHostingPage page = context.getHomePage().goToWordPressHosting();
        context.setWordPressHostingPage(page);
    }

    @When("I navigate to the VPS Hosting page")
    public void iNavigateToVpsHostingPage() {
        VpsHostingPage page = context.getHomePage().goToVpsHosting();
        context.setVpsHostingPage(page);
    }

    @When("I navigate to the {string} page")
    public void iNavigateToPage(String pageName) {
        if ("WordPress".equalsIgnoreCase(pageName)) {
            WordPressHostingPage page = context.getHomePage().goToWordPressHosting();
            context.setWordPressHostingPage(page);
            context.setCurrentPage("WordPress");
            return;
        }
        if ("VPS".equalsIgnoreCase(pageName)) {
            VpsHostingPage page = context.getHomePage().goToVpsHosting();
            context.setVpsHostingPage(page);
            context.setCurrentPage("VPS");
            return;
        }
        throw new IllegalArgumentException("Unsupported page name: " + pageName);
    }

    @Then("the WordPress Hosting page header should be {string}")
    public void theWordPressHostingPageHeaderShouldBe(String expected) {
        String actual = context.getWordPressHostingPage().getH1Text();
        Assertions.assertTrue(
                actual.equalsIgnoreCase(expected),
                "Expected WordPress H1 to match: " + expected + ", but was: " + actual
        );
    }

    @Then("the VPS Hosting page header should be {string}")
    public void theVpsHostingPageHeaderShouldBe(String expected) {
        String actual = context.getVpsHostingPage().getH1Text();
        Assertions.assertTrue(
                actual.equalsIgnoreCase(expected),
                "Expected VPS H1 to match: " + expected + ", but was: " + actual
        );
    }

    @Then("the page header should be {string}")
    public void thePageHeaderShouldBe(String expected) {
        String pageName = context.getCurrentPage();
        if ("WordPress".equalsIgnoreCase(pageName)) {
            theWordPressHostingPageHeaderShouldBe(expected);
            return;
        }
        if ("VPS".equalsIgnoreCase(pageName)) {
            theVpsHostingPageHeaderShouldBe(expected);
            return;
        }
        throw new IllegalStateException("Current page is not set for header assertion");
    }
}
