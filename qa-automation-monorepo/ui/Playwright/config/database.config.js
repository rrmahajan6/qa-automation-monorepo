/**
 * Database Configuration Manager
 * Loads database config based on environment
 */
import devConfig from './environments/dev.config.js';
import qaConfig from './environments/qa.config.js';
import stagingConfig from './environments/staging.config.js';
import prodConfig from './environments/prod.config.js';

const configs = {
  dev: devConfig,
  qa: qaConfig,
  staging: stagingConfig,
  prod: prodConfig,
};

/**
 * Get database configuration for current environment
 * @returns {Object} Database configuration
 */
export function getDatabaseConfig() {
  const env = process.env.ENV || 'dev';
  const config = configs[env];
  
  if (!config) {
    throw new Error(`No configuration found for environment: ${env}`);
  }
  
  return config.database;
}

/**
 * Connection pool settings
 */
export const poolConfig = {
  min: 2,
  max: 10,
  acquireTimeoutMillis: 30000,
  idleTimeoutMillis: 30000,
  connectionTimeoutMillis: 2000,
};
