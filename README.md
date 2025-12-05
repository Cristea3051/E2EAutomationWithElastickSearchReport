# Java Playwright Automation Framework

A comprehensive test automation framework for UI end-to-end testing using Java, Playwright, and Elasticsearch for reporting.

## Features

- **Multi-Browser Support**: Cross-browser testing with Chromium, Firefox, and WebKit
- **Parallel Execution**: Run tests in parallel for faster execution
- **Elasticsearch Reporting**: Store and analyze test results in Elasticsearch
- **Page Object Model**: Clean separation of test logic and page interactions
- **Screenshot on Failure**: Automatic screenshot capture for failed tests
- **Video Recording**: Optional video recording of test execution
- **Configurable**: Easy configuration through properties file
- **Logging**: Comprehensive logging with Logback
- **Thread-Safe**: ThreadLocal pattern for parallel test execution

## Project Structure

```
JavaOOP/
├── src/
│   ├── main/
│   │   └── java/
│   └── test/
│       ├── java/
│       │   └── com/
│       │       ├── framework/
│       │       │   ├── config/
│       │       │   │   ├── ConfigurationManager.java
│       │       │   │   └── BaseTest.java
│       │       │   ├── driver/
│       │       │   │   ├── BrowserType.java
│       │       │   │   └── BrowserFactory.java
│       │       │   ├── reporter/
│       │       │   │   ├── TestResult.java
│       │       │   │   └── ElasticsearchReporter.java
│       │       │   ├── listeners/
│       │       │   │   └── TestListener.java
│       │       │   └── utils/
│       │       │       ├── DateTimeUtils.java
│       │       │       └── ScreenshotUtils.java
│       │       ├── pages/
│       │       │   ├── BasePage.java
│       │       │   └── ExamplePage.java
│       │       └── tests/
│       │           └── ExampleTest.java
│       └── resources/
│           ├── config.properties
│           ├── logback.xml
│           ├── testng.xml
│           └── testng-single-browser.xml
├── pom.xml
└── README.md
```

## Prerequisites

- Java 17 or higher
- Maven 3.6 or higher
- Elasticsearch 8.x (optional, for reporting)

## Setup Instructions

### 1. Install Playwright Browsers

After cloning the project, install Playwright browsers:

```bash
mvn exec:java -e -D exec.mainClass=com.microsoft.playwright.CLI -D exec.args="install"
```

Or install specific browsers:

```bash
mvn exec:java -e -D exec.mainClass=com.microsoft.playwright.CLI -D exec.args="install chromium"
mvn exec:java -e -D exec.mainClass=com.microsoft.playwright.CLI -D exec.args="install firefox"
mvn exec:java -e -D exec.mainClass=com.microsoft.playwright.CLI -D exec.args="install webkit"
```

### 2. Setup Elasticsearch for Reporting (Optional)

**Quick Start with Docker:**

```bash
# Linux/Mac
./start-elasticsearch.sh

# Windows
start-elasticsearch.bat
```

This will start Elasticsearch on http://localhost:9200 and Kibana on http://localhost:5601

**Enable Elasticsearch reporting** in `src/test/resources/config.properties`:

```properties
elasticsearch.enabled=true
```

**For detailed setup instructions, see [ELASTICSEARCH_SETUP.md](ELASTICSEARCH_SETUP.md)**

The guide includes:
- Docker setup with Docker Compose
- Secured vs unsecured configurations
- Creating Kibana dashboards and visualizations
- Querying test results via API
- Troubleshooting common issues

### 3. Configure Test Properties

Edit `src/test/resources/config.properties` to customize your test execution:

```properties
# Browser Configuration
browser=chromium
headless=false
slowmo=0
timeout=30000

# Environment Configuration
base.url=https://example.com
env=qa

# Test Execution Configuration
parallel.execution=true
thread.count=4
screenshot.on.failure=true
video.on.failure=true
```

## Running Tests

### Run All Tests with Cross-Browser Support

```bash
mvn clean test
```

This will run tests in all three browsers (Chromium, Firefox, WebKit) in parallel.

### Run Tests in a Single Browser

```bash
mvn clean test -DsuiteXmlFile=src/test/resources/testng-single-browser.xml
```

### Run Tests with Specific Browser

```bash
mvn clean test -Dbrowser=firefox
```

### Run Tests in Headless Mode

```bash
mvn clean test -Dheadless=true
```

### Run Specific Test Class

```bash
mvn clean test -Dtest=ExampleTest
```

### Run Specific Test Method

```bash
mvn clean test -Dtest=ExampleTest#verifyPageTitleTest
```

## Creating New Tests

### 1. Create a Page Object

```java
package com.pages;

import com.microsoft.playwright.Page;

public class LoginPage extends BasePage {

    private static final String USERNAME_INPUT = "#username";
    private static final String PASSWORD_INPUT = "#password";
    private static final String LOGIN_BUTTON = "button[type='submit']";

    public LoginPage(Page page) {
        super(page);
    }

    public void login(String username, String password) {
        fill(USERNAME_INPUT, username);
        fill(PASSWORD_INPUT, password);
        click(LOGIN_BUTTON);
    }
}
```

### 2. Create a Test Class

```java
package com.tests;

import com.framework.config.BaseTest;
import com.pages.LoginPage;
import org.testng.annotations.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class LoginTest extends BaseTest {

    @Test(description = "Verify successful login")
    public void testSuccessfulLogin() {
        navigateTo("https://example.com/login");

        LoginPage loginPage = new LoginPage(page);
        loginPage.login("testuser", "password123");

        assertThat(getCurrentUrl()).contains("/dashboard");
    }
}
```

## TestNG Configuration

### Parallel Execution

The framework supports parallel execution at multiple levels:

- **Suite level**: Run multiple test suites in parallel
- **Test level**: Run multiple tests in parallel
- **Method level**: Run test methods in parallel

Example configuration in `testng.xml`:

```xml
<suite name="Test Suite" parallel="methods" thread-count="4">
    <test name="Chromium Tests">
        <parameter name="browser" value="chromium"/>
        <classes>
            <class name="com.tests.ExampleTest"/>
        </classes>
    </test>
</suite>
```

## Elasticsearch Reporting

Test results are automatically sent to Elasticsearch with the following information:

- Test name, class, and method
- Execution status (PASSED, FAILED, SKIPPED)
- Browser type
- Environment
- Start and end time
- Duration
- Error messages and stack traces
- Screenshots (Base64 encoded)
- Test parameters

### Viewing Results in Kibana

1. Access Kibana dashboard
2. Create an index pattern for your test results index
3. Visualize test results, failure trends, and execution times

## Best Practices

1. **Page Objects**: Always use page objects to encapsulate page interactions
2. **Assertions**: Use AssertJ for fluent and readable assertions
3. **Logging**: Add meaningful log messages to track test execution
4. **Locators**: Use stable locators (IDs, data-testid attributes)
5. **Waits**: Use Playwright's auto-waiting feature, explicit waits when needed
6. **Test Independence**: Each test should be independent and able to run in any order
7. **Clean Up**: Always clean up test data and close browser sessions

## Extending the Framework

### Adding Custom Utilities

Create utility classes in `com.framework.utils` package:

```java
package com.framework.utils;

public class DataGenerator {
    public static String generateRandomEmail() {
        return "test" + System.currentTimeMillis() + "@example.com";
    }
}
```

### Adding Custom Listeners

Create custom TestNG listeners in `com.framework.listeners` package:

```java
package com.framework.listeners;

import org.testng.ITestListener;
import org.testng.ITestResult;

public class CustomListener implements ITestListener {
    @Override
    public void onTestFailure(ITestResult result) {
        // Custom logic on test failure
    }
}
```

## Troubleshooting

### Playwright Browsers Not Found

Run the install command:

```bash
mvn exec:java -e -D exec.mainClass=com.microsoft.playwright.CLI -D exec.args="install"
```

### Elasticsearch Connection Issues

- Verify Elasticsearch is running
- Check the URL in config.properties
- Verify username/password if authentication is enabled
- Set `elasticsearch.enabled=false` to disable reporting

### Test Failures

- Check logs in `test-results/logs/`
- Review screenshots in `test-results/screenshots/`
- Review videos in `test-results/videos/`

## Contributing

1. Create a feature branch
2. Make your changes
3. Add tests for new functionality
4. Ensure all tests pass
5. Submit a pull request

## Dependencies

- **Playwright**: 1.48.0
- **TestNG**: 7.9.0
- **Elasticsearch Java Client**: 8.11.3
- **Jackson**: 2.16.1
- **SLF4J**: 2.0.9
- **Logback**: 1.4.14
- **AssertJ**: 3.25.1

## License

This project is licensed under the MIT License.
