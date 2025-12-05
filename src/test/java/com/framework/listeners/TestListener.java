package com.framework.listeners;

import com.framework.config.ConfigurationManager;
import com.framework.driver.BrowserFactory;
import com.framework.reporter.ElasticsearchReporter;
import com.framework.reporter.TestResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.Base64;

public class TestListener implements ITestListener {
    private static final Logger logger = LoggerFactory.getLogger(TestListener.class);
    private final ConfigurationManager config = ConfigurationManager.getInstance();
    private final ElasticsearchReporter esReporter = ElasticsearchReporter.getInstance();

    @Override
    public void onTestStart(ITestResult result) {
        logger.info("Test Started: {}.{}", result.getTestClass().getName(), result.getMethod().getMethodName());
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        logger.info("Test Passed: {}.{}", result.getTestClass().getName(), result.getMethod().getMethodName());
        reportTestResult(result, "PASSED");
    }

    @Override
    public void onTestFailure(ITestResult result) {
        logger.error("Test Failed: {}.{}", result.getTestClass().getName(), result.getMethod().getMethodName());

        if (config.isScreenshotOnFailure()) {
            try {
                byte[] screenshot = BrowserFactory.takeScreenshot();
                if (screenshot != null) {
                    result.setAttribute("screenshot", screenshot);
                    logger.info("Screenshot captured for failed test");
                }
            } catch (Exception e) {
                logger.error("Failed to capture screenshot", e);
            }
        }

        reportTestResult(result, "FAILED");
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        logger.warn("Test Skipped: {}.{}", result.getTestClass().getName(), result.getMethod().getMethodName());
        reportTestResult(result, "SKIPPED");
    }

    @Override
    public void onStart(ITestContext context) {
        logger.info("Test Suite Started: {}", context.getName());
    }

    @Override
    public void onFinish(ITestContext context) {
        logger.info("Test Suite Finished: {}", context.getName());
        logger.info("Tests Passed: {}", context.getPassedTests().size());
        logger.info("Tests Failed: {}", context.getFailedTests().size());
        logger.info("Tests Skipped: {}", context.getSkippedTests().size());
    }

    private void reportTestResult(ITestResult result, String status) {
        try {
            TestResult testResult = new TestResult();
            testResult.setTestName(result.getMethod().getMethodName());
            testResult.setClassName(result.getTestClass().getName());
            testResult.setMethodName(result.getMethod().getMethodName());
            testResult.setStatus(status);
            testResult.setBrowser(config.getBrowser());
            testResult.setEnvironment(config.getEnvironment());

            testResult.setStartTime(LocalDateTime.ofInstant(
                    java.time.Instant.ofEpochMilli(result.getStartMillis()),
                    ZoneId.systemDefault()
            ));

            testResult.setEndTime(LocalDateTime.ofInstant(
                    java.time.Instant.ofEpochMilli(result.getEndMillis()),
                    ZoneId.systemDefault()
            ));

            testResult.setDuration(result.getEndMillis() - result.getStartMillis());

            if (result.getThrowable() != null) {
                testResult.setErrorMessage(result.getThrowable().getMessage());
                testResult.setStackTrace(Arrays.toString(result.getThrowable().getStackTrace()));
            }

            byte[] screenshot = (byte[]) result.getAttribute("screenshot");
            if (screenshot != null) {
                testResult.setScreenshotBase64(Base64.getEncoder().encodeToString(screenshot));
            }

            if (result.getParameters() != null && result.getParameters().length > 0) {
                for (int i = 0; i < result.getParameters().length; i++) {
                    testResult.addParameter("param" + i, String.valueOf(result.getParameters()[i]));
                }
            }

            esReporter.indexTestResult(testResult);
        } catch (Exception e) {
            logger.error("Failed to report test result to Elasticsearch", e);
        }
    }
}
