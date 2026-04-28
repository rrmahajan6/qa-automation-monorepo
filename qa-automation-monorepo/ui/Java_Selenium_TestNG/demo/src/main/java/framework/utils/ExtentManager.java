package framework.utils;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Manages the ExtentReports instance and report configuration.
 */
public class ExtentManager {

    private static final String REPORT_DIR = "test-results/extent";
    private static final String REPORT_FILE = REPORT_DIR + "/extent-report.html";

    private static ExtentReports extent;

    private ExtentManager() {
        // Prevent instantiation
    }

    public static synchronized ExtentReports getExtent() {
        if (extent == null) {
            createReportDirectory();
            ExtentSparkReporter spark = new ExtentSparkReporter(REPORT_FILE);
            spark.config().setDocumentTitle("Automation Test Report");
            spark.config().setReportName("Automation Test Suite");
            spark.config().setTheme(Theme.STANDARD);

            extent = new ExtentReports();
            extent.attachReporter(spark);
        }
        return extent;
    }

    private static void createReportDirectory() {
        try {
            Path path = Paths.get(REPORT_DIR);
            if (!Files.exists(path)) {
                Files.createDirectories(path);
            }
        } catch (IOException e) {
            LoggerUtil.error(ExtentManager.class, "Unable to create report directory: " + e.getMessage(), e);
        }
    }
}
