package dal.IRepositories;

import models.Employee;

public interface IEmployeeRepository extends IRepository<Employee> {
	Employee getById(int id);
	Employee getByUsername(String username);
}
