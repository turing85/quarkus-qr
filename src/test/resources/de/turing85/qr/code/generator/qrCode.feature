Feature: Get QR Code

  Scenario: I can get the expected QR Code
    When I get "Hello world!" as QR-code
    Then I expect to get the correct QR code