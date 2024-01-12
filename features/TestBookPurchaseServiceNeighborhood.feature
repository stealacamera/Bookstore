@ServiceNeighborhood
Feature: Book purchases service testing
  Background:
  Given these existing Purchases saved in the database
	| amount | date |
	| 1 | 1999-12-12 |
	| 1 | 1999-12-12 |
	| 2 | 1999-12-12 |


	Scenario: Get all existing purchases
	When All purchases requested
	Then Categories "1" "1999-12-12","1" "1999-12-12","1" "1999-12-12" should be returned
	
  
  Scenario: Add new purchase
  When Amount "3" is given 
  Then New book purchase should be saved in database