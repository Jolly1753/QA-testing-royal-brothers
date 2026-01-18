package com.royalbrothers.pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.WaitForSelectorState;

public class HomePage {

    private final Page page;

    // City modal
    private final Locator cityModal;
    private final Locator cityInput;

    // Booking card inputs
    private final Locator pickupDate;
    private final Locator pickupTime;
    private final Locator dropoffDate;
    private final Locator dropoffTime;

    // Search button
    private final Locator searchBtn;

    public HomePage(Page page) {
        this.page = page;

        this.cityModal = page.locator("#modal-city");
        this.cityInput = page.locator("#autocomplete-input");

        this.pickupDate = page.locator("#pickup-date-other");
        this.pickupTime = page.locator("#pickup-time-other");
        this.dropoffDate = page.locator("#dropoff-date-other");
        this.dropoffTime = page.locator("#dropoff-time-other");

        this.searchBtn = page.locator("button.buttonLarge[type='submit']:visible");
    }

    public void open() {
        page.navigate("https://www.royalbrothers.com/");
        page.waitForLoadState();
        page.waitForTimeout(1500);
    }

    public boolean isHomeLoaded() {
        return page.url().contains("royalbrothers.com");
    }

    public boolean isCityModalVisible() {
        return cityModal.isVisible();
    }

    public void openCityModal() {
        Locator cityOpenBtn = page.locator("a.current-city").first();
        if (cityOpenBtn.count() > 0) {
            cityOpenBtn.click();
        }

        cityModal.waitFor(new Locator.WaitForOptions()
                .setState(WaitForSelectorState.VISIBLE)
                .setTimeout(20000));
    }

    public void selectCity(String city) {

        openCityModal();

        cityInput.waitFor(new Locator.WaitForOptions()
                .setState(WaitForSelectorState.VISIBLE)
                .setTimeout(20000));

        cityInput.click();
        cityInput.fill(city);

        Locator matchingCity = page.locator("a.city-box.city-box-true")
                .filter(new Locator.FilterOptions().setHasText(city))
                .first();

        matchingCity.waitFor(new Locator.WaitForOptions()
                .setState(WaitForSelectorState.VISIBLE)
                .setTimeout(20000));

        matchingCity.click();

        cityModal.waitFor(new Locator.WaitForOptions()
                .setState(WaitForSelectorState.HIDDEN)
                .setTimeout(20000));

        page.waitForTimeout(1200);
    }

    // ---------------- Helpers ----------------

    private void waitVisible(Locator locator) {
        locator.waitFor(new Locator.WaitForOptions()
                .setState(WaitForSelectorState.VISIBLE)
                .setTimeout(20000));
    }

    private void scrollToCenter(Locator locator) {
        waitVisible(locator);
        locator.scrollIntoViewIfNeeded();
        page.evaluate("el => el.scrollIntoView({behavior:'instant', block:'center'})", locator.elementHandle());
        page.waitForTimeout(300);
    }

    private void safeClick(Locator locator) {
        scrollToCenter(locator);
        locator.click(new Locator.ClickOptions().setForce(true));
        page.waitForTimeout(400);
    }

    private void selectDayFromCalendar(String rootId, String dayText) {
        Locator root = page.locator("#" + rootId);
        root.waitFor(new Locator.WaitForOptions()
                .setState(WaitForSelectorState.ATTACHED)
                .setTimeout(20000));

        Locator day = root.locator(".picker__day.picker__day--infocus:not(.picker__day--disabled)")
                .filter(new Locator.FilterOptions().setHasText(dayText))
                .first();

        waitVisible(day);
        day.click(new Locator.ClickOptions().setForce(true));
        page.waitForTimeout(600);
    }

    private void openDateAndSelect(Locator dateInput, String rootId, String dayText) {
        safeClick(dateInput);
        selectDayFromCalendar(rootId, dayText);
    }

    private void selectTimeExact(Locator timeInput, String rootId, String timeText) {
        safeClick(timeInput);

        Locator root = page.locator("#" + rootId);
        root.waitFor(new Locator.WaitForOptions()
                .setState(WaitForSelectorState.ATTACHED)
                .setTimeout(20000));

        Locator option = root.locator("li.picker__list-item")
                .filter(new Locator.FilterOptions().setHasText(timeText))
                .first();

        waitVisible(option);
        option.scrollIntoViewIfNeeded();
        option.click(new Locator.ClickOptions().setForce(true));
        page.waitForTimeout(600);
    }

    // ---------------- Main Flow ----------------

    public void selectPickupDropDateTime() {

        // Pickup Date
        openDateAndSelect(pickupDate, "pickup-date-other_root", "25");

        // Pickup Time
        selectTimeExact(pickupTime, "pickup-time-other_root", "10:00 AM");

        page.waitForTimeout(1200);

        // Dropoff Date
        openDateAndSelect(dropoffDate, "dropoff-date-other_root", "27");

        page.waitForTimeout(1200);

        // Dropoff Time
        selectTimeExact(dropoffTime, "dropoff-time-other_root", "10:00 AM");
    }

    public void clickSearch() {
        safeClick(searchBtn);
        page.waitForLoadState();
        page.waitForTimeout(1500);
    }
}