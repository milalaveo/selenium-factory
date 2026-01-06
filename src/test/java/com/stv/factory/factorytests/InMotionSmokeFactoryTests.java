package com.stv.factory.factorytests;

import com.stv.factory.factorypages.HomePage;
import com.stv.factory.factorypages.VpsHostingPage;
import com.stv.factory.factorypages.WordPressHostingPage;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Smoke tests for InMotion Hosting website.
 * These tests verify that main user flows are working:
 * - Homepage loads
 * - Navigation to key product pages works
 */
public class InMotionSmokeFactoryTests extends BaseTest {

    /**
     * Smoke test:
     * Open Home page -> accept cookies -> navigate to WordPress Hosting page
     * Verify that correct page is opened by checking H1 header.
     */
    @Test
    void home_to_wordpressHosting_h1_isCorrect() {
        WordPressHostingPage wp = new HomePage(driver)
                .open()
                .acceptCookiesIfPresent()
                .goToWordPressHosting();

        // Case-insensitive check to avoid dependency on UI text styling
        assertTrue(
                wp.getH1Text().equalsIgnoreCase("WordPress Host"),
                "H1 should match 'WordPress Host' (case-insensitive)"
        );
    }

    /**
     * Smoke test:
     * Open Home page -> accept cookies -> navigate to VPS Hosting page
     * Verify that correct page is opened by checking H1 header.
     */
    @Test
    void home_to_vpsHosting_h1_isCorrect() {
        VpsHostingPage vps = new HomePage(driver)
                .open()
                .acceptCookiesIfPresent()
                .goToVpsHosting();

        assertTrue(
                vps.getH1Text().equalsIgnoreCase("VPS Web Hosting Services"),
                "H1 should match 'VPS Web Hosting Services' (case-insensitive)"
        );
    }
}
