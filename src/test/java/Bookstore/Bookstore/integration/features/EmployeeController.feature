#Feature: Employee CRUD functionalities
#	Background:
#	Given the employee management page is showing
#	And the user clicks on the add button
#	And the create form is shown
#	And these existing employees
#	| username | full name | email | password | phone number | birthdate | salary | access level |
#	| tempUsername1 | foo bar | foo@gmail.com | password123 | 069 123 1233 | 1999-12-12 | 2000 | 1 |
#	| tempUsername2 | foo bar | foo@gmail.com | password123 | 069 123 1233 | 1999-12-12 | 2000 | 2 |
#
#	Scenario Outline: Add invalid new employee information
#		When the user fills the form with '<username>', '<full name>', '<email>', '<password>', '<phone number>', '<birthdate>', '<salary>', '<access level>'
#		And the user clicks the submit button
#		Then the '<error>' error message should be shown
#		And the new employee is not saved
#		
#		Examples:
#		| username | full name | email | password | phone number | birthdate | salary | access level | error |
#		| [blank] | [blank] | [blank] | [blank] | [blank] | [blank] | [blank] | [blank] | Input fields cannot be empty: Please enter a value for username |
#		| foo | [blank] | [blank] | [blank] | [blank] | [blank] | [blank] | [blank] | Incorrect username format: Correct format is 5-25 characters |
#		| tempUsername | foo | [blank] | [blank] | [blank] | [blank] | [blank] | [blank] | Incorrect full name format: Correct format is (first name) (last name) |
#		| tempUsername | foo bar | foo | [blank] | [blank] | [blank] | [blank] | [blank] | Incorrect email format: Correct format is (2 characters)@gmail.com |
#		| tempUsername | foo bar | foo@gmail.com | foo | [blank] | [blank] | [blank] | [blank] | Incorrect password format: Correct format is at least 8 characters with at least 1 digit and 1 letter |
#		| tempUsername | foo bar | foo@gmail.com | foo123 | [blank] | [blank] | [blank] | [blank] | Incorrect password format: Correct format is at least 8 characters with at least 1 digit and 1 letter |
#		| tempUsername | foo bar | foo@gmail.com | foobar123| 123 | [blank] | [blank] | [blank] | Incorrect phone number format: Correct format is 06X XXX XXXX |
#		| tempUsername | foo bar | foo@gmail.com | foobar123| 0691231233 | [blank] | [blank] | [blank] | Incorrect phone number format: Correct format is 06X XXX XXXX |
#		| tempUsername | foo bar | foo@gmail.com | foobar123| 069 123 1233 | 1999-01-01 | -1 | [blank] | Incorrect salary: Please enter a positive number for the field |
#	
#	Scenario: Add valid new employee information with an existing username
#		When the user fills the form with 'tempUsername2', 'bar foo', 'bar@gmail.com', 'password123', '069 123 1233', '1999-01-01', '2000', '2'
#		And the user clicks the submit button
#		Then the 'Element with these details (username) already exists' error message should be shown
#		And the new employee is not saved
#	
#	Scenario: Add valid new employee information
#		When the user fills the form with 'tempUsername3', 'bar foo', 'bar@gmail.com', 'password123', '069 123 1233', '1999-01-01', '2000', '2'
#		And the user clicks the submit button
#		Then the user should be back to the employee management page
#		And the new employee should be saved
#		And the new employee should be shown in the list