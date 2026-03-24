export function getHistoryPageLocators(page){
    return{
        table: page.locator(`//table[contains(@class,'table-bordered')]`),
        headerRows: page.locator(`//thead/tr/th`),
        orderIds:(rowNumber)=>{
            return page.locator(`//tbody/tr[${rowNumber}]/th[1]`);
        },
        fields: (rowNumber,columnNumber)=>{
            return page.locator(`//tbody/tr[${rowNumber}]/td[${columnNumber}]`);
        },
        rows: page.locator(`//tbody/tr`)
    }
}