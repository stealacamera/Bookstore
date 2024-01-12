@ServiceNeighborhood
Feature: Category service testing
  Background:
  Given these existing Categories saved in the database
	| ID | Category |
	| 1 | C1 |
	| 2 | C2 |
	| 3 | C3 |


	Scenario: Get all existing categories
	When All categories requested
	Then Categories "C1" , "C2", "C3" should be returned

  Scenario: Get category by ID
  Given the ID "1"
  Then Category "C1" should be returned
    
  Scenario: Add new category
  Given Category "C4"
  Then Category should be saved in the database
  
  