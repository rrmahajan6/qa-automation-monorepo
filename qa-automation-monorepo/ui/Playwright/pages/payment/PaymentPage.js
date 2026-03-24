import test from 'node:test';
import { BasePage } from '../base/BasePage.js';
import { getPaymentPageLocators } from '../locators/payment.locators.js';

export class PaymentPage extends BasePage{
    constructor(page){
        super(page);
        this.locators = getPaymentPageLocators(this.page);
    }
    async fillPaymentDetails({ creditCard, address } = {}, testInfo) {
    if (!creditCard || !address) {
      throw new Error('fillPaymentDetails expects: { creditCard, address }');
    }
    testInfo.attach('payment-details');
    // Card details
    await this.fill(this.locators.creditCard, creditCard.cardNumber);
    await this.fill(this.locators.cvv, creditCard.cvv);
    await this.selectByOption(this.locators.month,creditCard.expiryMonth);
    await this.selectByOption(this.locators.date,creditCard.expiryDate);
    await this.fill(this.locators.nameOnCard, creditCard.cardholderName);


    // Address details
    await this.fill(this.locators.email, address.email);
    if (address.country) {
      const countryOption = this.locators.selectCountry(address.country);
      await this.pressSequentially(
        this.locators.country,
        address.country
      );
      await countryOption.waitFor({ state: 'visible' });
      await this.click(countryOption);
      await this.pressKey('Tab');
    }       
    await this.click(this.locators.placeOrder)
    }
}