package Bookstore.Bookstore.integration.viewNeighborhoods;

import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDate;
import java.util.AbstractMap.SimpleEntry;
import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.testfx.api.FxRobot;
import org.testfx.framework.junit5.ApplicationExtension;
import org.testfx.framework.junit5.Start;
import org.testfx.util.WaitForAsyncUtils;

import Bookstore.Bookstore.TestingUtils;
import Bookstore.Bookstore.bll.dto.BillDTO;
import Bookstore.Bookstore.bll.dto.EmployeeDTO;
import Bookstore.Bookstore.bll.dto.LibrarianPerformanceDTO;
import Bookstore.Bookstore.bll.dto.UserDTO;
import Bookstore.Bookstore.bll.services.BillService;
import Bookstore.Bookstore.bll.services.BookPurchaseService;
import Bookstore.Bookstore.bll.services.EmployeeService;
import Bookstore.Bookstore.bll.services.iservices.IBillService;
import Bookstore.Bookstore.bll.services.iservices.IBookPurchaseService;
import Bookstore.Bookstore.bll.services.iservices.IEmployeeService;
import Bookstore.Bookstore.commons.utils.Utils;
import Bookstore.Bookstore.controllers.StatisticsController;
import Bookstore.Bookstore.dal.repositories.BillRepository;
import Bookstore.Bookstore.dal.repositories.BookPurchaseRepository;
import Bookstore.Bookstore.dal.repositories.DbContext;
import Bookstore.Bookstore.dal.repositories.EmployeeRepository;
import javafx.scene.Scene;
import javafx.scene.control.TableView;
import javafx.stage.Stage;

@ExtendWith(ApplicationExtension.class)
public class TestLibrarianPerformanceViewNeighborhood extends TestViewNeighborHoodBase {
	private static StatisticsController controller;
	private static Map<String, Map.Entry<Integer, Double>> librarianBills;
	
	@BeforeAll
	public static void setUp() throws Exception {		
		DbContext dbContext = new DbContext();
		IBillService billService = new BillService(new BillRepository(Utils.testDataDirPath, dbContext));
		IEmployeeService employeeService = new EmployeeService(new EmployeeRepository(Utils.testDataDirPath, dbContext));
		IBookPurchaseService bookPurchaseService = new BookPurchaseService(new BookPurchaseRepository(Utils.testDataDirPath, dbContext));		
		
		controller = new StatisticsController(billService, employeeService, bookPurchaseService);
		librarianBills = new HashMap<>();
		
		setUpData(billService, employeeService, librarianBills);
	}
	
	private static void setUpData(IBillService billService, IEmployeeService employeeService, Map<String, Map.Entry<Integer, Double>> librarianBills) throws Exception {
		// Adding librarians & their bills
		for(int i = 0; i < 2; i++) {
			EmployeeDTO librarian = new EmployeeDTO(
				new UserDTO("librarian" + i, "foo bar", "foo@gmail.com", "password123", "069 123 1234", LocalDate.now()), 
				200, 1
			);
			
			employeeService.add(librarian);
			
			int sellerId = employeeService.getByUsername(librarian.getUsername()).getId();
			BillDTO bill1 = new BillDTO(sellerId, 100 + i, 2), bill2 = new BillDTO(sellerId, 50 + i, 1);
			
			billService.add(bill1);
			billService.add(bill2);
			
			librarianBills.put(
				String.format("%s (%s)", librarian.getFullName(), librarian.getUsername()), 
				new SimpleEntry<Integer, Double>(2, bill1.getSaleAmount() + bill2.getSaleAmount())
			);
		}
		
		// Adding administrator & their bill
		EmployeeDTO admin = new EmployeeDTO(
			new UserDTO("admin", "foo bar", "foo@gmail.com", "password123", "069 123 1234", LocalDate.now()), 
			200, 3
		);
		
		employeeService.add(admin);
		billService.add(new BillDTO(employeeService.getByUsername(admin.getUsername()).getId(), 200, 2));
	}
	
	@Start
	private void start(Stage stage) {
		stage.setScene(new Scene(controller.getLibrarianPerformanceView()));
		stage.show();
	}
	
	@Test
	public void testShowLibrarianPerformance_InvalidValues_NonsequentialDates(FxRobot robot) throws InterruptedException {
		String startDate = "12/12/2000", endDate = "10/12/2000";
		
		robot.clickOn("#start-date").eraseText(startDate.length()).write(startDate);
		robot.clickOn("#end-date").eraseText(startDate.length()).write(endDate);
		robot.clickOn("#submit-btn");
		
		// Check for error pop-up
		TestingUtils.testErrorMessage(robot, "Starting date should follow ending date");
	}
	
	@Test
	public void testShowLibrarianPerformance_ValidValues(FxRobot robot) {
		robot.clickOn("#submit-btn");
		
		WaitForAsyncUtils.waitForFxEvents();
		TableView<LibrarianPerformanceDTO> table = robot.lookup("#performance-table").queryTableView();
		
		// Check all librarian bills
		assertAll(
			() -> assertEquals(librarianBills.size(), table.getItems().size()),
			() -> assertTrue(table.getItems().stream().allMatch(
					item -> librarianBills.containsKey(item.getEmployeeDescription()) & 
							librarianBills.get(item.getEmployeeDescription()).equals(
								new SimpleEntry<Integer, Double>(item.getNumOfBills(), item.getSalesAmount())
							)
			 		))
		);
	}
}
