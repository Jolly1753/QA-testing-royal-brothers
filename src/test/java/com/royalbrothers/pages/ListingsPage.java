package com.royalbrothers.pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.WaitForSelectorState;

import java.util.ArrayList;
import java.util.List;

public class ListingsPage {

    private final Page page;

    private final Locator filterSection;
    private final Locator locationFilterBtn;

    private final Locator bikeCards;
    private final Locator bikeModelNames;

    public ListingsPage(Page page) {
        this.page = page;

        // Filter section visible check
        this.filterSection = page.locator("text=Filter").first();

        // Location filter button
        this.locationFilterBtn = page.locator("text=Location").first();

        // Bike cards container (multiple fallbacks)
        this.bikeCards = page.locator(
                "#bikes .col-md-4, #bikes .col-lg-4, #bikes .product-card, " +
                        ".bikeCard, .productCard, .listingCard"
        );

        // Bike model names (scoped inside #bikes)
        this.bikeModelNames = page.locator("#bikes h3, #bikes .bikeName, #bikes .productTitle");
    }

    public boolean isListingsPageVisible() {
        return page.url().contains("search") || page.url().contains("bikes") || page.url().contains("booking");
    }

    public boolean isFilterBarVisible() {
        return filterSection.count() > 0;
    }

    public void waitForListingsToLoad() {
        page.waitForLoadState();
        page.waitForTimeout(2000);
    }

    public void applyLocationFilter(String location) {

        waitForListingsToLoad();

        locationFilterBtn.waitFor(new Locator.WaitForOptions()
                .setState(WaitForSelectorState.VISIBLE)
                .setTimeout(20000));

        locationFilterBtn.click();
        page.waitForTimeout(1500);

        // Location option click
        Locator option = page.locator("text=" + location).first();

        option.waitFor(new Locator.WaitForOptions()
                .setState(WaitForSelectorState.VISIBLE)
                .setTimeout(20000));

        option.click();
        page.waitForTimeout(2000);

        System.out.println("Location filter applied: " + location);
    }

    public List<String> printBikeModels() {
        List<String> result = new ArrayList<>();

        int count = bikeModelNames.count();
        for (int i = 0; i < Math.min(count, 10); i++) {
            String name = bikeModelNames.nth(i).innerText().trim();
            result.add(name);
            System.out.println("Bike Model: " + name);
        }

        return result;
    }
}
