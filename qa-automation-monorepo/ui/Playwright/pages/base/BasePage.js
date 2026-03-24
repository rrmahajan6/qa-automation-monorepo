/**
 * BasePage - Base Page Object
 * Contains common wrapper methods for all page objects
 */
import logger from '../../utils/logger.js';
import { waitForVisible, waitForPageLoad, waitForSpinner } from '../../utils/helpers/waitHelper.js';
import { clickWithRetry, fillWithValidation, scrollIntoView } from '../../utils/helpers/actionHelper.js';
import { TIMEOUTS } from '../../constants/timeouts.js';

export class BasePage {
  /**
   * @param {import('@playwright/test').Page} page - Playwright page object
   */
  constructor(page) {
    this.page = page;
  }

  /**
   * Navigate to URL
   * @param {string} url - URL to navigate to
   */
  async navigate(url) {
    try {
      logger.info(`Navigating to: ${url}`);
      await this.page.goto(url);
      await waitForPageLoad(this.page);
      await waitForSpinner(this.page);
      logger.success(`✓ Navigated to: ${url}`);
    } catch (error) {
      logger.error(`✗ Navigation failed: ${error.message}`);
      throw error;
    }
  }

  /**
   * Click element with enhanced error handling
   * @param {import('@playwright/test').Locator} locator - Element locator
   * @param {Object} options - Click options
   */
  async click(locator, options = {}) {
    try {
      await waitForVisible(locator);
      await scrollIntoView(locator);
      await locator.click(options);
      logger.info('Clicked element');
    } catch (error) {
      await this.takeScreenshot('click-error');
      logger.error(`Click failed: ${error.message}`);
      throw error;
    }
  }

  /**
   * Click with retry mechanism
   * @param {import('@playwright/test').Locator} locator - Element locator
   */
  async clickWithRetry(locator) {
    try {
      await clickWithRetry(locator);
      logger.info('Clicked element with retry');
    } catch (error) {
      await this.takeScreenshot('click-retry-error');
      logger.error(`Click with retry failed: ${error.message}`);
      throw error;
    }
  }

  /**
   * Fill input field
   * @param {import('@playwright/test').Locator} locator - Element locator
   * @param {string} text - Text to fill
   */
  async fill(locator, text) {
    try {
      await waitForVisible(locator);
      await locator.fill(text);
      logger.info(`Filled text: ${text.substring(0, 30)}...`);
    } catch (error) {
      await this.takeScreenshot('fill-error');
      logger.error(`Fill failed: ${error.message}`);
      throw error;
    }
  }

  /**
   * Fill with validation
   * @param {import('@playwright/test').Locator} locator - Element locator
   * @param {string} text - Text to fill
   */
  async fillWithValidation(locator, text) {
    try {
      await fillWithValidation(locator, text);
      logger.info(`Filled and validated text`);
    } catch (error) {
      await this.takeScreenshot('fill-validation-error');
      logger.error(`Fill with validation failed: ${error.message}`);
      throw error;
    }
  }

  /**
   * Fill sensitive data (passwords, etc.)
   * @param {import('@playwright/test').Locator} locator - Element locator
   * @param {string} value - Sensitive value
   */
  async fillSensitiveData(locator, value) {
    try {
      await waitForVisible(locator);
      await locator.fill(value);
      logger.info('Filled sensitive data: ****');
    } catch (error) {
      await this.takeScreenshot('sensitive-fill-error');
      logger.error('Failed to fill sensitive data');
      throw error;
    }
  }

  /**
   * Get element text
   * @param {import('@playwright/test').Locator} locator - Element locator
   * @returns {Promise<string>} Element text
   */
  async getText(locator) {
    try {
      await waitForVisible(locator);
      const text = await locator.textContent();
      logger.debug(`Got text: ${text}`);
      return text;
    } catch (error) {
      logger.error(`Get text failed: ${error.message}`);
      throw error;
    }
  }

  /**
   * Check if element is visible
   * @param {import('@playwright/test').Locator} locator - Element locator
   * @returns {Promise<boolean>} Visibility status
   */
  async isVisible(locator) {
    try {
      return await locator.isVisible();
    } catch (error) {
      return false;
    }
  }

  /**
   * Wait for element to be visible
   * @param {import('@playwright/test').Locator} locator - Element locator
   * @param {number} timeout - Timeout in ms
   */
  async waitForElement(locator, timeout = TIMEOUTS.LONG) {
    try {
      await locator.waitFor({ state: 'visible', timeout });
      logger.debug('Element is visible');
    } catch (error) {
      await this.takeScreenshot('wait-element-error');
      logger.error(`Element not visible: ${error.message}`);
      throw error;
    }
  }

  /**
   * Wait for page to be fully loaded
   */
  async waitForPageLoad() {
    try {
      await waitForPageLoad(this.page);
      await waitForSpinner(this.page);
      logger.debug('Page fully loaded');
    } catch (error) {
      logger.error(`Page load timeout: ${error.message}`);
      throw error;
    }
  }

  /**
   * Wait for navigation to complete
   * @param {string} urlPattern - Expected URL pattern
   */
  async waitForNavigation(urlPattern) {
    try {
      await this.page.waitForURL(`**/${urlPattern}`, { timeout: TIMEOUTS.LONG });
      await this.waitForPageLoad();
      logger.info(`✓ Navigated to: ${urlPattern}`);
    } catch (error) {
      logger.error(`Navigation timeout: ${error.message}`);
      throw error;
    }
  }

  /**
   * Take screenshot
   * @param {string} name - Screenshot name
   */
  async takeScreenshot(name) {
    try {
      const screenshotPath = `screenshots/${name}-${Date.now()}.png`;
      await this.page.screenshot({ path: screenshotPath, fullPage: true });
      logger.info(`Screenshot saved: ${screenshotPath}`);
    } catch (error) {
      logger.error(`Screenshot failed: ${error.message}`);
    }
  }

  /**
   * Get page title
   * @returns {Promise<string>} Page title
   */
  async getPageTitle() {
    try {
      const title = await this.page.title();
      logger.debug(`Page title: ${title}`);
      return title;
    } catch (error) {
      logger.error(`Get title failed: ${error.message}`);
      throw error;
    }
  }

  /**
   * Get current URL
   * @returns {string} Current URL
   */
  getCurrentUrl() {
    return this.page.url();
  }

  /**
   * Scroll to element
   * @param {import('@playwright/test').Locator} locator - Element locator
   */
  async scrollToElement(locator) {
    try {
      await scrollIntoView(locator);
      logger.debug('Scrolled to element');
    } catch (error) {
      logger.error(`Scroll failed: ${error.message}`);
      throw error;
    }
  }

  /**
   * Press keyboard key
   * @param {string} key - Key to press
   */
  async pressKey(key) {
    try {
      await this.page.keyboard.press(key);
      logger.debug(`Pressed key: ${key}`);
    } catch (error) {
      logger.error(`Key press failed: ${error.message}`);
      throw error;
    }
  }

  /**
   * Refresh page
   */
  async refresh() {
    try {
      await this.page.reload();
      await this.waitForPageLoad();
      logger.info('Page refreshed');
    } catch (error) {
      logger.error(`Refresh failed: ${error.message}`);
      throw error;
    }
  }

  /**
   * Go back
   */
  async goBack() {
    try {
      await this.page.goBack();
      await this.waitForPageLoad();
      logger.info('Navigated back');
    } catch (error) {
      logger.error(`Go back failed: ${error.message}`);
      throw error;
    }
  }
  /**
   * select option using text
   */
  async selectByOption(locator,option) {
    try {
      await locator.selectOption(option);
      logger.info('option is selected');
    } catch (error) {
      logger.error(`Go back failed: ${error.message}`);
      throw error;
    }
  }
  /**
   * select option using text
   */
  async pressSequentially(locator,text) {
    try {
      await locator.pressSequentially(text);
      logger.info('text typed sequentially');
    } catch (error) {
      logger.error(`Go back failed: ${error.message}`);
      throw error;
    }
  }
}
