package framework.utils;

import framework.config.GlobalConfig;
import framework.enums.WaitStrategy;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;

/**
 * Wait Utility Class
 * Provides explicit waits for various conditions
 * Eliminates Thread.sleep() anti-pattern
 */
public class WaitUtil {

    /**
     * Wait for element to be present in DOM
     * @param driver WebDriver instance
     * @param locator By locator
     * @return WebElement when found
     */
    public static WebElement waitForElement(WebDriver driver, By locator) {
        return waitForElement(driver, locator, GlobalConfig.getExplicitWait());
    }

    /**
     * Wait for element to be present with custom timeout
     * @param driver WebDriver instance
     * @param locator By locator
     * @param timeoutSeconds timeout in seconds
     * @return WebElement when found
     */
    public static WebElement waitForElement(WebDriver driver, By locator, int timeoutSeconds) {
        return wait(driver, timeoutSeconds, WaitStrategy.PRESENCE, locator);
    }

    /**
     * Wait for element to be visible
     * @param driver WebDriver instance
     * @param locator By locator
     * @return WebElement when visible
     */
    public static WebElement waitForElementToBeVisible(WebDriver driver, By locator) {
        return wait(driver, GlobalConfig.getExplicitWait(), WaitStrategy.VISIBILITY, locator);
    }

    /**
     * Wait for element to be clickable
     * @param driver WebDriver instance
     * @param locator By locator
     * @return WebElement when clickable
     */
    public static WebElement waitForElementToBeClickable(WebDriver driver, By locator) {
        return wait(driver, GlobalConfig.getExplicitWait(), WaitStrategy.CLICKABILITY, locator);
    }

    /**
     * Wait for element to be invisible
     * @param driver WebDriver instance
     * @param locator By locator
     * @return true when element becomes invisible
     */
    public static boolean waitForElementToBeInvisible(WebDriver driver, By locator) {
        try {
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(GlobalConfig.getExplicitWait()));
            return wait.until(ExpectedConditions.invisibilityOfElementLocated(locator));
        } catch (TimeoutException e) {
            LoggerUtil.warn(WaitUtil.class, "Element did not become invisible within timeout: " + locator);
            return false;
        }
    }

    /**
     * Wait for element to disappear from DOM
     * @param driver WebDriver instance
     * @param locator By locator
     * @return true when element is no longer present
     */
    public static boolean waitForElementToDisappear(WebDriver driver, By locator) {
        try {
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(GlobalConfig.getExplicitWait()));
            return wait.until(ExpectedConditions.stalenessOf(driver.findElement(locator)));
        } catch (NoSuchElementException | TimeoutException e) {
            return true;
        }
    }

    /**
     * Wait for text to be present in element
     * @param driver WebDriver instance
     * @param locator By locator
     * @param text text to wait for
     * @return true if text is found
     */
    public static boolean waitForTextToBePresent(WebDriver driver, By locator, String text) {
        try {
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(GlobalConfig.getExplicitWait()));
            return wait.until(ExpectedConditions.textToBePresentInElementLocated(locator, text));
        } catch (TimeoutException e) {
            LoggerUtil.warn(WaitUtil.class, "Text not found in element: " + text);
            return false;
        }
    }

    /**
     * Wait for any of multiple elements to be present
     * @param driver WebDriver instance
     * @param locators array of By locators
     * @return true if any element is found
     */
    public static boolean waitForAnyElement(WebDriver driver, By... locators) {
        try {
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(GlobalConfig.getExplicitWait()));
            return wait.until(driver1 -> {
                for (By locator : locators) {
                    try {
                        if (!driver.findElements(locator).isEmpty()) {
                            return true;
                        }
                    } catch (StaleElementReferenceException e) {
                        LoggerUtil.debug(WaitUtil.class, "Stale element, retrying: " + locator);
                    }
                }
                return false;
            });
        } catch (TimeoutException e) {
            LoggerUtil.warn(WaitUtil.class, "None of the elements found within timeout");
            return false;
        }
    }

    /**
     * Core wait method with strategy pattern
     * @param driver WebDriver instance
     * @param timeoutSeconds timeout in seconds
     * @param strategy wait strategy
     * @param locator By locator
     * @return WebElement when found
     */
    private static WebElement wait(WebDriver driver, int timeoutSeconds, WaitStrategy strategy, By locator) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(timeoutSeconds));
        wait.pollingEvery(Duration.ofMillis(GlobalConfig.getPollingInterval()));

        try {
            switch (strategy) {
                case VISIBILITY:
                    return wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
                case CLICKABILITY:
                    return wait.until(ExpectedConditions.elementToBeClickable(locator));
                case INVISIBILITY:
                    wait.until(ExpectedConditions.invisibilityOfElementLocated(locator));
                    return null;
                default:  // PRESENCE
                    return wait.until(ExpectedConditions.presenceOfElementLocated(locator));
            }
        } catch (TimeoutException e) {
            LoggerUtil.error(WaitUtil.class, "Wait timeout exceeded for locator: " + locator + " with strategy: " + strategy);
            throw new TimeoutException("Element not found within " + timeoutSeconds + " seconds: " + locator, e);
        }
    }

    /**
     * Static wait (use sparingly - for non-DOM related delays)
     * @param milliseconds time to wait in milliseconds
     */
    public static void sleep(long milliseconds) {
        try {
            Thread.sleep(milliseconds);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            LoggerUtil.error(WaitUtil.class, "Sleep interrupted: " + e.getMessage());
        }
    }
}
