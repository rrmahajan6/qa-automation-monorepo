package framework.listeners;

import framework.base.DriverManager;
import framework.utils.LoggerUtil;
import org.testng.IRetryAnalyzer;
import org.testng.ITestResult;

/**
 * Test Retry Analyzer
 * Automatically retries failed tests based on configuration
 * Useful for handling flaky tests
 */
public class RetryAnalyzer implements IRetryAnalyzer {

    private int retryCount = 0;
    private int maxRetryCount = 2;

    /**
     * Analyze test result and determine if retry is needed
     */
    @Override
    public boolean retry(ITestResult result) {
        if (result.getThrowable() instanceof AssertionError) {
            // Don't retry assertion failures
            return false;
        }

        if (retryCount < maxRetryCount) {
            LoggerUtil.warn(RetryAnalyzer.class, 
                "Retrying test: " + result.getMethod().getMethodName() + 
                " (Attempt " + (retryCount + 1) + "/" + maxRetryCount + ")");
            
            retryCount++;
            
            // Restart driver for fresh state
            try {
                DriverManager.restartDriver();
            } catch (Exception e) {
                LoggerUtil.error(RetryAnalyzer.class, "Error restarting driver: " + e.getMessage());
            }
            
            return true;
        }

        return false;
    }

    /**
     * Set max retry count
     */
    public void setMaxRetryCount(int maxRetryCount) {
        this.maxRetryCount = maxRetryCount;
    }
}
