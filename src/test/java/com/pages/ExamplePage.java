package com.pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.pages.base.BasePage;

public class ExamplePage extends BasePage {

    private final Locator SEARCH_INPUT = page.locator("input[name='q']");
    private static final String SEARCH_BUTTON = "button[type='submit']";
    private static final String PAGE_TITLE = "h1";

    public ExamplePage(Page page) {
        super(page);
    }

    public void enterSearchQuery(String query) {
        logger.info("Entering search query: {}", query);
        fill(String.valueOf(SEARCH_INPUT), query);
    }

    public void clickSearchButton() {
        logger.info("Clicking search button");
        click(SEARCH_BUTTON);
    }

    public void search(String query) {
        enterSearchQuery(query);
        clickSearchButton();
    }

    public String getPageTitle() {
        logger.info("Getting page title");
        return getText(PAGE_TITLE);
    }

    public boolean isSearchInputVisible() {
        return isVisible(SEARCH_INPUT);
    }

    public boolean isSearchButtonEnabled() {
        return isEnabled(SEARCH_BUTTON);
    }
}
