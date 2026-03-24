package com.framework.config;

import org.aeonbits.owner.ConfigFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Singleton configuration manager.
 * Loads the correct environment configuration once and caches it.
 *
 * Usage:
 *   String url = ConfigManager.get().baseUrl();
 */
public class ConfigManager {

    private static final Logger log = LogManager.getLogger(ConfigManager.class);
    private static volatile EnvironmentConfig instance;

    private ConfigManager() {}

    public static EnvironmentConfig get() {
        if (instance == null) {
            synchronized (ConfigManager.class) {
                if (instance == null) {
                    String env = System.getProperty("env", "dev");
                    System.setProperty("env", env);
                    instance = ConfigFactory.create(EnvironmentConfig.class, System.getProperties());
                    log.info("Configuration loaded for environment: {}", env);
                    log.info("Base URL: {}", instance.baseUrl());
                }
            }
        }
        return instance;
    }

    /** Force reload config — useful between test suites if env changes. */
    public static void reload() {
        instance = null;
    }
}
