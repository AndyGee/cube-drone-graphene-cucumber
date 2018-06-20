Feature: My First Feature

  Scenario: Perform some simple steps
    Given the system properties are printed
    When the page opens in the docker container
    Then wait for curl localhost:6666 if -Dwait=true