import { BasePage } from "../base/BasePage";
import { getHistoryPageLocators } from "../locators/historylocators";
export class HistoryPage extends BasePage{
    constructor(page){
        super(page);
        this.locators = getHistoryPageLocators(page);
    }
async verifyOrders(receivedOrderIds = []) {
    const expectedIds = (Array.isArray(receivedOrderIds) ? receivedOrderIds : [])
        .map(x => String(x).trim())
        .filter(x => x.length > 0);
    const expectedIdSet = new Set(expectedIds);
    const foundIds = new Set();

    if (!expectedIds.length) {
        console.log("No expected order IDs provided");
        return false;
    }
    await this.page.waitForLoadState('domcontentloaded');
    await this.locators.rows.first().waitFor({ state: 'visible', timeout: 10000 });

    const maxAttempts = 5;
    for (let attempt = 1; attempt <= maxAttempts; attempt++) {
        const rows = await this.locators.rows.count();
        const columns = await this.locators.headerRows.count();
        for (let i = 1; i <= rows; i++) {
            const currentOrderId = (await this.getText(this.locators.orderIds(i))).trim();

            if (expectedIdSet.has(currentOrderId) && !foundIds.has(currentOrderId)) {
                foundIds.add(currentOrderId);
                console.log(`match found for order id: ${currentOrderId}`);

                for (let j = 1; j < columns; j++) {
                    const cellText = (await this.getText(this.locators.fields(i, j))).trim();
                    console.log(cellText);
                }
            }
        }

        if (foundIds.size === expectedIdSet.size) {
            console.log("all expected order ids found");
            return true;
        }

        await this.page.waitForTimeout(500);
    }

    const missingIds = expectedIds.filter(id => !foundIds.has(id));
    console.log(`missing order ids: ${missingIds.join(", ")}`);
    return false;
}
}