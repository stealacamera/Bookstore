@ServiceNeighborhood
Feature: Book service testing
  Background:
  Given these existing books saved in the database
  | isbn | title | author | supplier | categoryId | purchasedPrice | originalPrice | sellingPrice| stock | purchasedDate |
  |123-4-34-564342-2| foo | bar | fooBar | 3 | 250 | 251 | 260 | 40 | 12/12/1999 |
  |123-4-34-564342-3| foo12 | bar | fooBar | 3 | 252 | 253 | 264 | 45 | 12/12/1998 |
  |123-4-34-564342-4| foo23 | bar | fooBar | 3 | 251 | 252 | 261 | 41 | 12/10/2000 |
  
  Scenario: Get book by ISBN
  Given ISBN "123-4-34-564342-4" of a book
  Then return book with title "foo23", author "bar", supplier "fooBar", category id "3", purchased price "251", original price "252", selling price "261", stock "41", purchased date "12/10/2000"
  
  Scenario: Add new book
  Given a valid DTO of "123-4-34-564342-5" "foo" "bar" "fooBar" "3" "250" "251" "260" "40" "12/12/1999" 
  Then the entity should be saved in the database
  
  Scenario: Update the stock of an existing book
  Given an ISBN "123-4-34-564342-2" of a book
  Then update the stock to "100"
  And the stock of the database entity should be updated