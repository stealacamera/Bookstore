@ServiceNeighborhood
Feature: Bill service testing
  Background:
  Given these existing Bills saved in the database
  | sellerId | saleAmount | numOfBooks | date |
  |1| 1 | 1 | 1999-12-12 |
  |2| 2 | 2 | 1999-12-12 | 
  |3| 3 | 3 | 1999-12-12 | 
  
  Scenario: Get all existing bills
  When all bills requested
  Then return all bills 
  
  Scenario: Remove a bill
  Given SelleId "2" 
  Then remove the bill from database
  And save it