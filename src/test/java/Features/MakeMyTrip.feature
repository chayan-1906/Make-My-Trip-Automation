#Author: your.email@your.domain.com
#Keywords Summary :
#Feature: List of scenarios.
#Scenario: Business rule through list of steps with arguments.
#Given: Some precondition step
#When: Some key actions
#Then: To observe outcomes or validation
#And,But: To enumerate more Given,When,Then steps
#Scenario Outline: List of steps for data-driven as an Examples and <placeholder>
#Examples: Container for s table
#Background: List of steps run before each of the scenarios
#""" (Doc Strings)
#| (Data Tables)
#@ (Tags/Labels):To group Scenarios
#<> (placeholder)
#""
## (Comments)
#Sample Feature Definition Template
@tag
Feature: Automate Flight Booking through MakeMyTrip

  Background: 
    Given user navigates to makemytrip website

  Scenario Outline: In order to check One way Trip or Round Trip
    Given It is a "<triptype>" trip
    When Selecting options "<fromcity>", "<tocity>", "<departuredate>", "<returndate>", "<triptype>"
    When Enter traveller type like "<adult>", "<children>", "<infant>"
    And Handling Alert Box
    And Selecting "<Airline>" flight
    And Validating "<Airline>", fromcity, tocity in each rows of selection
    And Validating "<departuredate>", returndate of "<triptype>"
    And Sort the grid in order to "<sorter>"
    And Selecting "<nth>" row, "<fareOption>" fare of "<triptype>"
    And Verifying the details of "<fromcity>", "<tocity>" of  "<Airline>", "<triptype>"
    And Remove Charity option
    And Validate total fare
    And Check the insurance
    And Validate total fare
    Then Navigating back

    Examples: 
      | triptype | fromcity | tocity                  | departuredate       | returndate   | adult  | children | infant  | sorter         | Airline  |  nth | fareOption |
      | oneway  | Gaya        | Bhubaneswar  | 10-7-2021           |                         |         1 |              2  |          1 | departure  | IndiGo  |   1    | Saver           |
      | round     | Gaya        | Bhubaneswar  | 3-7-2021           | 9-7-2021      |         3 |              1  |          3 | departure  | IndiGo  |   1    | Saver           |
