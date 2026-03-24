package com.framework.utils;

import com.framework.config.ConfigManager;
import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Map;

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

    private static final Logger log = LogManager.getLogger(SpecBuilder.class);

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
