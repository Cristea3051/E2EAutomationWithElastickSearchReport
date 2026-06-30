package com.pages.Sauce;

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
    public final Locator addToCartProduct = page.locator("#add-to-cart-sauce-labs-backpack");

    public void navigateToSauce(){

        logger.info("Proceed to navigate to sauce");
        page.navigate(PAGE_URL);
        assertThat(getCurrentUrl()).contains(PAGE_URL);
        assertThat(getTitle()).isNotEmpty();
        logger.info("Navigation verification completed");
    }

    public void addBagToCart(){
        if(isVisible(addToCartProduct)){
            addToCartProduct.click();
        }
    }



}
