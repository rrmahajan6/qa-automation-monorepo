package tests;

import framework.base.BaseTest;
import framework.pages.LoginPage;
import framework.pages.PageObjectManager;
import framework.utils.ExtentTestManager;
import framework.utils.LoggerUtil;
import org.testng.annotations.Test;
import static org.testng.Assert.*;

public class LoginTests extends BaseTest {

    private static final Class<?> logger = LoginTests.class;

    @Test(description = "Successful login with valid credentials1")
    public void testSuccessfulLogin() {
        LoggerUtil.info(logger, "Test: Successful login with valid credentials");

        PageObjectManager pages = new PageObjectManager(getDriver());
        LoginPage loginPage = pages.getLoginPage();
    
        assertTrue(loginPage.isLoginPageLoaded(), "Login page should load before entering credentials");
        
        loginPage.login("rrmahajan6@gmail.com", "Test@1234");
              
        LoggerUtil.info(logger, "User logged in successfully");
        ExtentTestManager.getTest().info("Login test executed successfully with valid credentials");
    }
}
