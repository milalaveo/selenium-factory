# Selenium Factory + BDD (InMotion Hosting)

## Overview
This project is a Selenium UI test suite for https://www.inmotionhosting.com/ using:
- Page Object + Page Factory pattern for UI pages
- JUnit 5 for classic smoke tests
- Cucumber (BDD) for feature-driven scenarios

## What tests are covered
### Smoke tests (JUnit 5)
Located in `src/test/java/com/stv/factory/factorytests`.
- Home page -> WordPress Hosting: verify H1 is "WordPress Host"
- Home page -> VPS Hosting: verify H1 is "VPS Web Hosting Services"

### BDD scenarios (Cucumber)
Feature file: `src/test/resources/bdd/inmotion_hosting.feature`
- Scenario: Home -> WordPress Hosting header is correct
- Scenario Outline: Home -> product page header is correct (WordPress, VPS)
- Scenario: Cookie preference center content is present but not visible

## Approach
- Page Objects in `src/test/java/com/stv/factory/factorypages` encapsulate selectors and actions.
- Tests read like user flows and assert page H1 text to confirm navigation.
- Cookies banner is handled via `acceptCookiesIfPresent()` on the Home page.
- Cucumber scenarios reuse the same Page Objects via step definitions.

## How to run
From the project root (`selenium-factory`):
- Run all tests (JUnit + Cucumber):
  - `mvn test`
- Run only JUnit smoke tests:
  - `mvn -Dtest=InMotionSmokeFactoryTests test`
- Run only Cucumber BDD tests:
  - `mvn -Dtest=RunCucumberTests test`
- Run only the cookie consent bug scenario:
  - `mvn -Dtest=RunCucumberTests test "-Dcucumber.filter.tags=@bug"`

## Important notes
- Java 17 is required.
- Chrome must be installed locally; Selenium Manager provides the driver.
- Tests are UI-driven and depend on the current site content and selectors.
- If a test fails due to UI changes, update the related Page Object first.

## Project structure
- `src/test/java/com/stv/factory/factorypages`: Page Objects (Page Factory)
- `src/test/java/com/stv/factory/factorytests`: JUnit 5 smoke tests
- `src/test/java/com/stv/factory/bdd`: Cucumber runner, steps, hooks
- `src/test/resources/bdd`: Feature files
