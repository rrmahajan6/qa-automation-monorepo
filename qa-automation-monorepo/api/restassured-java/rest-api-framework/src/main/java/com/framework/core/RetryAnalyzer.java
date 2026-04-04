package com.framework.core;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.IRetryAnalyzer;
import org.testng.ITestResult;

import com.framework.config.ConfigManager;

/**
 * Auto-retry analyzer for flaky tests.
 * Retry count is driven by config property {@code retry.count}.
 *
 * Usage on individual tests:
 * <pre>
 *   @Test(retryAnalyzer = RetryAnalyzer.class)
 * </pre>
 *
 * Or register globally via TestListener.
 */
public class RetryAnalyzer implements IRetryAnalyzer {

    private static final Logger log = LogManager.getLogger(RetryAnalyzer.class);
    private int attempt = 0;

    @Override
    public boolean retry(ITestResult result) {
        int maxRetry = ConfigManager.get().retryCount();
        if (attempt < maxRetry) {
            attempt++;
            log.warn("Retrying test '{}' — attempt {}/{}", result.getName(), attempt, maxRetry);
            return true;
        }
        return false;
    }
}
