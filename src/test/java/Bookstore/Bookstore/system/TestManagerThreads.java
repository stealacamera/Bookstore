package Bookstore.Bookstore.system;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDate;
import java.time.Month;
import java.util.Arrays;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.testfx.framework.junit5.ApplicationExtension;
import org.testfx.framework.junit5.Start;
import org.testfx.service.query.NodeQuery;

import Bookstore.Bookstore.TestingUtils;
import Bookstore.Bookstore.bll.dto.BillDTO;
import Bookstore.Bookstore.bll.dto.BookDTO;
import Bookstore.Bookstore.bll.dto.BookInventoryDTO;
import Bookstore.Bookstore.bll.dto.BookPurchaseDTO;
import Bookstore.Bookstore.bll.dto.CategoryDTO;
import Bookstore.Bookstore.bll.dto.EmployeeDTO;
import Bookstore.Bookstore.bll.dto.LibrarianPerformanceDTO;
import Bookstore.Bookstore.bll.dto.UserDTO;
import Bookstore.Bookstore.commons.exceptions.EmptyInputException;
import Bookstore.Bookstore.dal.models.utils.CustomDate;
import Bookstore.Bookstore.dal.models.utils.Role;
import Bookstore.Bookstore.startup.Main;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart.Series;
import javafx.scene.control.ListCell;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableView;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;

@ExtendWith(ApplicationExtension.class)
public class TestManagerThreads extends TestThreadBase {
	private static LocalDate billsDate = LocalDate.of(2000, 12, 12);
	private static EmployeeDTO librarian;
	private static EmployeeDTO manager;
	
	@BeforeAll
	public static void preconditions_AddManager() throws Exception {
		manager = new EmployeeDTO(
			new UserDTO("manager", "foo bar", "foo@gmail.com", "password123", "069 123 1346", LocalDate.now()), 
			100, 2
		);
		
		employeeService.add(manager);
	}
	
	@BeforeAll
	public static void preconditions_AddBook() throws Exception {
		CategoryDTO category = new CategoryDTO("category");
		
		BookInventoryDTO book = new BookInventoryDTO(
			new BookDTO("123-1-12-123132-1", "foobar", "foo bar", "foo", 1), 
			10, 10, 10, 10, LocalDate.now()
		);
		
		categoryService.add(category);
		bookInventoryService.add(book);
	}
	
	@BeforeAll
	public static void preconditions_AddLibrariansAndBills() throws Exception {
		for(int i = 0; i < 3; i++) {
			librarian = new EmployeeDTO(
				new UserDTO("librarian" + i, "foo bar", "foo@gmail.com", "password123", "069 123 1346", LocalDate.now()), 
				100, 1
			);
		
			employeeService.add(librarian);
			
			BillDTO bill = new BillDTO(employeeService.getByUsername(librarian.getUsername()).getId(), 10 + i, 3 + i, billsDate);
			billService.add(bill);
		}
	}
	
	@Start
	public void start(Stage stage) throws Exception {
		Main.setUpApplication(
			stage, bookInventoryService, categoryService, 
			billService, bookPurchaseService, employeeService
		);
	}
	
	@Test
	public void testAddNewBook() throws Exception {			
		// Move to add book page
		preconditions_LogInUser(manager.getUsername(), manager.getPassword());
		
		robot.clickOn("#" + Role.MANAGE_BOOKS.name());
		robot.clickOn("#add-btn");
		
		// Fill form
		String existingISBN = bookInventoryService.get(0).getBook().getIsbn();
		BookInventoryDTO book = new BookInventoryDTO(
			new BookDTO("798-7-78-789789-7", "foobar", "foo bar", "foo", 1), 
			10, 10, 10, 10, LocalDate.now()
		);
		
		// Put incorrect information
		robot.clickOn("#isbn").write(existingISBN);
		robot.clickOn("#title").write(book.getBook().getTitle());
		robot.clickOn("#author").write(book.getBook().getAuthor());
		robot.clickOn("#supplier").write(book.getBook().getSupplier());
		robot.clickOn("#purchased-price").eraseText(3).write(Double.toString(book.getPurchasedPrice()));
		robot.clickOn("#original-price").eraseText(3).write(Double.toString(book.getOriginalPrice()));
		robot.clickOn("#selling-price").eraseText(3).write(Double.toString(book.getSellingPrice()));
		robot.clickOn(robot.lookup(".list-cell").nth(0).queryAs(ListCell.class));
		
		robot.clickOn("#submit-btn");
		TestingUtils.testErrorMessage(robot, "Incorrect stock: Please enter a positive number for the field");
		
		// Put correct information & existing ISBN
		robot.clickOn("#stock").eraseText(1).write(Integer.toString(book.getStock()));
		robot.clickOn("#submit-btn");
		TestingUtils.testErrorMessage(robot, "Element with these details (ISBN) already exists.");
		
		// Put new, correct information
		robot.clickOn("#isbn").eraseText(existingISBN.length()).write(book.getBook().getIsbn());
		robot.clickOn("#submit-btn");
		
		// Post-conditions
		assertEquals(book, bookInventoryService.getByISBN(book.getBook().getIsbn()));		
		assertTrue(bookPurchaseService.getAll().contains(new BookPurchaseDTO(book.getSellingPrice() * book.getStock(), LocalDate.now())));
	}
	
	@Test
	public void testDeleteBook() throws EmptyInputException {
		preconditions_LogInUser(manager.getUsername(), manager.getPassword());
		
		robot.clickOn("#" + Role.MANAGE_BOOKS.name());
		
		NodeQuery booksListViewQuery = robot.lookup("#books-list");
		String isbn = booksListViewQuery.queryTableView().getColumns().get(0).getCellData(0).toString();
		
		robot.clickOn(booksListViewQuery.lookup(".table-cell").nth(0).queryAs(TableCell.class));
		robot.clickOn("#delete-btn");
		
		// Post-conditions
		assertNull(bookInventoryService.getByISBN(isbn));
	}
	
	@Test
	public void testCheckLibrariansPerformance() {
		preconditions_LogInUser(manager.getUsername(), manager.getPassword());
		
		robot.clickOn("#" + Role.GET_LIBR_PERFORMANCE.name());
		robot.clickOn("#submit-btn");
		
		// Assert nothing is shown
		TableView<LibrarianPerformanceDTO> table = robot.lookup("#performance-table").queryTableView();
		assertEquals(0, table.getItems().size());
		
		String date = billsDate.format(CustomDate.dateFormat);
		robot.clickOn("#start-date").eraseText(date.length()).write(date);
		robot.clickOn("#submit-btn");
		
		// Assert all bills are shown
		assertEquals(billService.count(), table.getItems().size());
	}
	
	@Test
	public void testBookExpenses() {
		// Preconditions
		preconditions_LogInUser(manager.getUsername(), manager.getPassword());
		
		robot.clickOn("#" + Role.GET_BOOK_STATS.name());
				
		// Check daily chart
		testBookExpenses_DailyChart(
			bookPurchaseService.getAll().stream().mapToDouble(e -> e.getAmount()).sum(),
			billService.getAll().stream().mapToDouble(e -> e.getSaleAmount()).sum()
		);
		
		// Check monthly chart
		double[] monthlySales = new double[12], monthlyPurchases = new double[12];
		monthlySales[billsDate.getMonthValue() - 1] = billService.getAll().stream().mapToDouble(e -> e.getSaleAmount()).sum();
		monthlyPurchases[LocalDate.now().getMonthValue() - 1] = bookPurchaseService.getAll().stream().mapToDouble(e -> e.getAmount()).sum();
		
		testBookExpenses_MonthlyChart(monthlySales, monthlyPurchases);
		
		// Check total chart
		testBookExpenses_TotalChart(
			Arrays.stream(monthlySales).sum(), 
			Arrays.stream(monthlyPurchases).sum()
		);
	}
	
	private void testBookExpenses_DailyChart(double dbBookPurchasesSum, double dbBillSalesSum) {
		robot.clickOn("#daily-toggle");
		PieChart chart = robot.lookup("#daily-chart").queryAs(PieChart.class);
		
		// Check data matches database
		assertEquals(0, chart.getData().get(0).getPieValue());
		assertEquals(dbBookPurchasesSum, chart.getData().get(1).getPieValue());
		
		String date = billsDate.format(CustomDate.dateFormat);
		robot.clickOn("#date-picker").eraseText(date.length()).write(date).press(KeyCode.ENTER);
		assertEquals(dbBillSalesSum, chart.getData().get(0).getPieValue());
	}
	
	@SuppressWarnings("unchecked")
	private void testBookExpenses_MonthlyChart(double[] monthlySales, double[] monthlyPurchases) {
		robot.clickOn("#monthly-toggle");
		
		BarChart<String, Number> monthlyChart = robot.lookup("#monthly-chart").queryAs(BarChart.class);
		Series<String, Number> salesData = monthlyChart.getData().get(0), purchasesData = monthlyChart.getData().get(1);
		
		assertTrue(salesData.getData().stream().allMatch(e -> e.getYValue().doubleValue() == monthlySales[Month.valueOf(e.getXValue().toUpperCase()).getValue() - 1]));
		assertTrue(purchasesData.getData().stream().allMatch(e -> e.getYValue().doubleValue() == monthlyPurchases[Month.valueOf(e.getXValue().toUpperCase()).getValue() - 1]));
	}

	private void testBookExpenses_TotalChart(double totalSales, double totalPurchases) {
		robot.clickOn("#total-toggle");
		
		PieChart totalChart = robot.lookup("#total-chart").queryAs(PieChart.class);
		assertEquals(totalSales, totalChart.getData().get(0).getPieValue());
		assertEquals(totalPurchases, totalChart.getData().get(1).getPieValue());
	}
} 
