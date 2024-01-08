package Bookstore.Bookstore.integration;

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
import org.testfx.api.FxAssert;
import org.testfx.api.FxRobot;
import org.testfx.framework.junit5.ApplicationExtension;
import org.testfx.framework.junit5.Start;
import org.testfx.matcher.control.LabeledMatchers;
import org.testfx.util.WaitForAsyncUtils;

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
public class TestLibrarianPerformanceViewNeighborhood extends TestNeighborHoodBase {
	private static StatisticsController controller;
	private static Map<String, Map.Entry<Integer, Double>> librarianBills;
	
	@BeforeAll
	private static void setUp() {		
		DbContext dbContext = new DbContext();
		IBillService billService = new BillService(new BillRepository(Utils.testingUserDataDirPath, dbContext));
		IEmployeeService employeeService = new EmployeeService(new EmployeeRepository(Utils.testingUserDataDirPath, dbContext));
		IBookPurchaseService bookPurchaseService = new BookPurchaseService(new BookPurchaseRepository(Utils.testingUserDataDirPath, dbContext));		
		
		controller = new StatisticsController(billService, employeeService, bookPurchaseService);
		librarianBills = new HashMap<>();
		
		setUpData(billService, employeeService, librarianBills);
	}
	
	private static void setUpData(IBillService billService, IEmployeeService employeeService, Map<String, Map.Entry<Integer, Double>> librarianBills) {
		try {
			// Adding librarians & their bills
			for(int i = 0; i < 2; i++) {
				UserDTO user = new UserDTO("librarian" + i, "foo bar", "foo@gmail.com", "password123", "069 123 1234", LocalDate.now());
				EmployeeDTO librarian = new EmployeeDTO(user, 200, 1);
				employeeService.add(librarian);			
				
				BillDTO bill1 = new BillDTO(i + 1, 100, 2), bill2 = new BillDTO(i + 1, 50, 1);				
				billService.add(bill1);
				billService.add(bill2);
				
				librarianBills.put(
					String.format("%s (%s)", librarian.getFullName(), librarian.getUsername()), 
					new SimpleEntry<Integer, Double>(2, bill1.getSaleAmount() + bill2.getSaleAmount()
					)
				);
			}
			
			// Adding administrator & their bill
			UserDTO user = new UserDTO("admin", "foo bar", "foo@gmail.com", "password123", "069 123 1234", LocalDate.now());
			EmployeeDTO admin = new EmployeeDTO(user, 200, 3);
			employeeService.add(admin);
			
			BillDTO bill = new BillDTO(3, 200, 2);
			
			billService.add(bill);
		} catch(Exception ex) {
			ex.printStackTrace(); // Won't be thrown
		}
	}
	
	@Start
	private void start(Stage stage) {
		stage.setScene(new Scene(controller.getLibrarianPerformanceView()));
		stage.show();
	}
	
	@Test
	void testShowLibrarianPerformance_InvalidValues_NonsequentialDates(FxRobot robot) throws InterruptedException {
		String startDate = "12/12/2000", endDate = "10/12/2000";
		
		robot.clickOn("#start-date").eraseText(startDate.length()).write(startDate);
		robot.clickOn("#end-date").eraseText(startDate.length()).write(endDate);
		robot.clickOn("#submit-btn");
		
		// Check for error pop-up
		WaitForAsyncUtils.waitForFxEvents();		
		FxAssert.verifyThat("#alert_error_message", LabeledMatchers.hasText("Starting date should follow ending date"));
	}
	
	@Test
	void testShowLibrarianPerformance_ValidValues(FxRobot robot) {
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
