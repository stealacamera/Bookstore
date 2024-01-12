package Bookstore.Bookstore.integration;

import static org.junit.Assert.assertTrue;

import java.time.LocalDate;
import java.util.stream.Stream;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.testfx.api.FxRobot;
import org.testfx.framework.junit5.ApplicationExtension;
import org.testfx.framework.junit5.Start;

import Bookstore.Bookstore.TestingUtils;
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
import Bookstore.Bookstore.controllers.LoginController;
import Bookstore.Bookstore.dal.repositories.DbContext;
import Bookstore.Bookstore.dal.repositories.EmployeeRepository;
import Bookstore.Bookstore.dal.repositories.irepositories.IEmployeeRepository;
import javafx.scene.Scene;
import javafx.stage.Stage;

@ExtendWith(ApplicationExtension.class)
public class TestLoginIndexViewNeighborhood extends TestNeighborHoodBase {
	private static IEmployeeRepository repository;
	private static IEmployeeService service;
	private static LoginController controller;
	FxRobot robot = new FxRobot();	
	
	@BeforeAll
	private static void setUp() throws ExistingObjectException, EmptyInputException, NonPositiveInputException, WrongFormatException, WrongLengthException, IncorrectPermissionsException {
		repository = new EmployeeRepository(Utils.testingUserDataDirPath, new DbContext());
		service = new EmployeeService(repository);
		controller = new LoginController(service);
		
		setUpTempData();
	}
	
	private static void setUpTempData() throws ExistingObjectException, EmptyInputException, NonPositiveInputException, WrongFormatException, WrongLengthException, IncorrectPermissionsException {
		for(int i = 0; i < 3; i++) {
			UserDTO user = new UserDTO("foobar" + i, "foo bar", "foo" + i + "@gmail.com", "password123", "069 123 1234", LocalDate.now());
			EmployeeDTO employee = new EmployeeDTO(user, 200, 1);
			employee.setPassword("password123");
			
			service.add(employee);
		}
	}	
	
	@Start
	private void start(Stage stage) {
		stage.setScene(new Scene(controller.getIndexView()));
		stage.show();
	}
	
	@ParameterizedTest
	@MethodSource("provideInvalidValues")
	void testLogin_InvalidValues(String username, String password, String errorMessage) {		
		fillForm(username, password);
		
		// Check for error pop-up
		TestingUtils.testErrorMessage(robot, errorMessage);
	}
	
	@Test
	void testLogin_ValidValues_NonexistingUser() {
		String errorMessage = "Incorrect username and/or password. Please try again.";
		
		fillForm("fake user", "fakepassword");
		
		// Check for error pop-up
		TestingUtils.testErrorMessage(robot, errorMessage);
	}
	
	@Test
	void testLogin_ValidValues_ExistingUser() {
		fillForm("foobar1", "password123");
		assertTrue(robot.listWindows().size() == 0);
	}
	
	private void fillForm(String username, String password) {
		robot.clickOn("#username").write(username);
		robot.clickOn("#password").write(password);
		robot.clickOn("#loginBtn");
	}
	
	private static Stream<Arguments> provideInvalidValues() {
		String errorBase = "Input fields cannot be empty: Please enter a value for";
		
		return Stream.of(
			Arguments.of("", "", errorBase + " username"),
			Arguments.of("foo", "", errorBase +" password")
		);
	}
}
