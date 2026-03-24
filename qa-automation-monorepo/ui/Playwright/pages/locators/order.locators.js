export function getOrderPageLocators(page){
    return{
        downloadCsv: page.getByRole('button',{name:'Click To Download Order Details in CSV'}),
        orderHistoryPage: page.locator(`//label[text()=' Orders History Page ']`),
        orderIds: page.locator(`//label[@class='ng-star-inserted']`)
    }
}