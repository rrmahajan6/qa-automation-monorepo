/**
 * Assertion Helper Functions
 * Custom assertion utilities
 */
import { expect } from '@playwright/test';
import logger from '../logger.js';

/**
 * Assert element is visible
 * @param {import('@playwright/test').Locator} locator - Element locator
 * @param {string} elementName - Element description
 */
export async function assertVisible(locator, elementName = 'Element') {
  try {
    await expect(locator).toBeVisible();
    logger.debug(`✓ ${elementName} is visible`);
  } catch (error) {
    logger.error(`✗ ${elementName} is not visible`);
    throw error;
  }
}

/**
 * Assert element is hidden
 * @param {import('@playwright/test').Locator} locator - Element locator
 * @param {string} elementName - Element description
 */
export async function assertHidden(locator, elementName = 'Element') {
  try {
    await expect(locator).toBeHidden();
    logger.debug(`✓ ${elementName} is hidden`);
  } catch (error) {
    logger.error(`✗ ${elementName} is visible`);
    throw error;
  }
}

/**
 * Assert text content
 * @param {import('@playwright/test').Locator} locator - Element locator
 * @param {string} expectedText - Expected text
 */
export async function assertText(locator, expectedText) {
  try {
    await expect(locator).toHaveText(expectedText);
    logger.debug(`✓ Text matches: ${expectedText}`);
  } catch (error) {
    logger.error(`✗ Text doesn't match. Expected: ${expectedText}`);
    throw error;
  }
}

/**
 * Assert URL contains text
 * @param {import('@playwright/test').Page} page - Page object
 * @param {string} urlPart - URL part to check
 */
export async function assertUrlContains(page, urlPart) {
  try {
    await expect(page).toHaveURL(new RegExp(urlPart));
    logger.debug(`✓ URL contains: ${urlPart}`);
  } catch (error) {
    logger.error(`✗ URL doesn't contain: ${urlPart}`);
    throw error;
  }
}

/**
 * Assert element count
 * @param {import('@playwright/test').Locator} locator - Element locator
 * @param {number} expectedCount - Expected count
 */
export async function assertCount(locator, expectedCount) {
  try {
    await expect(locator).toHaveCount(expectedCount);
    logger.debug(`✓ Element count is: ${expectedCount}`);
  } catch (error) {
    logger.error(`✗ Element count doesn't match. Expected: ${expectedCount}`);
    throw error;
  }
}
