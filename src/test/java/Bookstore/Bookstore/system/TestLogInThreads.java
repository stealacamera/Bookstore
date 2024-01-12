package Bookstore.Bookstore.system;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.testfx.api.FxAssert;
import org.testfx.api.FxRobot;
import org.testfx.framework.junit5.ApplicationExtension;
import org.testfx.framework.junit5.Start;
import org.testfx.matcher.base.WindowMatchers;
import org.testfx.matcher.control.LabeledMatchers;
import org.testfx.util.WaitForAsyncUtils;

import Bookstore.Bookstore.bll.dto.EmployeeDTO;
import Bookstore.Bookstore.bll.dto.UserDTO;
import Bookstore.Bookstore.startup.Main;
import Bookstore.Bookstore.startup.Session;
import javafx.stage.Stage;

@ExtendWith(ApplicationExtension.class)
public class TestLogInThreads extends TestThreadBase {
	private static EmployeeDTO currentUser;
	private static List<EmployeeDTO> users;
	
	@BeforeAll
	static void preconditions() {
		users = new ArrayList<>();

		for(int i = 0; i < 3; i++) {
			EmployeeDTO user = new EmployeeDTO(
				new UserDTO("foobar" + i, "foo bar", "foo" + i + "@gmail.com", "password123" + i, "069 123 1324", LocalDate.now()), 
				100, i + 1
			);
			
			try {
				employeeService.add(user);
				users.add(user);
			} catch (Exception e) {
				e.printStackTrace(); // Won't be thrown
			}
		}
	}
	
	@Start
	private void start(Stage stage) {
		Session.clear();
		
		Main.setUpApplication(
			stage, bookInventoryService, categoryService, 
			billService, bookPurchaseService, employeeService
		);
	}
	
	@Test
	void testLogInAsExistingUser(FxRobot robot) {
		currentUser = users.get(users.size() - 1);
		
		robot.clickOn("#username").write(currentUser.getUsername());
		robot.clickOn("#password").write(currentUser.getPassword());
		robot.clickOn("#loginBtn");
		
		WaitForAsyncUtils.waitForFxEvents();
		FxAssert.verifyThat(robot.window("Homepage"), WindowMatchers.isShowing());
		
		currentUser = employeeService.get(users.size() - 1);
	}
	
	@Test
	void testLogInAsNonexistingUser(FxRobot robot) {
		currentUser = null;
		
		robot.clickOn("#username").write("none existant user");
		robot.clickOn("#password").write("dummy password");
		robot.clickOn("#loginBtn");
		
		WaitForAsyncUtils.waitForFxEvents();
		FxAssert.verifyThat(
			"#alert_error_message", 
			LabeledMatchers.hasText("Incorrect username and/or password. Please try again.")
		);
	}
	
	@AfterEach
	void postconditions() {
		assertEquals(currentUser, Session.getCurrentUser());
	}
}
