//package Bookstore.Bookstore.integration.stepDefinitions;
//
//import static org.junit.Assert.assertNotNull;
//import static org.junit.Assert.assertNull;
//
//import java.io.File;
//import java.io.IOException;
//import java.nio.file.Files;
//import java.time.LocalDate;
//import java.util.List;
//import java.util.Map;
//
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.testfx.api.FxAssert;
//import org.testfx.api.FxRobot;
//import org.testfx.framework.junit5.ApplicationExtension;
//import org.testfx.framework.junit5.Start;
//import org.testfx.matcher.base.GeneralMatchers;
//import org.testfx.matcher.base.WindowMatchers;
//import org.testfx.matcher.control.ListViewMatchers;
//import org.testfx.util.WaitForAsyncUtils;
//
//import Bookstore.Bookstore.bll.dto.EmployeeDTO;
//import Bookstore.Bookstore.bll.dto.UserDTO;
//import Bookstore.Bookstore.bll.services.EmployeeService;
//import Bookstore.Bookstore.bll.services.iservices.IEmployeeService;
//import Bookstore.Bookstore.controllers.EmployeesController;
//import Bookstore.Bookstore.dal.repositories.DbContext;
//import Bookstore.Bookstore.dal.repositories.EmployeeRepository;
//import io.cucumber.java.AfterAll;
//import io.cucumber.java.DataTableType;
//import io.cucumber.java.en.Given;
//import io.cucumber.java.en.Then;
//import io.cucumber.java.en.When;
//import javafx.scene.Scene;
//import javafx.scene.control.Alert;
//import javafx.scene.control.ComboBox;
//import javafx.scene.control.DatePicker;
//import javafx.scene.control.PasswordField;
//import javafx.scene.control.TextField;
//import javafx.scene.layout.Pane;
//import javafx.stage.Stage;
//
//@ExtendWith(ApplicationExtension.class)
//public class EmployeeControllerSteps {
//	private static String tempDbPath = "temp_user_data/data";
//	private EmployeeDTO newEmployee;
//	private IEmployeeService service;
//	private EmployeesController controller;
//	
//	@AfterAll
//	static void deleteTempDb() throws IOException {
//		Files.delete(new File(tempDbPath).toPath());
//	}
//	
//	@Start
//	private void start(Stage stage) {
//		service = new EmployeeService(new EmployeeRepository(tempDbPath, new DbContext()));
//		controller = new EmployeesController(service);
//		
//		stage.setScene(new Scene(controller.getIndexView()));
//		stage.show();
//	}
//	
//	@Test
//	@Given("the employee management page is showing")
//	public void the_employee_management_page_is_showing(FxRobot robot) {
//		FxAssert.verifyThat(
//				robot.window(robot.lookup("#manage_employees_view").queryAs(Pane.class)), 
//				WindowMatchers.isShowing()
//			);
//	}
//
//	@Test
//	@Given("the user clicks on the add button")
//	public void the_user_clicks_on_the_add_button(FxRobot robot) {
//		robot.clickOn(robot.lookup("#create_btn").queryButton());
//	}
//
//	@Test
//	@Given("the create form is shown")
//	public void the_create_form_is_shown(FxRobot robot) {
//		FxAssert.verifyThat(
//				robot.window(robot.lookup("#upsert_employee_view").queryAs(Pane.class)), 
//				WindowMatchers.isShowing()
//			);
//		
////		robot.targetwind
////		new Scene(controller.getInsertView()).
////		FxAssert.verifyThat(robot.targetWindow(), WindowMatchers.isShowing());
//	}
//
//	@Test
//	@Given("these existing employees")
//	public void these_existing_employees(io.cucumber.datatable.DataTable dataTable) {
//		List<Map<String, String>> rowData = dataTable.asMaps();
//		
//		for(Map<String, String> data: rowData) {
//			UserDTO user = new UserDTO(
//					data.get("username"), data.get("full name"), 
//					data.get("email"), data.get("password"), 
//					data.get("phone number"), LocalDate.parse(data.get("birthdate"))
//				);
//			
//			try {
//				service.add(new EmployeeDTO(
//						user, 
//						Double.parseDouble(data.get("salary")), 
//						Integer.parseInt(data.get("access level"))
//					));
//			} catch(Exception e) {
//				e.printStackTrace(); // Known that this error won't happen
//			}
//		}
//	}
//
//	@Test
//	@When("the user fills the form with {string}, {string}, {string}, {string}, {string}, {string}, {string}, {string}")
//	public void the_user_fills_the_form_with(FxRobot robot,
//			String username, String fullName, String email, 
//			String password, String phoneNr, String birthdate, 
//			String salary, String accessLvl) {
//		UserDTO user = new UserDTO(username, fullName, email, password, phoneNr, LocalDate.parse(birthdate));
//		newEmployee = new EmployeeDTO(user, Double.parseDouble(salary), Integer.parseInt(accessLvl));
//		
//		robot.clickOn(robot.lookup("#username").queryAs(TextField.class)).write(username);
//		robot.clickOn(robot.lookup("#full_name").queryAs(TextField.class)).write(fullName);
//		robot.clickOn(robot.lookup("#email").queryAs(TextField.class)).write(email);
//		robot.clickOn(robot.lookup("#password").queryAs(PasswordField.class)).write(password);
//		robot.clickOn(robot.lookup("#phone_nr").queryAs(TextField.class)).write(phoneNr);
//		robot.clickOn(robot.lookup("#salary").queryAs(TextField.class)).write(salary);
//		
//		if(!birthdate.isBlank())
//			robot.interact(() -> robot.lookup("#birthdate").queryAs(DatePicker.class).setValue(LocalDate.parse(birthdate)));
//		
//		if(!accessLvl.isBlank())
//			robot.interact(() -> robot.lookup("#access_lvl").queryAs(ComboBox.class).getSelectionModel().select(Integer.parseInt(accessLvl)));
//	}
//
//	@Test
//	@When("the user clicks the submit button")
//	public void the_user_clicks_the_submit_button(FxRobot robot) {
//	    robot.clickOn(robot.lookup("#submit_btn").queryButton());
//	}
//
//	@Test
//	@Then("the {string} error message should be shown")
//	public void the_error_message_should_be_shown(FxRobot robot, String error) {
//		WaitForAsyncUtils.waitForFxEvents();
//		
//		FxAssert.verifyThat(
//				".dialog-pane", 
//				GeneralMatchers.typeSafeMatcher(Alert.class, "", e -> e.getContentText().equals(error))
//			);
//		
////		Node dialogPane = robot.lookup(".dialog-pane").query();
////		from(dialogPane).lookup((Text t) -> t.getText().startsWith("Are you sure"));
//	}
//
//	@Test
//	@Then("the new employee is not saved")
//	public void the_new_employee_is_not_saved() {
//	    assertNull(service.getByUsername(newEmployee.getUsername()));
//	}
//
//	@Test
//	@Then("the user should be back to the employee management page")
//	public void the_user_should_be_back_to_the_employee_management_page(FxRobot robot) {
//	    WaitForAsyncUtils.waitForFxEvents();	    
//	    FxAssert.verifyThat(robot.window(0), WindowMatchers.isShowing());
//	}
//
//	@Test
//	@Then("the new employee should be saved")
//	public void the_new_employee_should_be_saved() {
//	    assertNotNull(service.getByUsername(newEmployee.getUsername()));
//	}
//
//	@Test
//	@Then("the new employee should be shown in the list")
//	public void shown_in_the_list(FxRobot robot) {	
//	    FxAssert.verifyThat("#employees_list", ListViewMatchers.hasListCell(newEmployee));
//	}
//	
//	@DataTableType(replaceWithEmptyString = "[blank]")
//	public String stringType(String cell) {
//		return cell;
//	}
//}
