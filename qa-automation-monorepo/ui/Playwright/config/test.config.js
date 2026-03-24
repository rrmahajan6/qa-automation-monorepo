/**
 * Test Configuration Manager
 * Loads environment-specific configuration
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
 * Get configuration for current environment
 * @returns {Object} Environment configuration
 */
export function getConfig() {
  const env = process.env.ENV || 'dev';
  const config = configs[env];
  
  if (!config) {
    throw new Error(`No configuration found for environment: ${env}`);
  }
  
  console.log(`✓ Loaded configuration for environment: ${env}`);
  return config;
}

/**
 * Get current environment name
 * @returns {string} Environment name
 */
export function getEnvironment() {
  return process.env.ENV || 'dev';
}
