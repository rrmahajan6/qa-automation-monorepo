/**
 * Test Hooks
 * Hooks that run before/after each test
 */
import logger from '../utils/logger.js';

export class TestHooks {
  /**
   * Before each test
   * @param {Object} testInfo - Test information
   */
  static async beforeEach(testInfo) {
    logger.step(`Starting test: ${testInfo.title}`);
    logger.info(`Test file: ${testInfo.file}`);
  }

  /**
   * After each test
   * @param {import('@playwright/test').Page} page - Page object
   * @param {Object} testInfo - Test information
   */
  static async afterEach(page, testInfo) {
    // Take screenshot on failure
    if (testInfo.status === 'failed') {
      const screenshot = await page.screenshot();
      await testInfo.attach('failure-screenshot', {
        body: screenshot,
        contentType: 'image/png',
      });
      logger.error(`✗ Test failed: ${testInfo.title}`);
    } else if (testInfo.status === 'passed') {
      logger.success(`✓ Test passed: ${testInfo.title}`);
    }

    // Log test duration
    logger.info(`Test duration: ${testInfo.duration}ms`);
  }

  /**
   * Before all tests in a file
   */
  static async beforeAll() {
    logger.info('='.repeat(80));
    logger.info('Starting test suite');
  }

  /**
   * After all tests in a file
   */
  static async afterAll() {
    logger.info('Test suite completed');
    logger.info('='.repeat(80));
  }
}
