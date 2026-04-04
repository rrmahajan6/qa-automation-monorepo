package com.framework.services;

import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.List;
import java.util.Map;

import com.framework.constants.APIEndpoints;
import com.framework.models.request.ShopCreateOrder;
import com.framework.utils.RequestFactory;
import com.framework.utils.RequestFactory.HttpMethod;
import com.framework.utils.ResponseHelper;

import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;

/**
 * Service layer for Shop / E-Commerce API.
 *
 * <p>Each method builds the request and returns {@link ResponseHelper} —
 * tests own all assertions and deserialization.
 */
public class ShopService {

    private final RequestSpecification authSpec;
    private final String userId;

    public ShopService(RequestSpecification authSpec, String userId) {
        this.authSpec = authSpec;
        this.userId = userId;
    }

    // ─── Products ─────────────────────────────────────────────────────────

    public ResponseHelper addProduct(Map<String, Object> productData) {
        return addProduct(
                (String) productData.get("productName"),
                (String) productData.get("productCategory"),
                (String) productData.get("productSubCategory"),
                ((Number) productData.get("productPrice")).intValue(),
                (String) productData.get("productDescription"),
                (String) productData.get("productFor"),
                "testdata/dice.jpg"
        );
    }

    public ResponseHelper addProduct(String name, String category, String subCategory,
                                     int price, String description, String productFor,
                                     String imageResourcePath) {
        return buildRequest()
                .withContentType("multipart/form-data")
                .withParam("productName", name)
                .withParam("productAddedBy", userId)
                .withParam("productCategory", category)
                .withParam("productSubCategory", subCategory)
                .withParam("productPrice", price)
                .withParam("productDescription", description)
                .withParam("productFor", productFor)
                .withMultiPart("productImage", loadResource(imageResourcePath))
                .execute(HttpMethod.POST, APIEndpoints.SHOP_ADD_PRODUCT);
    }

    public ResponseHelper deleteProduct(String productId) {
        return buildJsonRequest()
                .withPathParam("productId", productId)
                .execute(HttpMethod.DELETE, APIEndpoints.SHOP_DELETE_PRODUCT);
    }

    // ─── Orders ───────────────────────────────────────────────────────────

    public ResponseHelper createOrder(String productId, String country) {
        ShopCreateOrder.Order order = new ShopCreateOrder.Order();
        order.setCountry(country);
        order.setProductOrderedId(productId);

        ShopCreateOrder payload = new ShopCreateOrder();
        payload.setOrders(List.of(order));

        return buildJsonRequest()
                .withBody(payload)
                .execute(HttpMethod.POST, APIEndpoints.SHOP_CREATE_ORDER);
    }

    public ResponseHelper getOrderDetails(String orderId) {
        return buildJsonRequest()
                .withQueryParam("id", orderId)
                .execute(HttpMethod.GET, APIEndpoints.SHOP_ORDER_DETAILS);
    }

    // ─── Internal ─────────────────────────────────────────────────────────

    private RequestFactory buildRequest() {
        return RequestFactory.request().withSpec(authSpec);
    }

    private RequestFactory buildJsonRequest() {
        return buildRequest().withContentType(ContentType.JSON);
    }

    private static File loadResource(String resourcePath) {
        URL url = Thread.currentThread().getContextClassLoader().getResource(resourcePath);
        if (url == null) {
            throw new IllegalArgumentException("Resource not found: " + resourcePath);
        }
        try {
            return new File(url.toURI());
        } catch (URISyntaxException e) {
            throw new RuntimeException("Invalid resource URI: " + resourcePath, e);
        }
    }
}
