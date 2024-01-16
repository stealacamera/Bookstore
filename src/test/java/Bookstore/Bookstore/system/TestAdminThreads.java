package Bookstore.Bookstore.system;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDate;
import java.util.Map;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.testfx.framework.junit5.ApplicationExtension;
import org.testfx.framework.junit5.Start;

import Bookstore.Bookstore.bll.dto.BillDTO;
import Bookstore.Bookstore.bll.dto.BookPurchaseDTO;
import Bookstore.Bookstore.bll.dto.EmployeeDTO;
import Bookstore.Bookstore.bll.dto.UserDTO;
import Bookstore.Bookstore.dal.models.utils.CustomDate;
import Bookstore.Bookstore.dal.models.utils.Role;
import Bookstore.Bookstore.startup.Main;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.XYChart.Series;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListCell;
import javafx.stage.Stage;

@ExtendWith(ApplicationExtension.class)
public class TestAdminThreads extends TestThreadBase {
	private static LocalDate transactionsDate = LocalDate.of(2000, 12, 12);
	private static EmployeeDTO admin;
	
	@BeforeAll
	public static void preconditions_AddAdminUser() throws Exception {
		admin = new EmployeeDTO(
			new UserDTO("admin", "foo bar", "admin@gmail.com", "password123", "069 123 1234", LocalDate.now()), 
			100, 3
		);
		
		employeeService.add(admin);
	}
	
	@BeforeAll
	public static void preconditions_AddEmployees() throws Exception {
		for(int i = 0; i < 2; i++) {
			EmployeeDTO user = new EmployeeDTO(
				new UserDTO("employee" + i, "foo bar", "employee@gmail.com", "password123", "069 123 1324", LocalDate.now()), 
				100, i + 1
			);
			
			employeeService.add(user);
		}
	}
	
	@BeforeAll
	public static void preconditions_AddBillsAndPurchases() throws Exception {
		for(int i = 0; i < 3; i++) {
			bookPurchaseService.add(new BookPurchaseDTO(100 + i, transactionsDate));
			billService.add(new BillDTO(1, 123 + i, 2 + i, transactionsDate));
		}
	}
	
	@Start
	public void start(Stage stage) {
		Main.setUpApplication(
			stage, bookInventoryService, categoryService, 
			billService, bookPurchaseService, employeeService
		);
	}
	
	@SuppressWarnings("unchecked")
	@Test
	public void testCheckCashflowStatistics() {
		// Preconditions
		preconditions_LogInUser(admin.getUsername(), admin.getPassword());
		
		robot.clickOn("#" + Role.GET_REVENUE_STATS.name());
		BarChart<String, Number> chart = robot.lookup("#cashflow-chart").queryAs(BarChart.class);
		Series<String, Number> incomeData = chart.getData().get(0), costsData = chart.getData().get(1);
		
		double totalSales = billService.getAll().stream().mapToDouble(e -> e.getSaleAmount()).sum(),
				totalPurchases = bookPurchaseService.getAll().stream().mapToDouble(e -> e.getAmount()).sum(),
				totalSalaries = employeeService.getAll().stream().mapToDouble(e -> e.getSalary()).sum();
		
		// Test empty chart
		robot.clickOn("#submit-btn");
		testCashflowChartValues(0, 0, totalSalaries, incomeData, costsData);
		
		// Test full chart
		String date = transactionsDate.format(CustomDate.dateFormat);
		robot.clickOn("#start-date").eraseText(date.length()).write(date);
		robot.clickOn("#submit-btn");
		
		testCashflowChartValues(totalSales, totalPurchases, totalSalaries, incomeData, costsData);
	}
	
	private void testCashflowChartValues(
		double totalSales, double totalPurchases, double totalSalaries,
		Series<String, Number> incomeData, 
		Series<String, Number> costsData
	) {
		assertEquals(totalSales, incomeData.getData().get(0).getYValue());
		assertEquals(totalPurchases, costsData.getData().get(0).getYValue());
		assertEquals(totalSalaries, costsData.getData().get(1).getYValue());
	}
	
	@Test
	public void testAddNewEmployee() {
		// Preconditions
		preconditions_LogInUser(admin.getUsername(), admin.getPassword());
		
		robot.clickOn("#" + Role.MANAGE_EMPLOYEES.name());
		robot.clickOn("#add-btn");
		
		String username = "newEmployee";
		robot.clickOn("#username").write(username);
		robot.clickOn("#full-name").write("foo bar");
		robot.clickOn("#email").write("foobar1@gmail.com");
		robot.clickOn("#birthdate").write("12/12/2000");
		robot.clickOn("#phone-nr").write("069 123 1324");
		robot.clickOn("#salary").eraseText(4).write("100");
		robot.clickOn("#password").write("password123");
		
		ComboBox<Integer> accessLvlMenu = robot.lookup("#access-lvl").queryComboBox();
		robot.clickOn(accessLvlMenu).clickOn(accessLvlMenu.getChildrenUnmodifiable().get(1));
		
		robot.clickOn("#submit-btn");
		
		// Post-conditions
		assertNotNull(employeeService.getByUsername(username));
	}
	
	@Test
	public void testDeleteEmployee() {
		// Preconditions
		preconditions_LogInUser(admin.getUsername(), admin.getPassword());
		
		robot.clickOn("#" + Role.MANAGE_EMPLOYEES.name());
		
		ListCell<EmployeeDTO> cellToDelete = getEmployeeListCell();
		int deletedEmployeeId = cellToDelete.getItem().getId();
		
		robot.clickOn(cellToDelete);
		robot.clickOn("#delete-btn");
		
		// Post-conditions
		assertNull(employeeService.getById(deletedEmployeeId));
	}
	
	@Test
	public void testModifyEmployee() {
		// Preconditions
		preconditions_LogInUser(admin.getUsername(), admin.getPassword());
		
		robot.clickOn("#" + Role.MANAGE_EMPLOYEES.name());
		
		ListCell<EmployeeDTO> cellToModify = getEmployeeListCell();
		EmployeeDTO employeeToModify = cellToModify.getItem();
		
		robot.clickOn(cellToModify);
		robot.clickOn("#modify-btn");
		
		double newSalary = 3500;
		robot.clickOn("#salary").eraseText(5).write(Double.toString(newSalary));
		robot.clickOn("#submit-btn");
		
		// Post-conditions		
		assertEquals(newSalary, employeeService.getById(employeeToModify.getId()).getSalary());
	}
	
	@Test
	public void testChangeEmployeePermissions() {
		// Preconditions
		preconditions_LogInUser(admin.getUsername(), admin.getPassword());
		
		robot.clickOn("#" + Role.MANAGE_EMPLOYEES.name());
		
		ListCell<EmployeeDTO> cellToModify = getEmployeeListCell();
		EmployeeDTO employeeToModify = cellToModify.getItem();
		Map<Role, Boolean> userPermissions = employeeToModify.getPermissions();
		Role roleToChange = (Role) userPermissions.keySet().toArray()[userPermissions.size() - 1];
		
		robot.clickOn(cellToModify);
		robot.clickOn("#modify-btn");
		
		robot.clickOn("#" + roleToChange.name());
		robot.clickOn("#submit-btn");
		
		// Post-conditions
		userPermissions.put(roleToChange, false);
		assertEquals(userPermissions, employeeService.getById(employeeToModify.getId()).getPermissions());
	}
	
	@SuppressWarnings("unchecked")
	private ListCell<EmployeeDTO> getEmployeeListCell() {
		ListCell<EmployeeDTO> cellToModify;
		
		for(int i = 0; true; i++) {
			cellToModify = robot.lookup(".list-cell").nth(i).queryAs(ListCell.class);
			
			if(!cellToModify.isDisabled())
				break;
		}
		
		return cellToModify;
	}
}
