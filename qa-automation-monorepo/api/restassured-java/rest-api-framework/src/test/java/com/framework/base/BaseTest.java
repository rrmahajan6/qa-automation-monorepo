package com.framework.base;

import java.lang.reflect.Method;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;

import com.framework.config.ConfigManager;
import com.framework.core.TokenManager;
import com.framework.utils.SpecBuilder;

import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.RestAssured;
import io.restassured.specification.RequestSpecification;

/**
 * Base class for all API test classes.
 *
 * Responsibilities:
 *  - Initialise REST Assured global config once per suite
 *  - Provide pre-built RequestSpecifications to sub-classes
 *  - Integrate TokenManager for authenticated tests
 *  - Log test start / end / result
 */
public abstract class BaseTest {

    protected final Logger log = LogManager.getLogger(getClass());

    protected RequestSpecification requestSpec;
    protected RequestSpecification authRequestSpec;

    @BeforeSuite(alwaysRun = true)
    public void globalSetup() {
        String env = System.getProperty("env", "dev");
        log.info("=== Suite started — environment: {} | base URL: {} ===",
                env, ConfigManager.get().baseUrl());

        RestAssured.baseURI = ConfigManager.get().baseUrl();
        RestAssured.useRelaxedHTTPSValidation();
        RestAssured.filters(new AllureRestAssured());
    }

    @BeforeMethod(alwaysRun = true)
    public void setUp(Method method) {
        log.info(">>> TEST START: {}.{}", getClass().getSimpleName(), method.getName());
        requestSpec = SpecBuilder.defaultSpec();
    }

    /**
     * Get an authenticated request spec using TokenManager.
     * Lazily fetches / refreshes token for the default shop user.
     */
    protected RequestSpecification getAuthSpec() {
        String token = TokenManager.getInstance().getToken();
        return SpecBuilder.shopAuthSpec(token);
    }

    /**
     * Get an authenticated request spec for a specific user.
     */
    protected RequestSpecification getAuthSpec(String email, String password) {
        String token = TokenManager.getInstance().getToken(email, password);
        return SpecBuilder.shopAuthSpec(token);
    }

    /**
     * Get the userId for the default shop user (requires prior getToken/getAuthSpec call).
     */
    protected String getShopUserId() {
        return TokenManager.getInstance().getUserId();
    }

    /**
     * Get the userId for a specific user.
     */
    protected String getShopUserId(String email) {
        return TokenManager.getInstance().getUserId(email);
    }

    @AfterMethod(alwaysRun = true)
    public void tearDown(ITestResult result) {
        String status = switch (result.getStatus()) {
            case ITestResult.SUCCESS -> "PASSED";
            case ITestResult.FAILURE -> "FAILED";
            case ITestResult.SKIP    -> "SKIPPED";
            default                  -> "UNKNOWN";
        };
        log.info("<<< TEST {}: {}.{}\n", status, getClass().getSimpleName(), result.getName());
    }

    @AfterSuite(alwaysRun = true)
    public void globalTearDown() {
        TokenManager.getInstance().clearAll();
        log.info("=== Suite finished ===");
    }
}
