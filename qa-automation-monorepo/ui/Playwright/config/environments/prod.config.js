/**
 * Production Environment Configuration
 * NOTE: Use this only for smoke tests, not full regression
 */
export default {
  name: 'prod',
  baseUrl: 'https://www.example.com',
  apiUrl: 'https://api.example.com',
  timeout: 30000,
  
  // Authentication
  auth: {
    username: process.env.PROD_AUTH_USERNAME || '',
    password: process.env.PROD_AUTH_PASSWORD || '',
  },

  // Feature flags
  features: {
    enableNewUI: true,
    enableBetaFeatures: false,
  },

  // Database (for prod environment - READ ONLY)
  database: {
    host: process.env.PROD_DB_HOST || '',
    port: Number(process.env.PROD_DB_PORT || 5432),
    database: process.env.PROD_DB_NAME || '',
    user: process.env.PROD_DB_USER || '',
    password: process.env.PROD_DB_PASSWORD || '',
    readOnly: true,
  },
};
