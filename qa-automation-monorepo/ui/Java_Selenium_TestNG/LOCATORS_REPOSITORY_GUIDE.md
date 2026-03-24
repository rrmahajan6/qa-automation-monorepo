# 🗂️ LOCATORS REPOSITORY PATTERN

## Overview

This framework uses the **Locators Repository Pattern**, which keeps all page element locators in **separate, dedicated classes** instead of hardcoding them in page objects.

---

## 🎯 Benefits

✅ **Maintainability** - Change locators in one place  
✅ **Reusability** - Share locators across multiple page objects  
✅ **Readability** - Clean page object code, easy to understand  
✅ **Flexibility** - Easy to switch to external files (Properties, JSON, etc.)  
✅ **Scalability** - Organized structure for large projects  
✅ **Version Control** - Locator updates tracked separately  

---

## 📁 Project Structure

```
src/main/java/framework/pages/
├── LoginPageLocators.java          # All Login page locators
├── LoginPage.java                   # Page actions only (no locators)
├── DashboardPageLocators.java      # All Dashboard page locators
└── DashboardPage.java               # Page actions only (no locators)
```

---

## 🔧 How to Use

### Step 1: Create Locators File

Create a file named `YourPageLocators.java`:

```java
package framework.pages;

import org.openqa.selenium.By;

/**
 * YourPageLocators Repository
 * Centralized storage for all page-specific locators
 */
public class YourPageLocators {

    // Common elements
    public static final By HEADER_TITLE = By.xpath("//h1[@class='header']");
    public static final By MAIN_CONTENT = By.id("main-content");
    
    // Buttons
    public static final By SUBMIT_BUTTON = By.id("submit-btn");
    public static final By CANCEL_BUTTON = By.xpath("//button[contains(text(), 'Cancel')]");
    
    // Form Fields
    public static final By EMAIL_INPUT = By.id("email");
    public static final By PASSWORD_INPUT = By.id("password");
    public static final By CONFIRM_PASSWORD_INPUT = By.id("confirm-password");
    
    // Messages
    public static final By SUCCESS_MESSAGE = By.css(".alert-success");
    public static final By ERROR_MESSAGE = By.css(".alert-error");
    
    // Private constructor to prevent instantiation
    private YourPageLocators() {
        throw new AssertionError("Cannot instantiate locators repository");
    }
}
```

### Step 2: Update Page Object Class

**Before (Old Way - Locators in Page Class):**
```java
public class YourPage extends BasePage {
    
    // ❌ BAD - Locators mixed with page logic
    private static final By HEADER_TITLE = By.xpath("//h1[@class='header']");
    private static final By EMAIL_INPUT = By.id("email");
    private static final By SUBMIT_BUTTON = By.id("submit-btn");
    
    public void fillEmail(String email) {
        type(EMAIL_INPUT, email);
    }
}
```

**After (New Way - Locators in Separate File):**
```java
public class YourPage extends BasePage {
    
    private static final Class<?> logger = YourPage.class;
    
    public YourPage(WebDriver driver) {
        super(driver);
        LoggerUtil.info(logger, "Initializing YourPage");
    }
    
    // ✅ GOOD - Clean page object, locators elsewhere
    public void fillEmail(String email) {
        type(YourPageLocators.EMAIL_INPUT, email);
    }
    
    public void clickSubmit() {
        click(YourPageLocators.SUBMIT_BUTTON);
    }
}
```

### Step 3: Use in Tests

```java
@Test
public void testFillForm() {
    YourPage page = new YourPage(driver);
    
    // Use page objects
    page.fillEmail("user@example.com");
    page.clickSubmit();
    
    // Verify using locators directly if needed
    assertTrue(page.isElementVisible(YourPageLocators.SUCCESS_MESSAGE));
}
```

---

## 📝 Locators Repository Examples

### Example 1: LoginPageLocators

```java
public class LoginPageLocators {
    // Input Fields
    public static final By USERNAME_INPUT = By.id("userEmail");
    public static final By PASSWORD_INPUT = By.id("userPassword");
    
    // Buttons
    public static final By LOGIN_BUTTON = By.id("login");
    
    // Messages
    public static final By ERROR_MESSAGE = By.id("error");
    
    // Links
    public static final By FORGOT_PASSWORD_LINK = By.xpath("//a[.='Forgot']");
    
    private LoginPageLocators() {
        throw new AssertionError("Cannot instantiate");
    }
}
```

### Example 2: DashboardPageLocators

```java
public class DashboardPageLocators {
    // Headers & Titles
    public static final By PAGE_TITLE = By.xpath("//h1[.='Dashboard']");
    
    // User Info
    public static final By USER_PROFILE = By.id("user-profile");
    public static final By USER_NAME = By.css(".user-name");
    
    // Navigation
    public static final By MENU_BUTTON = By.id("menu");
    public static final By LOGOUT_BUTTON = By.id("logout");
    
    private DashboardPageLocators() {
        throw new AssertionError("Cannot instantiate");
    }
}
```

---

## 🔄 Advanced: Organizing Complex Pages

For pages with many elements, organize locators by section:

```java
public class ComplexPageLocators {
    
    // ===== HEADER SECTION =====
    public static final By HEADER = By.id("header");
    public static final By LOGO = By.css(".logo");
    public static final By SEARCH_INPUT = By.id("search");
    
    // ===== SIDEBAR SECTION =====
    public static final By SIDEBAR = By.id("sidebar");
    public static final By SIDEBAR_MENU = By.css(".sidebar-menu > li");
    public static final By SIDEBAR_PROFILE = By.css(".sidebar-profile");
    
    // ===== MAIN CONTENT SECTION =====
    public static final By MAIN_CONTENT = By.id("main");
    public static final By CONTENT_TITLE = By.css(".content-title");
    public static final By CONTENT_BODY = By.css(".content-body");
    
    // ===== FOOTER SECTION =====
    public static final By FOOTER = By.id("footer");
    public static final By COPYRIGHT = By.css(".copyright");
    
    private ComplexPageLocators() {
        throw new AssertionError("Cannot instantiate");
    }
}
```

---

## 💡 Best Practices

### ✅ DO

```java
// ✅ GOOD - Descriptive names
public static final By USER_PROFILE_DROPDOWN = By.id("user-dropdown");

// ✅ GOOD - Organized by sections
// ===== LOGIN FORM =====
// ===== BUTTONS =====

// ✅ GOOD - Comments for complex locators
// Find all active notification badges
public static final By ACTIVE_NOTIFICATIONS = By.css(".notification.active");

// ✅ GOOD - One locator per element
public static final By SUBMIT_BUTTON = By.id("submit");
```

### ❌ DON'T

```java
// ❌ BAD - Vague names
public static final By BTN = By.id("button");

// ❌ BAD - Duplicate locators
public static final By SUBMIT1 = By.id("submit");
public static final By SUBMIT2 = By.id("submit");  // Duplicate!

// ❌ BAD - Complex selectors without explanation
public static final By X = By.xpath("//div[@class='x']/span[2]/button");

// ❌ BAD - Hardcoding locators in tests
driver.findElement(By.id("submit")).click();  // Should use page object
```

---

## 🛠️ How to Add New Locators

### Scenario: Adding a new element to LoginPage

1. **Identify the locator** on the application UI
   ```
   Element: "Remember Me" checkbox
   Locator: By.id("remember-me")
   ```

2. **Add to LoginPageLocators.java**
   ```java
   public static final By REMEMBER_ME_CHECKBOX = By.id("remember-me");
   ```

3. **Add page action method**
   ```java
   // In LoginPage.java
   public void clickRememberMe() {
       click(LoginPageLocators.REMEMBER_ME_CHECKBOX);
   }
   ```

4. **Use in tests**
   ```java
   loginPage.clickRememberMe();
   ```

---

## 🔍 Updating Locators When UI Changes

### Before: Locators mixed with page code
```
Problem: Must search entire page class, risk breaking other methods
```

### After: Locators in separate file
```
Solution: Go directly to YourPageLocators.java and update
Impact: Clear and contained, no risk to page logic
```

**Example:**
```java
// OLD locator (broken after UI redesign)
public static final By LOGIN_BUTTON = By.id("login");

// NEW locator (updated after UI change)
public static final By LOGIN_BUTTON = By.css(".btn-primary");
```

---

## 🚀 Future Enhancement: External Locators File

You can easily extend this to use external files:

### Option 1: Properties File
```properties
# login_page.properties
username.input=id:userEmail
password.input=id:userPassword
login.button=id:login
```

### Option 2: JSON File
```json
{
  "LoginPage": {
    "usernameInput": "By.id('userEmail')",
    "passwordInput": "By.id('userPassword')",
    "loginButton": "By.id('login')"
  }
}
```

### Option 3: YAML File
```yaml
LoginPage:
  usernameInput: id:userEmail
  passwordInput: id:userPassword
  loginButton: id:login
```

Then create a `LocatorsFactory` to load and parse these files dynamically.

---

## 📊 Example: Complete Workflow

### 1. Create Locators Repository
```java
// src/main/java/framework/pages/RegistrationPageLocators.java
public class RegistrationPageLocators {
    public static final By FIRST_NAME = By.id("firstName");
    public static final By LAST_NAME = By.id("lastName");
    public static final By EMAIL = By.id("email");
    public static final By PASSWORD = By.id("password");
    public static final By REGISTER_BUTTON = By.id("register");
    public static final By SUCCESS_MESSAGE = By.css(".alert-success");
    private RegistrationPageLocators() {}
}
```

### 2. Create Page Object
```java
// src/main/java/framework/pages/RegistrationPage.java
public class RegistrationPage extends BasePage {
    
    public RegistrationPage(WebDriver driver) {
        super(driver);
    }
    
    public void enterFirstName(String firstName) {
        type(RegistrationPageLocators.FIRST_NAME, firstName);
    }
    
    public void registerNewUser(String firstName, String lastName, 
                               String email, String password) {
        enterFirstName(firstName);
        type(RegistrationPageLocators.LAST_NAME, lastName);
        type(RegistrationPageLocators.EMAIL, email);
        type(RegistrationPageLocators.PASSWORD, password);
        click(RegistrationPageLocators.REGISTER_BUTTON);
    }
}
```

### 3. Use in Tests
```java
// src/test/java/tests/RegistrationTests.java
@Test
public void testUserRegistration() {
    RegistrationPage registrationPage = new RegistrationPage(driver);
    
    registrationPage.registerNewUser("John", "Doe", 
                                     "john@example.com", "Pass123");
    
    assertTrue(registrationPage.isElementVisible(
        RegistrationPageLocators.SUCCESS_MESSAGE
    ));
}
```

---

## 🎯 Current Framework Status

### ✅ Already Implemented
- LoginPageLocators.java - All Login page locators
- DashboardPageLocators.java - All Dashboard page locators
- LoginPage.java - Updated to use LoginPageLocators
- DashboardPage.java - Updated to use DashboardPageLocators

### 📋 What This Gives You
- **Clean page objects** - Only business logic
- **Reusable locators** - Easy to share across classes
- **Maintainable code** - Update locators in one place
- **Scalable structure** - Easy to add more pages
- **Professional approach** - Industry standard pattern

---

## 🎓 Summary

The **Locators Repository Pattern** is the industry-standard approach for managing web element locators in automation frameworks. It provides:

1. **Separation of Concerns** - Locators separate from page logic
2. **Easy Maintenance** - Update locators without touching page code
3. **Reusability** - Share locators across page objects
4. **Scalability** - Easy to manage large test suites
5. **Professionalism** - Enterprise-grade practice

This pattern is used in frameworks across the world and recommended by QA professionals! 🎉

---

**Your framework now follows this best practice!**

