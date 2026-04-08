import { BasePage } from '../base/BasePage.js';
import { getHomePageLocators } from '../locators/home.locators.js';

export class HomePage extends BasePage {
  constructor(page) {
    super(page);
    this.locators = getHomePageLocators(this.page);
  }

  async addToCartProducts(productName) {
    await this.click(this.locators.addToCartButtonByProduct(productName));
    await this.click(this.locators.cart);
  }
  async addToCartAllProducts() {
    await this.page.waitForLoadState('domcontentloaded');
    await this.locators.commonAddToCart.first().waitFor({ state: 'visible', timeout: 10000 });
    const count = await this.locators.commonAddToCart.count();
    for(let i=0;i<count;i++){
      await this.click(this.locators.commonAddToCart.nth(i))
    }
    await this.click(this.locators.cart);
  }
}
