@ServiceNeighborhood
Feature: Category service testing
  Background:
  Given these existing Categories saved in the database
	| ID | Category |
	| 1 | C1 |
	| 2 | C2 |
	| 3 | C3 |

	Scenario: Get all existing categories
	When all categories requested
	Then categories "C1" , "C2", "C3" should be returned

  Scenario: Get category by ID
  Given the ID "1"
  Then category "C1" should be returned
    
  Scenario: Add new category
  Given new category "C4"
  Then category should be saved in the database