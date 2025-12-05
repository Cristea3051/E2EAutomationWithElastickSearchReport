package com.pages;

import com.microsoft.playwright.Page;

import static com.framework.driver.BrowserFactory.getCurrentUrl;
import static org.assertj.core.api.Assertions.assertThat;

public class DemoQAPage extends BasePage{

    private static final String PAGE_URL = "https://demoqa.com/automation-practice-form";

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


}
