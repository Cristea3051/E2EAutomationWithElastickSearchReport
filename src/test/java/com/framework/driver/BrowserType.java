package com.framework.driver;

public enum BrowserType {
    CHROMIUM("chromium"),
    FIREFOX("firefox"),
    WEBKIT("webkit");

    private final String browserName;

    BrowserType(String browserName) {
        this.browserName = browserName;
    }

    public String getBrowserName() {
        return browserName;
    }

    public static BrowserType fromString(String browser) {
        for (BrowserType type : BrowserType.values()) {
            if (type.browserName.equalsIgnoreCase(browser)) {
                return type;
            }
        }
        throw new IllegalArgumentException("Unknown browser type: " + browser);
    }
}
