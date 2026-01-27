package com.stv.factory.bdd;

import com.stv.factory.factorypages.CookiePreferencesCenter;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.junit.jupiter.api.Assertions;

public class CookieConsentSteps {

    private final ScenarioContext context;

    public CookieConsentSteps(ScenarioContext context) {
        this.context = context;
    }

    @When("I open the cookie preferences center")
    public void iOpenTheCookiePreferencesCenter() {
        CookiePreferencesCenter center = context.getHomePage().openCookiePreferencesCenter();
        context.setCookiePreferencesCenter(center);
    }

    @Then("the {string} category tab should show content")
    public void theCategoryTabShouldShowContent(String categoryName) {
        String content = context.getCookiePreferencesCenter().getCategoryDescriptionText(categoryName);
        Assertions.assertFalse(
                content.isBlank(),
                "Expected cookie category content for: " + categoryName
        );
    }

    @Then("the {string} category content should be present but not visible in the modal")
    public void theCategoryContentShouldBePresentButNotVisibleInTheModal(String categoryName) {
        boolean presentButNotVisible = context.getCookiePreferencesCenter().isCategoryContentPresentButNotVisible(categoryName);
        Assertions.assertTrue(
                presentButNotVisible,
                "Expected cookie category content to be present but outside the visible modal area for: " + categoryName
        );
    }
}
