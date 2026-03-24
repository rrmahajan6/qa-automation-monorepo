// @ts-check
import { defineConfig, devices } from '@playwright/test';

// Suppress NODE_TLS_REJECT_UNAUTHORIZED warning for dev/test environments
// This warning appears when connecting to Azure Key Vault with certain auth methods
const originalEmitWarning = process.emitWarning;
process.emitWarning = (warning, ...args) => {
  // Filter out the specific TLS warning
  if (typeof warning === 'string' && warning.includes('NODE_TLS_REJECT_UNAUTHORIZED')) {
    return; // Suppress this warning
  }
  const emitWarning = /** @type {any} */ (originalEmitWarning);
  return emitWarning.call(process, warning, ...args);
};

/**
 * Enterprise-grade Playwright Configuration
 * Environment: Set via ENV environment variable (dev, qa, staging, prod)
 * Example: ENV=qa yarn playwright test
 */

/**
 * @see https://playwright.dev/docs/test-configuration
 */
export default defineConfig({
  testDir: './tests',
  
  /* Global setup and teardown */
  globalSetup: './hooks/globalSetup.js',
  globalTeardown: './hooks/globalTeardown.js',
  
  /* Run tests in files in parallel */
  fullyParallel: true,
  
  /* Fail the build on CI if you accidentally left test.only in the source code. */
  forbidOnly: !!process.env.CI,
  
  /* Retry on CI only */
  retries: process.env.CI ? 2 : 0,
  
  /* Configure workers based on environment */
  workers: process.env.CI ? 1 : 1,
  
  /* Timeout settings */
  timeout: 60000, // 60 seconds per test
  expect: {
    timeout: 10000, // 10 seconds for assertions
  },

  /* Reporter configuration */
  reporter: [
    ['html', { outputFolder: 'playwright-report', open: 'never' }],
    ['json', { outputFile: 'test-results/results.json' }],
    ['junit', { outputFile: 'test-results/junit.xml' }],
    ['list'],
  ],
  
  /* Shared settings for all the projects below */
  use: {
    /* Screenshots and videos */
    screenshot: 'only-on-failure',
    video: 'retain-on-failure',
    trace: 'retain-on-failure',
    
    /* Browser settings */
    headless: process.env.HEADLESS !== 'false',
    viewport: { width: 1920, height: 1080 },
    
    /* Action timeouts */
    actionTimeout: 15000,
    navigationTimeout: 30000,
    
    /* Ignore HTTPS errors */
    ignoreHTTPSErrors: true,
  },

  /* Configure projects for major browsers */
  projects: [
    {
      name: 'chromium',
      use: { ...devices['Desktop Chrome'] },
    },

    // {
    //   name: 'firefox',
    //   use: { ...devices['Desktop Firefox'] },
    // },

    // {
    //   name: 'webkit',
    //   use: { ...devices['Desktop Safari'] },
    // },

    /* Test against mobile viewports. */
    // {
    //   name: 'Mobile Chrome',
    //   use: { ...devices['Pixel 5'] },
    // },
    // {
    //   name: 'Mobile Safari',
    //   use: { ...devices['iPhone 12'] },
    // },

    /* Test against branded browsers. */
    // {
    //   name: 'Microsoft Edge',
    //   use: { ...devices['Desktop Edge'], channel: 'msedge' },
    // },
    // {
    //   name: 'Google Chrome',
    //   use: { ...devices['Desktop Chrome'], channel: 'chrome' },
    // },
  ],

  /* Run your local dev server before starting the tests */
  // webServer: {
  //   command: 'npm run start',
  //   url: 'http://localhost:3000',
  //   reuseExistingServer: !process.env.CI,
  // },
});

