package Bookstore.Bookstore.integration.viewNeighborhoods;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDate;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.testfx.api.FxAssert;
import org.testfx.api.FxRobot;
import org.testfx.framework.junit5.ApplicationExtension;
import org.testfx.framework.junit5.Start;
import org.testfx.matcher.control.LabeledMatchers;
import org.testfx.util.WaitForAsyncUtils;

import Bookstore.Bookstore.bll.dto.BookDTO;
import Bookstore.Bookstore.bll.dto.BookInventoryDTO;
import Bookstore.Bookstore.bll.dto.CategoryDTO;
import Bookstore.Bookstore.bll.services.BookInventoryService;
import Bookstore.Bookstore.bll.services.BookPurchaseService;
import Bookstore.Bookstore.bll.services.CategoryService;
import Bookstore.Bookstore.bll.services.iservices.IBookInventoryService;
import Bookstore.Bookstore.bll.services.iservices.IBookPurchaseService;
import Bookstore.Bookstore.bll.services.iservices.ICategoryService;
import Bookstore.Bookstore.commons.utils.Utils;
import Bookstore.Bookstore.controllers.BookController;
import Bookstore.Bookstore.dal.repositories.BookInventoryRepository;
import Bookstore.Bookstore.dal.repositories.BookPurchaseRepository;
import Bookstore.Bookstore.dal.repositories.CategoryRepository;
import Bookstore.Bookstore.dal.repositories.DbContext;
import javafx.scene.Scene;
import javafx.scene.control.ListCell;
import javafx.stage.Stage;

@ExtendWith(ApplicationExtension.class)
public class TestBookInsertViewNeighborhood extends TestViewNeighborHoodBase {
	private static BookInventoryDTO book;
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
		createDummyData(categoryService);
		
		book = new BookInventoryDTO(new BookDTO("123-1-12-123123-1", "foobar", "foo bar", "foobar", 1), 10, 20, 30, 40, LocalDate.now());
	}
	
	static void createDummyData(ICategoryService categoryService) {
		try {
			categoryService.add(new CategoryDTO("foo"));
		} catch (Exception e) {
			e.printStackTrace(); // Won't be thrown
		}
	}
	
	@Start
	private void start(Stage stage) {				
		stage.setScene(new Scene(controller.getInsertView()));
		stage.show();
	}
	
	@Test
	void testAddBook_InvalidValues(FxRobot robot) {
		submitForm(
			robot, book.getBook().getIsbn(), "", book.getBook().getAuthor(), 
			book.getBook().getCategoryId(), book.getBook().getSupplier(), book.getPurchasedPrice(), 
			book.getOriginalPrice(), book.getSellingPrice(), book.getStock());
		
		WaitForAsyncUtils.waitForFxEvents();	
		FxAssert.verifyThat(
			"#alert_error_message", 
			LabeledMatchers.hasText("Input fields cannot be empty: Please enter a value for title")
		);
	}
	
	@Test
	void testAddBook_ValidValues(FxRobot robot) {
		submitForm(
			robot, book.getBook().getIsbn(), book.getBook().getTitle(), book.getBook().getAuthor(), 
			book.getBook().getCategoryId(), book.getBook().getSupplier(), book.getPurchasedPrice(),
			book.getOriginalPrice(), book.getSellingPrice(), book.getStock());
		
		assertEquals(1, bookService.count());
		assertEquals(book, bookService.get(0));
		
		assertEquals(1, bookPurchaseService.count());
		assertEquals(book.getSellingPrice() * book.getStock(), bookPurchaseService.get(0).getAmount());
	}
	
	void submitForm(FxRobot robot, String isbn, String title, String author, int categoryId, String supplier, double purchasedPrice, double originalPrice, double sellingPrice, int stock) {
		robot.clickOn("#isbn").write(isbn);
		robot.clickOn("#title").write(title);
		robot.clickOn("#author").write(author);
		robot.clickOn("#supplier").write(supplier);
		robot.clickOn("#purchased-price").eraseText(4).write(Double.toString(purchasedPrice));
		robot.clickOn("#original-price").eraseText(4).write(Double.toString(originalPrice));
		robot.clickOn("#selling-price").eraseText(4).write(Double.toString(sellingPrice));
		robot.clickOn("#stock").eraseText(1).write(Integer.toString(stock));
		robot.clickOn(robot.lookup(".list-cell").nth(categoryId - 1).queryAs(ListCell.class));
		
		robot.clickOn("#submit-btn");
	}
}
