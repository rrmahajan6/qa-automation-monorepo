/**
 * Development Environment Configuration
 */
export default {
  name: 'dev',
  baseUrl: 'https://rahulshettyacademy.com/client/#/auth/login',
  apiUrl: 'https://api-dev.example.com',
  timeout: 30000,
  
  // Authentication
  auth: {
    username: process.env.DEV_AUTH_USERNAME || 'rrmahajan6@gmail.com',
    password: process.env.DEV_AUTH_PASSWORD || 'Test@1234',
  },

  // Feature flags
  features: {
    enableNewUI: true,
    enableBetaFeatures: true,
  },
  database: {
    host: process.env.DEV_DB_HOST || '',
    database: process.env.DEV_DB_NAME || '',
    user: process.env.DEV_DB_USER || '',
    password: process.env.DEV_DB_PASSWORD || '',
  },
};
