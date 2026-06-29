package com.tests;

import com.framework.config.BaseTest;
import com.pages.SauceDemoPage;
import org.testng.annotations.Test;

public class SauceDemoTest extends BaseTest {

    @Test
    public void navigateToSauce(){
        SauceDemoPage saucePage = new SauceDemoPage(page());
        saucePage.navigateToSauce();
        saucePage.loginToSauce("standard_user", "secret_sauce");
        saucePage.addBagToCart();
    }

}
