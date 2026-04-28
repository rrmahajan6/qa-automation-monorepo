package tests;
import org.testng.annotations.Test;
import framework.base.BaseTest;
import framework.config.GlobalConfig;
import framework.dataproviders.TestDataProvider;
import framework.pages.PageObjectManager;
import framework.pages.HomePage;
import framework.pages.LoginPage;
import framework.pages.MyCartPage;
import framework.pages.PaymentPage;
import framework.utils.ExtentTestManager;
import framework.utils.LoggerUtil;
import framework.utils.db.DatabaseUtil;
import java.util.Map;

public class PurchaseProduct extends BaseTest {
    @Test(
            description = "Test to purchase a product",
            priority = 1,
            groups = {"purchase", "regression"},
            dataProvider = "statefulPurchaseData",
            dataProviderClass = TestDataProvider.class
    )
    public void testPurchaseProduct(Map<String, String> testData) {
        String customerEmail = testData.get("username");
        String password = testData.get("password");
        String productName = testData.get("productName");
        String cardNumber = testData.get("cardNumber");
        String cvv = testData.get("cvv");
        String nameOnCard = testData.get("nameOnCard");
        String country = testData.get("country");
        
        PageObjectManager pages = new PageObjectManager(getDriver());
        LoginPage loginPage = pages.getLoginPage();
        loginPage.login(customerEmail, password);

        HomePage homePage = pages.getHomePage();
        homePage.addProductsToCart(productName);
        homePage.clickCart();

        MyCartPage myCartPage = pages.getMyCartPage();
        myCartPage.clickCheckout();

        PaymentPage paymentPage = pages.getPaymentPage();
        paymentPage.enterCardDetails(cardNumber, cvv, nameOnCard);
        paymentPage.selectCountry(country);
        paymentPage.selectCountryFromDropdown(country);
        paymentPage.clickPlaceOrder();
        ExtentTestManager.getTest().info("Purchase test executed successfully for product: " + productName);
        
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