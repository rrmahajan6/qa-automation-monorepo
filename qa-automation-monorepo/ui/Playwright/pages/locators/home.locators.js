export function getHomePageLocators(page) {
    return {
        addToCartButtonByProduct: (productName) =>
            page.locator(`xpath=//b[normalize-space()='${productName}']/../following-sibling::button[normalize-space()='Add To Cart']`),

        productTitleByName: (productName) =>
            page.locator(`xpath=//b[normalize-space()='${productName}']`),
        simpleAddToCartForAdidas : page.locator(`//b[text()='ADIDAS ORIGINAL']/../following-sibling::button[normalize-space()='Add To Cart']`),
        cart : page.locator(`//button[@routerlink='/dashboard/cart']`),
        commonAddToCart: page.getByRole('button', { name: /Add To Cart/i })
    };
}