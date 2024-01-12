package Bookstore.Bookstore.integration.features.stepDefinitions;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.File;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import Bookstore.Bookstore.bll.dto.EmployeeDTO;
import Bookstore.Bookstore.bll.dto.UserDTO;
import Bookstore.Bookstore.bll.services.EmployeeService;
import Bookstore.Bookstore.bll.services.iservices.IEmployeeService;
import Bookstore.Bookstore.commons.exceptions.EmptyInputException;
import Bookstore.Bookstore.commons.exceptions.ExistingObjectException;
import Bookstore.Bookstore.commons.exceptions.IncorrectPermissionsException;
import Bookstore.Bookstore.commons.exceptions.NonPositiveInputException;
import Bookstore.Bookstore.commons.exceptions.WrongFormatException;
import Bookstore.Bookstore.commons.exceptions.WrongLengthException;
import Bookstore.Bookstore.commons.utils.Utils;
import Bookstore.Bookstore.dal.models.Employee;
import Bookstore.Bookstore.dal.models.User;
import Bookstore.Bookstore.dal.models.utils.CustomDate;
import Bookstore.Bookstore.dal.repositories.DbContext;
import Bookstore.Bookstore.dal.repositories.EmployeeRepository;
import Bookstore.Bookstore.dal.repositories.irepositories.IEmployeeRepository;
import io.cucumber.java.After;
import io.cucumber.java.AfterAll;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

public class TestEmployeeServiceNeighborhood {
	private static String tempDataDirPath = Utils.testingUserDataDirPath;
	private IEmployeeRepository repository = new EmployeeRepository(tempDataDirPath, new DbContext());
	private IEmployeeService service = new EmployeeService(repository);
	
	private ArrayList<Employee> data;
	private EmployeeDTO dtoModelInUse;
	
	@AfterAll
	public static void deleteFolder() {
		File dataDir = new File(tempDataDirPath);
		File parentDir = dataDir.getParentFile();
		
		dataDir.delete();
		parentDir.delete();
	}
	
	@After
	public void deleteData() {
		new File(tempDataDirPath).delete();
		repository = new EmployeeRepository(tempDataDirPath, new DbContext());
	}
	
	@Given("these existing employees saved in the database")
	public void these_existing_employees_saved_in_the_database(io.cucumber.datatable.DataTable dataTable) {
		List<Map<String, String>> rowData = dataTable.asMaps();
		this.data = new ArrayList<>();
		
		for(Map<String, String> data: rowData) {
			try {
				User user = new User(
						data.get("username"), data.get("full name"), 
						data.get("email"), data.get("password"), 
						data.get("phone number"), new CustomDate()
					);
					
					Employee employee = new Employee(user, Double.parseDouble(data.get("salary")), Integer.parseInt(data.get("access level")));
				
				repository.add(employee);
				this.data.add(employee);
			} catch (EmptyInputException | NonPositiveInputException | WrongFormatException e) {
				e.printStackTrace();
			}
		}
	}
	
	@Given("a DTO with values {string}, {string}, {string}, {string}, {string}, {string}, {string}")
	public void a_dto_with_values(String username, String fullName, String email, String password, String phoneNumber, String salary, String accessLevel) {
	    UserDTO user = new UserDTO(username, fullName, email, password, phoneNumber, LocalDate.now());
	    dtoModelInUse = new EmployeeDTO(user, Double.parseDouble(salary), Integer.parseInt(accessLevel));
	}

	@Then("the {string} error message should be shown")
	public void the_error_message_should_be_shown(String errorMessage) {
		Exception error = assertThrows(Exception.class, () -> service.add(dtoModelInUse));
	    assertEquals(errorMessage, error.getMessage());
	}

	@Then("the new employee is not saved")
	public void the_new_employee_is_not_saved() {
		repository.saveChanges();
	    assertEquals(data.size() + 3, repository.count());
	}

	@Then("the new employee should be saved")
	public void the_new_employee_should_be_saved() {
		repository.saveChanges();
	    assertNotNull(service.getByUsername(dtoModelInUse.getUsername()));
	}

	@When("updating employee with values {string}, {string}")
	public void updating_employee_with_values(String fullName, String email) throws ExistingObjectException, EmptyInputException, NonPositiveInputException, WrongFormatException, WrongLengthException, IncorrectPermissionsException {
		Employee model = data.get(0);
		
		UserDTO user = new UserDTO(
			model.getId(), model.getUsername(), model.getFullName(), 
			model.getEmail(), model.getHashedPassword(), model.getPhoneNum(), 
			model.getBirthdate().getDate()
		);
		
//		dtoModelInUse = data.get(0);
//		EmployeeDTO oldModel = new EmployeeDTO(dtoModelInUse);
	    
		EmployeeDTO oldModel = new EmployeeDTO(user, model.getSalary(), model.getAccessLvl());
		dtoModelInUse = new EmployeeDTO(oldModel);
		
	    dtoModelInUse.setFullName(fullName);
	    dtoModelInUse.setEmail(email);
	    
	    service.update(oldModel, dtoModelInUse);
	}

	@Then("the database entity should retain changes")
	public void the_database_entity_should_retain_changes() {
		repository.saveChanges();
	    Employee dbEntity = repository.getByUsername(dtoModelInUse.getUsername());
	    
	    assertEquals(dbEntity.getFullName(), dtoModelInUse.getFullName());
	    assertEquals(dbEntity.getEmail(), dtoModelInUse.getEmail());
	}

	@Given("the employee with username {string}, the old password {string}, new password {string}")
	public void the_employee_with_username_the_old_password_new_password(String username, String oldPassword, String newPassword) throws EmptyInputException, NonPositiveInputException, WrongFormatException, WrongLengthException, IncorrectPermissionsException {
		dtoModelInUse = service.getByUsername(username);
		service.changePassword(dtoModelInUse, oldPassword, newPassword);
	}
	
	@Then("the database entity password should be changed to {string}")
	public void the_database_entity_password_should_be_changed_to(String password) throws EmptyInputException {
	    repository.saveChanges();
	    assertTrue(repository.getByUsername(dtoModelInUse.getUsername()).isCorrectPassword(password));
	}

	@Given("the index {string}")
	public void the_index(String index) {
		int parsedIndex = Integer.parseInt(index);
		
	    dtoModelInUse = service.get(parsedIndex);
	    service.remove(parsedIndex);
	}

	@Then("the entity with username {string} should not be in the database")
	public void the_entity_with_username_should_not_be_in_the_database(String username) {
	    repository.saveChanges();
	    assertNull(repository.getByUsername(username));
	}

	@Given("the username {string} and password {string}")
	public void the_username_and_password(String username, String password) {
	    dtoModelInUse.setUsername(username);
	    dtoModelInUse.setPassword(password);
	}

	@Then("a truthy value should be returned")
	public void a_truthy_value_should_be_returned() {
	    try {
			assertTrue(service.canLogin(dtoModelInUse.getUsername(), dtoModelInUse.getPassword()));
		} catch (EmptyInputException e) {
			// Will not happen
		}
	}
}
