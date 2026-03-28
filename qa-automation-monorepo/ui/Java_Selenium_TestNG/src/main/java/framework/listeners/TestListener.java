package framework.listeners;

import com.aventstack.extentreports.MediaEntityBuilder;
import com.aventstack.extentreports.Status;
import framework.base.DriverManager;
import framework.utils.ExtentManager;
import framework.utils.ExtentTestManager;
import framework.utils.LoggerUtil;
import framework.utils.ScreenshotUtil;
import org.testng.IAnnotationTransformer;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;
import org.testng.annotations.ITestAnnotation;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

/**
 * TestNG Listener for Test Execution Events
 * Handles logging, screenshots, and reporting
 * Also enables automatic retry for flaky tests
 */
public class TestListener implements ITestListener, IAnnotationTransformer {

    private static final Class<?> logger = TestListener.class;

    /**
     * Called when test starts
     */
    @Override
    public void onTestStart(ITestResult result) {
        // Initialize Extent report for this test only if it does not already exist.
        if (ExtentTestManager.getTest() == null) {
            ExtentTestManager.createTest(result.getMethod().getMethodName());
        }
        ExtentTestManager.getTest().assignCategory(result.getTestClass().getRealClass().getSimpleName());

        LoggerUtil.info(logger, "");
        LoggerUtil.info(logger, "========== TEST STARTED: " + result.getMethod().getMethodName() + " ==========");
        LoggerUtil.info(logger, "Test Class: " + result.getTestClass().getRealClass().getSimpleName());
    }

    /**
     * Called when test passes
     */
    @Override
    public void onTestSuccess(ITestResult result) {
        ExtentTestManager.getTest().log(Status.PASS, "Test passed");
        LoggerUtil.info(logger, "✓ TEST PASSED: " + result.getMethod().getMethodName());
        LoggerUtil.info(logger, "Execution Time: " + (result.getEndMillis() - result.getStartMillis()) + "ms");
        LoggerUtil.info(logger, "=".repeat(80));
    }

    /**
     * Called when test fails
     */
    @Override
    public void onTestFailure(ITestResult result) {
        ExtentTestManager.getTest().log(Status.FAIL, "Test failed");

        LoggerUtil.error(logger, "✗ TEST FAILED: " + result.getMethod().getMethodName());

        // Attach screenshot to the report
        try {
            if (DriverManager.isDriverInitialized()) {
                String screenshotPath = ScreenshotUtil.takeScreenshot(
                        DriverManager.getDriver(),
                        result.getMethod().getMethodName());
                if (screenshotPath != null) {
                    ExtentTestManager.getTest().fail("Screenshot:",
                            MediaEntityBuilder.createScreenCaptureFromPath(screenshotPath).build());
                }
            }
        } catch (Exception e) {
            LoggerUtil.warn(logger, "Failed to capture screenshot on failure: " + e.getMessage());
        }

        // Log failure reason
        if (result.getThrowable() != null) {
            ExtentTestManager.getTest().fail(result.getThrowable());
            LoggerUtil.error(logger, "Failure Reason: " + result.getThrowable().getMessage());
            LoggerUtil.error(logger, "Stack Trace: " + getStackTrace(result.getThrowable()));
        }

        LoggerUtil.info(logger, "Execution Time: " + (result.getEndMillis() - result.getStartMillis()) + "ms");
        LoggerUtil.info(logger, "=".repeat(80));
    }

    /**
     * Called when test is skipped
     */
    @Override
    public void onTestSkipped(ITestResult result) {
        ExtentTestManager.getTest().log(Status.SKIP, "Test skipped");
        LoggerUtil.warn(logger, "⊘ TEST SKIPPED: " + result.getMethod().getMethodName());
        if (result.getThrowable() != null) {
            ExtentTestManager.getTest().skip(result.getThrowable());
            LoggerUtil.warn(logger, "Reason: " + result.getThrowable().getMessage());
        }
        LoggerUtil.info(logger, "=".repeat(80));
    }

    /**
     * Called when test fails but within success percentage
     */
    @Override
    public void onTestFailedButWithinSuccessPercentage(ITestResult result) {
        ExtentTestManager.getTest().log(Status.WARNING, "Test failed but within success percentage");
        LoggerUtil.warn(logger, "⚠ TEST FAILED BUT WITHIN SUCCESS %: " + result.getMethod().getMethodName());
    }

    /**
     * Called when test suite starts
     */
    @Override
    public void onStart(ITestContext context) {
        // Initialize Extent report once per suite
        ExtentManager.getExtent();

        LoggerUtil.info(logger, "");
        LoggerUtil.info(logger, "#".repeat(80));
        LoggerUtil.info(logger, "TEST SUITE STARTED: " + context.getSuite().getName());
        LoggerUtil.info(logger, "Total Tests: " + context.getAllTestMethods().length);
        LoggerUtil.info(logger, "#".repeat(80));
        LoggerUtil.info(logger, "");
    }

    /**
     * Called when test suite finishes
     */
    @Override
    public void onFinish(ITestContext context) {
        LoggerUtil.info(logger, "");
        LoggerUtil.info(logger, "#".repeat(80));
        LoggerUtil.info(logger, "TEST SUITE COMPLETED: " + context.getSuite().getName());
        LoggerUtil.info(logger, "Passed Tests: " + context.getPassedTests().size());
        LoggerUtil.info(logger, "Failed Tests: " + context.getFailedTests().size());
        LoggerUtil.info(logger, "Skipped Tests: " + context.getSkippedTests().size());
        LoggerUtil.info(logger, "Total Duration: " + (context.getEndDate().getTime() - context.getStartDate().getTime()) + "ms");
        LoggerUtil.info(logger, "#".repeat(80));
        LoggerUtil.info(logger, "");

        // Flush Extent report to disk
        ExtentManager.getExtent().flush();
    }

    /**
     * Helper method to get stack trace as string
     */
    private String getStackTrace(Throwable throwable) {
        StringBuilder sb = new StringBuilder();
        for (StackTraceElement element : throwable.getStackTrace()) {
            sb.append(element.toString()).append("\n");
        }
        return sb.toString();
    }

    /**
     * Automatically attach RetryAnalyzer to all test methods
     * This enables automatic retry of failed tests
     */
    @Override
    @SuppressWarnings({"rawtypes", "unchecked"})
    public void transform(ITestAnnotation annotation, Class testClass, Constructor testConstructor, Method testMethod) {
        // Set retry analyzer for all test methods
        annotation.setRetryAnalyzer(RetryAnalyzer.class);
    }
}
