package framework.pages;

import org.openqa.selenium.WebDriver;

/**
 * PageObjectManager
 *
 * Centralized manager to provide page object instances.
 * Ensures a single instance per page type per test session.
 */
public class PageObjectManager {

    private final WebDriver driver;

    private LoginPage loginPage;
    private HomePage homePage;
    private MyCartPage myCartPage;
    private PaymentPage paymentPage;

    public PageObjectManager(WebDriver driver) {
        this.driver = driver;
    }

    public LoginPage getLoginPage() {
        if (loginPage == null) {
            loginPage = new LoginPage(driver);
        }
        return loginPage;
    }

    public HomePage getHomePage() {
        if (homePage == null) {
            homePage = new HomePage(driver);
        }
        return homePage;
    }

    public MyCartPage getMyCartPage() {
        if (myCartPage == null) {
            myCartPage = new MyCartPage(driver);
        }
        return myCartPage;
    }

    public PaymentPage getPaymentPage() {
        if (paymentPage == null) {
            paymentPage = new PaymentPage(driver);
        }
        return paymentPage;
    }
}
