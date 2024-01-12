package Bookstore.Bookstore.integration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;

import java.time.LocalDate;
import java.util.Map;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.testfx.api.FxRobot;
import org.testfx.framework.junit5.ApplicationExtension;
import org.testfx.framework.junit5.Start;
import org.testfx.util.WaitForAsyncUtils;

import Bookstore.Bookstore.TestingUtils;
import Bookstore.Bookstore.bll.dto.EmployeeDTO;
import Bookstore.Bookstore.bll.dto.UserDTO;
import Bookstore.Bookstore.bll.services.EmployeeService;
import Bookstore.Bookstore.bll.services.iservices.IEmployeeService;
import Bookstore.Bookstore.commons.utils.Utils;
import Bookstore.Bookstore.controllers.EmployeesController;
import Bookstore.Bookstore.dal.models.utils.Role;
import Bookstore.Bookstore.dal.repositories.DbContext;
import Bookstore.Bookstore.dal.repositories.EmployeeRepository;
import javafx.scene.Scene;
import javafx.scene.control.Cell;
import javafx.scene.control.CheckBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.ListView;
import javafx.stage.Stage;

@ExtendWith(ApplicationExtension.class)
public class TestEmployeeIndexViewNeighborhood extends TestNeighborHoodBase {
	private static IEmployeeService employeeService;
	private static EmployeesController controller;
	
	@BeforeAll
	static void setUp() {
		employeeService = new EmployeeService(new EmployeeRepository(Utils.testingUserDataDirPath, new DbContext()));
		controller = new EmployeesController(employeeService);
		
		setUpDummyData(employeeService);
	}
	
	static void setUpDummyData(IEmployeeService employeeService) {
		try {
			for(int i = 0; i < 2; i++) {
				EmployeeDTO user = new EmployeeDTO(
					new UserDTO("foobar" + i, "foo bar", "foo@gmail.com", "password123", "069 123 1234", LocalDate.now()), 
					100, 1
				);
				
				employeeService.add(user);
			}
		} catch (Exception e) {
			e.printStackTrace(); // Won't be thrown
		}
	}
	
	@Start
	private void start(Stage stage) {				
		stage.setScene(new Scene(controller.getIndexView()));
		stage.show();
	}
	
	@Test
	void testIndexList(FxRobot robot) {
		ListView<EmployeeDTO> employeeList = robot.lookup("#employees-list").queryListView();
		
		assertEquals(2, employeeList.getItems().size());
		assertIterableEquals(employeeService.getAll(), employeeList.getItems());		
	}
	
	@Test
	void testModifyEmployeeFormFill_EntityNotClicked(FxRobot robot) {
		robot.clickOn("#modify-btn");
		
		TestingUtils.testErrorMessage(robot, "Please select an employee");
	}
	
	@Test
	void testModifyEmployeeFormFill_EntityClicked(FxRobot robot) {
		robot.clickOn(robot.lookup(".list-cell").nth(0).queryAs(Cell.class));
		robot.clickOn("#modify-btn");
		
		WaitForAsyncUtils.waitForFxEvents();
		EmployeeDTO employee = employeeService.get(0);
		
		assertEquals(employee.getUsername(), robot.lookup("#username").queryTextInputControl().getText());
		assertEquals(employee.getFullName(), robot.lookup("#full-name").queryTextInputControl().getText());
		assertEquals(employee.getEmail(), robot.lookup("#email").queryTextInputControl().getText());
		assertEquals(employee.getPhoneNum(), robot.lookup("#phone-nr").queryTextInputControl().getText());
		assertEquals(employee.getSalary(), Double.valueOf(robot.lookup("#salary").queryTextInputControl().getText()));
		assertEquals(employee.getBirthdate(), robot.lookup("#birthdate").queryAs(DatePicker.class).getValue());
		
		for(Map.Entry<Role, Boolean> permission: employee.getPermissions().entrySet())
			assertEquals(
				permission.getValue().booleanValue(), 
				robot.lookup("#" + permission.getKey().name()).queryAs(CheckBox.class).isSelected()
			);
	}
	
	@Test
	void testDeleteEmployee_EntityNotClicked(FxRobot robot) {
		robot.clickOn("#delete-btn");
		
		TestingUtils.testErrorMessage(robot, "Please select an employee");

		ListView<EmployeeDTO> employeeList = robot.lookup("#employees-list").queryListView();
		assertEquals(1, employeeList.getItems().size());
		assertEquals(employeeService.count(), employeeList.getItems().size());
	}
	
	@Test
	void testDeleteEmployee_EntityClicked(FxRobot robot) {
		robot.clickOn(robot.lookup(".list-cell").nth(0).queryAs(Cell.class));
		robot.clickOn("#delete-btn");
		
		ListView<EmployeeDTO> employeeList = robot.lookup("#employees-list").queryListView();
		assertEquals(1, employeeList.getItems().size());
		assertEquals(employeeService.count(), employeeList.getItems().size());
	}
}
