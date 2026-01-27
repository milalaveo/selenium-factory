package com.stv.factory.bdd;

import com.stv.factory.factorypages.HomePage;
import com.stv.factory.factorypages.LoginPage;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.junit.jupiter.api.Assertions;

import java.net.URI;

public class LoginSteps {

    private final ScenarioContext context;

    public LoginSteps(ScenarioContext context) {
        this.context = context;
    }

    @When("I open the login page from the header")
    public void iOpenTheLoginPageFromTheHeader() {
        LoginPage loginPage = context.getHomePage().openLoginPageFromHeader();
        context.setLoginPage(loginPage);
        context.setLoginPageUrl(stripQuery());
    }

    @Then("the login page should be loaded")
    public void theLoginPageShouldBeLoaded() {
        Assertions.assertTrue(
                context.getLoginPage().isLoaded(),
                "Expected login page to be loaded"
        );
    }

    @Then("the email address field should be visible with placeholder {string}")
    public void theEmailAddressFieldShouldBeVisibleWithPlaceholder(String expected) {
        String actual = context.getLoginPage().getEmailPlaceholder();
        Assertions.assertTrue(
                actual != null && actual.equalsIgnoreCase(expected),
                "Expected email placeholder to be: " + expected + ", but was: " + actual
        );
    }

    @When("I enter {string} into the email address field")
    public void iEnterIntoTheEmailAddressField(String value) {
        context.getLoginPage().enterEmail(value);
    }

    @When("I scroll down and click the login button")
    public void iScrollDownAndClickTheLoginButton() {
        context.getLoginPage().scrollToLoginAndSubmit();
    }

    @Then("the email address field should contain {string}")
    public void theEmailAddressFieldShouldContain(String expected) {
        String actual = context.getLoginPage().getEmailValue();
        Assertions.assertEquals(
                expected,
                actual,
                "Expected email field value to remain unchanged"
        );
    }

    @Then("I should remain on the login page")
    public void iShouldRemainOnTheLoginPage() {
        String expected = context.getLoginPageUrl();
        String current = stripQuery();
        Assertions.assertEquals(
                expected,
                current,
                "Expected to remain on the same login page"
        );
    }

    @When("I click the site logo to return home")
    public void iClickTheSiteLogoToReturnHome() {
        HomePage homePage = context.getLoginPage().clickLogoToReturnHome();
        context.setHomePage(homePage);
    }

    private String stripQuery() {
        try {
            URI uri = URI.create(context.getDriver().getCurrentUrl());
            String base = uri.getScheme() + "://" + uri.getAuthority() + uri.getPath();
            return base.endsWith("/") ? base.substring(0, base.length() - 1) : base;
        } catch (Exception ignored) {
            return context.getDriver().getCurrentUrl();
        }
    }
}
