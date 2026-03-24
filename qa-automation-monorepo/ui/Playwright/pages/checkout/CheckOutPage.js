import { BasePage } from '../base/BasePage.js';
import { getCheckoutPageLocators } from '../locators/checkout.locators.js';

export class CheckOutPage extends BasePage{
    constructor(page){
        super(page);
        this.locators = getCheckoutPageLocators(this.page);
    }
    async checkoutProduct(){
        await this.click(this.locators.checkout);
    }
}