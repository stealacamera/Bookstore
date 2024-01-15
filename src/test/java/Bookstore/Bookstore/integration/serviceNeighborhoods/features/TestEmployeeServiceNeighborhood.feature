@ServiceNeighborhood
Feature: Employee service testing
	Background:
		Given these existing employees saved in the database
	| username | full name | email | password | phone number | birthdate | salary | access level |
	| tempUsername1 | foo bar | foo1@gmail.com | password123 | 069 123 1233 | 12/12/1999 | 2000 | 1 |
	| tempUsername2 | foo bar | foo2@gmail.com | password123 | 069 123 1233 | 12/12/1999 | 2000 | 2 |
	| tempUsername3 | foo bar | foo3@gmail.com | password123 | 069 123 1233 | 12/12/1999 | 2000 | 1 |
  
  Scenario Outline: Add new employee with invalid values
  	Given a DTO with values '<username>', '<full name>', '<email>', '<password>', '<phone number>', '<salary>', '<access level>'
  	Then the '<error>' error message should be shown
		And the new employee is not saved in the database

		Examples:
		| username | full name | email | password | phone number | salary | access level | error |
		| foo | foo bar | foo@gmail.com | foo123 | 069 123 1233 | -1 | 1 | Incorrect username format: Correct format is 5-25 characters |
		| foobar | foo bar | foo@gmail.com | foo | 069 123 1233 | -1 | 1 | Incorrect password format: Correct format is at least 8 characters with at least 1 digit and 1 letter |
		| foobar | foo bar | foo@gmail.com | foobar123 | 123 | -1 | 1 | Incorrect phone number format: Correct format is 06X XXX XXXX |
		| foobar | foo bar | foo@gmail.com | foobar123 | 069 123 1233 | -1 | 1 | Incorrect salary: Please enter a positive number for the field |
	  
  Scenario: Add new employee with existing username
		Given a DTO with values 'tempUsername2', 'bar foo', 'bar@gmail.com', 'password123', '069 123 1233', '2000', '2'
		Then the 'Element with these details (username) already exists.' error message should be shown
		And the new employee is not saved in the database
	
	Scenario: Add new employee with valid values
		Given a DTO with values 'tempUsername4', 'bar foo', 'bar@gmail.com', 'password123', '069 123 1233', '2000', '2'
		Then the new employee should be saved in the database
  
  Scenario: Update an existing employee
  	When updating employee with new full name 'bar foo', new email 'barfoo@gmail.com'
  	Then the database entity should retain changes
  
  Scenario Outline: Change an existing employees password
  	Given the employee with username 'tempUsername1', the old password '<oldPassword>', new password '<newPassword>'
  	Then the database entity password should be '<password>'
  	
  	Examples:
	  | oldPassword | newPassword | password |
	  | wrongPassword | foobar123 | password123 |
	  | password123 | foobar123 | foobar123 |
  
	Scenario: Validate login
		Given the username 'tempUsername2' and password 'password123'
		Then a truthy value should be returned