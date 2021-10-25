# Testing project with browser automation tool Microsoft Playwright + Java + TestNG

o run tests from command line use:

    mvn test -Dsuite={test_suite_name}.xml

To run browser in headless mode, add:

    -Dbrowser=headless

To run browser in incognito mode, add:

    -Dbrowser=incognito


To use alternative URL, add:

    -Dtest.url={url}

To generate a report, use command:

    Allure serve

### Allure reporting
### About Allure reporter information could be found [here.](https://docs.qameta.io/allure/#_about)
