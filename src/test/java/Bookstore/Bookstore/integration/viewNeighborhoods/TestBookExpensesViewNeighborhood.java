package Bookstore.Bookstore.integration.viewNeighborhoods;

import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDate;
import java.time.Month;
import java.util.Arrays;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.testfx.api.FxRobot;
import org.testfx.framework.junit5.ApplicationExtension;
import org.testfx.framework.junit5.Start;

import Bookstore.Bookstore.bll.dto.BillDTO;
import Bookstore.Bookstore.bll.dto.BookPurchaseDTO;
import Bookstore.Bookstore.bll.services.BillService;
import Bookstore.Bookstore.bll.services.BookPurchaseService;
import Bookstore.Bookstore.bll.services.iservices.IBillService;
import Bookstore.Bookstore.bll.services.iservices.IBookPurchaseService;
import Bookstore.Bookstore.commons.utils.Utils;
import Bookstore.Bookstore.controllers.StatisticsController;
import Bookstore.Bookstore.dal.models.utils.CustomDate;
import Bookstore.Bookstore.dal.repositories.BillRepository;
import Bookstore.Bookstore.dal.repositories.BookPurchaseRepository;
import Bookstore.Bookstore.dal.repositories.DbContext;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart.Series;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;

@ExtendWith(ApplicationExtension.class)
public class TestBookExpensesViewNeighborhood extends TestViewNeighborHoodBase {
	private static LocalDate date = LocalDate.now(), dateBefore = date.minusMonths(1);
	private static double dailySales = 0, dailyPurchases = 0, dailyBeforeSales = 0, dailyBeforePurchases = 0;
	private static double[] monthlySales = new double[12], monthlyPurchases = new double[12];
	private static IBillService billService;
	private static IBookPurchaseService bookPurchaseService;
	private static StatisticsController controller;
	
	@BeforeAll
	static void setUp() {
		DbContext context = new DbContext();
		billService = new BillService(new BillRepository(Utils.testDataDirPath, context));
		bookPurchaseService = new BookPurchaseService(new BookPurchaseRepository(Utils.testDataDirPath, context));

		controller = new StatisticsController(billService, null, bookPurchaseService);
		createDummyData(billService, bookPurchaseService);
	}
	
	static void createDummyData(IBillService billService, IBookPurchaseService bookPurchaseService) {
		BillDTO bill1 = new BillDTO(1, 100, 3, date), bill2 = new BillDTO(1, 10, 2, dateBefore);
		BookPurchaseDTO purchase1 = new BookPurchaseDTO(23, date), purchase2 = new BookPurchaseDTO(500, dateBefore);
		
		dailySales += bill1.getSaleAmount();
		dailyPurchases += purchase1.getAmount();
		dailyBeforeSales += bill2.getSaleAmount();
		dailyBeforePurchases += purchase2.getAmount();
		
		monthlySales[date.getMonthValue() - 1] = dailySales;
		monthlySales[dateBefore.getMonthValue() - 1] = bill2.getSaleAmount();
		
		monthlyPurchases[date.getMonthValue() - 1] = dailyPurchases;
		monthlyPurchases[dateBefore.getMonthValue() - 1] = purchase2.getAmount();
		
		try {
			billService.add(bill1);
			billService.add(bill2);
			
			bookPurchaseService.add(purchase1);
			bookPurchaseService.add(purchase2);
		} catch (Exception e) {
			e.printStackTrace(); // Won't be thrown
		}		
	}
	
	@Start
	private void start(Stage stage) {		
		stage.setScene(new Scene(controller.getBookExpensesView()));
		stage.show();
	}

	@SuppressWarnings("unchecked")
	@Test
	void testGetPeriodStatistics(FxRobot robot) {
		// Check monthly chart
		robot.clickOn("#monthly-toggle");
		
		BarChart<String, Number> monthlyChart = robot.lookup("#monthly-chart").queryAs(BarChart.class);
		Series<String, Number> salesData = monthlyChart.getData().get(0), purchasesData = monthlyChart.getData().get(1);
		
		assertTrue(salesData.getData().stream().allMatch(e -> e.getYValue().doubleValue() == monthlySales[Month.valueOf(e.getXValue().toUpperCase()).getValue() - 1]));
		assertTrue(purchasesData.getData().stream().allMatch(e -> e.getYValue().doubleValue() == monthlyPurchases[Month.valueOf(e.getXValue().toUpperCase()).getValue() - 1]));
		
		// Check total chart
		robot.clickOn("#total-toggle");

		PieChart totalChart = robot.lookup("#total-chart").queryAs(PieChart.class);
		assertEquals(Arrays.stream(monthlySales).sum(), totalChart.getData().get(0).getPieValue());
		assertEquals(Arrays.stream(monthlyPurchases).sum(), totalChart.getData().get(1).getPieValue());
	}
	
	@Test
	void testDayStatistics(FxRobot robot) {
		String dateBeforeString = dateBefore.format(CustomDate.dateFormat);
				
		robot.clickOn("#daily-toggle");
		
		PieChart chart = robot.lookup("#daily-chart").queryAs(PieChart.class);
		assertEquals(dailySales, chart.getData().get(0).getPieValue());
		assertEquals(dailyPurchases, chart.getData().get(1).getPieValue());
		
		robot.clickOn("#date-picker").eraseText(dateBeforeString.length()).write(dateBeforeString).press(KeyCode.ENTER);
		
		chart = robot.lookup("#daily-chart").queryAs(PieChart.class);
		assertEquals(dailyBeforeSales, chart.getData().get(0).getPieValue());
		assertEquals(dailyBeforePurchases, chart.getData().get(1).getPieValue());
	}
}
