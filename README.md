## Bookstore management application
A 3-tier user system handling the management of a bookstore's stock, employees, and finances, through JavaFX.

### Roles & their capabilities/permissions:
1. Librarian:
    * Creates bills for a list of books a client buys (for each bill, a printable .txt file of the bill is created in the user's computer)
2. Manager
    * Adds new books to storage
    * Handles stock
    * Checks performance of librarian's sales
    * Checks the financial reports/statistics of the store (sales, costs, etc.)
3. Administrators
    * All capabilities of previous roles
    * Manages employees (CRUD functionalities regarding basic information details e.g.: name, email, etc., and their allowed permissions)
---
### Project structure:
  * n-layered MVC architecture
  * Repository & unit of work patterns
  * Dependency injection

### Functionality details:
  * Data saved through local serialized files
  * Statistical reports created through JavaFX graphical reports
---
### Testing
All three levels of testing created:
  * Unit level (equivalence testing with mocking)
  * Integration (call-graph neighborhood integration & BDD testing)
  * System (use-case based)

[Code coverage Report (rendered file)](https://htmlpreview.github.io/?https://github.com/stealacamera/software-testing-project/blob/main/Reports/jacoco-overall-report/index.html)\
[SpotBugs Report (rendered file)](https://htmlpreview.github.io/?https://github.com/stealacamera/software-testing-project/blob/main/Reports/SpotBugs%20Report.html)
