import { BasePage } from '../base/BasePage.js';
import { getLoginLocators } from '../locators/login.locators.js';
import { getConfig } from '../../config/test.config.js';
import logger from '../../utils/logger.js';

export class LoginPage extends BasePage {
  constructor(page) {
    super(page);
    this.config = getConfig();
    this.locators = getLoginLocators(this.page);
  }

  async goto() {
    await this.navigate(this.config.baseUrl);
  }

  async login(username, password) {
    try {
      logger.step('Performing login');
      await this.fill(this.locators.usernameInput, username);
      await this.fillSensitiveData(this.locators.passwordInput, password);
      await this.click(this.locators.loginButton);
      logger.success('✓ Login successful');
    } catch (error) {
      await this.takeScreenshot('login-failed');
      logger.error(`✗ Login failed: ${error.message}`);
      throw error;
    }
  }

  async loginWithDefaultCredentials() {
    await this.login(this.config.auth.username, this.config.auth.password);
  }
}
