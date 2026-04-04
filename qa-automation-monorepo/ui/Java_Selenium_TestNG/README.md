# Selenium TestNG Automation Framework

## 📋 Overview

This is a **production-grade automation framework** built with:
- **Selenium WebDriver 4** - Web browser automation
- **TestNG** - Test execution and reporting
- **Maven** - Build and dependency management
- **Page Object Model (POM)** - Maintainable test architecture
- **Allure/ExtentReports** - Beautiful test reports
- **Log4j 2** - Comprehensive logging

## 🎯 Key Features

✅ **Page Object Model** - Clean separation of page logic and tests  
✅ **Cross-Browser Support** - Chrome, Firefox, Edge, Safari  
✅ **Parallel Execution** - Run tests in parallel for faster results  
✅ **Explicit Waits** - Eliminates flaky tests with smart waits  
✅ **Retry Logic** - Automatic retry on failure  
✅ **Multi-Environment Support** - Run same suite against qa/dev/staging/prod  
✅ **Data-Driven Testing** - TestNG DataProvider + external JSON datasets  
✅ **Comprehensive Logging** - Track everything with Log4j 2  
✅ **Screenshot Capture** - Auto screenshots on test failure  
✅ **Test Data Management** - External config file support  
✅ **CI/CD Ready** - Maven profiles for different environments  
✅ **ThreadLocal Driver Management** - Safe for parallel execution  

---

## 📁 Project Structure

```
AutomationFramework/
├── src/
│   ├── main/java/framework/
│   │   ├── base/
│   │   │   ├── DriverManager.java      # WebDriver lifecycle management
│   │   │   ├── BasePage.java           # Parent page class with common actions
│   │   │   └── BaseTest.java           # Parent test class with setup/teardown
│   │   ├── pages/
│   │   │   ├── LoginPage.java          # Example page object
│   │   │   └── DashboardPage.java      # Example page object
│   │   ├── utils/
│   │   │   ├── LoggerUtil.java         # Logging utility
│   │   │   ├── WaitUtil.java           # Explicit wait utilities
│   │   │   └── ScreenshotUtil.java     # Screenshot capture
│   │   ├── config/
│   │   │   ├── ConfigReader.java       # Properties file reader
│   │   │   └── GlobalConfig.java       # Centralized configuration
│   │   ├── enums/
│   │   │   ├── BrowserType.java        # Browser enumeration
│   │   │   └── WaitStrategy.java       # Wait strategy enumeration
│   │   └── listeners/
│   │       ├── TestListener.java       # TestNG event listener
│   │       └── RetryAnalyzer.java      # Test retry logic
│   ├── test/java/tests/
│   │   └── LoginTests.java             # Example test class
│   └── test/resources/
│       ├── application.properties      # Configuration file
│       ├── log4j2.xml                  # Logging configuration
│       └── testng.xml                  # TestNG suite configuration
├── pom.xml                              # Maven configuration
├── README.md                            # This file
└── testng.xml                           # TestNG execution configuration
```

---

## 🚀 Quick Start

### Prerequisites
- Java 11+
- Maven 3.9+
- Chrome/Firefox/Edge browser installed
- Git (optional)

### Installation & Setup

1. **Clone or download the repository**
   ```bash
   cd /path/to/project
   ```

2. **Update configuration (Optional)**
   Edit `src/test/resources/application.properties`:
   ```properties
   browser=chrome
   headless=false
   base.url=https://practicetestautomation.com
   ```

3. **Build the project**
   ```bash
   mvn clean install
   ```

4. **Run tests**
   ```bash
   mvn test
   ```

---

## 🧪 Running Tests

### Run All Tests
```bash
mvn test
```

### Run Specific Test Suite
```bash
mvn test -Dtest=LoginTests
```

### Run Single Test
```bash
mvn test -Dtest=LoginTests#testSuccessfulLogin
```

### Run Tests with Different Browser
```bash
mvn test -Dbrowser=firefox
```

### Run Tests in Headless Mode
```bash
mvn test -Dheadless=true
```

### Run Tests in Parallel (update threadCount in pom.xml)
```bash
mvn test -Dparallel=true -DthreadCount=4
```

### Run with Environment and Parallel Overrides (Recommended)
```bash
mvn clean test -Denv=qa -Dbrowser=chrome -Dheadless=true -Dthread.count=4 -Dmax.retries=2
```

### Run with Different Environment Profile
```bash
mvn test -Pdev          # Local/Dev environment
mvn test -Pstaging      # Staging environment
mvn test -Pprod         # Production environment
```

### Run Data-Driven Suite (default)
```bash
mvn clean test
```
Data files are located under `src/test/resources/testdata/`.

---

## 📝 Writing New Tests

### Step 1: Create a Page Object
```java
package framework.pages;

import framework.base.BasePage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class MyPage extends BasePage {
    
    private static final By ELEMENT = By.id("element-id");
    
    public MyPage(WebDriver driver) {
        super(driver);
    }
    
    public void clickElement() {
        click(ELEMENT);
    }
    
    public String getElementText() {
        return getText(ELEMENT);
    }
}
```

### Step 2: Create Test Class
```java
package tests;

import framework.base.BaseTest;
import framework.pages.MyPage;
import org.testng.annotations.Test;
import static org.testng.Assert.*;

public class MyTests extends BaseTest {
    
    @Test(description = "Verify element is clickable")
    public void testElementClick() {
        MyPage page = new MyPage(driver);
        page.clickElement();
        assertEquals(page.getElementText(), "Expected Text");
    }
}
```

### Step 3: Add Test to testng.xml
```xml
<test name="My Tests">
    <classes>
        <class name="tests.MyTests"/>
    </classes>
</test>
```

### Step 4: Run Tests
```bash
mvn test
```

---

## 🔍 BasePage Methods (Common Actions)

### Element Actions
```java
// Click element
click(By locator);

// Type text
type(By locator, String text);

// Get text
String text = getText(By locator);

// Get attribute
String value = getAttribute(By locator, "attribute-name");

// Find element
WebElement element = findElement(By locator);

// Find multiple elements
List<WebElement> elements = findElements(By locator);
```

### Wait Methods
```java
// Wait for element presence
WaitUtil.waitForElement(driver, locator);

// Wait for element visibility
WaitUtil.waitForElementToBeVisible(driver, locator);

// Wait for element to be clickable
WaitUtil.waitForElementToBeClickable(driver, locator);

// Wait for element invisibility
WaitUtil.waitForElementToBeInvisible(driver, locator);

// Wait for text in element
WaitUtil.waitForTextToBePresent(driver, locator, "text");
```

### Verification Methods
```java
// Check element presence
boolean present = isElementPresent(By locator);

// Check element visibility
boolean visible = isElementVisible(By locator);

// Check element enabled state
boolean enabled = isElementEnabled(By locator);

// Get count of elements
int count = getElementCount(By locator);
```

### Navigation Methods
```java
// Navigate to URL
navigateTo("https://example.com");

// Get current URL
String url = getCurrentUrl();

// Get page title
String title = getPageTitle();

// Refresh page
refreshPage();

// Navigate back
navigateBack();

// Navigate forward
navigateForward();
```

---

## ⚙️ Configuration Management

### Edit Configuration File
File: `src/test/resources/application.properties`

```properties
# Browser Configuration
browser=chrome              # chrome, firefox, edge, safari
headless=false              # true for headless mode

# Base URL
base.url=https://example.com

# Timeout Configuration
page.load.timeout=30        # seconds
implicit.wait=10            # seconds
explicit.wait=20            # seconds
polling.interval=500        # milliseconds

# Test Execution
max.retries=2
take.screenshots=true
parallel.execution=true
thread.count=4
```

### Access Configuration in Code
```java
// Get value with default
String value = GlobalConfig.getBaseUrl();

// Get value from ConfigReader
String property = ConfigReader.getProperty("key");

// Get value with fallback
String property = ConfigReader.getProperty("key", "defaultValue");
```

---

## 🗄️ MySQL Database Access

### Configuration
Update `src/test/resources/application.properties`:

```properties
db.enabled=true
db.url=jdbc:mysql://localhost:3306/automation_db
db.username=root
db.password=your_password
db.connection.timeout.seconds=10
```

### Utility Classes
- `framework.utils.db.DatabaseManager` - Connection creation and health check
- `framework.utils.db.DatabaseUtil` - Query/update helpers with prepared statements
- `framework.utils.db.DatabaseException` - Runtime exception wrapper for DB operations

### Example Usage in Test
```java
import framework.base.BaseTest;
import framework.utils.db.DatabaseUtil;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.List;
import java.util.Map;

public class UserDbValidationTest extends BaseTest {

    @Test
    public void validateUserExistsInDatabase() {
        List<Map<String, Object>> rows = DatabaseUtil.executeQuery(
                "SELECT id, email FROM users WHERE email = ?",
                "test.user@example.com"
        );

        Assert.assertFalse(rows.isEmpty(), "Expected user record was not found in DB");
        Assert.assertEquals(rows.get(0).get("email"), "test.user@example.com");
    }
}
```

---

## 📊 Test Reports

### Generate Allure Report
```bash
mvn allure:report
```

### View Allure Report
```bash
mvn allure:serve
```

### Report Location
- Default: `test-results/`
- Screenshots: `test-results/screenshots/`
- Logs: `logs/`

---

## 🔍 Logging

### Log Levels
- **DEBUG** - Detailed diagnostic information
- **INFO** - General informational messages
- **WARN** - Warning messages
- **ERROR** - Error messages

### View Logs
```java
LoggerUtil.info(YourClass.class, "Info message");
LoggerUtil.debug(YourClass.class, "Debug message");
LoggerUtil.warn(YourClass.class, "Warning message");
LoggerUtil.error(YourClass.class, "Error message", exception);
```

### Log Files Location
- `logs/automation.log` - All logs
- `logs/error.log` - Error logs only
- `logs/automation-yyyy-MM-dd.log` - Daily logs

---

## 🔗 Advanced Features

### Data-Driven Testing
```java
@Test(dataProvider = "loginData")
public void testLoginWithData(String username, String password) {
    LoginPage page = new LoginPage(driver);
    page.login(username, password);
}

@DataProvider
public Object[][] loginData() {
    return new Object[][] {
        {"user1", "password1"},
        {"user2", "password2"}
    };
}
```

### Retry Failed Tests
```java
@Test(retryAnalyzer = RetryAnalyzer.class)
public void testFlakySituation() {
    // This test will retry 2 times if it fails
}
```

### Test Grouping
```java
@Test(groups = {"smoke"})
public void testCriticalFeature() { }

@Test(groups = {"regression"})
public void testNormalFeature() { }
```

Run smoke tests only:
```bash
mvn test -DgroupsToInclude="smoke"
```

### Parallel Execution with TestNG
Edit `testng.xml`:
```xml
<suite name="Suite" parallel="methods" thread-count="4">
```

---

## 🐛 Troubleshooting

### WebDriver Not Initialized
**Error**: `WebDriver is not initialized`  
**Solution**: Ensure `@BeforeClass` is being called. Extend from `BaseTest`.

### Element Not Found
**Error**: `NoSuchElementException`  
**Solution**: 
- Check locator syntax
- Add wait conditions
- Verify element exists in DOM
- Take screenshot for debugging

### Test Timeout
**Error**: `TimeoutException`  
**Solution**:
- Increase `explicit.wait` timeout
- Check for JavaScript loading
- Verify network conditions

### Parallel Execution Issues
**Error**: `StaleElementReferenceException`  
**Solution**:
- Re-find element after operations
- Use ThreadLocal driver management (already implemented)
- Minimize shared state between threads

---

## 📚 Best Practices

✅ **Use Page Object Model** - Keep tests clean and maintainable  
✅ **Avoid Thread.sleep()** - Use explicit waits instead  
✅ **One assertion per test** - Or use soft assertions  
✅ **Meaningful test names** - Describe what the test does  
✅ **Logging** - Add logs for debugging  
✅ **Screenshot on failure** - Configured by default  
✅ **Independent tests** - No test should depend on another  
✅ **Data-driven tests** - Use parameterization  
✅ **Configuration externalization** - Use properties files  
✅ **Retry logic** - For flaky tests  

---

## 🤝 Contributing

1. Create new page objects in `src/main/java/framework/pages/`
2. Create test classes in `src/test/java/tests/`
3. Add test to `testng.xml`
4. Follow the existing code style
5. Document your changes

---

## 📞 Support & Resources

### Documentation
- [Selenium Documentation](https://selenium.dev/documentation/)
- [TestNG Documentation](https://testng.org/doc/)
- [Maven Documentation](https://maven.apache.org/guides/)

### Useful Commands
```bash
# Clean build
mvn clean

# Build without tests
mvn clean install -DskipTests

# Display dependency tree
mvn dependency:tree

# Update dependencies
mvn versions:display-dependency-updates

# Check for vulnerabilities
mvn dependency-check:check
```

---

## 📄 License

This framework is provided as-is for learning and commercial use.

---

## 🎓 Learning Path

1. Understand **Page Object Model** concept
2. Learn **Selenium WebDriver** basics
3. Explore **TestNG** features
4. Master **explicit waits** (no Thread.sleep!)
5. Implement **parallel execution**
6. Add **reporting** and **logging**
7. Setup **CI/CD integration**
8. Explore **advanced patterns**

---

**Happy Testing! 🚀**

---

*Last Updated: March 7, 2026*
