package framework.factory;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.InputStream;
import java.io.InputStreamReader;

import org.openqa.selenium.By;

public class LocatorReader {

    static String currentPage;
    static JsonObject jsonObject;

    public static void switchToPage(String pageName) {

        currentPage = pageName;

        String resourcePath = "framework/factory/locators/" + pageName + ".json";
        try (InputStream resourceStream = LocatorReader.class.getClassLoader().getResourceAsStream(resourcePath)) {
            if (resourceStream == null) {
                throw new RuntimeException("Locator JSON not found on classpath: " + resourcePath);
            }
            try (InputStreamReader reader = new InputStreamReader(resourceStream)) {
                jsonObject = JsonParser.parseReader(reader).getAsJsonObject();
            }
        } catch (Exception e) {
            throw new RuntimeException("Unable to load locator file: " + pageName, e);
        }
    }

    public static By get(String elementName) {

        JsonObject element = jsonObject.getAsJsonObject(elementName);
        if (element == null) {
            throw new IllegalArgumentException("Element not found in locator file: " + elementName);
        }

        String type = element.get("type").getAsString();
        String value = element.get("value").getAsString();

        return LocatorFactory.getLocator(type, value);
    }
}