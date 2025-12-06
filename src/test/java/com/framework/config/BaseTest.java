package com.framework.config;

import com.framework.driver.BrowserFactory;
import com.framework.listeners.TestListener;
import com.microsoft.playwright.Page;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.*;


@Listeners(TestListener.class)
public class BaseTest {
    protected static final Logger logger = LoggerFactory.getLogger(BaseTest.class);
    protected ConfigurationManager config;

    @BeforeMethod(alwaysRun = true)
    @Parameters({"browser"})
    public void setUp(@Optional String browser) {
        logger.info("Setting up test");
        config = ConfigurationManager.getInstance();

        if (browser != null && !browser.isEmpty()) {
            BrowserFactory.initializeBrowser(browser);
        } else {
            BrowserFactory.initializeBrowser();
        }

        page().setDefaultTimeout(config.getTimeout());
        logger.info("Test setup completed");
    }

    @AfterMethod(alwaysRun = true)
    public void tearDown() {
        logger.info("Tearing down test");
        BrowserFactory.closeBrowser();
        logger.info("Test teardown completed");
    }
    protected Page page() {
        return BrowserFactory.getPage();
    }

    protected void navigateTo(String url) {
        logger.info("Navigating to: {}", url);
        page().navigate(url);
    }

    protected void navigateToBaseUrl() {
        navigateTo(config.getBaseUrl());
    }

    protected String getCurrentUrl() {
        return BrowserFactory.getCurrentUrl();
    }

    protected String getTitle() {
        return BrowserFactory.getTitle();
    }

    protected byte[] takeScreenshot() {
        return BrowserFactory.takeScreenshot();
    }
}
