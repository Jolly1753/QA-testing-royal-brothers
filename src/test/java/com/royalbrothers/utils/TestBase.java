package com.royalbrothers.utils;

import com.microsoft.playwright.*;

public class TestBase {

    private static Playwright playwright;
    private static Browser browser;
    private static BrowserContext context;
    private static Page page;

    public static void init() {
        playwright = Playwright.create();

        browser = playwright.chromium().launch(new BrowserType.LaunchOptions()
                .setHeadless(false)
                .setSlowMo(250));

        context = browser.newContext(new Browser.NewContextOptions()
                .setViewportSize(1366, 768));

        page = context.newPage();
    }

    public static Page getPage() {
        return page;
    }

    public static void tearDown() {
        context.close();
        browser.close();
        playwright.close();
    }
}