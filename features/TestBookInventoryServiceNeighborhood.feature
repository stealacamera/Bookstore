@ServiceNeighborhood
Feature: Book service testing
  Background:
  Given these existing Books saved in the database
  |isbn | title | author | supplier | categoryId | purchasedPrice | originalPrice | sellingPrice| stock | purchasedDate|
  |123-4-34-564342-2| foo | bar | fooBar | 3 | 250£ | 251£ | 260£| 40 | 1999-12-12 |
  |123-4-34-564342-3| foo | bar | fooBar | 3 | 250£ | 251£ | 260£| 40 | 1999-12-12 |
  |123-4-34-564342-4| foo | bar | fooBar | 3 | 250£ | 251£ | 260£| 40 | 1999-12-12 |
  

	Scenario: Get all existing books
	When All Books Requested
	Then Return titles 

  Scenario: Get book by ISBN
  Given ISBN "123-4-34-564342-4" of a book
  Then return title "foo" of the book
  
  Scenario: Add new book
  Given a valid DTO of "123-4-34-564342-5" "foo" "bar" "fooBar" "3" "250£" "251£" "260£" "40" "1999-12-12" 
  Then save it on database
  
  Scenario: Update an existing book
  Given ISBN "" of a book
  Then update the title to "foo1"
  And save it on database
  
  Scenario: Update an existing book's stock
  Given an ISBN "" of a book
  Then update the stock to "100"
  And save it on database
  
  Scenario: Remove a book
  Given an ISBN "" of a book
  Then remove the book from database
  And Save the removal