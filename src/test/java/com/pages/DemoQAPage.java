package com.pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;

import static com.framework.driver.BrowserFactory.getCurrentUrl;
import static org.assertj.core.api.Assertions.assertThat;

public class DemoQAPage extends BasePage{

    private static final String PAGE_URL = "https://demoqa.com/automation-practice-form";
    public final Locator FIRST_NAME = page.locator("#firstName");
    public final Locator LAST_NAME = page.locator("#lastName");
    public final Locator USER_EMAIL = page.locator("#userEmail");


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

    public void fillFirstName(String firstName){
        logger.info("Fill name");
        FIRST_NAME.fill(firstName);


    }

    public void fillLastName(String lastName){
        logger.info("Proceed to fill last name...");
        LAST_NAME.fill(lastName);

    }

    public void fillUserEmail(String email){

        logger.info("Proceed to fill User Email");
        USER_EMAIL.fill(email);


    }


}