/**
 * Wait Helper Functions
 * Custom wait utilities for better test stability
 */
import logger from '../logger.js';

/**
 * Wait for element to be visible
 * @param {import('@playwright/test').Locator} locator - Element locator
 * @param {number} timeout - Timeout in milliseconds
 */
export async function waitForVisible(locator, timeout = 30000) {
  try {
    await locator.waitFor({ state: 'visible', timeout });
    logger.debug('Element is visible');
  } catch (error) {
    logger.error(`Element not visible after ${timeout}ms`);
    throw error;
  }
}

/**
 * Wait for element to be hidden
 * @param {import('@playwright/test').Locator} locator - Element locator
 * @param {number} timeout - Timeout in milliseconds
 */
export async function waitForHidden(locator, timeout = 30000) {
  try {
    await locator.waitFor({ state: 'hidden', timeout });
    logger.debug('Element is hidden');
  } catch (error) {
    logger.error(`Element still visible after ${timeout}ms`);
    throw error;
  }
}

/**
 * Wait for page to be fully loaded
 * @param {import('@playwright/test').Page} page - Page object
 */
export async function waitForPageLoad(page) {
  try {
    await page.waitForLoadState('domcontentloaded');
    await page.waitForLoadState('networkidle');
    logger.debug('Page fully loaded');
  } catch (error) {
    logger.error('Page load timeout');
    throw error;
  }
}

/**
 * Wait for custom condition
 * @param {Function} condition - Condition function
 * @param {number} timeout - Timeout in milliseconds
 * @param {string} message - Error message
 */
export async function waitForCondition(condition, timeout = 30000, message = 'Condition not met') {
  const startTime = Date.now();
  
  while (Date.now() - startTime < timeout) {
    if (await condition()) {
      logger.debug('Condition met');
      return true;
    }
    await new Promise(resolve => setTimeout(resolve, 500));
  }
  
  logger.error(message);
  throw new Error(message);
}

/**
 * Wait for spinner to disappear
 * @param {import('@playwright/test').Page} page - Page object
 * @param {string} selector - Spinner selector
 */
export async function waitForSpinner(page, selector = '.spinner, .loading') {
  try {
    const spinner = page.locator(selector).first();
    await spinner.waitFor({ state: 'hidden', timeout: 30000 }).catch(() => {});
    logger.debug('Spinner disappeared');
  } catch (error) {
    // Spinner might not exist, which is fine
    logger.debug('No spinner found or already gone');
  }
}
