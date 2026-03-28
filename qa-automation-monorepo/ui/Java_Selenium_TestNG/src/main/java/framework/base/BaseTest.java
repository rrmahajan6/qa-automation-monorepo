package framework.base;

import framework.listeners.TestListener;
import framework.config.GlobalConfig;
import framework.utils.ExtentTestManager;
import framework.utils.LoggerUtil;
import framework.utils.db.DataCleanupUtil;
import org.openqa.selenium.WebDriver;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Listeners;
import framework.utils.ScreenshotUtil;

import java.lang.reflect.Method;

/**
 * BaseTest Class
 * Parent class for all test classes
 * Handles setup and teardown of WebDriver
 */
@Listeners(TestListener.class)
public abstract class BaseTest {

    /**
     * Retrieve the WebDriver instance for the current thread
     */
    protected WebDriver getDriver() {
        return DriverManager.getDriver();
    }

    /**
     * Run once per test suite - Initialize framework configuration
     */
    @BeforeSuite(alwaysRun = true)
    public void beforeSuite() {
        LoggerUtil.info(BaseTest.class, "========== Test Suite Started ==========");
        GlobalConfig.initialize();
        
        // Clean database before test execution (if DB is enabled)
        if (GlobalConfig.isDbEnabled()) {
            LoggerUtil.info(BaseTest.class, "Database integration is enabled. Cleaning test data...");
            try {
                DataCleanupUtil.cleanAllConfiguredTables();
                
                // Verify cleanup was successful
                if (DataCleanupUtil.verifyCleanupSuccess()) {
                    LoggerUtil.info(BaseTest.class, "Database cleanup verified successfully");
                } else {
                    LoggerUtil.warn(BaseTest.class, "Some tables may not be completely cleaned");
                }
            } catch (Exception e) {
                LoggerUtil.warn(BaseTest.class, "Database cleanup failed: " + e.getMessage());
            }
        } else {
            LoggerUtil.info(BaseTest.class, "Database integration is disabled. Skipping DB cleanup.");
        }
    }

    /**
     * Run before each test method - Initialize WebDriver and navigate to base URL
     */
    @BeforeMethod(alwaysRun = true)
    public void beforeMethod(Method method) {
        LoggerUtil.info(BaseTest.class, "========== Starting test: " + this.getClass().getSimpleName() + " ==========");

        // Always reset thread-local test context before starting a new method.
        ExtentTestManager.removeTest();

        // Fallback initialization for direct class/method execution where listener wiring may be skipped.
        if (ExtentTestManager.getTest() == null) {
            ExtentTestManager.createTest(method.getName());
        }

        // Initialize WebDriver for each test method (thread-safe)
        DriverManager.initializeDriver();
        WebDriver currentDriver = DriverManager.getDriver();

        if (currentDriver == null) {
            throw new RuntimeException("Failed to initialize WebDriver");
        }

        // Navigate to base URL
        currentDriver.navigate().to(GlobalConfig.getBaseUrl());
        LoggerUtil.info(BaseTest.class, "Navigated to: " + GlobalConfig.getBaseUrl());
    }

    /**
     * Run after each test method - optionally clean up or capture failure data
     */
    @AfterMethod(alwaysRun = true)
    public void afterMethod(ITestResult result) {
        try {
            DriverManager.quitDriver();
            LoggerUtil.info(BaseTest.class, "WebDriver closed successfully");
        } catch (Exception e) {
            LoggerUtil.error(BaseTest.class, "Error closing WebDriver: " + e.getMessage(), e);
        } finally {
            ExtentTestManager.removeTest();
        }
    }

    /**
     * Take screenshot on failure
     * @param testName name of the test
     * @return path to screenshot file, or null if screenshot not taken
     */
    protected String takeScreenshotOnFailure(String testName) {
        WebDriver currentDriver = getDriver();
        if (GlobalConfig.isTakeScreenshots() && currentDriver != null) {
            return ScreenshotUtil.takeScreenshot(currentDriver, testName);
        }
        return null;
    }
}