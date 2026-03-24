package framework.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import framework.base.BasePage;

public class HomePage extends BasePage {
    public HomePage(WebDriver driver) {
        super(driver);
    }
    private static final By addidasOriginal = By.xpath("//h5/b[text()='ADIDAS ORIGINAL']/../following-sibling::button[normalize-space()='Add To Cart']");
    private static final By zaraCoat = By.xpath("//h5/b[text()='ZARA COAT 3']/../following-sibling::button[normalize-space()='Add To Cart']");
    private static final By iphone13 = By.xpath("//h5/b[text()='iphone 13 pro']/../following-sibling::button[normalize-space()='Add To Cart']");
    private static final By cart = By.xpath("//button[@routerlink='/dashboard/cart']");
    public void addProductsToCart(String productName) {
        switch (productName) {
            case "ADIDAS ORIGINAL":
                click(addidasOriginal);
                break;
            case "ZARA COAT 3":
                click(zaraCoat);
                break;
            case "iphone 13 pro":
                click(iphone13);
                break;
        }
    }
    public void clickCart() {
        click(cart);
    }
}
