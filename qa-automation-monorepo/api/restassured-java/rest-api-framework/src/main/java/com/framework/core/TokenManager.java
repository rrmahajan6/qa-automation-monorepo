package com.framework.core;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.framework.config.ConfigManager;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;

/**
 * Thread-safe Token Manager with automatic refresh on expiry.
 *
 * <p>Supports multiple tokens keyed by identifier (e.g. user email).
 * Automatically detects JWT expiry and refreshes before use.
 *
 * <p>Usage:
 * <pre>
 *   String token = TokenManager.getInstance().getToken("user@example.com");
 * </pre>
 */
public final class TokenManager {

    private static final Logger log = LogManager.getLogger(TokenManager.class);
    private static volatile TokenManager instance;

    private final ConcurrentHashMap<String, TokenInfo> tokenStore = new ConcurrentHashMap<>();

    private TokenManager() {}

    public static TokenManager getInstance() {
        if (instance == null) {
            synchronized (TokenManager.class) {
                if (instance == null) {
                    instance = new TokenManager();
                }
            }
        }
        return instance;
    }

    /**
     * Get a valid token for the default shop user configured in properties.
     * Auto-refreshes if expired.
     */
    public synchronized String getToken() {
        String email = ConfigManager.get().shopUserEmail();
        return getToken(email, ConfigManager.get().shopUserPassword());
    }

    /**
     * Get a valid token for a specific user. Auto-refreshes if expired.
     */
    public synchronized String getToken(String email, String password) {
        TokenInfo info = tokenStore.get(email);
        if (info != null && !isTokenExpired(info.token)) {
            log.debug("Reusing cached token for: {}", email);
            return info.token;
        }

        log.info("Token missing or expired for: {} — requesting new token", email);
        return refreshToken(email, password);
    }

    /**
     * Force refresh token for a user.
     */
    public synchronized String refreshToken(String email, String password) {
        String baseUrl = ConfigManager.get().shopBaseUrl();
        log.info("Requesting new token from: {}/api/ecom/auth/login for user: {}", baseUrl, email);

        Response response = RestAssured.given()
                .baseUri(baseUrl)
                .contentType(ContentType.JSON)
                .body(Map.of("userEmail", email, "userPassword", password))
                .when()
                .post("/api/ecom/auth/login")
                .then()
                .extract().response();

        if (response.getStatusCode() != 200) {
            throw new RuntimeException("Login failed for " + email
                    + ". Status: " + response.getStatusCode()
                    + " Body: " + response.getBody().asString());
        }

        String token = response.jsonPath().getString("token");
        String userId = response.jsonPath().getString("userId");

        tokenStore.put(email, new TokenInfo(token, userId, System.currentTimeMillis()));
        log.info("Token refreshed successfully for: {} (userId: {})", email, userId);
        return token;
    }

    /**
     * Get the userId associated with the last token fetch for a user.
     */
    public String getUserId(String email) {
        TokenInfo info = tokenStore.get(email);
        if (info == null) {
            throw new IllegalStateException("No token found for: " + email + ". Call getToken() first.");
        }
        return info.userId;
    }

    /**
     * Get userId for the default shop user.
     */
    public String getUserId() {
        return getUserId(ConfigManager.get().shopUserEmail());
    }

    /**
     * Check if a JWT token is expired (or about to expire within buffer).
     */
    public boolean isTokenExpired(String token) {
        if (token == null || token.isBlank()) {
            return true;
        }
        try {
            String[] parts = token.split("\\.");
            if (parts.length < 2) {
                return true;
            }
            String payload = new String(Base64.getUrlDecoder().decode(parts[1]), StandardCharsets.UTF_8);
            ObjectMapper mapper = new ObjectMapper();
            Map<String, Object> claims = mapper.readValue(payload, mapper.getTypeFactory()
                    .constructMapType(Map.class, String.class, Object.class));

            Object expObj = claims.get("exp");
            if (expObj == null) {
                return false; // No expiry claim — treat as valid
            }

            long expEpochSeconds = ((Number) expObj).longValue();
            int bufferSeconds = ConfigManager.get().tokenExpiryBufferSeconds();
            long nowEpochSeconds = System.currentTimeMillis() / 1000;

            boolean expired = nowEpochSeconds >= (expEpochSeconds - bufferSeconds);
            if (expired) {
                log.debug("Token expired: exp={}, now={}, buffer={}s", expEpochSeconds, nowEpochSeconds, bufferSeconds);
            }
            return expired;
        } catch (Exception e) {
            log.warn("Could not parse JWT expiry — treating as expired: {}", e.getMessage());
            return true;
        }
    }

    /**
     * Clear all cached tokens.
     */
    public void clearAll() {
        tokenStore.clear();
        log.info("All cached tokens cleared");
    }

    /**
     * Clear token for a specific user.
     */
    public void clearToken(String email) {
        tokenStore.remove(email);
        log.info("Token cleared for: {}", email);
    }

    private static class TokenInfo {
        final String token;
        final String userId;
        final long fetchedAt;

        TokenInfo(String token, String userId, long fetchedAt) {
            this.token = token;
            this.userId = userId;
            this.fetchedAt = fetchedAt;
        }
    }
}
