package com.framework.base;

import com.framework.config.ConfigManager;
import com.framework.utils.SpecBuilder;
import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.RestAssured;
import io.restassured.specification.RequestSpecification;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;

import java.lang.reflect.Method;

/**
 * Base class for all API test classes.
 *
 * Responsibilities:
 *  - Initialise REST Assured global config once per suite
 *  - Provide pre-built RequestSpecifications to sub-classes
 *  - Log test start / end / result
 *  - Capture response body on failure for Allure attachment
 */
public abstract class BaseTest {

    protected final Logger log = LogManager.getLogger(getClass());

    protected RequestSpecification requestSpec;
    protected RequestSpecification authRequestSpec;

    @BeforeSuite(alwaysRun = true)
    public void globalSetup() {
        log.info("=== Suite started — environment: {} | base URL: {} ===",
                System.getProperty("env", "dev"),
                ConfigManager.get().baseUrl());

        RestAssured.baseURI = ConfigManager.get().baseUrl();
        RestAssured.useRelaxedHTTPSValidation();
        RestAssured.filters(new AllureRestAssured());
    }

    @BeforeMethod(alwaysRun = true)
    public void setUp(Method method) {
        log.info(">>> TEST START: {}", method.getName());
        requestSpec     = SpecBuilder.defaultSpec();
        authRequestSpec = SpecBuilder.authSpec();
    }

    @AfterMethod(alwaysRun = true)
    public void tearDown(ITestResult result) {
        String status = switch (result.getStatus()) {
            case ITestResult.SUCCESS -> "PASSED";
            case ITestResult.FAILURE -> "FAILED";
            case ITestResult.SKIP    -> "SKIPPED";
            default                  -> "UNKNOWN";
        };
        log.info("<<< TEST {}: {}\n", status, result.getName());
    }

    @AfterSuite(alwaysRun = true)
    public void globalTearDown() {
        log.info("=== Suite finished ===");
    }
}
