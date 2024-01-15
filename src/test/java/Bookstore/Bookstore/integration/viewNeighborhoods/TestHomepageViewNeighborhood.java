package Bookstore.Bookstore.integration.viewNeighborhoods;

import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDate;
import java.util.Map;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.testfx.api.FxRobot;
import org.testfx.framework.junit5.ApplicationExtension;
import org.testfx.framework.junit5.Start;
import org.testfx.util.WaitForAsyncUtils;

import Bookstore.Bookstore.bll.dto.BookDTO;
import Bookstore.Bookstore.bll.dto.BookInventoryDTO;
import Bookstore.Bookstore.bll.dto.EmployeeDTO;
import Bookstore.Bookstore.bll.dto.UserDTO;
import Bookstore.Bookstore.bll.services.BillService;
import Bookstore.Bookstore.bll.services.BookInventoryService;
import Bookstore.Bookstore.bll.services.BookPurchaseService;
import Bookstore.Bookstore.bll.services.CategoryService;
import Bookstore.Bookstore.bll.services.EmployeeService;
import Bookstore.Bookstore.bll.services.iservices.IBillService;
import Bookstore.Bookstore.bll.services.iservices.IBookInventoryService;
import Bookstore.Bookstore.bll.services.iservices.IBookPurchaseService;
import Bookstore.Bookstore.bll.services.iservices.ICategoryService;
import Bookstore.Bookstore.bll.services.iservices.IEmployeeService;
import Bookstore.Bookstore.commons.utils.Utils;
import Bookstore.Bookstore.controllers.HomepageController;
import Bookstore.Bookstore.dal.models.utils.Role;
import Bookstore.Bookstore.dal.repositories.BillRepository;
import Bookstore.Bookstore.dal.repositories.BookInventoryRepository;
import Bookstore.Bookstore.dal.repositories.BookPurchaseRepository;
import Bookstore.Bookstore.dal.repositories.CategoryRepository;
import Bookstore.Bookstore.dal.repositories.DbContext;
import Bookstore.Bookstore.dal.repositories.EmployeeRepository;
import Bookstore.Bookstore.startup.Session;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

@TestMethodOrder(OrderAnnotation.class)
@ExtendWith(ApplicationExtension.class)
public class TestHomepageViewNeighborhood extends TestViewNeighborHoodBase {	
	private int accessLevel = 0;
	private static EmployeeDTO loggedInUser;
	private static BookInventoryDTO lowStockBook;
	private static IEmployeeService employeeService;
	private static IBookInventoryService bookService;
	private static HomepageController controller;
	
	@BeforeAll
	static void setUp() {
		DbContext dbContext = new DbContext();
		
		bookService = new BookInventoryService(new BookInventoryRepository(Utils.testDataDirPath, dbContext));
		employeeService = new EmployeeService(new EmployeeRepository(Utils.testDataDirPath, dbContext));
		IBillService billService = new BillService(new BillRepository(Utils.testDataDirPath, dbContext));
		ICategoryService categoryService = new CategoryService(new CategoryRepository(Utils.testDataDirPath, dbContext));
		IBookPurchaseService bookPurchaseService = new BookPurchaseService(new BookPurchaseRepository(Utils.testDataDirPath, dbContext));
		
		controller = new HomepageController(bookService, categoryService, billService, bookPurchaseService, employeeService);
		
		setUpDummyData(bookService, employeeService);
		loggedInUser = employeeService.get(0);
		Session.setCurrentUser(loggedInUser);
	}
	
	private static void setUpDummyData(IBookInventoryService bookService, IEmployeeService employeeService) {
		// Creating user
		loggedInUser = new EmployeeDTO(
			new UserDTO("foobar", "foo bar", "foo@gmail.com", "password123", "069 123 4566", LocalDate.now()), 
			100, 2
		);
		
		// Creating books
		lowStockBook = new BookInventoryDTO(
			new BookDTO("123-1-12-123123-1", "low stock book", "foo", "Foo Co.", 1), 
			100, 100, 100, 2, LocalDate.now()
		);

		BookInventoryDTO highStockBook = new BookInventoryDTO(
			new BookDTO("123-1-12-123123-2", "high stock book", "foo", "Foo Co.", 1), 
			100, 100, 100, 200, LocalDate.now()
		);
		
		try {
			employeeService.add(loggedInUser);
			
			bookService.add(lowStockBook);
			bookService.add(highStockBook);
		} catch (Exception e) {
			e.printStackTrace(); // Won't be thrown
		}
	}
	
	void changeUserLevel(int accessLevel) {
		try {
			loggedInUser.setId(0);
			loggedInUser.setAccessLvl(accessLevel);
			loggedInUser.setPassword("password123");
						
			employeeService.remove(0);
			employeeService.add(loggedInUser);
			
			loggedInUser = employeeService.get(0);
			Session.setCurrentUser(loggedInUser);
		} catch (Exception e) {
			e.printStackTrace(); // Won't be thrown
		}
	}
	
	@Start
	private void start(Stage stage) {
		if(accessLevel == 0)
			accessLevel++;
		else
			changeUserLevel(accessLevel++);
		
		stage.setScene(new Scene(controller.getIndexView()));
		stage.show();
	}
	
	@Order(1)
	@Test
	void testLowStockWarning(FxRobot robot) {
		WaitForAsyncUtils.waitForFxEvents();
		
		Node alert = robot.lookup(".dialog-pane").query();
		robot.from(alert).lookup((Text t) -> t.getText().equals("These books need to be restocked:\n" + lowStockBook.getBook().getTitle()));
	}
	
	@Test
	@Order(2)
	void testHomeActionButtons_LibrarianUser(FxRobot robot) {
		testHomeActionButtons(robot);
	}
	
	@Test
	@Order(3)
	void testHomeActionButtons_ManagerUser(FxRobot robot) {
		testHomeActionButtons(robot);
	}
	
	@Test
	@Order(4)
	void testHomeActionButtons_AdminUser(FxRobot robot) {
		testHomeActionButtons(robot);
	}
	
	void testHomeActionButtons(FxRobot robot) {
		Map<Role, Boolean> permissions = loggedInUser.getPermissions();
		Pane actionsPane =  robot.lookup("#user-actions").queryAs(Pane.class);
			
		assertAll(
			() -> {
				int nrOfActions = permissions.size();
				if(loggedInUser.getAccessLvl() != 1) nrOfActions--;
				
				assertEquals(nrOfActions, actionsPane.getChildren().size());
			},
			() -> 
			{
				// Check all action buttons
				for(Node actionBtn: actionsPane.getChildren()) {
					Button btn = (Button) actionBtn;
					Role btnRole = Role.fromString(btn.getText());
					
					assertTrue(permissions.containsKey(btnRole));
					assertEquals(permissions.get(btnRole), !btn.isDisabled());
				}
			}
		);
	}
}
