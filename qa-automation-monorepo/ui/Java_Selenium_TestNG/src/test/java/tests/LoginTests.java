package tests;

import framework.base.BaseTest;
import framework.dataproviders.TestDataProvider;
import framework.pages.LoginPage;
import framework.pages.PageObjectManager;
import framework.utils.ExtentTestManager;
import framework.utils.LoggerUtil;
import org.testng.annotations.Test;
import java.util.Map;
import static org.testng.Assert.*;

public class LoginTests extends BaseTest {

    private static final Class<?> logger = LoginTests.class;

    @Test(
            description = "Login validation with data-driven credentials",
            dataProvider = "parallelLoginData",
            dataProviderClass = TestDataProvider.class,
            groups = {"smoke", "regression"}
    )
    public void testLoginFlow(Map<String, String> testData) {
        String username = testData.get("username");
        String password = testData.get("password");
        String expectedResult = testData.get("expectedResult");

        LoggerUtil.info(logger, "Executing login flow for user: " + username + " expected: " + expectedResult);

        PageObjectManager pages = new PageObjectManager(getDriver());
        LoginPage loginPage = pages.getLoginPage();
    
        assertTrue(loginPage.isLoginPageLoaded(), "Login page should load before entering credentials");
        
        loginPage.login(username, password);

        if ("success".equalsIgnoreCase(expectedResult)) {
            assertFalse(loginPage.isErrorMessageDisplayed(), "No login error should be shown for valid credentials");
            LoggerUtil.info(logger, "User logged in successfully");
            ExtentTestManager.getTest().info("Login test passed for user: " + username);
        } else {
            assertTrue(loginPage.isErrorMessageDisplayed(), "Error should be shown for invalid credentials");
            LoggerUtil.info(logger, "Login failed as expected for user: " + username);
            ExtentTestManager.getTest().info("Negative login test validated for user: " + username);
        }
    }
}
