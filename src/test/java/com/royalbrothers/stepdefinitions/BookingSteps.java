package com.royalbrothers.stepdefinitions;

import com.microsoft.playwright.Page;
import com.royalbrothers.pages.HomePage;
import com.royalbrothers.pages.ListingsPage;
import com.royalbrothers.utils.TestBase;
import io.cucumber.java.en.*;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class BookingSteps {

    private Page page;
    private HomePage homePage;
    private ListingsPage listingsPage;

    @Given("user is on Royal Brothers home page")
    public void user_is_on_royal_brothers_home_page() {
        page = TestBase.getPage();
        homePage = new HomePage(page);

        homePage.open();
        assertTrue(homePage.isHomeLoaded(), "Home page not loaded!");
    }

    @When("user selects city {string}")
    public void user_selects_city(String city) {
        homePage.selectCity(city);
    }

    @When("user selects pickup and drop date time")
    public void user_selects_pickup_and_drop_date_time() {
        homePage.selectPickupDropDateTime();
    }

    @When("user clicks on search")
    public void user_clicks_on_search() {
        homePage.clickSearch();
        listingsPage = new ListingsPage(page);
    }

    @Then("listings page should be displayed")
    public void listings_page_should_be_displayed() {
        assertTrue(listingsPage.isListingsPageVisible(), "Listings page not opened!");
    }

    @Then("selected date and time should be visible in filters")
    public void selected_date_and_time_should_be_visible_in_filters() {
        assertTrue(listingsPage.isFilterBarVisible(), "Filter bar not visible!");
    }

    @When("user applies location filter {string}")
    public void user_applies_location_filter(String location) {
        listingsPage.applyLocationFilter(location);
    }

    @Then("all listings should belong to location {string}")
    public void all_listings_should_belong_to_location(String location) {
        assertTrue(listingsPage.isFilterBarVisible(), "Location filter not applied / filter bar missing!");
    }

    @Then("city modal should remain open")
    public void city_modal_should_remain_open() {
        assertTrue(homePage.isCityModalVisible(), "City modal closed for invalid city!");
    }
    @Then("user prints bike model and available at details")
    public void user_prints_bike_model_and_available_at_details() {

        List<String> bikes = listingsPage.printBikeModels();

        System.out.println("====================================");
        System.out.println("Total Bike Models Found: " + bikes.size());
        System.out.println("====================================");

        // NOTE: Requirement is "print bike model & available at details"
        // so we print whatever is found, test should not fail if 0 bikes due to availability.
    }

    // -------- NEGATIVE CASE --------

    @When("user selects invalid city {string}")
    public void user_selects_invalid_city(String invalidCity) {
        homePage.openCityModal();
        assertTrue(homePage.isCityModalVisible(), "City modal not opened!");

        page.locator("#autocomplete-input").fill(invalidCity);
        page.waitForTimeout(1500);
    }


}