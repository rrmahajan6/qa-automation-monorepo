/**
 * Staging Environment Configuration
 */
export default {
  name: 'staging',
  baseUrl: 'https://staging.example.com',
  apiUrl: 'https://api-staging.example.com',
  timeout: 30000,
  
  // Authentication
  auth: {
    username: process.env.STAGING_AUTH_USERNAME || '',
    password: process.env.STAGING_AUTH_PASSWORD || '',
  },

  // Feature flags
  features: {
    enableNewUI: true,
    enableBetaFeatures: false,
  },

  // Database (for staging environment)
  database: {
    host: process.env.STAGING_DB_HOST || '',
    port: Number(process.env.STAGING_DB_PORT || 5432),
    database: process.env.STAGING_DB_NAME || '',
    user: process.env.STAGING_DB_USER || '',
    password: process.env.STAGING_DB_PASSWORD || '',
  },
};
