package Bookstore.Bookstore.integration.viewNeighborhoods;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDate;
import java.util.function.Consumer;
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
import Bookstore.Bookstore.commons.utils.Utils;
import Bookstore.Bookstore.controllers.EmployeesController;
import Bookstore.Bookstore.dal.repositories.DbContext;
import Bookstore.Bookstore.dal.repositories.EmployeeRepository;
import javafx.scene.Scene;
import javafx.scene.control.DatePicker;
import javafx.stage.Stage;

@ExtendWith(ApplicationExtension.class)
public class TestEmployeeModifyViewNeighborhood extends TestViewNeighborHoodBase {
	private static FxRobot robot = new FxRobot();
	private static EmployeeDTO employee;
	private static IEmployeeService employeeService;
	private static EmployeesController controller;
	
	@BeforeAll
	public static void setUp() {
		employeeService = new EmployeeService(new EmployeeRepository(Utils.testDataDirPath, new DbContext()));
		controller = new EmployeesController(employeeService);
		
		try {
			employee = new EmployeeDTO(
				new UserDTO("foobar", "foo bar", "foo@gmail.com", "password123", "069 123 1234", LocalDate.now()), 
				100, 1);
			
			employeeService.add(employee);
			employee = employeeService.get(0);
		} catch (Exception e) {
			e.printStackTrace(); // Won't be thrown
		}
	}
	
	@Start
	private void start(Stage stage) {				
		stage.setScene(new Scene(controller.getModifyView(employeeService.get(0))));
		stage.show();
	}
	
	@Test
	public void testFormIsFilledCorrectly() {		
		assertAll(
			"Form values",
			() -> assertEquals(robot.lookup("#birthdate").queryAs(DatePicker.class).getValue(), employee.getBirthdate()),
			() -> assertEquals(robot.lookup("#username").queryTextInputControl().getText(), employee.getUsername()),
			() -> assertEquals(robot.lookup("#full-name").queryTextInputControl().getText(), employee.getFullName()),
			() -> assertEquals(robot.lookup("#email").queryTextInputControl().getText(), employee.getEmail()),
			() -> assertEquals(robot.lookup("#phone-nr").queryTextInputControl().getText(), employee.getPhoneNum()),
			() -> assertEquals(Double.valueOf(robot.lookup("#salary").queryTextInputControl().getText()), employee.getSalary())
		);
	}
	
	@ParameterizedTest
	@MethodSource("provideInvalidValues")
	public void testModifyEmployee_InvalidValues(String fieldId, int nrCharsToRemove, String newValue, String errorMessage) {
		// Input change		
		robot.clickOn(fieldId).eraseText(nrCharsToRemove).write(newValue);
		robot.clickOn("#submit-btn");
				
		// Check for error pop-up
		TestingUtils.testErrorMessage(robot, errorMessage);
		
		// Check database entity hasn't changed
		assertEquals(employee, employeeService.get(0));
	}
	
	@ParameterizedTest
	@MethodSource("provideValidValues")
	public void testModifyEmployee_ValidValues(String fieldId, int nrCharsToRemove, String newValue, Consumer<EmployeeDTO> updateModel) {
		// Input change
		robot.clickOn(fieldId).eraseText(nrCharsToRemove).write(newValue);
		robot.clickOn("#submit-btn");
		
		// Check database entity has changed
		updateModel.accept(employee);
		assertEquals(employee, employeeService.get(0));
	}
	
	private static Stream<Arguments> provideInvalidValues() {
		return Stream.of(
			Arguments.of("#username", employee.getUsername().length(), "", "Input fields cannot be empty: Please enter a value for username"),
			Arguments.of("#email", employee.getEmail().length(), "foo", "Incorrect email format: Correct format is (2 characters)@gmail.com"),
			Arguments.of("#phone-nr", employee.getPhoneNum().length(), "123", "Incorrect phone number format: Correct format is 06X XXX XXXX")
		);
	}
	
	private static Stream<Arguments> provideValidValues() {
		String newUsername = "newUsername", newEmail = "newEmail@gmail.com", newPhoneNr = "068 789 7890";
		
		return Stream.of(
			Arguments.of("#username", employee.getUsername().length(), newUsername, (Consumer<EmployeeDTO>) employee -> employee.setUsername(newUsername)),
			Arguments.of("#email", employee.getEmail().length(), newEmail, (Consumer<EmployeeDTO>) employee -> employee.setEmail(newEmail)),
			Arguments.of("#phone-nr", employee.getPhoneNum().length(), newPhoneNr, (Consumer<EmployeeDTO>) employee -> employee.setPhoneNum(newPhoneNr))
		);
	}
}
