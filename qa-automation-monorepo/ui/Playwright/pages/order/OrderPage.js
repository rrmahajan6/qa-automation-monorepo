import { logger } from '@azure/keyvault-secrets';
import { BasePage } from '../base/BasePage.js';
import { getOrderPageLocators } from '../locators/order.locators.js';

export class OrderPage extends BasePage{
    constructor(page){
        super(page);
        this.locators = getOrderPageLocators(this.page);
    }
    async goToHistoryPage(){
        await this.click(this.locators.orderHistoryPage);
    }
    async downLoadInvoice(){
        await this.click(this.locators.downloadCsv);
    }
    async getOrderIds(){
        await this.waitForPageLoad();
        await this.waitForElement(this.locators.orderIds.first());
        const idsCount = await this.locators.orderIds.count();
        const orderIds = [];
        for (let i = 0; i < idsCount; i++) {
            const rawText = (await this.getText(this.locators.orderIds.nth(i))).split("|")[1];
            const orderId = String(rawText ?? '').trim();
            if (orderId.length > 0) {
                orderIds.push(orderId);
            }
        }
        return orderIds;
    }
}