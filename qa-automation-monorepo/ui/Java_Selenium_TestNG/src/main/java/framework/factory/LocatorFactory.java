package framework.factory;
import org.openqa.selenium.By;
public class LocatorFactory {

    public static By getLocator(String type, String value) {

        switch (type.toLowerCase()) {

            case "id":
                return By.id(value);

            case "xpath":
                return By.xpath(value);

            case "css":
                return By.cssSelector(value);

            case "name":
                return By.name(value);

            default:
                throw new RuntimeException("Invalid locator type");
        }
    }
}