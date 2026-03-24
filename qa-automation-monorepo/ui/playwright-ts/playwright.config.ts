import { defineConfig } from '@playwright/test';

export default defineConfig({
  testDir: './tests',
  timeout: 30_000,
  retries: 0,
  use: {
    baseURL: process.env.BASE_URL || 'https://example.com',
    headless: true,
    trace: 'on-first-retry'
  },
  reporter: [['list'], ['html', { open: 'never' }]]
});
