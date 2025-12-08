package com.tests;

import com.framework.config.BaseTest;
import com.pages.DemoQAPage;
import org.testng.annotations.Test;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

public class DemoQATest extends BaseTest {

    @Test
    public void testNavigateToDemoQa() {
        DemoQAPage demoQAPage = new DemoQAPage(page());
        demoQAPage.navigateToDemoQa();

        String expectedFirstName = "Dino";
        String expectedLastName = "Ferrari";
        String expectedEmail = "DinoFerrari@mail.com";

        demoQAPage.fillFirstName(expectedFirstName);
        assertThat(demoQAPage.FIRST_NAME).hasValue(expectedFirstName);
        logger.info("filled with {}", expectedFirstName);

        demoQAPage.fillLastName(expectedLastName);
        assertThat(demoQAPage.LAST_NAME).hasValue(expectedLastName);
        logger.info("filled with {}", expectedLastName);

        demoQAPage.fillUserEmail(expectedEmail);
        assertThat(demoQAPage.USER_EMAIL).hasValue(expectedEmail);
        logger.info("filled with {}", expectedEmail);
    }




}