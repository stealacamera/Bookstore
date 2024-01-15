package Bookstore.Bookstore.integration.viewNeighborhoods;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDate;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.testfx.api.FxAssert;
import org.testfx.api.FxRobot;
import org.testfx.framework.junit5.ApplicationExtension;
import org.testfx.framework.junit5.Start;
import org.testfx.matcher.control.LabeledMatchers;
import org.testfx.util.WaitForAsyncUtils;

import Bookstore.Bookstore.bll.dto.EmployeeDTO;
import Bookstore.Bookstore.bll.dto.UserDTO;
import Bookstore.Bookstore.bll.services.EmployeeService;
import Bookstore.Bookstore.bll.services.iservices.IEmployeeService;
import Bookstore.Bookstore.commons.utils.Identity;
import Bookstore.Bookstore.commons.utils.Utils;
import Bookstore.Bookstore.controllers.EmployeesController;
import Bookstore.Bookstore.dal.models.utils.CustomDate;
import Bookstore.Bookstore.dal.repositories.DbContext;
import Bookstore.Bookstore.dal.repositories.EmployeeRepository;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.stage.Stage;

@TestMethodOrder(OrderAnnotation.class)
@ExtendWith(ApplicationExtension.class)
public class TestEmployeeInsertViewNeighborhood extends TestViewNeighborHoodBase {
	private static EmployeeDTO employee;
	private static IEmployeeService employeeService;
	private static EmployeesController controller;
	
	@BeforeAll
	static void setUp() {
		employeeService = new EmployeeService(new EmployeeRepository(Utils.testDataDirPath, new DbContext()));
		controller = new EmployeesController(employeeService);
		
		employee = new EmployeeDTO(new UserDTO("foobar", "foo bar", "foo@gmail.com", "password123", "069 123 1234", LocalDate.now()), 100, 1);
	}
	
	@Start
	private void start(Stage stage) {				
		stage.setScene(new Scene(controller.getInsertView()));
		stage.show();
	}
	
	void submitForm(FxRobot robot, String username, String fullName, String email, String birthdate, String phoneNr, double salary, String password, int accessLvl) {
		robot.clickOn("#username").write(username);
		robot.clickOn("#full-name").write(fullName);
		robot.clickOn("#email").write(email);
		robot.clickOn("#birthdate").write(birthdate);
		robot.clickOn("#phone-nr").write(phoneNr);
		robot.clickOn("#salary").eraseText(4).write(Double.toString(salary));
		robot.clickOn("#password").write(password);
		
		ComboBox<Integer> accessLvlMenu = robot.lookup("#access-lvl").queryComboBox();
		robot.clickOn(accessLvlMenu).clickOn(accessLvlMenu.getChildrenUnmodifiable().get(accessLvl - 1));
		
		robot.clickOn("#submit-btn");
	}
	
	@Test
	@Order(1)
	void testAddEmployee_ValidValues(FxRobot robot) {
		submitForm(
			robot, employee.getUsername(), employee.getFullName(), 
			employee.getEmail(), CustomDate.format(employee.getBirthdate()), employee.getPhoneNum(),
			employee.getSalary(), employee.getPassword(), employee.getAccessLvl()
		);
		
		employee.setId(1);
		employee.setHashedPassword(Identity.hashPassword(employee.getPassword()));
		
		assertEquals(1, employeeService.count());
		assertEquals(employee, employeeService.get(0));
	}
	
	@Test
	@Order(2)
	void testAddEmployee_InvalidValues(FxRobot robot) {
		submitForm(
			robot, employee.getUsername(), employee.getFullName(), 
			employee.getEmail(), CustomDate.format(employee.getBirthdate()), "123",
			employee.getSalary(), employee.getPassword(), employee.getAccessLvl()
		);
		
		WaitForAsyncUtils.waitForFxEvents();	
		FxAssert.verifyThat(
			"#alert_error_message", 
			LabeledMatchers.hasText("Incorrect phone number format: Correct format is 06X XXX XXXX")
		);
		
		assertEquals(1, employeeService.count());
	}
}
