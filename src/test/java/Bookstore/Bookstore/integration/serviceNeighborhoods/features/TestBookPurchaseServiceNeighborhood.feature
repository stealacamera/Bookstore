@ServiceNeighborhood
Feature: Book purchases service testing
  Background:
  Given these existing purchases saved in the database
	| amount | date |
	| 123 | 12/12/1999 |
	| 341 | 12/12/1999 |
	| 20 | 12/12/1999 |

	Scenario: Get all existing purchases
	When all purchases requested
	Then purchases "123" "12/12/1999", "341" "12/12/1999", "20" "12/12/1999" should be returned
	
  Scenario: Add new purchase
  When amount "53" is given 
  Then new book purchase should be saved in database with the current date