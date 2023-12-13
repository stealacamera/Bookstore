package dal.IRepositories;

import dal.models.Employee;

public interface IEmployeeRepository extends IRepository<Employee> {
	Employee getById(int id);
	Employee getByUsername(String username);
}
