package framework.utils;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import java.util.Set;

/**
 * Browser Utility Class
 * Provides browser-related operations
 */
public class BrowserUtil {

    private static final Class<?> logger = BrowserUtil.class;

    /**
     * Switch to new window/tab
     * @param driver WebDriver instance
     */
    public static void switchToNewWindow(WebDriver driver) {
        String originalWindow = driver.getWindowHandle();
        Set<String> handles = driver.getWindowHandles();
        
        handles.forEach(handle -> {
            if (!handle.equals(originalWindow)) {
                driver.switchTo().window(handle);
                LoggerUtil.info(logger, "Switched to new window");
            }
        });
    }

    /**
     * Switch to specific window by index
     * @param driver WebDriver instance
     * @param windowIndex index of window
     */
    public static void switchToWindow(WebDriver driver, int windowIndex) {
        Set<String> handles = driver.getWindowHandles();
        int index = 0;
        
        for (String handle : handles) {
            if (index == windowIndex) {
                driver.switchTo().window(handle);
                LoggerUtil.info(logger, "Switched to window: " + windowIndex);
                return;
            }
            index++;
        }
    }

    /**
     * Switch to iframe by index
     * @param driver WebDriver instance
     * @param index index of iframe
     */
    public static void switchToFrame(WebDriver driver, int index) {
        driver.switchTo().frame(index);
        LoggerUtil.info(logger, "Switched to frame: " + index);
    }

    /**
     * Switch to iframe by WebElement
     * @param driver WebDriver instance
     * @param element iframe element
     */
    public static void switchToFrame(WebDriver driver, WebElement element) {
        driver.switchTo().frame(element);
        LoggerUtil.info(logger, "Switched to frame");
    }

    /**
     * Switch back to parent frame
     * @param driver WebDriver instance
     */
    public static void switchToParentFrame(WebDriver driver) {
        driver.switchTo().parentFrame();
        LoggerUtil.info(logger, "Switched to parent frame");
    }

    /**
     * Switch to default content
     * @param driver WebDriver instance
     */
    public static void switchToDefaultContent(WebDriver driver) {
        driver.switchTo().defaultContent();
        LoggerUtil.info(logger, "Switched to default content");
    }

    /**
     * Handle alert - Accept
     * @param driver WebDriver instance
     */
    public static void acceptAlert(WebDriver driver) {
        try {
            driver.switchTo().alert().accept();
            LoggerUtil.info(logger, "Alert accepted");
        } catch (Exception e) {
            LoggerUtil.warn(logger, "No alert present to accept");
        }
    }

    /**
     * Handle alert - Dismiss
     * @param driver WebDriver instance
     */
    public static void dismissAlert(WebDriver driver) {
        try {
            driver.switchTo().alert().dismiss();
            LoggerUtil.info(logger, "Alert dismissed");
        } catch (Exception e) {
            LoggerUtil.warn(logger, "No alert present to dismiss");
        }
    }

    /**
     * Get alert text
     * @param driver WebDriver instance
     * @return alert text
     */
    public static String getAlertText(WebDriver driver) {
        try {
            String text = driver.switchTo().alert().getText();
            LoggerUtil.info(logger, "Alert text: " + text);
            return text;
        } catch (Exception e) {
            LoggerUtil.warn(logger, "No alert present");
            return null;
        }
    }

    /**
     * Type in alert
     * @param driver WebDriver instance
     * @param text text to type
     */
    public static void typeInAlert(WebDriver driver, String text) {
        try {
            driver.switchTo().alert().sendKeys(text);
            LoggerUtil.info(logger, "Typed in alert: " + text);
        } catch (Exception e) {
            LoggerUtil.error(logger, "Error typing in alert: " + e.getMessage());
        }
    }

    /**
     * Scroll to top of page
     * @param driver WebDriver instance
     */
    public static void scrollToTop(WebDriver driver) {
        ((JavascriptExecutor) driver).executeScript("window.scrollTo(0, 0);");
        LoggerUtil.info(logger, "Scrolled to top of page");
    }

    /**
     * Scroll to bottom of page
     * @param driver WebDriver instance
     */
    public static void scrollToBottom(WebDriver driver) {
        ((JavascriptExecutor) driver).executeScript("window.scrollTo(0, document.body.scrollHeight);");
        LoggerUtil.info(logger, "Scrolled to bottom of page");
    }

    /**
     * Get page load readyState
     * @param driver WebDriver instance
     * @return ready state (loading, interactive, complete)
     */
    public static String getPageReadyState(WebDriver driver) {
        String readyState = (String) ((JavascriptExecutor) driver)
            .executeScript("return document.readyState;");
        LoggerUtil.debug(logger, "Page ready state: " + readyState);
        return readyState;
    }

    /**
     * Wait for page to load completely using JS
     * @param driver WebDriver instance
     */
    public static void waitForPageLoad(WebDriver driver) {
        int maxAttempts = 10;
        int attempts = 0;
        
        while (attempts < maxAttempts) {
            String readyState = getPageReadyState(driver);
            if ("complete".equals(readyState)) {
                LoggerUtil.info(logger, "Page loaded successfully");
                return;
            }
            attempts++;
            WaitUtil.sleep(500);
        }
        
        LoggerUtil.warn(logger, "Page did not load within timeout");
    }

    /**
     * Execute async JavaScript
     * @param driver WebDriver instance
     * @param script script to execute
     * @param args arguments
     * @return script result
     */
    public static Object executeAsyncScript(WebDriver driver, String script, Object... args) {
        LoggerUtil.debug(logger, "Executing async script");
        return ((JavascriptExecutor) driver).executeAsyncScript(script, args);
    }

    /**
     * Get browser name
     * @param driver WebDriver instance
     * @return browser name
     */
    public static String getBrowserName(WebDriver driver) {
        String browserName = driver.getClass().getSimpleName();
        LoggerUtil.info(logger, "Current browser: " + browserName);
        return browserName;
    }

    /**
     * Get window size
     * @param driver WebDriver instance
     * @return window dimensions
     */
    public static String getWindowSize(WebDriver driver) {
        int width = driver.manage().window().getSize().getWidth();
        int height = driver.manage().window().getSize().getHeight();
        return "Width: " + width + ", Height: " + height;
    }

    /**
     * Maximize window
     * @param driver WebDriver instance
     */
    public static void maximizeWindow(WebDriver driver) {
        driver.manage().window().maximize();
        LoggerUtil.info(logger, "Window maximized");
    }

    /**
     * Minimize window
     * @param driver WebDriver instance
     */
    public static void minimizeWindow(WebDriver driver) {
        driver.manage().window().minimize();
        LoggerUtil.info(logger, "Window minimized");
    }
}
