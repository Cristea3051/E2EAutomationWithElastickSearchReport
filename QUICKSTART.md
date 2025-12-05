# Quick Start Guide

Get up and running with the automation framework in 5 minutes!

## Prerequisites

- Java 17+ installed
- Maven 3.6+ installed
- Git installed

## Step 1: Install Dependencies

```bash
mvn clean install
```

## Step 2: Install Playwright Browsers

### On Linux/Mac:
```bash
./install-browsers.sh
```

### On Windows:
```cmd
install-browsers.bat
```

### Or manually:
```bash
mvn exec:java -e -D exec.mainClass=com.microsoft.playwright.CLI -D exec.args="install"
```

## Step 3: Configure Your Tests

Edit `src/test/resources/config.properties`:

```properties
base.url=https://your-application-url.com
browser=chromium
headless=false
```

## Step 4: Run Your First Test

```bash
mvn clean test
```

## Step 5: View Results

- **Console Output**: Real-time test execution logs
- **Logs**: `test-results/logs/test-execution.log`
- **Screenshots**: `test-results/screenshots/` (for failed tests)
- **Videos**: `test-results/videos/` (if enabled)

## What's Next?

### Create Your First Page Object

1. Create a new class in `src/test/java/com/pages/`
2. Extend `BasePage`
3. Define your locators and methods

```java
public class MyPage extends BasePage {
    private static final String BUTTON = "#myButton";

    public MyPage(Page page) {
        super(page);
    }

    public void clickButton() {
        click(BUTTON);
    }
}
```

### Create Your First Test

1. Create a new class in `src/test/java/com/tests/`
2. Extend `BaseTest`
3. Use `@Test` annotation

```java
public class MyTest extends BaseTest {
    @Test
    public void testSomething() {
        navigateTo("https://example.com");
        MyPage page = new MyPage(this.page);
        page.clickButton();
    }
}
```

### Run Tests in Different Browsers

```bash
# Chromium
mvn clean test -Dbrowser=chromium

# Firefox
mvn clean test -Dbrowser=firefox

# WebKit
mvn clean test -Dbrowser=webkit
```

### Run Tests in Headless Mode

```bash
mvn clean test -Dheadless=true
```

### Run Specific Test

```bash
mvn clean test -Dtest=ExampleTest#verifyPageTitleTest
```

## Common Issues

### "Playwright not found"
Solution: Run `./install-browsers.sh` or `install-browsers.bat`

### "Java version mismatch"
Solution: Ensure Java 17+ is installed and set as default

### Tests failing?
- Check `test-results/logs/` for detailed logs
- Review screenshots in `test-results/screenshots/`
- Ensure your `base.url` in config.properties is correct

## Need Help?

- Read the full [README.md](README.md)
- Check test examples in `src/test/java/com/tests/`
- Review page object examples in `src/test/java/com/pages/`
