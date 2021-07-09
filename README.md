# CoxCodingExercise - Amit Patel

## This is a Spring Boot RESTful Web Service that requires Java 1.8 or later.

## Setup
* After cloning this repo, open a terminal and cd into the repo directory.
* Type `./gradlew bootRun` to run this project as a Spring Boot application on local port 8080.

## Resources 
* A Postman collection for the REST endpoints are in postman directory and can be imported into Postman.  
* Equivalent cURL commands are also provided in the curl directory.  
* Javadocs are in the docs directory (index.html).  
* The business logic is in the YahooFinanceStockService which implements the StockService interface.  
* Test cases are in StocksControllerTests.java under src/test and run tests against the service at the controller level.  The Yahoo Finance API is not mocked so the tests need a live connection to the Internet run properly.




