package test.unit.dal;

import static org.junit.Assert.assertNull;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import dal.EmployeeRepository;
import dal.models.Employee;
import dal.models.User;
import dal.models.utilities.CustomDate;
import exceptions.EmptyInputException;
import exceptions.NonPositiveInputException;
import exceptions.WrongFormatException;

public class TestEmployeeRepository extends TestRepositoryBase {
	private EmployeeRepository repository;
	
	@BeforeEach
	void setUpEach() throws IOException {
		dataFile = new File(dataDir, Employee.class.getSimpleName());
		dataFile.createNewFile();
	}
	
	@Test
	void testGetByUsername_NotInDatabase() {
		repository = new EmployeeRepository(dataDirPath, dbContext);
		assertNull(repository.getByUsername("foo"));
	}
	
	@Test
	void testGetByUsername_InDatabase() throws EmptyInputException, WrongFormatException, NonPositiveInputException, IOException, ClassNotFoundException {
		Employee[] employees = createTempData();
		setTempDataToFile(new ArrayList<Employee>(Arrays.asList(employees)));
		repository = new EmployeeRepository(dataDirPath, dbContext);
		
		// Testing at index 0, 1, 3, 5, 6
		for(int i = 0; i < employees.length; i++) {
			if(i == 2 || i == 4) continue;
			assertEquals(employees[i], repository.getByUsername(employees[i].getUsername()));
		}
	}
	
	@Test
	void testGetById_NotInDatabase() throws EmptyInputException, WrongFormatException, NonPositiveInputException, IOException {
		Employee[] employees = createTempData();
		setTempDataToFile(new ArrayList<>(Arrays.asList(employees)));
		repository = new EmployeeRepository(dataDirPath, dbContext);
		
		assertNull(repository.getById(0));
		assertNull(repository.getById(employees[employees.length - 1].getId() + 1));
	}
	
	@Test
	void testGetById_InDatabase() throws EmptyInputException, WrongFormatException, NonPositiveInputException, IOException {
		Employee[] employees = createTempData();
		setTempDataToFile(new ArrayList<Employee>(Arrays.asList(employees)));
		repository = new EmployeeRepository(dataDirPath, dbContext);
		
		// Testing at index 0, 1, 3, 5, 6 for id 1, 2, 4, 6, 7
		for(int i = 0; i < employees.length; i++) {
			if(i == 2 || i == 4) continue;
			assertEquals(employees[i], repository.getById(employees[i].getId()));
		}
	}
	
	private Employee[] createTempData() throws EmptyInputException, WrongFormatException, NonPositiveInputException {
		Employee[] employees = new Employee[7];
		
		for(int i = 0; i < employees.length; i++) {
			User user = new User("a".repeat(i + 5), "foo bar",
					"foo@gmail.com", "Password123",
					"069 123 1233", new CustomDate()
					);
			
			employees[i] = new Employee(user, 1, 1);
		}
		
		return employees;
	}
}
