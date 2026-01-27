package com.stv.factory.bdd;

import com.stv.factory.factorypages.CookiePreferencesCenter;
import com.stv.factory.factorypages.HomePage;
import com.stv.factory.factorypages.LoginPage;
import com.stv.factory.factorypages.VpsHostingPage;
import com.stv.factory.factorypages.WordPressHostingPage;
import org.openqa.selenium.WebDriver;

public class ScenarioContext {

    private WebDriver driver;
    private HomePage homePage;
    private CookiePreferencesCenter cookiePreferencesCenter;
    private WordPressHostingPage wordPressHostingPage;
    private VpsHostingPage vpsHostingPage;
    private LoginPage loginPage;
    private String currentPage;
    private String loginPageUrl;

    public WebDriver getDriver() {
        return driver;
    }

    public void setDriver(WebDriver driver) {
        this.driver = driver;
    }

    public HomePage getHomePage() {
        return homePage;
    }

    public void setHomePage(HomePage homePage) {
        this.homePage = homePage;
    }

    public CookiePreferencesCenter getCookiePreferencesCenter() {
        return cookiePreferencesCenter;
    }

    public void setCookiePreferencesCenter(CookiePreferencesCenter cookiePreferencesCenter) {
        this.cookiePreferencesCenter = cookiePreferencesCenter;
    }

    public WordPressHostingPage getWordPressHostingPage() {
        return wordPressHostingPage;
    }

    public void setWordPressHostingPage(WordPressHostingPage wordPressHostingPage) {
        this.wordPressHostingPage = wordPressHostingPage;
    }

    public VpsHostingPage getVpsHostingPage() {
        return vpsHostingPage;
    }

    public void setVpsHostingPage(VpsHostingPage vpsHostingPage) {
        this.vpsHostingPage = vpsHostingPage;
    }

    public LoginPage getLoginPage() {
        return loginPage;
    }

    public void setLoginPage(LoginPage loginPage) {
        this.loginPage = loginPage;
    }

    public String getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(String currentPage) {
        this.currentPage = currentPage;
    }

    public String getLoginPageUrl() {
        return loginPageUrl;
    }

    public void setLoginPageUrl(String loginPageUrl) {
        this.loginPageUrl = loginPageUrl;
    }
}
