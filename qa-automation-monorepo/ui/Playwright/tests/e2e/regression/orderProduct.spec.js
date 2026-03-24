import { test, expect } from '../../../fixtures/testFixtures.js';
import { PageManager } from '../../../pages/PageManager.js';
import orderData from '../../../data/testData/dev/OrderProduct.json' assert { type: 'json' };

for (const productName of orderData.products) {
  test(`Buy ${productName}`, { tag: '@smoke' }, async ({ authenticatedPage }, testInfo) => {
    const pageManager = new PageManager(authenticatedPage);
    const home = pageManager.homePage();
    const checkoutpage = pageManager.checkOutPage();
    const paymentPage = pageManager.paymentPage();
    const orderPage = pageManager.orderPage();
    const historyPage = pageManager.HistoryPage();

    await home.addToCartProducts(productName);
    await checkoutpage.checkoutProduct();
    await testInfo.attach('debug-log.txt', {
    body: 'Important debug message: user id=123\nMore info...',
    contentType: 'text/plain',
  });

    await paymentPage.fillPaymentDetails({
      creditCard: orderData.creditCard,
      address: orderData.address
    }, testInfo);

    const orderIds = await orderPage.getOrderIds();
    await orderPage.goToHistoryPage();

    const isMatched = await historyPage.verifyOrders(orderIds);
    expect(isMatched).toBeTruthy();
  });
}