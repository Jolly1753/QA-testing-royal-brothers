Feature: Royal Brothers Booking Search

  @positive
  Scenario Outline: Search bikes for a city and apply location filter
    Given user is on Royal Brothers home page
    When user selects city "<city>"
    And user selects pickup and drop date time
    And user clicks on search
    Then listings page should be displayed
    And selected date and time should be visible in filters
    When user applies location filter "<location>"
    Then all listings should belong to location "<location>"
    And user prints bike model and available at details

    Examples:
      | city      | location     |
      | Bangalore | Indiranagar  |

  @negative
  Scenario Outline: Invalid city should not be selectable
    Given user is on Royal Brothers home page
    When user selects invalid city "<invalidCity>"
    Then city modal should remain open

    Examples:
      | invalidCity   |
      | Puneee        |
      | Bangalorrr    |
      | XyzRandomCity |