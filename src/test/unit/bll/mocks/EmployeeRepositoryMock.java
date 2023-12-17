package test.unit.bll.mocks;

import dal.IRepositories.IEmployeeRepository;
import dal.models.Employee;

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
