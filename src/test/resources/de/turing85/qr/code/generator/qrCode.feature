Feature: Get QR Code

  Scenario: I can get the expected QR Code
    When I get a QR Code for the text "Hello, world!"
    Then I expect the QR code to decode to that text

  Scenario: No data leads to a bad request response
    When I pass no text to generate a QR Code
    Then I expect to receive a bad request response

  Scenario: Empty data leads to a bad request response
    When I get a QR Code for the text ""
    Then I expect to receive a bad request response