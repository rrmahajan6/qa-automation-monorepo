package com.framework.tests;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import org.testng.annotations.Test;

import com.framework.base.BaseTest;
import com.framework.constants.StatusCodes;
import com.framework.core.RetryAnalyzer;
import com.framework.dataprovider.TestDataProvider;
import com.framework.models.response.ShopAddProductResponse;
import com.framework.models.response.ShopCreateOrderResponse;
import com.framework.models.response.ShopViewDetailsResponse;
import com.framework.reporting.AllureLogger;
import com.framework.services.ShopService;

import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.Story;

/**
 * E2E Shopping flow tests: Login → Add Product → Create Order → View Order → Delete Product.
 *
 * <p>Data-driven via external JSON test data.
 * <p>Uses TokenManager for automatic token management with auto-refresh.
 * <p>Thread-safe for parallel execution.
 */
@Epic("E-Commerce")
@Feature("Shopping Flow")
public class ShoppingTests extends BaseTest {

    @Test(dataProvider = "shoppingData", dataProviderClass = TestDataProvider.class,
            groups = {"regression", "e2e"}, retryAnalyzer = RetryAnalyzer.class)
    @Story("Complete shopping flow")
    @Severity(SeverityLevel.BLOCKER)
    @Description("E2E: Add product → Create order → View order details → Delete product")
    public void completeShoppingFlow(Map<String, Object> testData) {
        String productName = (String) testData.get("productName");
        String country = (String) testData.get("country");

        AllureLogger.addParameter("productName", productName);
        AllureLogger.addParameter("country", country);

        ShopService shop = new ShopService(getAuthSpec(), getShopUserId());

        // Step 1: Add product
        AllureLogger.step("Adding product: " + productName);
        ShopAddProductResponse addProductResponse = shop.addProduct(testData)
                .assertStatusCode(StatusCodes.CREATED)
                .as(ShopAddProductResponse.class);
        String productId = addProductResponse.getProductId();
        assertThat(productId).as("Product ID should not be null").isNotNull();
        log.info("Product added with ID: {}", productId);

        try {
            // Step 2: Create order
            AllureLogger.step("Creating order for product: " + productId);
            ShopCreateOrderResponse orderResponse = shop.createOrder(productId, country)
                    .assertStatusCode(StatusCodes.CREATED)
                    .as(ShopCreateOrderResponse.class);
            assertThat(orderResponse.getOrders()).as("Order IDs should not be empty").isNotEmpty();
            String orderId = orderResponse.getOrders().get(0);
            log.info("Order created with ID: {}", orderId);

            // Step 3: View order details
            AllureLogger.step("Viewing order details: " + orderId);
            ShopViewDetailsResponse viewDetails = shop.getOrderDetails(orderId)
                    .assertStatusCode(StatusCodes.OK)
                    .as(ShopViewDetailsResponse.class);
            assertThat(viewDetails.getMessage()).contains("Successfully");
            log.info("Order details retrieved: {}", viewDetails.getMessage());

        } finally {
            // Step 4: Cleanup — Delete product (always runs)
            AllureLogger.step("Cleaning up — deleting product: " + productId);
            shop.deleteProduct(productId).assertStatusCode(StatusCodes.OK);
            log.info("Product {} cleaned up successfully", productId);
        }
    }

    @Test(groups = {"smoke"}, retryAnalyzer = RetryAnalyzer.class)
    @Story("Add and delete product")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Smoke test: Login, add a product, then delete it")
    public void smokeTest_addAndDeleteProduct() {
        ShopService shop = new ShopService(getAuthSpec(), getShopUserId());

        AllureLogger.step("Adding product for smoke test");
        ShopAddProductResponse addResponse = shop.addProduct(
                "SmokeTest Product", "fashion", "clothing",
                500, "Smoke test product", "men", "testdata/dice.jpg")
                .assertStatusCode(StatusCodes.CREATED)
                .as(ShopAddProductResponse.class);
        String productId = addResponse.getProductId();
        assertThat(productId).isNotNull();

        AllureLogger.step("Deleting smoke test product: " + productId);
        shop.deleteProduct(productId).assertStatusCode(StatusCodes.OK);

        log.info("Smoke test passed — product added and deleted: {}", productId);
    }
}
