package com.pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.pages.base.BasePage;

import static com.framework.driver.BrowserFactory.getCurrentUrl;
import static org.assertj.core.api.Assertions.assertThat;

public class SauceDemoPage extends BasePage {
    public SauceDemoPage(Page page) {
        super(page);
    }

    private static final String PAGE_URL = "https://www.saucedemo.com/";
    public final Locator userNameInput = page.locator( "#user-name");
    public final Locator passwordInput = page.locator( "#password");
    public final Locator loginButton = page.locator("#login-button");
    public final Locator errorMessage = page.locator("[data-test='error']");
    public final Locator addToCartProduct = page.locator("#add-to-cart-sauce-labs-backpack");
    public final Locator inventoryNameLocator = page.locator("[data-test='inventory-item-name']");



    public void navigateToSauce(){

        logger.info("Proceed to navigate to sauce");
        page.navigate(PAGE_URL);
        assertThat(getCurrentUrl()).contains(PAGE_URL);
        assertThat(getTitle()).isNotEmpty();
        logger.info("Navigation verification completed");
    }

    public void loginToSauce(String userName, String password){

        userNameInput.fill(userName);
        passwordInput.fill(password);
        loginButton.click();

        if(isLoginSuccessful()){
            logger.info("Login succesful for user: " + userName);
        }else {
            String error = errorMessage.textContent();
            logger.error("Login failed: " + error);
            throw new AssertionError("Login failed: " + error);
        }

    }

    public void addBagToCart(){
        String s = inventoryNameLocator.getByText();



    }

    public Boolean isLoginSuccessful(){
        return page.locator("[data-test='title']").isVisible();
    }

    public String getErrorMessage() {
        return errorMessage.isVisible() ? errorMessage.textContent() : null;
    }


}
