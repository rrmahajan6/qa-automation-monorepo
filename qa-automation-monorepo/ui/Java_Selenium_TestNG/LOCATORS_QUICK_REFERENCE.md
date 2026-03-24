# 🚀 QUICK REFERENCE: Using Locators Repository Pattern

## 5-Step Process to Create a New Page

### Step 1: Create Locators Respository
```java
// File: YourPageLocators.java
package framework.pages;

import org.openqa.selenium.By;

public class YourPageLocators {
    // Group locators by section
    public static final By HEADER_TITLE = By.xpath("//h1");
    public static final By INPUT_FIELD = By.id("input");
    public static final By SUBMIT_BUTTON = By.id("submit");
    public static final By ERROR_MESSAGE = By.css(".error");
    
    private YourPageLocators() {
        throw new AssertionError("Cannot instantiate");
    }
}
```

### Step 2: Create Page Object
```java
// File: YourPage.java
package framework.pages;

import framework.base.BasePage;
import org.openqa.selenium.WebDriver;

public class YourPage extends BasePage {
    private static final Class<?> logger = YourPage.class;
    
    public YourPage(WebDriver driver) {
        super(driver);
        LoggerUtil.info(logger, "Initializing YourPage");
    }
    
    public void fillInput(String text) {
        type(YourPageLocators.INPUT_FIELD, text);
    }
    
    public void clickSubmit() {
        click(YourPageLocators.SUBMIT_BUTTON);
    }
}
```

### Step 3: Write Test
```java
// File: YourTests.java
@Test
public void testYourFeature() {
    YourPage page = new YourPage(driver);
    page.fillInput("test data");
    page.clickSubmit();
}
```

---

## Project Files

```
src/main/java/framework/pages/
├── LoginPageLocators.java              ✅ All Login locators
├── LoginPage.java                      ✅ Login page actions
├── DashboardPageLocators.java          ✅ All Dashboard locators
├── DashboardPage.java                  ✅ Dashboard page actions
```

---

## Copy-Paste Template

### Locators Template
```java
package framework.pages;

import org.openqa.selenium.By;

/**
 * [PageName]Locators Repository
 * Centralized storage for all [PageName] page locators
 */
public class [PageName]Locators {
    
    // ===== SECTION 1 =====
    public static final By ELEMENT_1 = By.id("element1");
    public static final By ELEMENT_2 = By.css(".element2");
    
    // ===== SECTION 2 =====
    public static final By BUTTON_1 = By.xpath("//button");
    
    private [PageName]Locators() {
        throw new AssertionError("Cannot instantiate");
    }
}
```

### Page Object Template
```java
package framework.pages;

import framework.base.BasePage;
import framework.utils.LoggerUtil;
import org.openqa.selenium.WebDriver;

public class [PageName] extends BasePage {
    private static final Class<?> logger = [PageName].class;
    
    public [PageName](WebDriver driver) {
        super(driver);
        LoggerUtil.info(logger, "Initializing [PageName]");
    }
    
    // Add page action methods here
    public void actionMethod() {
        click([PageName]Locators.ELEMENT_1);
    }
}
```

---

## Locator Best Practices

| Checklist | Status |
|-----------|--------|
| Locators in separate *Locators.java classes | ✅ |
| Descriptive locator names | ✅ |
| Organized by sections with comments | ✅ |
| Page objects don't define locators | ✅ |
| Static final By fields in locators class | ✅ |
| Private constructor in locators class | ✅ |

---

## Usage Examples

### Example 1: Using Existing Locators
```java
LoginPage loginPage = new LoginPage(driver);
loginPage.enterUsername("user@example.com");  // Uses LoginPageLocators internally
```

### Example 2: Direct Locator Access (if needed)
```java
assertTrue(page.isElementVisible(LoginPageLocators.LOGIN_BUTTON));
```

### Example 3: Adding New Locator
```java
// 1. Add to LoginPageLocators
public static final By TWO_FACTOR_INPUT = By.id("2fa-code");

// 2. Add method to LoginPage
public void enter2FACode(String code) {
    type(LoginPageLocators.TWO_FACTOR_INPUT, code);
}

// 3. Use in test
loginPage.enter2FACode("123456");
```

---

## Benefits Summary

| Benefit | Impact |
|---------|--------|
| Locators centralized | Easy to update when UI changes |
| Page objects clean | Focus on behavior, not locators |
| Reusable | Share locators across classes |
| Maintainable | No scattered locator definitions |
| Professional | Industry-standard approach |
| Scalable | Handles 100+ pages easily |

---

## Current Framework Structure

```
✅ LoginPageLocators.java
   - USERNAME_INPUT = By.id("userEmail")
   - PASSWORD_INPUT = By.id("userPassword")
   - LOGIN_BUTTON = By.id("login")
   - ERROR_MESSAGE = By.id("error")

✅ LoginPage.java
   - enterUsername(String)
   - enterPassword(String)
   - clickLoginButton()
   - login(String, String)

✅ DashboardPageLocators.java
   - WELCOME_MESSAGE
   - USER_PROFILE
   - LOGOUT_BUTTON
   - MENU_BUTTON

✅ DashboardPage.java
   - isDashboardPageLoaded()
   - getWelcomeMessage()
   - logout()
   - clickMenuButton()
```

---

## Files to Study

1. **[LOCATORS_REPOSITORY_GUIDE.md](LOCATORS_REPOSITORY_GUIDE.md)** - Complete guide with examples
2. **[src/main/java/framework/pages/LoginPageLocators.java](src/main/java/framework/pages/LoginPageLocators.java)** - Example locators file
3. **[src/main/java/framework/pages/LoginPage.java](src/main/java/framework/pages/LoginPage.java)** - Example page object using locators

---

## Next Steps

1. ✅ Review the existing LoginPageLocators and LoginPage
2. ✅ Review the existing DashboardPageLocators and DashboardPage
3. ✅ Read [LOCATORS_REPOSITORY_GUIDE.md](LOCATORS_REPOSITORY_GUIDE.md)
4. ✅ Create your first page following the template
5. ✅ Write tests using your page objects

---

**Your framework now uses industry-standard locators repository pattern! 🎉**

This approach makes your tests maintainable, scalable, and professional!
