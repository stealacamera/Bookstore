@ServiceNeighborhood
Feature: Bill service testing
  Background:
  	Given these existing bills saved in the database
  | sellerId | saleAmount | numOfBooks | date |
  | 1 | 1 | 1 | 12/12/1999 |
  | 2 | 2 | 2 | 13/11/1989 | 
  | 3 | 3 | 3 | 03/02/2000 | 
  
  Scenario: Get all existing bills
  When all bills requested
  Then return all bills 
  
  Scenario: Add a new bill
  Given the seller id "2", sale amount "123", nr. of books "3" 
  Then add the bill
  And the database should contain the new bill with the current date