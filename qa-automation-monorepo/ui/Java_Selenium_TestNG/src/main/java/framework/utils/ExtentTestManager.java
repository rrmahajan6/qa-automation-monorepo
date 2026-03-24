package framework.utils;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;

/**
 * Manages the ExtentTest instance for each executing thread.
 */
public class ExtentTestManager {

    private static final ThreadLocal<ExtentTest> extentTest = new ThreadLocal<>();

    public static ExtentTest getTest() {
        return extentTest.get();
    }

    public static void setTest(ExtentTest test) {
        extentTest.set(test);
    }

    public static void removeTest() {
        extentTest.remove();
    }

    public static ExtentTest createTest(String testName) {
        ExtentReports extent = ExtentManager.getExtent();
        ExtentTest test = extent.createTest(testName);
        setTest(test);
        return test;
    }
}
