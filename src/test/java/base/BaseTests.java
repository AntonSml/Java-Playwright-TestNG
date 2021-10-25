package base;

import com.microsoft.playwright.*;
import core.Configure;
import io.restassured.RestAssured;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import pages.MainPage;


public class BaseTests {

    private Browser browser;
    private Configure configure;
    protected MainPage mainPage;

    @BeforeClass
    public void setUp() {
        configure = new Configure();

        //Open a browser (supports Chromium (Chrome, Edge), Firefox, and Webkit (Safari))
        browser = configure.startBrowser();

        Page page = browser.newPage();
        page.navigate(configure.getUrl());
        RestAssured.baseURI = configure.getUrl() + "v2";
        mainPage = new MainPage(page);
    }

    @AfterClass
    public void tearDown() {
        browser.close();
    }
}
