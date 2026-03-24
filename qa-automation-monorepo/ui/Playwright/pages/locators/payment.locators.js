export function getPaymentPageLocators(page){
    return{
        creditCard: page.locator(`//div[contains(text(),'Credit Card Number')]/../input`),
        cvv: page.locator(`//div[contains(text(),'CVV')]/../input`),
        month: page.locator(`//select[@class='input ddl']`).first(),
        date: page.locator(`(//select[@class='input ddl'])[2]`),
        nameOnCard: page.locator(`//div[text()='Name on Card ']/../input`),
        apply: page.locator(`//input[@name]`),
        country: page.locator(`//input[@placeholder]`),
        selectCountry: (countryName)=>{
            return page.getByRole('button', { name: new RegExp(`\\b${countryName}\\b`, 'i') }).first();
        },
        placeOrder: page.getByText('Place Order', { exact: true }),
        email: page.locator(`//div[@class='user__name mt-5']/input`)
    }
}