package framework.config;

import framework.enums.BrowserType;

/**
 * Global Configuration Class
 * Centralized place for all framework configuration
 * Loads from properties files during test execution
 */
public class GlobalConfig {

    private static String environment = "qa";

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
            String configPath = System.getProperty("configPath");
            if (configPath != null && !configPath.isBlank()) {
                ConfigReader.loadProperties(configPath);
                environment = System.getProperty("env", environment);
            } else {
                environment = readFromSystemOrEnv("env", "TEST_ENV", "qa");
                ConfigReader.loadEnvironmentProperties(environment);
            }

            browserType = BrowserType.fromString(getValue("browser", "BROWSER", "chrome"));
            baseUrl = getValue("base.url", "BASE_URL", baseUrl);
            headless = Boolean.parseBoolean(getValue("headless", "HEADLESS", "false"));
            pageLoadTimeout = Integer.parseInt(getValue("page.load.timeout", "PAGE_LOAD_TIMEOUT", "30"));
            implicitWait = Integer.parseInt(getValue("implicit.wait", "IMPLICIT_WAIT", "10"));
            explicitWait = Integer.parseInt(getValue("explicit.wait", "EXPLICIT_WAIT", "20"));
            pollingInterval = Integer.parseInt(getValue("polling.interval", "POLLING_INTERVAL", "500"));

            maxRetries = Integer.parseInt(getValue("max.retries", "MAX_RETRIES", "2"));
            takeScreenshots = Boolean.parseBoolean(getValue("take.screenshots", "TAKE_SCREENSHOTS", "true"));
            capturePageSource = Boolean.parseBoolean(getValue("capture.page.source", "CAPTURE_PAGE_SOURCE", "true"));
            reportPath = getValue("report.path", "REPORT_PATH", "test-results/");
            parallel = Boolean.parseBoolean(getValue("parallel.execution", "PARALLEL_EXECUTION", "false"));
            threadCount = Integer.parseInt(getValue("thread.count", "THREAD_COUNT", "4"));

            logLevel = getValue("log.level", "LOG_LEVEL", "INFO");

            dbEnabled = Boolean.parseBoolean(getValue("db.enabled", "DB_ENABLED", "false"));
            dbUrl = getValue("db.url", "DB_URL", dbUrl);
            dbUsername = getValue("db.username", "DB_USERNAME", dbUsername);
            dbPassword = getValue("db.password", "DB_PASSWORD", dbPassword);
            dbConnectionTimeoutSeconds = Integer.parseInt(getValue("db.connection.timeout.seconds", "DB_CONNECTION_TIMEOUT_SECONDS", "10"));
        } catch (Exception e) {
            System.err.println("Warning: Could not load all configuration properties. Using defaults. " + e.getMessage());
        }
    }

    private static String getValue(String propertyKey, String envKey, String defaultValue) {
        String systemOrEnvValue = readFromSystemOrEnv(propertyKey, envKey, null);
        if (systemOrEnvValue != null && !systemOrEnvValue.isBlank()) {
            return systemOrEnvValue;
        }
        return ConfigReader.getProperty(propertyKey, defaultValue);
    }

    private static String readFromSystemOrEnv(String systemKey, String envKey, String defaultValue) {
        String systemValue = System.getProperty(systemKey);
        if (systemValue != null && !systemValue.isBlank()) {
            return systemValue;
        }

        String envValue = System.getenv(envKey);
        if (envValue != null && !envValue.isBlank()) {
            return envValue;
        }

        return defaultValue;
    }

    // Getters
    public static BrowserType getBrowserType() {
        return browserType;
    }

    public static String getEnvironment() {
        return environment;
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
