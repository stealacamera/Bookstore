package Bookstore.Bookstore.integration.viewNeighborhoods;

import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.File;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Stream;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.testfx.api.FxRobot;
import org.testfx.framework.junit5.ApplicationExtension;
import org.testfx.framework.junit5.Start;

import Bookstore.Bookstore.TestingUtils;
import Bookstore.Bookstore.bll.dto.BillDTO;
import Bookstore.Bookstore.bll.dto.BookDTO;
import Bookstore.Bookstore.bll.dto.BookInventoryDTO;
import Bookstore.Bookstore.bll.dto.EmployeeDTO;
import Bookstore.Bookstore.bll.dto.UserDTO;
import Bookstore.Bookstore.bll.services.BillService;
import Bookstore.Bookstore.bll.services.BookInventoryService;
import Bookstore.Bookstore.bll.services.EmployeeService;
import Bookstore.Bookstore.commons.utils.Utils;
import Bookstore.Bookstore.controllers.BillController;
import Bookstore.Bookstore.dal.repositories.BillRepository;
import Bookstore.Bookstore.dal.repositories.BookInventoryRepository;
import Bookstore.Bookstore.dal.repositories.DbContext;
import Bookstore.Bookstore.dal.repositories.EmployeeRepository;
import Bookstore.Bookstore.startup.Session;
import javafx.scene.Scene;
import javafx.scene.control.TableCell;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;

@ExtendWith(ApplicationExtension.class)
public class TestBillInsertViewNeighborhood extends TestViewNeighborHoodBase {
	private static FxRobot robot = new FxRobot();
	private static List<BookInventoryDTO> books;
	
	private static BillService billService;
	private static BookInventoryService bookService;
	private static BillController controller;
	
	@BeforeAll
	public static void setUp() throws Exception {
		DbContext dbContext = new DbContext();
		
		billService = new BillService(new BillRepository(Utils.testDataDirPath, dbContext));
		bookService = new BookInventoryService(new BookInventoryRepository(Utils.testDataDirPath, dbContext));
		controller = new BillController(billService, bookService);
		
		books = new ArrayList<>();
		EmployeeService employeeService = new EmployeeService(new EmployeeRepository(Utils.testDataDirPath, dbContext));
		createDummyData(employeeService, bookService, books);
		
		Session.setCurrentUser(employeeService.get(0));
	}
	
	static void createDummyData(EmployeeService employeeService, BookInventoryService bookService, List<BookInventoryDTO> booksList) throws Exception {
		EmployeeDTO user = new EmployeeDTO(
			new UserDTO("foobar", "foo bar", "foo@gmail.com", "password123", "069 123 1234", LocalDate.now()), 
			100, 1
		);
		
		employeeService.add(user);
		
		for(int i = 0; i < 3; i++) {
			BookInventoryDTO book = new BookInventoryDTO(
				new BookDTO("123-1-12-123123-" + i, "foobar", "foo bar", "foobar", 1), 
				10, 10, 10, 10, LocalDate.now()
			);
			
			bookService.add(book);
			booksList.add(book);
		}
	}
	
	@Start
	private void start(Stage stage) {				
		stage.setScene(new Scene(controller.getCreateView()));
		stage.show();
	}
	
	@Test
	public void testBooksIndexList() {
		List<Object> viewList = robot.lookup("#books-list").queryTableView().getItems();
		
		assertEquals(books.size(), viewList.size());
		assertTrue(books.containsAll(viewList));
	}
	
	@Test
	public void testAddBookToBill_InvalidValues_BookNotSelected() {
		robot.clickOn("#add-btn");
		
		TestingUtils.testErrorMessage(robot, "Please select a book");
		assertEquals(0, robot.lookup("#bills-list").queryTableView().getItems().size());
	}
	
	@Test
	public void testAddBookToBill_InvalidValues_ExistingBillItem() {
		for(int i = 0; i < 2; i++) {
			robot.clickOn(robot.lookup("#books-list").lookup(".table-cell").nth(0).queryAs(TableCell.class));
			robot.clickOn("#quantity").eraseText(2).write("1");
			robot.clickOn("#add-btn");
		}
		
		TestingUtils.testErrorMessage(robot, "Element with these details (book entity) already exists.");
	}
	
	@ParameterizedTest
	@MethodSource("provideBookQuantityInvalidValues")
	public void testAddBookToBill_InvalidValues(String quantity, String errorMessage) {
		robot.clickOn(robot.lookup("#books-list").lookup(".table-cell").nth(0).queryAs(TableCell.class));
		robot.clickOn("#quantity").eraseText(2).write(quantity);
		robot.clickOn("#add-btn");
		
		TestingUtils.testErrorMessage(robot, errorMessage);
		assertEquals(0, robot.lookup("#bills-list").queryTableView().getItems().size());
	}
	
	@SuppressWarnings("unchecked")
	@Test
	public void testAddBookToBill_ValidValues() {
		robot.clickOn(robot.lookup("#books-list").lookup(".table-cell").nth(0).queryAs(TableCell.class));
		robot.clickOn("#quantity").eraseText(2).write("1");
		robot.clickOn("#add-btn");
		
		List<Object> billItems = robot.lookup("#bills-list").queryTableView().getItems();
		assertEquals(1, billItems.size());
		
		Entry<BookInventoryDTO, Integer> entry = (Map.Entry<BookInventoryDTO, Integer>) billItems.get(0);
		
		assertEquals(books.get(books.size() - 1), entry.getKey());
		assertEquals(1, entry.getValue());
	}
	
	@ParameterizedTest
	@MethodSource("provideBookQuantityInvalidValues")
	public void testChangeBillItemStock_InvalidValues(String newQuantity, String errorMessage) {
		robot.clickOn(robot.lookup("#books-list").lookup(".table-cell").nth(0).queryAs(TableCell.class));
		robot.clickOn("#quantity").eraseText(2).write("1");
		robot.clickOn("#add-btn");
		
		robot.doubleClickOn(robot.lookup("#bills-list").lookup(".table-cell").nth(2).queryAs(TableCell.class))
			 .eraseText(1).write(newQuantity).press(KeyCode.ENTER);
		
		TestingUtils.testErrorMessage(robot, errorMessage);
	}
	
	@Test
	public void testRemoveBillItem_InvalidValues() {
		robot.clickOn("#remove-btn");
		TestingUtils.testErrorMessage(robot, "Please select an item in the bill");
	}
	
	@Test
	public void testRemoveBillItem_ValidValues() {
		robot.clickOn(robot.lookup("#books-list").lookup(".table-cell").nth(0).queryAs(TableCell.class));
		robot.clickOn("#quantity").eraseText(2).write("1");
		robot.clickOn("#add-btn");
		
		robot.clickOn(robot.lookup("#bills-list").lookup(".table-cell").nth(0).queryAs(TableCell.class));
		robot.clickOn("#remove-btn");
		
		assertEquals(0, robot.lookup("#bills-list").queryTableView().getItems().size());
	}
	
	@Test
	public void testSubmitBill() {
		int quantity = 3;
		
		robot.clickOn(robot.lookup("#books-list").lookup(".table-cell").nth(0).queryAs(TableCell.class));
		robot.clickOn("#quantity").eraseText(2).write(Integer.toString(quantity)).press(KeyCode.ENTER);
		robot.clickOn("#add-btn");
		robot.clickOn("#submit-btn");
		
		assertEquals(1, billService.count());
		
		BillDTO bill = billService.get(0);
		assertEquals(quantity, bill.getNumOfBooks());
		assertEquals(books.get(books.size() - 1).getSellingPrice() * quantity, bill.getSaleAmount());
		
		File billFiles = new File(Utils.billsDirPath);
		assertTrue(billFiles.exists());

		assertEquals(1, Arrays.stream(billFiles.listFiles()).filter(file -> file.getName().equals(billService.count() + ".txt")).count());
		new File(Utils.billsDirPath, billService.count() + ".txt").delete();
	}
	
	private static Stream<Arguments> provideBookQuantityInvalidValues() {
		return Stream.of(
			Arguments.of("", "Input fields cannot be empty: Please enter a value for stock"),
			Arguments.of("10000000", "Not enough stock")
		);
	}
}
