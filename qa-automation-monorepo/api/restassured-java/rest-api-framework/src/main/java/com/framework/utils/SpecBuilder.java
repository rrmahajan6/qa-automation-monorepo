package com.framework.utils;

import java.util.Map;

import com.framework.config.ConfigManager;

import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;

/**
 * Factory for reusable REST Assured RequestSpecifications.
 *
 * <p>Centralises base URL, headers, auth, logging and Allure filters so
 * individual tests stay concise and DRY.
 *
 * Usage:
 * <pre>
 *   RequestSpecification spec = SpecBuilder.defaultSpec();
 *   given().spec(spec).get("/users/1").then().statusCode(200);
 * </pre>
 */
public final class SpecBuilder {

    private SpecBuilder() {}

    // ─── Base spec ────────────────────────────────────────────────────────

    /**
     * Standard spec: base URL, JSON content type, Allure filter, logging.
     */
    public static RequestSpecification defaultSpec() {
        return new RequestSpecBuilder()
                .setBaseUri(ConfigManager.get().baseUrl())
                .setContentType(ContentType.JSON)
                .setAccept(ContentType.JSON)
                .addFilter(new AllureRestAssured())            // Attach req/resp to Allure report
                .addFilter(new RequestLoggingFilter(LogDetail.ALL))
                .addFilter(new ResponseLoggingFilter(LogDetail.ALL))
                .build();
    }

    /**
     * Spec with Bearer token authentication.
     */
    public static RequestSpecification authSpec(String bearerToken) {
        return new RequestSpecBuilder()
                .addRequestSpecification(defaultSpec())
                .addHeader("Authorization", "Bearer " + bearerToken)
                .build();
    }

    /**
     * Spec with Bearer token from config.
     */
    public static RequestSpecification authSpec() {
        return authSpec(ConfigManager.get().authToken());
    }

    /**
     * Spec for the Shop API with token-based auth (token in Authorization header, no "Bearer" prefix).
     */
    public static RequestSpecification shopAuthSpec(String token) {
        return new RequestSpecBuilder()
                .setBaseUri(ConfigManager.get().shopBaseUrl())
                .addHeader("Authorization", token)
                .addFilter(new AllureRestAssured())
                .addFilter(new RequestLoggingFilter(LogDetail.ALL))
                .addFilter(new ResponseLoggingFilter(LogDetail.ALL))
                .build();
    }

    /**
     * Spec for the Places API (with API key).
     */
    public static RequestSpecification placesSpec() {
        return new RequestSpecBuilder()
                .setBaseUri(ConfigManager.get().baseUrl())
                .setContentType(ContentType.JSON)
                .addQueryParam("key", ConfigManager.get().placesApiKey())
                .addFilter(new AllureRestAssured())
                .addFilter(new RequestLoggingFilter(LogDetail.ALL))
                .addFilter(new ResponseLoggingFilter(LogDetail.ALL))
                .build();
    }

    /**
     * Spec with Basic authentication.
     */
    public static RequestSpecification basicAuthSpec() {
        return new RequestSpecBuilder()
                .addRequestSpecification(defaultSpec())
                .setAuth(io.restassured.RestAssured.basic(
                        ConfigManager.get().authUsername(),
                        ConfigManager.get().authPassword()))
                .build();
    }

    /**
     * Spec with custom headers merged on top of the default spec.
     */
    public static RequestSpecification withHeaders(Map<String, String> headers) {
        return new RequestSpecBuilder()
                .addRequestSpecification(defaultSpec())
                .addHeaders(headers)
                .build();
    }

    /**
     * Minimal spec — no logging (useful for performance-sensitive health checks).
     */
    public static RequestSpecification minimalSpec() {
        return new RequestSpecBuilder()
                .setBaseUri(ConfigManager.get().baseUrl())
                .setContentType(ContentType.JSON)
                .setAccept(ContentType.JSON)
                .addFilter(new AllureRestAssured())
                .build();
    }
}
