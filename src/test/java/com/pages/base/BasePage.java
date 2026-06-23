package com.pages.base;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.WaitForSelectorState;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BasePage {
    protected final Logger logger = LoggerFactory.getLogger(this.getClass());
    protected final Page page;

    public BasePage(Page page) {
        this.page = page;
    }

    protected void click(String selector) {
        logger.debug("Clicking element: {}", selector);
        page.click(selector);
    }

    protected void fill(String selector, String text) {
        logger.debug("Filling element: {} with text: {}", selector, text);
        page.fill(selector, text);
    }

    protected String getText(String selector) {
        logger.debug("Getting text from element: {}", selector);
        return page.textContent(selector);
    }

    protected boolean isVisible(String selector) {
        return page.isVisible(selector);
    }

    protected boolean isEnabled(String selector) {
        return page.isEnabled(selector);
    }

    protected void waitForSelector(String selector) {
        logger.debug("Waiting for selector: {}", selector);
        page.waitForSelector(selector);
    }

    protected void waitForSelector(String selector, int timeout) {
        logger.debug("Waiting for selector: {} with timeout: {}ms", selector, (Object) timeout);
        page.waitForSelector(selector, new Page.WaitForSelectorOptions().setTimeout(timeout));
    }

    protected void waitForSelectorToBeHidden(String selector) {
        logger.debug("Waiting for selector to be hidden: {}", selector);
        page.waitForSelector(selector, new Page.WaitForSelectorOptions()
                .setState(WaitForSelectorState.HIDDEN));
    }

    protected void selectOption(String selector, String value) {
        logger.debug("Selecting option: {} in element: {}", value, selector);
        page.selectOption(selector, value);
    }

    protected void check(String selector) {
        logger.debug("Checking checkbox: {}", selector);
        page.check(selector);
    }

    protected void uncheck(String selector) {
        logger.debug("Unchecking checkbox: {}", selector);
        page.uncheck(selector);
    }

    protected boolean isChecked(String selector) {
        return page.isChecked(selector);
    }

    protected Locator getLocator(String selector) {
        return page.locator(selector);
    }

    protected void scrollToElement(String selector) {
        logger.debug("Scrolling to element: {}", selector);
        page.locator(selector).scrollIntoViewIfNeeded();
    }

    protected void hover(String selector) {
        logger.debug("Hovering over element: {}", selector);
        page.hover(selector);
    }

    protected int getElementCount(String selector) {
        return page.locator(selector).count();
    }

    protected void pressKey(String key) {
        logger.debug("Pressing key: {}", key);
        page.keyboard().press(key);
    }

    protected String getUrl() {
        return page.url();
    }

    protected String getTitle() {
        return page.title();
    }

    protected void reload() {
        logger.debug("Reloading page");
        page.reload();
    }

    protected void goBack() {
        logger.debug("Navigating back");
        page.goBack();
    }

    protected void goForward() {
        logger.debug("Navigating forward");
        page.goForward();
    }
}
