package core;

import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserType;
import com.microsoft.playwright.Playwright;

import java.util.Arrays;
import java.util.List;

import static core.Browsers.*;

public class Configure {

    private String url = "https://petstore.swagger.io/";
    private Browsers browsers = CHROME;

    public Configure() {
    }

    public String getUrl() {
        return url;
    }

    private void readBrowserOverride() {
        // Browser override by env var
        String browserOverride = System.getProperty("browser");
        String testSiteAddressOverride = System.getProperty("test.url");
        List<String> neverOverride = Arrays.asList("headless", "htmlunit", "mobile");
        if (browserOverride != null && !browserOverride.isEmpty() && !neverOverride.contains(getBrowsers().toString())) {
            browsers = Browsers.findByString(browserOverride.toLowerCase());
        }
        if (testSiteAddressOverride != null && !testSiteAddressOverride.isEmpty()) {
            url = testSiteAddressOverride;
        }
    }

    public Browser startBrowser() {
        readBrowserOverride();
        switch (getBrowsers()) {
            case CHROME:
                return Playwright
                        .create()
                        .chromium()
                        .launch(new BrowserType.LaunchOptions().setHeadless(false));
            case FIREFOX:
                return Playwright
                        .create()
                        .firefox()
                        .launch(new BrowserType.LaunchOptions().setHeadless(false));
            case WEBKIT:
                return Playwright
                        .create()
                        .webkit()
                        .launch(new BrowserType.LaunchOptions().setHeadless(false));
            default:
                throw new IllegalArgumentException("Configure(): invalid browser value");
        }
    }

    public Browsers getBrowsers() {
        return browsers;
    }
}
