package framework.pages;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import framework.base.BasePage;

public class MyCartPage extends BasePage {
    public MyCartPage(WebDriver driver) {
        super(driver);
    }
    private static final By checkout = By.xpath("//button[text()='Checkout']");
    public void clickCheckout() {
        click(checkout);
    }
}
