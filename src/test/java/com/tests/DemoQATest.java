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
    }

    @Test
    public void verifyNameInputIsCorrectlyFilled() {
        String expectedName = "Dino";
        DemoQAPage demoQAPage = new DemoQAPage(page());

        demoQAPage.fillName(expectedName);

    }




}