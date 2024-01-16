package Bookstore.Bookstore.unit.dal.repositories;

import static org.junit.Assert.assertNull;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.Stream;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import Bookstore.Bookstore.dal.models.Employee;
import Bookstore.Bookstore.dal.models.User;
import Bookstore.Bookstore.dal.models.utils.CustomDate;
import Bookstore.Bookstore.dal.repositories.EmployeeRepository;
import Bookstore.Bookstore.commons.exceptions.EmptyInputException;
import Bookstore.Bookstore.commons.exceptions.NonPositiveInputException;
import Bookstore.Bookstore.commons.exceptions.WrongFormatException;

public class TestEmployeeRepository extends TestRepositoryBase {
	private EmployeeRepository repository;
	private static Employee[] employees;
	
	@BeforeAll
	public static void setUpDummyData() throws EmptyInputException, WrongFormatException, NonPositiveInputException {
		employees = new Employee[7];
		
		for(int i = 0; i < employees.length; i++) {
			User user = new User("a".repeat(i + 5), "foo bar",
					"foo@gmail.com", "Password123",
					"069 123 1233", new CustomDate()
					);
			
			employees[i] = new Employee(user, 1, 1);
		}
	}
	
	@BeforeEach
	public void setUpEach() throws IOException {
		dataFile = new File(dataDir, Employee.class.getSimpleName());
		dataFile.createNewFile();
		
		setTempDataToFile(new ArrayList<Employee>(Arrays.asList(employees)));
		repository = new EmployeeRepository(dataDirPath, dbContext);
	}
	
	@Test
	public void testGetByUsername_NotInDatabase() {
		repository = new EmployeeRepository(dataDirPath, dbContext);
		assertNull(repository.getByUsername("nonExistingUsername"));
	}
	
	@ParameterizedTest
	@MethodSource("provideValuesForExistingData")
	public void testGetByUsername_InDatabase(Employee model) throws EmptyInputException, WrongFormatException, NonPositiveInputException, IOException, ClassNotFoundException {
		assertEquals(model, repository.getByUsername(model.getUsername()));
	}
	
	@Test
	public void testGetById_NotInDatabase() throws EmptyInputException, WrongFormatException, NonPositiveInputException, IOException {		
		assertNull(repository.getById(0));
		assertNull(repository.getById(employees[employees.length - 1].getId() + 1));
	}
	
	@ParameterizedTest
	@MethodSource("provideValuesForExistingData")
	public void testGetById_InDatabase(Employee model) throws EmptyInputException, WrongFormatException, NonPositiveInputException, IOException {
		assertEquals(model, repository.getById(model.getId()));
	}
	
	private static Stream<Arguments> provideValuesForExistingData() {		
		return Stream.of(
			Arguments.of(employees[0]),
			Arguments.of(employees[1]),
			Arguments.of(employees[3]),
			Arguments.of(employees[5]),
			Arguments.of(employees[6])
		);
	}
}
