package com.framework.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ConfigurationManager {
    private static final Logger logger = LoggerFactory.getLogger(ConfigurationManager.class);
    private static ConfigurationManager instance;
    private final Properties properties;

    private ConfigurationManager() {
        properties = new Properties();
        loadProperties();
    }

    public static synchronized ConfigurationManager getInstance() {
        if (instance == null) {
            instance = new ConfigurationManager();
        }
        return instance;
    }

    private void loadProperties() {
        try (InputStream input = getClass().getClassLoader().getResourceAsStream("config.properties")) {
            if (input == null) {
                logger.error("Unable to find config.properties");
                throw new RuntimeException("config.properties not found");
            }
            properties.load(input);
            logger.info("Configuration loaded successfully");
        } catch (IOException e) {
            logger.error("Error loading configuration", e);
            throw new RuntimeException("Failed to load configuration", e);
        }
    }

    public String getProperty(String key) {
        String value = System.getProperty(key);
        if (value == null) {
            value = properties.getProperty(key);
        }
        return value;
    }

    public String getProperty(String key, String defaultValue) {
        String value = getProperty(key);
        return value != null ? value : defaultValue;
    }

    public boolean getBooleanProperty(String key, boolean defaultValue) {
        String value = getProperty(key);
        return value != null ? Boolean.parseBoolean(value) : defaultValue;
    }

    public int getIntProperty(String key, int defaultValue) {
        String value = getProperty(key);
        try {
            return value != null ? Integer.parseInt(value) : defaultValue;
        } catch (NumberFormatException e) {
            logger.warn("Invalid integer value for key: {}, using default: {}", key, defaultValue);
            return defaultValue;
        }
    }

    public String getBrowser() {
        return getProperty("browser", "chromium");
    }

    public boolean isHeadless() {
        return getBooleanProperty("headless", false);
    }

    public int getSlowMo() {
        return getIntProperty("slowmo", 0);
    }

    public int getTimeout() {
        return getIntProperty("timeout", 30000);
    }

    public int getViewportWidth() {
        return getIntProperty("viewport.width", 1920);
    }

    public int getViewportHeight() {
        return getIntProperty("viewport.height", 1080);
    }

    public String getBaseUrl() {
        return getProperty("base.url", "https://example.com");
    }

    public String getEnvironment() {
        return getProperty("env", "qa");
    }

    public String getElasticsearchUrl() {
        return getProperty("elasticsearch.url", "http://localhost:9200");
    }

    public String getElasticsearchIndex() {
        return getProperty("elasticsearch.index", "test-results");
    }

    public boolean isElasticsearchEnabled() {
        return getBooleanProperty("elasticsearch.enabled", true);
    }

    public String getElasticsearchUsername() {
        return getProperty("elasticsearch.username", "");
    }

    public String getElasticsearchPassword() {
        return getProperty("elasticsearch.password", "");
    }

    public boolean isParallelExecution() {
        return getBooleanProperty("parallel.execution", true);
    }

    public int getThreadCount() {
        return getIntProperty("thread.count", 4);
    }

    public boolean isScreenshotOnFailure() {
        return getBooleanProperty("screenshot.on.failure", true);
    }

    public boolean isVideoOnFailure() {
        return getBooleanProperty("video.on.failure", true);
    }
}
