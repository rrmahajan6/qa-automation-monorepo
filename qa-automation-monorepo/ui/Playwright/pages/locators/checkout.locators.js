export function getCheckoutPageLocators(page){
    return{
        checkout : page.getByRole('button', { name: 'Checkout❯' })
    }
}