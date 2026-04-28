package framework.pages;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import framework.base.BasePage;
public class OrderHistoryPage extends BasePage {
    public OrderHistoryPage(WebDriver driver) {
        super(driver);
    }
    private static final By orderHistory = By.xpath("//label[@routerlink='/dashboard/myorders']");
    public void clickOrderHistory() {
        click(orderHistory);
    }
}
