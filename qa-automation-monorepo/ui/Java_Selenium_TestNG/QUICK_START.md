# QUICK START GUIDE

## 🚀 5-Minute Setup

### Step 1: Build the Project
```bash
cd /Users/rahulmahajan/Desktop/Learning/Java_Selenium_TestNG
mvn clean install
```

### Step 2: Run Sample Tests
```bash
mvn test
```

### Step 3: View Results
- **Logs**: Check `logs/automation.log`
- **Screenshots**: Check `test-results/screenshots/` (on failure)
- **Test Report**: Will be in `test-results/`

---

## 📖 Creating Your First Test

### 1. Create a New Page Object

Create file: `src/main/java/framework/pages/MyApplicationPage.java`

```java
package framework.pages;

import framework.base.BasePage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class MyApplicationPage extends BasePage {
    
    // Define page elements (locators)
    private static final By BUTTON = By.id("my-button");
    private static final By RESULT = By.id("result");
    
    // Constructor
    public MyApplicationPage(WebDriver driver) {
        super(driver);
    }
    
    // Page actions (methods)
    public void clickButton() {
        click(BUTTON);
    }
    
    public String getResult() {
        return getText(RESULT);
    }
    
    // Compound action
    public String clickAndGetResult() {
        clickButton();
        return getResult();
    }
}
```

### 2. Create Test Class

Create file: `src/test/java/tests/MyApplicationTests.java`

```java
package tests;

import framework.base.BaseTest;
import framework.pages.MyApplicationPage;
import org.testng.annotations.Test;
import static org.testng.Assert.*;

public class MyApplicationTests extends BaseTest {
    
    @Test(description = "Test button click functionality")
    public void testButtonClick() {
        // Create page object
        MyApplicationPage page = new MyApplicationPage(driver);
        
        // Perform action
        String result = page.clickAndGetResult();
        
        // Verify result
        assertEquals(result, "Expected Result", "Result should match");
    }
}
```

### 3. Add Test to TestNG Suite

Edit: `testng.xml`

```xml
<test name="My Application Tests">
    <classes>
        <class name="tests.MyApplicationTests"/>
    </classes>
</test>
```

### 4. Run Your Test
```bash
mvn test -Dtest=MyApplicationTests
```

---

## 🎯 Common Patterns

### Pattern 1: Page Navigation
```java
// LoginPage
public class LoginPage extends BasePage {
    public DashboardPage login(String username, String password) {
        enterUsername(username);
        enterPassword(password);
        clickLoginButton();
        return new DashboardPage(driver);  // Return next page
    }
}

// Test
LoginPage loginPage = new LoginPage(driver);
DashboardPage dashboard = loginPage.login("user", "pass");
```

### Pattern 2: Data-Driven Testing
```java
@Test(dataProvider = "testData")
public void testWithData(String username, String password, String expected) {
    LoginPage page = new LoginPage(driver);
    page.login(username, password);
    assertTrue(page.isErrorMessage(), expected);
}

@DataProvider
public Object[][] testData() {
    return new Object[][] {
        {"user1", "pass1", "Error expected"},
        {"user2", "pass2", "Error expected"},
        {"user3", "pass3", "Error expected"}
    };
}
```

### Pattern 3: Soft Assertions
```java
SoftAssert softAssert = new SoftAssert();
softAssert.assertEquals(actual1, expected1);
softAssert.assertEquals(actual2, expected2);
softAssert.assertEquals(actual3, expected3);
softAssert.assertAll();  // Fails if any assertion failed
```

### Pattern 4: Test Retry
```java
@Test(retryAnalyzer = RetryAnalyzer.class)
public void testFlakyScenario() {
    // Will retry up to 2 times if fails
}
```

### Pattern 5: Test Groups
```java
@Test(groups = {"smoke", "regression"})
public void testCriticalFeature() { }

@Test(groups = {"regression"})
public void testNormalFeature() { }
```

Run only smoke tests:
```bash
mvn test -DgroupsToInclude="smoke"
```

---

## 🔧 Common Issues & Solutions

### Issue 1: "Element not found"
```java
// BAD - No wait
WebElement element = driver.findElement(By.id("element"));

// GOOD - With explicit wait
WebElement element = WaitUtil.waitForElementToBeVisible(driver, By.id("element"));

// ALSO GOOD - Using BasePage method
click(By.id("element"));  // Wait is built-in
```

### Issue 2: "Stale Element Reference"
```java
// BAD - Element became stale
WebElement element = driver.findElement(By.id("element"));
doSomething();  // Page refreshed
element.click();  // FAIL: Stale element

// GOOD - Re-find element
click(By.id("element"));  // Re-finds automatically
```

### Issue 3: "Test runs but doesn't find elements"
```java
// Check if element exists first
if (isElementPresent(By.id("element"))) {
    click(By.id("element"));
} else {
    // Take screenshot for debugging
    ScreenshotUtil.takeScreenshot(driver, "debug");
    fail("Element not found");
}
```

### Issue 4: "Tests pass locally but fail in CI"
```
Possible causes:
- Different screen resolution
- Network/DNS issues
- Headless mode issues

Solutions:
1. Run tests in headless mode locally to reproduce
2. Add longer waits for CI environment
3. Check CI environment configuration
```

---

## 📚 Method Reference

### WaitUtil Methods
```java
WaitUtil.waitForElement(driver, locator);              // Presence
WaitUtil.waitForElement(driver, locator, 30);          // Presence + timeout
WaitUtil.waitForElementToBeVisible(driver, locator);   // Visibility
WaitUtil.waitForElementToBeClickable(driver, locator); // Clickability
WaitUtil.waitForElementToBeInvisible(driver, locator); // Invisibility
WaitUtil.waitForTextToBePresent(driver, locator, text); // Text
WaitUtil.sleep(500);                                   // Static wait (use sparingly)
```

### BasePage Click/Type Methods
```java
click(By locator);                        // Click with wait
click(WebElement element);               // Click element
doubleClick(By locator);                 // Double click
rightClick(By locator);                  // Right click
type(By locator, String text);           // Type with clear
type(WebElement element, String text);   // Type element
```

### BasePage Verification Methods
```java
isElementPresent(By locator);            // In DOM
isElementVisible(By locator);            // Visible on page
isElementEnabled(By locator);            // Can interact
getElementCount(By locator);             // Count of elements
getText(By locator);                     // Element text
getAttribute(By locator, "attr");        // Element attribute
```

### BrowserUtil Methods
```java
BrowserUtil.switchToNewWindow(driver);            // Switch to new tab
BrowserUtil.switchToFrame(driver, 0);             // Switch to iframe
BrowserUtil.acceptAlert(driver);                  // Accept alert
BrowserUtil.dismissAlert(driver);                 // Dismiss alert
BrowserUtil.getAlertText(driver);                 // Get alert text
BrowserUtil.scrollToElement(driver, locator);     // Scroll to element
BrowserUtil.scrollToTop(driver);                  // Scroll to top
BrowserUtil.scrollToBottom(driver);               // Scroll to bottom
```

### ScreenshotUtil Methods
```java
ScreenshotUtil.takeScreenshot(driver, "testName");      // Save PNG
ScreenshotUtil.getScreenshotAsBase64(driver);           // Get base64
ScreenshotUtil.clearScreenshots();                      // Delete old shots
```

### LoggerUtil Methods
```java
LoggerUtil.info(LoginTests.class, "Message");           // Info log
LoggerUtil.debug(LoginTests.class, "Message");          // Debug log
LoggerUtil.warn(LoginTests.class, "Message");           // Warn log
LoggerUtil.error(LoginTests.class, "Message", exception); // Error log
```

---

## 🔐 Best Practices Checklist

Before committing test code:

- ✅ Does the test have a descriptive name?
- ✅ Does it run independently (no dependencies)?
- ✅ Does it use explicit waits (no Thread.sleep)?
- ✅ Does it clean up after itself?
- ✅ Are page objects used consistently?
- ✅ Is logging added for debugging?
- ✅ Are assertions meaningful?
- ✅ Is the test data externalized?
- ✅ Does it handle exceptions?
- ✅ Can it run in parallel?

---

## 🚀 Next Steps

1. **Customize Base URL**
   - Edit `src/test/resources/application.properties`
   - Change `base.url` to your application

2. **Create Your Page Objects**
   - Add pages to `src/main/java/framework/pages/`
   - Define locators and actions

3. **Write Your Tests**
   - Add tests to `src/test/java/tests/`
   - Extend from `BaseTest`

4. **Run Tests**
   ```bash
   mvn test
   ```

5. **View Reports**
   - Check logs: `logs/automation.log`
   - Check screenshots: `test-results/screenshots/`

---

## 💡 Pro Tips

### Tip 1: Use Logical Locators
```java
// Bad - Too specific
By locator = By.xpath("//div[@class='content']/div[@id='login-section']/form/input[1]");

// Good - More maintainable
By locator = By.id("username");
// or
By locator = By.xpath("//input[@placeholder='Enter username']");
```

### Tip 2: Page Factory with Annotations
```java
@FindBy(id = "username")
private WebElement usernameField;

// Then use:
usernameField.sendKeys("user");
```

### Tip 3: Environment-Specific Configuration
```bash
# Dev environment
mvn test -Pdev

# Staging environment
mvn test -Pstaging

# Prod environment (read-only)
mvn test -Pprod
```

### Tip 4: Parallel Execution
```bash
# Update testng.xml: parallel="methods" thread-count="4"
mvn test

# Or override:
mvn test -Dparallel=methods -DthreadCount=8
```

### Tip 5: Quick Debugging
```java
// Take instant screenshot
ScreenshotUtil.takeScreenshot(driver, "debug");

// Print current state
System.out.println("URL: " + driver.getCurrentUrl());
System.out.println("Title: " + driver.getTitle());

// Enable DEBUG logging
// Update log4j2.xml: <Root level="DEBUG">
```

---

## 📞 Need Help?

Check these resources:
1. [README.md](README.md) - Full documentation
2. [Selenium Docs](https://selenium.dev/documentation/)
3. [TestNG Docs](https://testng.org/doc/)
4. Framework code comments - Extensive inline documentation

---

**Happy Testing! 🎉**

*Last Updated: March 7, 2026*
