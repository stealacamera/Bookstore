package Bookstore.Bookstore.system;

import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.File;
import java.time.LocalDate;
import java.util.Arrays;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.testfx.framework.junit5.ApplicationExtension;
import org.testfx.framework.junit5.Start;

import Bookstore.Bookstore.TestingUtils;
import Bookstore.Bookstore.bll.dto.BillDTO;
import Bookstore.Bookstore.bll.dto.BookDTO;
import Bookstore.Bookstore.bll.dto.BookInventoryDTO;
import Bookstore.Bookstore.bll.dto.EmployeeDTO;
import Bookstore.Bookstore.bll.dto.UserDTO;
import Bookstore.Bookstore.commons.utils.Utils;
import Bookstore.Bookstore.dal.models.utils.Role;
import Bookstore.Bookstore.startup.Main;
import javafx.scene.control.TableCell;
import javafx.stage.Stage;

@ExtendWith(ApplicationExtension.class)
public class TestLibrarianThreads extends TestThreadBase {
	private static EmployeeDTO currentUser;
	
	@BeforeAll
	static void preconditions_AddLibrarian() throws Exception {
		currentUser = new EmployeeDTO(
			new UserDTO("foobar", "foo bar", "foo@gmail.com", "password123", "069 123 1345", LocalDate.now()), 
			123, 1
		);
		
		employeeService.add(currentUser);
		currentUser.setId(employeeService.get(0).getId());
	}
	
	@BeforeAll
	static void preconditions_AddBooks() throws Exception {
		for(int i = 0; i < 3; i++)
			bookInventoryService.add(new BookInventoryDTO(
				new BookDTO("123-1-12-123123-" + i, "foobar", "foo bar", "foo", 1), 
				100 + i, 150 + i, 200 + i, 250 + i, LocalDate.now()
			));
	}
	
	@Start
	private void start(Stage stage) {
		Main.setUpApplication(
			stage, bookInventoryService, categoryService, 
			billService, bookPurchaseService, employeeService
		);
	}
	
	@SuppressWarnings("unchecked")
	@Test
	void testBillCreation() {
		// Preconditions
		preconditions_LogInUser(currentUser.getUsername(), currentUser.getPassword());
		
		robot.clickOn("#" + Role.CREATE_BILL.name());
		
		TableCell<BookInventoryDTO, String> bookRow = robot.lookup("#books-list").lookup(".table-cell").nth(0).queryAs(TableCell.class);
		BookInventoryDTO bookToAddInBill = bookRow.getTableRow().getItem();
		int quantity = 1, stock = bookToAddInBill.getStock() + 1;
		
		robot.clickOn(bookRow);
		
		// Exceed stock
		robot.clickOn("#quantity").eraseText(4).write(Integer.toString(stock + 1));
		robot.clickOn("#add-btn");
		TestingUtils.testErrorMessage(robot, "Not enough stock");
		
		// Set quantity within range
		robot.clickOn("#quantity").eraseText(4).write(Integer.toString(quantity));
		robot.clickOn("#add-btn");
		robot.clickOn("#submit-btn");
		
		// Post-conditions
		postconditions_BillAddedInDatabase(bookToAddInBill.getSellingPrice(), quantity);
		postconditions_BillReceiptSavedInSystem();
	}

	void postconditions_BillReceiptSavedInSystem() {
		File billFiles = new File(Utils.billsDirPath);
		assertTrue(billFiles.exists());

		assertEquals(1, Arrays.stream(billFiles.listFiles()).filter(file -> file.getName().equals(billService.count() + ".txt")).count());
		new File(Utils.billsDirPath, billService.count() + ".txt").delete();
	}
	
	void postconditions_BillAddedInDatabase(double sellingPrice, int quantity) {
		assertEquals(1, billService.count());
		assertEquals(new BillDTO(currentUser.getId(), quantity * sellingPrice, quantity), billService.get(0));
	}
}
