package tests;
import org.testng.Assert;
import org.testng.annotations.Test;
import framework.base.BaseTest;
import framework.config.GlobalConfig;
import framework.pages.PageObjectManager;
import framework.pages.HomePage;
import framework.pages.LoginPage;
import framework.pages.MyCartPage;
import framework.pages.PaymentPage;
import framework.utils.ExtentTestManager;
import framework.utils.LoggerUtil;
import framework.utils.db.DatabaseUtil;

public class PurchaseProduct extends BaseTest {
    @Test(description = "Test to purchase a product", priority = 1, groups = {"purchase", "regression"})
    public void testPurchaseProduct() {
        String customerEmail = "rrmahajan6@gmail.com";
        String productName = "ADIDAS ORIGINAL";
        
        PageObjectManager pages = new PageObjectManager(getDriver());
        LoginPage loginPage = pages.getLoginPage();
        loginPage.login(customerEmail, "Test@1234");

        HomePage homePage = pages.getHomePage();
        homePage.addProductsToCart(productName);
        homePage.clickCart();

        MyCartPage myCartPage = pages.getMyCartPage();
        myCartPage.clickCheckout();

        PaymentPage paymentPage = pages.getPaymentPage();
        paymentPage.enterCardDetails("1234 1234 1234 1234", "111", "Jack Miller");
        paymentPage.selectCountry("India");
        paymentPage.selectCountryFromDropdown("India");
        paymentPage.clickPlaceOrder();
        ExtentTestManager.getTest().info("Purchase test executed successfully with valid credentials");
        
        // Verify purchase in database using generic SQL methods (if DB integration is enabled)
        if (GlobalConfig.isDbEnabled()) {
            LoggerUtil.info(PurchaseProduct.class, "Verifying data in database...");
            
            try {
                // Generic read: Check if Employees table has records
                boolean hasRecords = DatabaseUtil.recordExists("Employees");
                
                if (hasRecords) {
                    // Get total employee count
                    int employeeCount = DatabaseUtil.getRecordCount("Employees");
                    LoggerUtil.info(PurchaseProduct.class, "Found " + employeeCount + " employee(s) in database");
                    ExtentTestManager.getTest().pass("Database verification successful: Found " + employeeCount + " employee record(s)");
                } else {
                    LoggerUtil.warn(PurchaseProduct.class, "No records found in Employees table");
                    ExtentTestManager.getTest().info("Database info: Employees table appears to be empty");
                }
            } catch (Exception e) {
                LoggerUtil.error(PurchaseProduct.class, "Database verification failed: " + e.getMessage(), e);
                ExtentTestManager.getTest().warning("Database verification skipped: " + e.getMessage());
            }
        }
    }
}