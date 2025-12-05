package com.tests;

import com.framework.config.BaseTest;
import com.pages.DemoQAPage;
import org.testng.annotations.Test;

public class DemoQATest extends BaseTest {

    @Test
    public void testNavigateToDemoQa() {
        DemoQAPage demoQAPage = new DemoQAPage(page);
        demoQAPage.navigateToDemoQa();
    }

}