/**
 * QA Environment Configuration
 */
export default {
  name: 'qa',
  baseUrl: 'https://qa.example.com',
  apiUrl: 'https://api-qa.example.com',
  timeout: 30000,
  
  // Authentication
  auth: {
    username: process.env.QA_AUTH_USERNAME || '',
    password: process.env.QA_AUTH_PASSWORD || '',
  },

  // Feature flags
  features: {
    enableNewUI: true,
    enableBetaFeatures: false,
  },

  // Database (for qa environment)
  database: {
    host: process.env.QA_DB_HOST || '',
    port: Number(process.env.QA_DB_PORT || 5432),
    database: process.env.QA_DB_NAME || '',
    user: process.env.QA_DB_USER || '',
    password: process.env.QA_DB_PASSWORD || '',
  },
};
