package framework.base;

import framework.enums.WaitStrategy;
import framework.utils.LoggerUtil;
import framework.utils.WaitUtil;
import org.openqa.selenium.By;
import org.openqa.selenium.ElementClickInterceptedException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import java.util.List;

/**
 * BasePage Class
 * Parent class for all page objects
 * Provides common actions and utilities for all pages
 */
public abstract class BasePage {

    protected WebDriver driver;
    protected Actions actions;
    private static final Class<?> logger = BasePage.class;

    /**
     * Constructor initializes WebDriver and page factory
     * @param driver WebDriver instance
     */
    public BasePage(WebDriver driver) {
        this.driver = driver;
        this.actions = new Actions(driver);
    }

    /**
     * Get the current page title
     * @return page title
     */
    public String getPageTitle() {
        LoggerUtil.info(logger, "Getting page title");
        return driver.getTitle();
    }

    /**
     * Get the current page URL
     * @return page URL
     */
    public String getCurrentUrl() {
        LoggerUtil.info(logger, "Getting current URL");
        return driver.getCurrentUrl();
    }

    /**
     * Check if element is present in DOM
     * @param locator By locator
     * @return true if element is found
     */
    public boolean isElementPresent(By locator) {
        LoggerUtil.debug(logger, "Checking if element is present: " + locator);
        return !driver.findElements(locator).isEmpty();
    }

    /**
     * Check if element is visible on page
     * @param locator By locator
     * @return true if element is visible
     */
    public boolean isElementVisible(By locator) {
        LoggerUtil.debug(logger, "Checking if element is visible: " + locator);
        try {
            WebElement element = WaitUtil.waitForElementToBeVisible(driver, locator);
            return element.isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Check if element is enabled
     * @param locator By locator
     * @return true if element is enabled
     */
    public boolean isElementEnabled(By locator) {
        LoggerUtil.debug(logger, "Checking if element is enabled: " + locator);
        try {
            return WaitUtil.waitForElement(driver, locator).isEnabled();
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Click on element
     * @param locator By locator
     */
    public void click(By locator) {
        LoggerUtil.info(logger, "Clicking on element: " + locator);
        
        // // Wait for any loading overlays (spinners) to disappear before clicking
        // waitForLoadingSpinnerToDisappear();

        try {
            WebElement element = WaitUtil.waitForElementToBeClickable(driver, locator);
            element.click();
        } catch (ElementClickInterceptedException e) {
            LoggerUtil.warn(BasePage.class, "Click intercepted, waiting for overlay to disappear: " + locator);
            waitForLoadingSpinnerToDisappear();
            WebElement element = WaitUtil.waitForElementToBeClickable(driver, locator);
            element.click();
        }
    }

    /**
     * Click on WebElement  
     * @param element WebElement to click
     */
    public void click(WebElement element) {
        LoggerUtil.info(logger, "Clicking on element");
        element.click();
    }

    /**
     * Double-click on element
     * @param locator By locator
     */
    public void doubleClick(By locator) {
        LoggerUtil.info(logger, "Double-clicking on element: " + locator);
        WebElement element = WaitUtil.waitForElementToBeVisible(driver, locator);
        actions.doubleClick(element).perform();
    }

    /**
     * Right-click on element
     * @param locator By locator
     */
    public void rightClick(By locator) {
        LoggerUtil.info(logger, "Right-clicking on element: " + locator);
        WebElement element = WaitUtil.waitForElementToBeVisible(driver, locator);
        actions.contextClick(element).perform();
    }

    /**
     * Type text into element
     * @param locator By locator
     * @param text text to type
     */
    public void type(By locator, String text) {
        LoggerUtil.info(logger, "Typing '" + text + "' in element: " + locator);
        WebElement element = WaitUtil.waitForElementToBeVisible(driver, locator);
        element.clear();
        element.sendKeys(text);
    }

    /**
     * Type text into WebElement
     * @param element WebElement to type into
     * @param text text to type
     */
    public void type(WebElement element, String text) {
        LoggerUtil.info(logger, "Typing '" + text + "' in element");
        element.clear();
        element.sendKeys(text);
    }

    /**
     * Get text of element
     * @param locator By locator
     * @return element text
     */
    public String getText(By locator) {
        LoggerUtil.debug(logger, "Getting text from element: " + locator);
        WebElement element = WaitUtil.waitForElementToBeVisible(driver, locator);
        return element.getText().trim();
    }

    /**
     * Get text of WebElement
     * @param element WebElement
     * @return element text
     */
    public String getText(WebElement element) {
        LoggerUtil.debug(logger, "Getting text from element");
        return element.getText().trim();
    }

    /**
     * Get attribute value of element
     * @param locator By locator
     * @param attributeName attribute name
     * @return attribute value
     */
    public String getAttribute(By locator, String attributeName) {
        LoggerUtil.debug(logger, "Getting attribute '" + attributeName + "' from element: " + locator);
        WebElement element = WaitUtil.waitForElement(driver, locator);
        return element.getAttribute(attributeName);
    }

    /**
     * Get CSS property value of element
     * @param locator By locator
     * @param propertyName CSS property name
     * @return CSS property value
     */
    public String getCSSValue(By locator, String propertyName) {
        LoggerUtil.debug(logger, "Getting CSS value '" + propertyName + "' from element: " + locator);
        WebElement element = WaitUtil.waitForElement(driver, locator);
        return element.getCssValue(propertyName);
    }

    /**
     * Find element by locator
     * @param locator By locator
     * @return WebElement
     */
    public WebElement findElement(By locator) {
        LoggerUtil.debug(logger, "Finding element: " + locator);
        return WaitUtil.waitForElement(driver, locator);
    }

    /**
     * Find all elements by locator
     * @param locator By locator
     * @return List of WebElements
     */
    public List<WebElement> findElements(By locator) {
        LoggerUtil.debug(logger, "Finding elements: " + locator);
        return driver.findElements(locator);
    }

    /**
     * Get count of elements
     * @param locator By locator
     * @return count of elements
     */
    public int getElementCount(By locator) {
        LoggerUtil.debug(logger, "Getting element count: " + locator);
        return findElements(locator).size();
    }

    /**
     * Hover on element
     * @param locator By locator
     */
    public void hover(By locator) {
        LoggerUtil.info(logger, "Hovering on element: " + locator);
        WebElement element = WaitUtil.waitForElementToBeVisible(driver, locator);
        actions.moveToElement(element).perform();
    }

    /**
     * Scroll element into view
     * @param locator By locator
     */
    public void scrollToElement(By locator) {
        LoggerUtil.info(logger, "Scrolling to element: " + locator);
        WebElement element = findElement(locator);
        ((org.openqa.selenium.JavascriptExecutor) driver)
                .executeScript("arguments[0].scrollIntoView(true);", element);
    }

    /**
     * Execute JavaScript
     * @param script JavaScript code
     * @param args arguments for script
     * @return script result
     */
    public Object executeScript(String script, Object... args) {
        LoggerUtil.debug(logger, "Executing JavaScript");
        return ((org.openqa.selenium.JavascriptExecutor) driver).executeScript(script, args);
    }

    /**
     * Wait for element with specific wait strategy
     * @param locator By locator
     * @param strategy WaitStrategy
     * @return WebElement
     */
    public WebElement waitForElement(By locator, WaitStrategy strategy) {
        LoggerUtil.debug(logger, "Waiting for element with strategy: " + strategy);
        switch (strategy) {
            case VISIBILITY:
                return WaitUtil.waitForElementToBeVisible(driver, locator);
            case CLICKABILITY:
                return WaitUtil.waitForElementToBeClickable(driver, locator);
            default:
                return WaitUtil.waitForElement(driver, locator);
        }
    }

    /**
     * Refresh page
     */
    public void refreshPage() {
        LoggerUtil.info(logger, "Refreshing page");
        driver.navigate().refresh();
    }

    /**
     * Navigate back
     */
    public void navigateBack() {
        LoggerUtil.info(logger, "Navigating back");
        driver.navigate().back();
    }

    /**
     * Navigate forward
     */
    public void navigateForward() {
        LoggerUtil.info(logger, "Navigating forward");
        driver.navigate().forward();
    }

    /**
     * Navigate to URL
     * @param url URL to navigate to
     */
    public void navigateTo(String url) {
        LoggerUtil.info(logger, "Navigating to: " + url);
        driver.navigate().to(url);
    }

    /**
     * Wait for any known loading spinner/overlay to disappear
     */
    private void waitForLoadingSpinnerToDisappear() {
        // Common overlay class used in this app; adjust if needed.
        By spinnerOverlay = By.cssSelector(".ngx-spinner-overlay");
        WaitUtil.waitForElementToBeInvisible(driver, spinnerOverlay);
    }

    /**
     * Get WebDriver instance
     * @return WebDriver
     */
    public WebDriver getDriver() {
        return this.driver;
    }
}
