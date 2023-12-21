package Bookstore.Bookstore.unit.bll.mocks;

import Bookstore.Bookstore.dal.models.Employee;
import Bookstore.Bookstore.dal.repositories.irepositories.IEmployeeRepository;

public class EmployeeRepositoryMock extends RepositoryMock<Employee> implements IEmployeeRepository {
	@Override
	public Employee getById(int id) {
		return dummyData.stream().filter(e -> e.getId() == id).findFirst().orElse(null);
	}

	@Override
	public Employee getByUsername(String username) {
		return dummyData.stream().filter(e -> e.getUsername().equals(username)).findFirst().orElse(null);
	}

}
