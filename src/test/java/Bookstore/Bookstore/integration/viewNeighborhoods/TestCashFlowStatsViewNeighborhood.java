package Bookstore.Bookstore.integration.viewNeighborhoods;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDate;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.testfx.api.FxRobot;
import org.testfx.framework.junit5.ApplicationExtension;
import org.testfx.framework.junit5.Start;

import Bookstore.Bookstore.TestingUtils;
import Bookstore.Bookstore.bll.dto.BillDTO;
import Bookstore.Bookstore.bll.dto.BookPurchaseDTO;
import Bookstore.Bookstore.bll.dto.EmployeeDTO;
import Bookstore.Bookstore.bll.dto.UserDTO;
import Bookstore.Bookstore.bll.services.BillService;
import Bookstore.Bookstore.bll.services.BookPurchaseService;
import Bookstore.Bookstore.bll.services.EmployeeService;
import Bookstore.Bookstore.bll.services.iservices.IBillService;
import Bookstore.Bookstore.bll.services.iservices.IBookPurchaseService;
import Bookstore.Bookstore.bll.services.iservices.IEmployeeService;
import Bookstore.Bookstore.commons.utils.Utils;
import Bookstore.Bookstore.controllers.StatisticsController;
import Bookstore.Bookstore.dal.models.utils.CustomDate;
import Bookstore.Bookstore.dal.repositories.BillRepository;
import Bookstore.Bookstore.dal.repositories.BookPurchaseRepository;
import Bookstore.Bookstore.dal.repositories.DbContext;
import Bookstore.Bookstore.dal.repositories.EmployeeRepository;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.XYChart.Series;
import javafx.stage.Stage;

@ExtendWith(ApplicationExtension.class)
public class TestCashFlowStatsViewNeighborhood extends TestViewNeighborHoodBase {
	private static double totalBillsSales = 0, totalBookPurchases = 0, totalSalaries = 0;
	private static LocalDate date = LocalDate.now();
	private static StatisticsController controller;
	
	@BeforeAll
	static void setUp() {
		DbContext dbContext = new DbContext();
		IBillService billService = new BillService(new BillRepository(Utils.testDataDirPath, dbContext));
		IBookPurchaseService bookPurchaseService = new BookPurchaseService(new BookPurchaseRepository(Utils.testDataDirPath, dbContext));
		IEmployeeService employeeService = new EmployeeService(new EmployeeRepository(Utils.testDataDirPath, dbContext));
		
		controller = new StatisticsController(billService, employeeService, bookPurchaseService);
		createDummyData(billService, bookPurchaseService, employeeService);
	}
	
	static void createDummyData(
		IBillService billService, 
		IBookPurchaseService bookPurchaseService, 
		IEmployeeService employeeService) 
	{
		LocalDate oldDate = LocalDate.parse("2000-12-12");
		
		// Old data
		for(int i = 1; i < 3; i++) {
			BookPurchaseDTO bookPurchase = new BookPurchaseDTO(10 * i, oldDate);
			BillDTO bill = new BillDTO(1, 10 * i, i, oldDate);
			
			try {
				billService.add(bill);
				bookPurchaseService.add(bookPurchase);
			} catch (Exception e) {
				e.printStackTrace(); // Won't be thrown
			}
		}
		
		// New data
		for(int i = 1; i < 3; i++) {
			BillDTO bill = new BillDTO(1, 10 * i, i, date);
			BookPurchaseDTO bookPurchase = new BookPurchaseDTO(10 * i, date);
			EmployeeDTO employee = new EmployeeDTO(
				new UserDTO("foobar" + i, "foo bar", "foo@gmail.com", "password123", "069 123 1345", date), 
				10 * i, 1);
			
			totalBillsSales += bill.getSaleAmount();
			totalBookPurchases += bookPurchase.getAmount();
			totalSalaries += employee.getSalary();
			
			try {
				billService.add(bill);
				bookPurchaseService.add(bookPurchase);
				employeeService.add(employee);
			} catch (Exception e) {
				e.printStackTrace(); // Won't be thrown
			}
		}
	}
	
	@Start
	private void start(Stage stage) {				
		stage.setScene(new Scene(controller.getCashFlowStatsView()));
		stage.show();
	}
	
	@Test
	void testCashFlowStatistics_InvalidValues(FxRobot robot) {
		String startDate = "12/12/2000", endDate = "10/12/2000";
		
		robot.clickOn("#start-date").eraseText(startDate.length()).write(startDate);
		robot.clickOn("#end-date").eraseText(endDate.length()).write(endDate);
		robot.clickOn("#submit-btn");
		
		TestingUtils.testErrorMessage(robot, "Starting date should follow ending date");
	}
	
	@SuppressWarnings("unchecked")
	@Test
	void testCashFlowStatistics_ValidValues(FxRobot robot) {
		String date = TestCashFlowStatsViewNeighborhood.date.format(CustomDate.dateFormat);
		
		robot.clickOn("#start-date").eraseText(date.length()).write(date);
		robot.clickOn("#end-date").eraseText(date.length()).write(date);
		robot.clickOn("#submit-btn");
		
		BarChart<String, Number> chart = robot.lookup("#cashflow-chart").queryAs(BarChart.class);
		Series<String, Number> incomeData = chart.getData().get(0), costsData = chart.getData().get(1);
		
		assertEquals(totalBillsSales, incomeData.getData().get(0).getYValue());
		assertEquals(totalBookPurchases, costsData.getData().get(0).getYValue());
		assertEquals(totalSalaries, costsData.getData().get(1).getYValue());
	}
}
