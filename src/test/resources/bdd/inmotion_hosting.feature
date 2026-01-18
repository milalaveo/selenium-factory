Feature: InMotion Hosting navigation

  Scenario: Home to WordPress Hosting header is correct
    Given I am on the InMotion Hosting home page
    And I accept cookies if present
    When I navigate to the WordPress Hosting page
    Then the WordPress Hosting page header should be "WordPress Host"

  Scenario Outline: Home to product pages shows correct header
    Given I am on the InMotion Hosting home page
    And I accept cookies if present
    When I navigate to the "<page>" page
    Then the page header should be "<h1>"

    Examples:
      | page      | h1                       |
      | WordPress | WordPress Host           |
      | VPS       | VPS Web Hosting Services |
