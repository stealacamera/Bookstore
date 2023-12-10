package dal;

import dal.IRepositories.IEmployeeRepository;
import exceptions.EmptyInputException;
import exceptions.NonPositiveInputException;
import exceptions.WrongFormatException;
import models.Employee;
import models.User;
import models.utilities.CustomDate;

public class EmployeeRepository extends Repository<Employee> implements IEmployeeRepository {
	// Instances are saved in ascending alphabetical order of username
	public EmployeeRepository(DbContext context) {
		super(context.table(Employee.class));
		seedData();
	}

	@Override
	public Employee getByUsername(String username) {
		return instances.stream().filter(e -> e.getUsername().equals(username)).findFirst().orElse(null);
	}

	@Override
	public Employee getById(int id) {
		return instances.stream().filter(e -> e.getId() == id).findFirst().orElse(null);
	}
	
	private void seedData() {
		if(instances.size() == 0) {
			User librarian, manager, admin;
			
			try {
				librarian = new User("librarian", "Name Surname", "librarian@gmail.com", "Password123", "069 123 123", new CustomDate());
				manager = new User("manager", "Name Surname", "manager@gmail.com", "Password123", "069 123 123", new CustomDate());
				admin = new User("admin", "Name Surname", "admin@gmail.com", "Password123", "069 123 123", new CustomDate());
				
				add(new Employee(admin, 1400.89, 3));
				add(new Employee(librarian, 500, 1));
				add(new Employee(manager, 655.45, 2));
				
				saveChanges();
			} catch (EmptyInputException | NonPositiveInputException | WrongFormatException e) {
				e.printStackTrace();
			}
		}
	}
}
