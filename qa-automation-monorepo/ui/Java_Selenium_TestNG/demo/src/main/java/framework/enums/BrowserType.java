package framework.enums;

/**
 * Browser type enumeration for cross-browser testing support
 */
public enum BrowserType {
    CHROME("chrome"),
    FIREFOX("firefox"),
    EDGE("edge"),
    SAFARI("safari");

    private final String browserName;

    BrowserType(String browserName) {
        this.browserName = browserName;
    }

    public String getBrowserName() {
        return browserName;
    }

    public static BrowserType fromString(String browser) {
        for (BrowserType bt : BrowserType.values()) {
            if (bt.browserName.equalsIgnoreCase(browser)) {
                return bt;
            }
        }
        throw new IllegalArgumentException("Browser not found: " + browser);
    }
}
