package Bookstore.Bookstore.dal.repositories.irepositories;

import Bookstore.Bookstore.dal.models.Employee;

public interface IEmployeeRepository extends IRepository<Employee> {
	Employee getById(int id);
	Employee getByUsername(String username);
}
