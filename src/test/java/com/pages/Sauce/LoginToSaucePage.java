package com.pages.Sauce;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.pages.base.BasePage;

public class LoginToSaucePage extends BasePage {

    public LoginToSaucePage(Page page) {
        super(page);
    }

    public final Locator userNameInput = page.locator( "#user-name");
    public final Locator passwordInput = page.locator( "#password");
    public final Locator loginButton = page.locator("#login-button");
    public final Locator errorMessage = page.locator("[data-test='error']");

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

    public String getErrorMessage() {
        return errorMessage.isVisible() ? errorMessage.textContent() : null;
    }

    public Boolean isLoginSuccessful(){
        return page.locator("[data-test='title']").isVisible();
    }


}
