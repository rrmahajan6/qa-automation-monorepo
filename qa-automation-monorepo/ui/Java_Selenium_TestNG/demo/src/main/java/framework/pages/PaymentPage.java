package framework.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import framework.base.BasePage;
import framework.utils.ExtentTestManager;

public class PaymentPage extends BasePage {
    public PaymentPage(WebDriver driver) {
        super(driver);
    }
    private static final By placeOrderButton = By.xpath("//a[normalize-space()='Place Order']");
    private static final By creditCardNumber = By.xpath("//div[normalize-space()='Credit Card Number']/input");
    private static final By cvv = By.xpath("//div[normalize-space()='CVV Code ?']/input");
    private static final By nameOnCard = By.xpath("//div[normalize-space()='Name on Card']/input");
    private static final By applyCoupon = By.xpath("//div[normalize-space()='Apply Coupon']/input");
    private static final By emailInput = By.xpath("//input[@class='input txt text-validated ng-untouched ng-pristine ng-valid']");
    private static final By selectCountry = By.xpath("//input[@placeholder='Select Country']");
    private By countryOption(String country) {
        // Matches the country option text shown in the dropdown
        return By.xpath("//input[@placeholder]/following-sibling::section//button[normalize-space()='" + country + "']");
    }

    public void enterCardDetails(String cardNumber, String cvvCode, String name) {
        type(creditCardNumber, cardNumber);
        type(cvv, cvvCode);
        type(nameOnCard, name);
        type(applyCoupon, "rahul");
        ExtentTestManager.getTest().info("Entered card details and applied coupon code");
    }

    public void enterEmail(String email) {
        type(emailInput, email);
    }

    public void selectCountry(String country) {
        type(selectCountry, country);
    }

    public void selectCountryFromDropdown(String country) {
        click(countryOption(country));
    }

    public void clickPlaceOrder() {
        click(placeOrderButton);
    }
}
