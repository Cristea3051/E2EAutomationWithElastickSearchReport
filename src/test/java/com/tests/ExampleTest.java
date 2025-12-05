package com.tests;

import com.framework.config.BaseTest;
import com.pages.ExamplePage;
import org.testng.annotations.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class ExampleTest extends BaseTest {

    @Test(description = "Example test to verify page title")
    public void verifyPageTitleTest() {
        logger.info("Starting test: verifyPageTitleTest");

        navigateToBaseUrl();

        ExamplePage examplePage = new ExamplePage(page);

        assertThat(page.title()).isNotEmpty();
        logger.info("Page title verification completed");
    }

    @Test(description = "Example test to verify navigation")
    public void verifyNavigationTest() {
        logger.info("Starting test: verifyNavigationTest");

        navigateTo("https://www.google.com");

        assertThat(getCurrentUrl()).contains("google.com");
        assertThat(getTitle()).isNotEmpty();

        logger.info("Navigation verification completed");
    }

    @Test(description = "Example test with page object", enabled = false)
    public void searchFunctionalityTest() {
        logger.info("Starting test: searchFunctionalityTest");

        navigateToBaseUrl();
        ExamplePage examplePage = new ExamplePage(page);

        assertThat(examplePage.isSearchInputVisible()).isTrue();
        assertThat(examplePage.isSearchButtonEnabled()).isTrue();

        examplePage.search("test automation");

        logger.info("Search functionality test completed");
    }
}
