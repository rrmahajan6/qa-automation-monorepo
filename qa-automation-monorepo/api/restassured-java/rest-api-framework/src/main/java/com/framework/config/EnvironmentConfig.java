package com.framework.config;

import org.aeonbits.owner.Config;
import org.aeonbits.owner.Config.Sources;

/**
 * Environment-aware configuration using Owner library.
 * Reads from environment-specific properties file, falling back to config.properties.
 * Set -Denv=dev|staging|prod at runtime to switch environments.
 */
@Sources({
    "classpath:config/${env}.properties",
    "classpath:config/config.properties"
})
public interface EnvironmentConfig extends Config {

    @Key("base.url")
    String baseUrl();

    @Key("api.version")
    @DefaultValue("v1")
    String apiVersion();

    @Key("connection.timeout")
    @DefaultValue("30000")
    int connectionTimeout();

    @Key("read.timeout")
    @DefaultValue("30000")
    int readTimeout();

    @Key("auth.token")
    @DefaultValue("")
    String authToken();

    @Key("auth.username")
    @DefaultValue("")
    String authUsername();

    @Key("auth.password")
    @DefaultValue("")
    String authPassword();

    @Key("shop.base.url")
    @DefaultValue("")
    String shopBaseUrl();

    @Key("shop.user.email")
    @DefaultValue("")
    String shopUserEmail();

    @Key("shop.user.password")
    @DefaultValue("")
    String shopUserPassword();

    @Key("places.api.key")
    @DefaultValue("qaclick123")
    String placesApiKey();

    @Key("token.expiry.buffer.seconds")
    @DefaultValue("60")
    int tokenExpiryBufferSeconds();

    @Key("retry.count")
    @DefaultValue("1")
    int retryCount();

    @Key("log.level")
    @DefaultValue("INFO")
    String logLevel();
}
