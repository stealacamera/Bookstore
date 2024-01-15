package Bookstore.Bookstore.integration.viewNeighborhoods;

import static org.junit.Assert.assertNull;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
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
import Bookstore.Bookstore.bll.dto.BookDTO;
import Bookstore.Bookstore.bll.dto.BookInventoryDTO;
import Bookstore.Bookstore.bll.dto.BookPurchaseDTO;
import Bookstore.Bookstore.bll.services.BookInventoryService;
import Bookstore.Bookstore.bll.services.BookPurchaseService;
import Bookstore.Bookstore.bll.services.CategoryService;
import Bookstore.Bookstore.bll.services.iservices.IBookInventoryService;
import Bookstore.Bookstore.bll.services.iservices.IBookPurchaseService;
import Bookstore.Bookstore.bll.services.iservices.ICategoryService;
import Bookstore.Bookstore.commons.exceptions.EmptyInputException;
import Bookstore.Bookstore.commons.utils.Utils;
import Bookstore.Bookstore.controllers.BookController;
import Bookstore.Bookstore.dal.repositories.BookInventoryRepository;
import Bookstore.Bookstore.dal.repositories.BookPurchaseRepository;
import Bookstore.Bookstore.dal.repositories.CategoryRepository;
import Bookstore.Bookstore.dal.repositories.DbContext;
import javafx.scene.Scene;
import javafx.scene.control.TableCell;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;

@ExtendWith(ApplicationExtension.class)
public class TestBookIndexViewNeighborhood extends TestViewNeighborHoodBase {
	private FxRobot robot;
	private static List<BookInventoryDTO> books;
	private static List<BookPurchaseDTO> purchases;
	private static IBookInventoryService bookService;
	private static IBookPurchaseService bookPurchaseService;
	private static BookController controller;
	
	@BeforeAll
	static void setUp() {
		DbContext dbContext = new DbContext();
		bookService = new BookInventoryService(new BookInventoryRepository(Utils.testDataDirPath, dbContext));
		bookPurchaseService = new BookPurchaseService(new BookPurchaseRepository(Utils.testDataDirPath, dbContext));
		ICategoryService categoryService = new CategoryService(new CategoryRepository(Utils.testDataDirPath, dbContext));
		
		controller = new BookController(bookService, bookPurchaseService, categoryService);
		createDummyData(bookService);
		
		books = bookService.getAll();
		purchases = new ArrayList<>();
	}
	
	static void createDummyData(IBookInventoryService bookService) {		
		for(int i = 0; i < 3; i++) {
			BookInventoryDTO book = new BookInventoryDTO(
				new BookDTO("123-1-12-123123-" + i, "foobar", "foo bar", "foobar", 1), 
				10, 10, 10, 10, LocalDate.now()
			);
			
			try {
				bookService.add(book);
			} catch (Exception e) {
				e.printStackTrace(); // Won't be thrown
			}
		}
	}
	
	@Start
	private void start(Stage stage) {				
		stage.setScene(new Scene(controller.getIndexView()));
		stage.show();
	}
	
	@Test
	void testIndexList(FxRobot robot) {
		assertEquals(bookService.count(), robot.lookup("#books-list").queryTableView().getItems().size());
	}
	
	@ParameterizedTest
	@MethodSource("provideStockChangeValidValues")
	void testChangeBookStock_ValidValues(int oldStock, int newStock) throws EmptyInputException {
		robot.doubleClickOn(robot.lookup(".table-cell").nth(2).queryAs(TableCell.class))
			 .eraseText(5).write(Integer.toString(newStock)).press(KeyCode.ENTER);
		
		BookInventoryDTO book = bookService.get(0);
		assertEquals(newStock, bookService.getByISBN(book.getBook().getIsbn()).getStock());		
		
		if(oldStock < newStock) {
			double purchasePrice = book.getPurchasedPrice() * (newStock - oldStock);
			purchases.add(new BookPurchaseDTO(purchasePrice, LocalDate.now()));
			
			assertEquals(purchases.size(), bookPurchaseService.count());
			assertEquals(purchasePrice, bookPurchaseService.get(purchases.size() - 1).getAmount());
		}
	}
	
	@ParameterizedTest
	@MethodSource("provideStockChangeInvalidValues")
	void testChangeBookStock_InvalidValues(BookInventoryDTO book, String newStock, String errorMessage) throws EmptyInputException {
		robot.doubleClickOn(robot.lookup(".table-cell").nth(2).queryAs(TableCell.class))
			 .eraseText(5).write(newStock).press(KeyCode.ENTER);
		
		TestingUtils.testErrorMessage(robot, errorMessage);
		
		assertEquals(book.getStock(), bookService.getByISBN(book.getBook().getIsbn()).getStock());
	}
	
	@Test
	void testDeleteBook_EntityNotSelected(FxRobot robot) {
		robot.clickOn("#delete-btn");
		
		TestingUtils.testErrorMessage(robot, "Please select an item");
		
		assertEquals(books.size(), bookService.count());
	}
	
	@Test
	void testDeleteBook_EntitySelected(FxRobot robot) throws EmptyInputException {
		robot.clickOn(robot.lookup(".table-cell").nth(0).queryAs(TableCell.class));
		robot.clickOn("#delete-btn");
		
		String isbnRemoved = books.get(0).getBook().getIsbn();
		books.remove(0);
		
		assertEquals(books.size(), bookService.count());
		assertNull(bookService.getByISBN(isbnRemoved));
	}
	
	private static Stream<Arguments> provideStockChangeValidValues() {
		BookInventoryDTO book = books.get(0);
		
		return Stream.of(
			Arguments.of(book.getStock(), book.getStock() - 1),
			Arguments.of(book.getStock() - 1, book.getStock() + 5)
		);
	}
	
	private static Stream<Arguments> provideStockChangeInvalidValues() {
		BookInventoryDTO book = bookService.get(0);
		
		return Stream.of(
			Arguments.of(book, "", "Input fields cannot be empty: Please enter a value for stock"),
			Arguments.of(book, "-2", "Incorrect stock: Please enter a positive number for the field")
		);
	}
}
