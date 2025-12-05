package com.framework.driver;

import com.framework.config.ConfigurationManager;
import com.microsoft.playwright.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.file.Paths;

public class BrowserFactory {
    private static final Logger logger = LoggerFactory.getLogger(BrowserFactory.class);
    private static final ConfigurationManager config = ConfigurationManager.getInstance();
    private static final ThreadLocal<Playwright> playwright = new ThreadLocal<>();
    private static final ThreadLocal<Browser> browser = new ThreadLocal<>();
    private static final ThreadLocal<BrowserContext> context = new ThreadLocal<>();
    private static final ThreadLocal<Page> page = new ThreadLocal<>();

    public static void initializeBrowser() {
        initializeBrowser(config.getBrowser());
    }

    public static void initializeBrowser(String browserName) {
        try {
            logger.info("Initializing browser: {}", browserName);

            Playwright playwrightInstance = Playwright.create();
            playwright.set(playwrightInstance);

            BrowserType browserType = BrowserType.fromString(browserName);
            Browser browserInstance = createBrowser(playwrightInstance, browserType);
            browser.set(browserInstance);

            BrowserContext contextInstance = createContext(browserInstance);
            context.set(contextInstance);

            Page pageInstance = contextInstance.newPage();
            page.set(pageInstance);

            logger.info("Browser initialized successfully: {}", browserName);
        } catch (Exception e) {
            logger.error("Failed to initialize browser: {}", browserName, e);
            throw new RuntimeException("Browser initialization failed", e);
        }
    }

    private static Browser createBrowser(Playwright playwright, BrowserType browserType) {
        com.microsoft.playwright.BrowserType.LaunchOptions launchOptions = new com.microsoft.playwright.BrowserType.LaunchOptions()
                .setHeadless(config.isHeadless())
                .setSlowMo(config.getSlowMo());

        return switch (browserType) {
            case CHROMIUM -> playwright.chromium().launch(launchOptions);
            case FIREFOX -> playwright.firefox().launch(launchOptions);
            case WEBKIT -> playwright.webkit().launch(launchOptions);
        };
    }

    private static BrowserContext createContext(Browser browser) {
        Browser.NewContextOptions contextOptions = new Browser.NewContextOptions()
                .setViewportSize(config.getViewportWidth(), config.getViewportHeight());

        if (config.isVideoOnFailure()) {
            contextOptions.setRecordVideoDir(Paths.get("test-results/videos"));
        }

        return browser.newContext(contextOptions);
    }

    public static Page getPage() {
        Page currentPage = page.get();
        if (currentPage == null) {
            throw new RuntimeException("Page not initialized. Call initializeBrowser() first.");
        }
        return currentPage;
    }

    public static BrowserContext getContext() {
        BrowserContext currentContext = context.get();
        if (currentContext == null) {
            throw new RuntimeException("Context not initialized. Call initializeBrowser() first.");
        }
        return currentContext;
    }

    public static Browser getBrowser() {
        Browser currentBrowser = browser.get();
        if (currentBrowser == null) {
            throw new RuntimeException("Browser not initialized. Call initializeBrowser() first.");
        }
        return currentBrowser;
    }

    public static void closeBrowser() {
        try {
            if (page.get() != null) {
                page.get().close();
                page.remove();
                logger.debug("Page closed");
            }

            if (context.get() != null) {
                context.get().close();
                context.remove();
                logger.debug("Context closed");
            }

            if (browser.get() != null) {
                browser.get().close();
                browser.remove();
                logger.debug("Browser closed");
            }

            if (playwright.get() != null) {
                playwright.get().close();
                playwright.remove();
                logger.debug("Playwright closed");
            }

            logger.info("Browser session closed successfully");
        } catch (Exception e) {
            logger.error("Error closing browser", e);
        }
    }

    public static byte[] takeScreenshot() {
        try {
            return getPage().screenshot();
        } catch (Exception e) {
            logger.error("Failed to take screenshot", e);
            return null;
        }
    }

    public static String getCurrentUrl() {
        return getPage().url();
    }

    public static String getTitle() {
        return getPage().title();
    }
}
