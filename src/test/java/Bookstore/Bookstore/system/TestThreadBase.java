package Bookstore.Bookstore.system;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.testfx.api.FxRobot;
import org.testfx.util.WaitForAsyncUtils;

import Bookstore.Bookstore.TestingUtils;
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
import Bookstore.Bookstore.dal.repositories.BillRepository;
import Bookstore.Bookstore.dal.repositories.BookInventoryRepository;
import Bookstore.Bookstore.dal.repositories.BookPurchaseRepository;
import Bookstore.Bookstore.dal.repositories.CategoryRepository;
import Bookstore.Bookstore.dal.repositories.DbContext;
import Bookstore.Bookstore.dal.repositories.EmployeeRepository;

public abstract class TestThreadBase {
	protected static FxRobot robot = new FxRobot();
	
	protected static IBookInventoryService bookInventoryService;
	protected static ICategoryService categoryService;
	protected static IBillService billService;
	protected static IBookPurchaseService bookPurchaseService;
	protected static IEmployeeService employeeService;
	
	@BeforeAll
	static void setUp() {
		DbContext dbContext = new DbContext();

		bookInventoryService = new BookInventoryService(new BookInventoryRepository(Utils.testDataDirPath, dbContext));
		categoryService = new CategoryService(new CategoryRepository(Utils.testDataDirPath, dbContext));
		billService = new BillService(new BillRepository(Utils.testDataDirPath, dbContext));
		bookPurchaseService = new BookPurchaseService(new BookPurchaseRepository(Utils.testDataDirPath, dbContext));
		employeeService = new EmployeeService(new EmployeeRepository(Utils.testDataDirPath, dbContext));
	}
	
	@AfterAll
	protected static void tearDown() {
		TestingUtils.deleteTestDatabase(null);
	}
	
	void preconditions_LogInUser(String username, String password) {
		robot.clickOn("#username").write(username);
		robot.clickOn("#password").write(password);
		robot.clickOn("#loginBtn");
		
		WaitForAsyncUtils.waitForFxEvents();
	}
}
