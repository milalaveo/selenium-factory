Feature: Home page login validation

  Scenario: Cart stays empty after invalid login attempt
    Given I am on the InMotion Hosting home page
    And I accept cookies if present
    Then the main menu cart should be empty
    And the login tooltip should be "Login"
    When I open the login page from the header
    Then the page should be loaded
    And the login page should be loaded
    And the email address field should be visible with placeholder "Email Address"
    When I enter "invalid-email" into the email address field
    And I scroll down and click the login button
    Then the email address field should contain "invalid-email"
    And I should remain on the login page
    When I click the site logo to return home
    Then the main menu cart should be empty
