package com.pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;

import static com.framework.driver.BrowserFactory.getCurrentUrl;
import static org.assertj.core.api.Assertions.assertThat;

public class DemoQAPage extends BasePage{

    private static final String PAGE_URL = "https://demoqa.com/automation-practice-form";
    public final Locator FIRST_NAME = page.locator("#firstName");

    public DemoQAPage(Page page) {
        super(page);
    }

    public void navigateToDemoQa() {

        logger.info("navigate to demoQA");
        page.navigate(PAGE_URL);

        assertThat(getCurrentUrl()).contains(PAGE_URL);
        assertThat(getTitle()).isNotEmpty();

        logger.info("Navigation verification completed");
    }

    public void fillName(String firstName){
        logger.info("Fill name");
        FIRST_NAME.fill(firstName);

        logger.info("filled with{} ", firstName);

    }


}
