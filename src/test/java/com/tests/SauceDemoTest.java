package com.tests;

import com.framework.config.BaseTest;
import com.pages.Sauce.LoginToSaucePage;
import com.pages.Sauce.SauceDemoPage;
import org.testng.annotations.Test;

public class SauceDemoTest extends BaseTest {

    @Test
    public void navigateToSauce(){
        SauceDemoPage saucePage = new SauceDemoPage(page());
        LoginToSaucePage loginSauce = new LoginToSaucePage();
        saucePage.navigateToSauce();
        loginSauce.loginToSauce("standard_user", "secret_sauce");
        saucePage.addBagToCart();
    }

}
