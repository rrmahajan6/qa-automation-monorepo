package com.framework.listeners;

import io.qameta.allure.Allure;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.*;

import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;

/**
 * TestNG listener that integrates with Allure and provides rich test lifecycle logging.
 *
 * Register in testng.xml:
 * <listeners>
 *   <listener class-name="com.framework.listeners.TestListener"/>
 * </listeners>
 */
public class TestListener implements ITestListener, ISuiteListener {

    private static final Logger log = LogManager.getLogger(TestListener.class);

    // ─── ISuiteListener ───────────────────────────────────────────────────

    @Override
    public void onStart(ISuite suite) {
        log.info("╔══════════════════════════════════════════");
        log.info("║  SUITE: {}", suite.getName());
        log.info("╚══════════════════════════════════════════");
    }

    @Override
    public void onFinish(ISuite suite) {
        log.info("╔══════════════════════════════════════════");
        log.info("║  SUITE COMPLETE: {}", suite.getName());
        log.info("╚══════════════════════════════════════════");
    }

    // ─── ITestListener ────────────────────────────────────────────────────

    @Override
    public void onTestStart(ITestResult result) {
        log.info("▶  {}", fullName(result));
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        log.info("✔  PASSED  — {}", fullName(result));
    }

    @Override
    public void onTestFailure(ITestResult result) {
        log.error("✘  FAILED  — {}", fullName(result));
        Throwable throwable = result.getThrowable();
        if (throwable != null) {
            log.error("   Cause: {}", throwable.getMessage());
            java.io.StringWriter sw = new java.io.StringWriter();
            throwable.printStackTrace(new java.io.PrintWriter(sw));
            Allure.addAttachment("Failure Stack Trace", "text/plain",
                    new ByteArrayInputStream(sw.toString().getBytes(StandardCharsets.UTF_8)), ".txt");
        }
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        log.warn("⤳  SKIPPED — {}", fullName(result));
    }

    @Override
    public void onTestFailedButWithinSuccessPercentage(ITestResult result) {
        log.warn("~  FLAKY   — {}", fullName(result));
    }

    private String fullName(ITestResult result) {
        return result.getTestClass().getName() + "#" + result.getName();
    }
}
