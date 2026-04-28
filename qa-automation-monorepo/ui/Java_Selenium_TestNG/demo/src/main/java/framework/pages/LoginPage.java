package framework.pages;

import framework.base.BasePage;
import framework.utils.LoggerUtil;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;


public class LoginPage extends BasePage {

    private static final By USERNAME_INPUT = By.id("userEmail");
    private static final By PASSWORD_INPUT = By.id("userPassword");
    private static final By LOGIN_BUTTON = By.id("login");
    private static final By ERROR_MESSAGE = By.id("error");
    private static final By FORGOT_PASSWORD_LINK = By.id("forgotPassword");
    private static final By SIGN_UP_LINK = By.id("signUp");

    private static final Class<?> logger = LoginPage.class;

    public LoginPage(WebDriver driver) {
        super(driver);
        LoggerUtil.info(logger, "Initializing LoginPage");
    }

    public String getLoginPageTitle() {
        LoggerUtil.info(logger, "Getting login page title");
        return getPageTitle();
    }

    public String getLoginPageURL() {
        LoggerUtil.info(logger, "Getting login page URL");
        return getCurrentUrl();
    }

    public boolean isLoginPageLoaded() {
        LoggerUtil.info(logger, "Verifying login page is loaded");
        return isElementVisible(USERNAME_INPUT) && isElementVisible(PASSWORD_INPUT);
    }

    /**
     * Enter username
     * @param username username to enter
     */
    public void enterUsername(String username) {
        LoggerUtil.info(logger, "Entering username: " + username);
        type(USERNAME_INPUT, username);
    }

    /**
     * Enter password
     * @param password password to enter
     */
    public void enterPassword(String password) {
        LoggerUtil.info(logger, "Entering password (hidden for security)");
        type(PASSWORD_INPUT, password);
    }

    /**
     * Click login button
     */
    public void clickLoginButton() {
        LoggerUtil.info(logger, "Clicking login button");
        click(LOGIN_BUTTON);
    }

    /**
     * Perform login action
     * @param username username
     * @param password password
     */
    public void login(String username, String password) {
        LoggerUtil.info(logger, "Performing login with username: " + username);
        enterUsername(username);
        enterPassword(password);
        clickLoginButton();
    }

    /**
     * Check if error message is displayed
     * @return true if error message is visible
     */
    public boolean isErrorMessageDisplayed() {
        LoggerUtil.debug(logger, "Checking if error message is displayed");
        return isElementVisible(ERROR_MESSAGE);
    }

    /**
     * Get error message text
     * @return error message text
     */
    public String getErrorMessage() {
        LoggerUtil.info(logger, "Getting error message");
        return getText(ERROR_MESSAGE);
    }

    /**
     * Click forgot password link
     */
    public void clickForgotPasswordLink() {
        LoggerUtil.info(logger, "Clicking forgot password link");
        click(FORGOT_PASSWORD_LINK);
    }

    /**
     * Click sign up link
     */
    public void clickSignUpLink() {
        LoggerUtil.info(logger, "Clicking sign up link");
        click(SIGN_UP_LINK);
    }

    /**
     * Clear all login form fields
     */
    public void clearLoginForm() {
        LoggerUtil.info(logger, "Clearing login form");
        findElement(USERNAME_INPUT).clear();
        findElement(PASSWORD_INPUT).clear();
    }

    /**
     * Get username field value
     * @return username value
     */
    public String getUsernameFieldValue() {
        LoggerUtil.debug(logger, "Getting username field value");
        return getAttribute(USERNAME_INPUT, "value");
    }

    /**
     * Check if login button is enabled
     * @return true if login button is enabled
     */
    public boolean isLoginButtonEnabled() {
        LoggerUtil.debug(logger, "Checking if login button is enabled");
        return isElementEnabled(LOGIN_BUTTON);
    }
}
