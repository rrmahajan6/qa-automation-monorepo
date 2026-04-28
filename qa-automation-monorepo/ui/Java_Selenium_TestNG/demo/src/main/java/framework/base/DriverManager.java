package framework.base;

import framework.config.GlobalConfig;
import framework.enums.BrowserType;
import framework.utils.LoggerUtil;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.safari.SafariDriver;
import java.time.Duration;

/**
 * WebDriver Manager Class
 * Handles WebDriver initialization and lifecycle management
 * Supports parallel execution with ThreadLocal
 */
public class DriverManager {

    // ThreadLocal to manage WebDriver instances per thread (for parallel execution)
    private static final ThreadLocal<WebDriver> drivers = new ThreadLocal<>();

    /**
     * Initialize WebDriver based on configuration
     * Supports Chrome, Firefox, Edge, Safari
     * @return initialized WebDriver instance
     */
    public static WebDriver initializeDriver() {
        BrowserType browser = GlobalConfig.getBrowserType();
        WebDriver driver = createDriver(browser);

        if (driver != null) {
            driver.manage().timeouts()
                    .pageLoadTimeout(Duration.ofSeconds(GlobalConfig.getPageLoadTimeout()))
                    .implicitlyWait(Duration.ofSeconds(GlobalConfig.getImplicitWait()));
            driver.manage().window().maximize();
            drivers.set(driver);
            LoggerUtil.info(DriverManager.class, "WebDriver initialized: " + browser.getBrowserName());
        }

        return drivers.get();
    }

    /**
     * Create WebDriver instance based on browser type
     * @param browser BrowserType enum
     * @return WebDriver instance
     */
    private static WebDriver createDriver(BrowserType browser) {
        switch (browser) {
            case CHROME:
                return createChromeDriver();
            case FIREFOX:
                return createFirefoxDriver();
            case EDGE:
                return createEdgeDriver();
            case SAFARI:
                return createSafariDriver();
            default:
                throw new IllegalArgumentException("Unsupported browser: " + browser);
        }
    }

    /**
     * Create Chrome WebDriver
     * @return ChromeDriver instance
     */
    private static WebDriver createChromeDriver() {
        WebDriverManager.chromedriver().setup();
        ChromeOptions options = new ChromeOptions();

        if (GlobalConfig.isHeadless()) {
            options.addArguments("--headless=new");
        }

        // Common useful arguments
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--disable-blink-features=AutomationControlled");
        options.addArguments("--user-agent=Mozilla/5.0");

        return new ChromeDriver(options);
    }

    /**
     * Create Firefox WebDriver
     * @return FirefoxDriver instance
     */
    private static WebDriver createFirefoxDriver() {
        WebDriverManager.firefoxdriver().setup();
        FirefoxOptions options = new FirefoxOptions();

        if (GlobalConfig.isHeadless()) {
            options.addArguments("--headless");
        }

        return new FirefoxDriver(options);
    }

    /**
     * Create Edge WebDriver
     * @return EdgeDriver instance
     */
    private static WebDriver createEdgeDriver() {
        WebDriverManager.edgedriver().setup();
        EdgeOptions options = new EdgeOptions();

        if (GlobalConfig.isHeadless()) {
            options.addArguments("--headless");
        }

        return new EdgeDriver(options);
    }

    /**
     * Create Safari WebDriver
     * @return SafariDriver instance
     */
    private static WebDriver createSafariDriver() {
        // SafariDriver doesn't support many options
        return new SafariDriver();
    }

    /**
     * Get current WebDriver instance for this thread
     * @return WebDriver instance
     */
    public static WebDriver getDriver() {
        WebDriver driver = drivers.get();
        if (driver == null) {
            throw new RuntimeException("WebDriver is not initialized. Call initializeDriver() first.");
        }
        return driver;
    }

    /**
     * Quit WebDriver and remove from ThreadLocal
     */
    public static void quitDriver() {
        WebDriver driver = drivers.get();
        if (driver != null) {
            try {
                driver.quit();
                LoggerUtil.info(DriverManager.class, "WebDriver quit successfully");
            } catch (Exception e) {
                LoggerUtil.error(DriverManager.class, "Error quitting driver: " + e.getMessage(), e);
            } finally {
                drivers.remove();
            }
        }
    }

    /**
     * Get WebDriver instance for specific thread
     * @param threadId thread identifier
     * @return WebDriver instance
     */
    public static WebDriver getDriver(long threadId) {
        return drivers.get();
    }

    /**
     * Restart WebDriver
     */
    public static void restartDriver() {
        quitDriver();
        initializeDriver();
    }

    /**
     * Check if driver is still running
     * @return true if driver is initialized
     */
    public static boolean isDriverInitialized() {
        try {
            return drivers.get() != null && drivers.get().getWindowHandle() != null;
        } catch (Exception e) {
            return false;
        }
    }
}
