package Bookstore.Bookstore.integration.serviceNeighborhoods.stepDefinitions;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import Bookstore.Bookstore.TestingUtils;
import Bookstore.Bookstore.bll.dto.EmployeeDTO;
import Bookstore.Bookstore.bll.dto.UserDTO;
import Bookstore.Bookstore.bll.services.EmployeeService;
import Bookstore.Bookstore.bll.services.iservices.IEmployeeService;
import Bookstore.Bookstore.commons.utils.Utils;
import Bookstore.Bookstore.dal.models.Employee;
import Bookstore.Bookstore.dal.models.User;
import Bookstore.Bookstore.dal.models.utils.CustomDate;
import Bookstore.Bookstore.dal.repositories.DbContext;
import Bookstore.Bookstore.dal.repositories.EmployeeRepository;
import Bookstore.Bookstore.dal.repositories.irepositories.IEmployeeRepository;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

public class TestEmployeeServiceNeighborhood {
	private static IEmployeeRepository repository;
	private static IEmployeeService service;
	
	private ArrayList<Employee> data;
	private EmployeeDTO dtoModelInUse = new EmployeeDTO();
	
	@Before
	public void recreateDatabase() {		
		repository = new EmployeeRepository(Utils.testDataDirPath, new DbContext());
		service = new EmployeeService(repository);
	}
	
	@After
	public void tearDownDatabase() {
		TestingUtils.deleteTestDatabase(null);
	}
	
	@Given("these existing employees saved in the database")
	public void these_existing_employees_saved_in_the_database(io.cucumber.datatable.DataTable dataTable) throws Exception {
		List<Map<String, String>> rowData = dataTable.asMaps();
		this.data = new ArrayList<>();
		
		for(Map<String, String> data: rowData) {
			Employee employee = new Employee(
				new User(
					data.get("username"), data.get("full name"), 
					data.get("email"), data.get("password"), 
					data.get("phone number"), new CustomDate(data.get("birthdate"))), 
				Double.parseDouble(data.get("salary")), 
				Integer.parseInt(data.get("access level"))
			);
			
			repository.add(employee);
			this.data.add(employee);
		}
	}
	
	@Given("a DTO with values {string}, {string}, {string}, {string}, {string}, {string}, {string}")
	public void a_dto_with_values(String username, String fullName, String email, String password, String phoneNumber, String salary, String accessLevel) {
	    dtoModelInUse = new EmployeeDTO(
    		new UserDTO(username, fullName, email, password, phoneNumber, LocalDate.now()), 
    		Double.parseDouble(salary), 
    		Integer.parseInt(accessLevel)
		);
	}

	@Then("the {string} error message should be shown")
	public void the_error_message_should_be_shown(String errorMessage) {
		Exception error = assertThrows(Exception.class, () -> service.add(dtoModelInUse));
	    assertEquals(errorMessage, error.getMessage());
	}

	@Then("the new employee is not saved in the database")
	public void the_new_employee_is_not_saved_in_the_database() {
		repository.saveChanges();
		
		assertEquals(data.size(), repository.count());
	}

	@Then("the new employee should be saved in the database")
	public void the_new_employee_should_be_saved_in_the_database() throws Exception {
		service.add(dtoModelInUse);
		repository.saveChanges();
		
	    assertNotNull(service.getByUsername(dtoModelInUse.getUsername()));
	}

	@When("updating employee with new full name {string}, new email {string}")
	public void updating_employee_with_new_full_name_new_email(String fullName, String email) throws Exception {
		EmployeeDTO oldModel = service.get(0);
		dtoModelInUse = new EmployeeDTO(oldModel);
		
	    dtoModelInUse.setFullName(fullName);
	    dtoModelInUse.setEmail(email);
	    
	    service.update(oldModel, dtoModelInUse);
	}

	@Then("the database entity should retain changes")
	public void the_database_entity_should_retain_changes() throws Exception {
		repository.saveChanges();
		
		Employee expectedEntity = new Employee(
			new User(dtoModelInUse.getId(), dtoModelInUse.getUsername(), dtoModelInUse.getFullName(), 
					dtoModelInUse.getEmail(), dtoModelInUse.getHashedPassword(), dtoModelInUse.getPhoneNum(), 
					new CustomDate(dtoModelInUse.getBirthdate())), 
			dtoModelInUse.getSalary(), 
			dtoModelInUse.getAccessLvl()
		);
		
	    assertEquals(expectedEntity, repository.getByUsername(dtoModelInUse.getUsername()));
	}

	@Given("the employee with username {string}, the old password {string}, new password {string}")
	public void the_employee_with_username_the_old_password_new_password(String username, String oldPassword, String newPassword) throws Exception {
		dtoModelInUse = service.getByUsername(username);
		service.changePassword(dtoModelInUse, oldPassword, newPassword);
	}
	
	@Then("the database entity password should be {string}")
	public void the_database_entity_password_should_be(String password) throws Exception {
		repository.saveChanges();
		assertTrue(repository.getByUsername(dtoModelInUse.getUsername()).isCorrectPassword(password));
	}

	@Given("the username {string} and password {string}")
	public void the_username_and_password(String username, String password) {
	    dtoModelInUse.setUsername(username);
	    dtoModelInUse.setPassword(password);
	}

	@Then("a truthy value should be returned")
	public void a_truthy_value_should_be_returned() throws Exception {
		assertTrue(service.canLogin(dtoModelInUse.getUsername(), dtoModelInUse.getPassword()));
	}
}
