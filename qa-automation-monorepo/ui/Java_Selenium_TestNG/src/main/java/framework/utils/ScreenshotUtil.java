package framework.utils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

/**
 * Screenshot Utility Class
 * Provides methods to capture and manage screenshots
 */
public class ScreenshotUtil {

    // Save screenshots inside the Extent report folder so they can be referenced from the HTML report.
    private static final String SCREENSHOT_PATH = "test-results/extent/screenshots/";

    /**
     * Take screenshot and save to file
     * @param driver WebDriver instance
     * @param testName name of the test
     * @return screenshot file path
     */
    public static String takeScreenshot(WebDriver driver, String testName) {
        if (!driver.getClass().getName().toLowerCase().contains("chrome") &&
            !driver.getClass().getName().toLowerCase().contains("firefox") &&
            !driver.getClass().getName().toLowerCase().contains("edge")) {
            LoggerUtil.warn(ScreenshotUtil.class, "Screenshot not supported for this driver");
            return null;
        }

        try {
            // Create screenshot directory if not exists
            Files.createDirectories(Paths.get(SCREENSHOT_PATH));

            // Generate unique filename with timestamp
            String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss_SSS").format(new Date());
            String fileName = testName + "_" + timestamp + ".png";
            String filePath = SCREENSHOT_PATH + fileName;

            // Take screenshot
            File source = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
            File destination = new File(filePath);
            Files.copy(source.toPath(), destination.toPath());

            // Return relative path from the Extent report file location (test-results/extent)
            String relativePath = "screenshots/" + fileName;
            LoggerUtil.info(ScreenshotUtil.class, "Screenshot saved: " + filePath);
            return relativePath;
        } catch (IOException e) {
            LoggerUtil.error(ScreenshotUtil.class, "Failed to take screenshot: " + e.getMessage(), e);
            return null;
        }
    }

    /**
     * Take screenshot with default naming
     * @param driver WebDriver instance
     * @return screenshot file path
     */
    public static String takeScreenshot(WebDriver driver) {
        return takeScreenshot(driver, "screenshot");
    }
    
    /**
     * Get screenshot as base64 string (useful for reports)
     * @param driver WebDriver instance
     * @return base64 encoded screenshot
     */
    public static String getScreenshotAsBase64(WebDriver driver) {
        try {
            return ((TakesScreenshot) driver).getScreenshotAs(OutputType.BASE64);
        } catch (Exception e) {
            LoggerUtil.error(ScreenshotUtil.class, "Failed to get screenshot as base64: " + e.getMessage(), e);
            return null;
        }
    }

    /**
     * Clear old screenshots (optional cleanup)
     */
    public static void clearScreenshots() {
        try {
            File dir = new File(SCREENSHOT_PATH);
            if (dir.exists()) {
                File[] files = dir.listFiles();
                if (files != null) {
                    for (File file : files) {
                        if (file.isFile() && file.delete()) {
                            LoggerUtil.debug(ScreenshotUtil.class, "Deleted old screenshot: " + file.getName());
                        }
                    }
                }
            }
        } catch (Exception e) {
            LoggerUtil.warn(ScreenshotUtil.class, "Failed to clear old screenshots: " + e.getMessage());
        }
    }
}
