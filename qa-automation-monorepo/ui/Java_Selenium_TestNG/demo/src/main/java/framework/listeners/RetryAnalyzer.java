package framework.listeners;

import framework.config.GlobalConfig;
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

    /**
     * Analyze test result and determine if retry is needed
     */
    @Override
    public boolean retry(ITestResult result) {
        int maxRetryCount = GlobalConfig.getMaxRetries();
        if (retryCount < maxRetryCount) {
            LoggerUtil.warn(RetryAnalyzer.class, 
                "Retrying test: " + result.getMethod().getMethodName() + 
                " (Attempt " + (retryCount + 1) + "/" + maxRetryCount + ")");
            
            retryCount++;

            return true;
        }

        return false;
    }
}
