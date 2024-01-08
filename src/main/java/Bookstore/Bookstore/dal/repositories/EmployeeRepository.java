package Bookstore.Bookstore.dal.repositories;

import Bookstore.Bookstore.dal.models.Employee;
import Bookstore.Bookstore.dal.repositories.irepositories.IEmployeeRepository;

public class EmployeeRepository extends Repository<Employee> implements IEmployeeRepository {
	// Instances are saved in ascending alphabetical order of username
	public EmployeeRepository(String dataDirPath, DbContext context) {
		super(context.table(dataDirPath, Employee.class));
	}

	@Override
	public Employee getByUsername(String username) {
		return instances.stream().filter(e -> e.getUsername().equals(username)).findFirst().orElse(null);
	}

	@Override
	public Employee getById(int id) {
		return instances.stream().filter(e -> e.getId() == id).findFirst().orElse(null);
	}
}
