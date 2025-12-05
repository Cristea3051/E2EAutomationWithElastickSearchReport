package com.framework.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Base64;

public class ScreenshotUtils {
    private static final Logger logger = LoggerFactory.getLogger(ScreenshotUtils.class);
    private static final String SCREENSHOT_DIR = "test-results/screenshots";

    public static String saveScreenshot(byte[] screenshot, String testName) {
        try {
            Path screenshotPath = Paths.get(SCREENSHOT_DIR);
            if (!Files.exists(screenshotPath)) {
                Files.createDirectories(screenshotPath);
            }

            String fileName = testName + "_" + DateTimeUtils.getCurrentDateTimeForFile() + ".png";
            Path filePath = screenshotPath.resolve(fileName);

            Files.write(filePath, screenshot);
            logger.info("Screenshot saved: {}", filePath.toAbsolutePath());

            return filePath.toString();
        } catch (IOException e) {
            logger.error("Failed to save screenshot for test: {}", testName, e);
            return null;
        }
    }

    public static String encodeToBase64(byte[] screenshot) {
        if (screenshot == null || screenshot.length == 0) {
            return null;
        }
        return Base64.getEncoder().encodeToString(screenshot);
    }

    public static byte[] decodeFromBase64(String base64String) {
        if (base64String == null || base64String.isEmpty()) {
            return null;
        }
        return Base64.getDecoder().decode(base64String);
    }
}
