package framework.config;

import framework.enums.BrowserType;

/**
 * Global Configuration Class
 * Centralized place for all framework configuration
 * Loads from properties files during test execution
 */
public class GlobalConfig {

    // Browser Configuration
    private static BrowserType browserType = BrowserType.CHROME;
    private static String baseUrl = "https://practicetestautomation.com";
    private static boolean headless = false;
    private static int pageLoadTimeout = 30;
    private static int implicitWait = 10;
    private static int explicitWait = 20;
    private static int pollingInterval = 500;

    // Test Execution Configuration
    private static int maxRetries = 2;
    private static boolean takeScreenshots = true;
    private static boolean capturePageSource = true;
    private static String reportPath = "test-results/";
    private static boolean parallel = false;
    private static int threadCount = 4;

    // Logging Configuration
    private static String logLevel = "INFO";

    // Database Configuration
    private static boolean dbEnabled = false;
    private static String dbUrl = "jdbc:mysql://localhost:3306/userdata";
    private static String dbUsername = "root";
    private static String dbPassword = "test1234";
    private static int dbConnectionTimeoutSeconds = 10;

    /**
     * Initialize configuration from properties file
     */
    public static void initialize() {
        try {
            String configPath = System.getProperty("configPath", "src/test/resources/application.properties");
            ConfigReader.loadProperties(configPath);

            browserType = BrowserType.fromString(ConfigReader.getProperty("browser", "chrome"));
            baseUrl = ConfigReader.getProperty("base.url", baseUrl);
            headless = Boolean.parseBoolean(ConfigReader.getProperty("headless", "false"));
            pageLoadTimeout = Integer.parseInt(ConfigReader.getProperty("page.load.timeout", "30"));
            implicitWait = Integer.parseInt(ConfigReader.getProperty("implicit.wait", "10"));
            explicitWait = Integer.parseInt(ConfigReader.getProperty("explicit.wait", "20"));
            pollingInterval = Integer.parseInt(ConfigReader.getProperty("polling.interval", "500"));

            maxRetries = Integer.parseInt(ConfigReader.getProperty("max.retries", "2"));
            takeScreenshots = Boolean.parseBoolean(ConfigReader.getProperty("take.screenshots", "true"));
            capturePageSource = Boolean.parseBoolean(ConfigReader.getProperty("capture.page.source", "true"));
            reportPath = ConfigReader.getProperty("report.path", "test-results/");
            parallel = Boolean.parseBoolean(ConfigReader.getProperty("parallel.execution", "false"));
            threadCount = Integer.parseInt(ConfigReader.getProperty("thread.count", "4"));

            logLevel = ConfigReader.getProperty("log.level", "INFO");

            dbEnabled = Boolean.parseBoolean(ConfigReader.getProperty("db.enabled", "false"));
            dbUrl = ConfigReader.getProperty("db.url", dbUrl);
            dbUsername = ConfigReader.getProperty("db.username", dbUsername);
            dbPassword = ConfigReader.getProperty("db.password", dbPassword);
            dbConnectionTimeoutSeconds = Integer.parseInt(ConfigReader.getProperty("db.connection.timeout.seconds", "10"));
        } catch (Exception e) {
            System.err.println("Warning: Could not load all configuration properties. Using defaults. " + e.getMessage());
        }
    }

    // Getters
    public static BrowserType getBrowserType() {
        return browserType;
    }

    public static String getBaseUrl() {
        return baseUrl;
    }

    public static boolean isHeadless() {
        return headless;
    }

    public static int getPageLoadTimeout() {
        return pageLoadTimeout;
    }

    public static int getImplicitWait() {
        return implicitWait;
    }

    public static int getExplicitWait() {
        return explicitWait;
    }

    public static int getPollingInterval() {
        return pollingInterval;
    }

    public static int getMaxRetries() {
        return maxRetries;
    }

    public static boolean isTakeScreenshots() {
        return takeScreenshots;
    }

    public static boolean isCapturePageSource() {
        return capturePageSource;
    }

    public static String getReportPath() {
        return reportPath;
    }

    public static boolean isParallel() {
        return parallel;
    }

    public static int getThreadCount() {
        return threadCount;
    }

    public static String getLogLevel() {
        return logLevel;
    }

    public static boolean isDbEnabled() {
        return dbEnabled;
    }

    public static String getDbUrl() {
        return dbUrl;
    }

    public static String getDbUsername() {
        return dbUsername;
    }

    public static String getDbPassword() {
        return dbPassword;
    }

    public static int getDbConnectionTimeoutSeconds() {
        return dbConnectionTimeoutSeconds;
    }

    // Setters (for runtime configuration override)
    public static void setBrowserType(BrowserType browserType) {
        GlobalConfig.browserType = browserType;
    }

    public static void setBaseUrl(String baseUrl) {
        GlobalConfig.baseUrl = baseUrl;
    }

    public static void setHeadless(boolean headless) {
        GlobalConfig.headless = headless;
    }

    public static void setExplicitWait(int seconds) {
        GlobalConfig.explicitWait = seconds;
    }
}
