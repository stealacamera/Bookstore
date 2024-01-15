package Bookstore.Bookstore.integration.viewNeighborhoods;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.extension.ExtendWith;
import org.testfx.api.FxRobot;
import org.testfx.framework.junit5.ApplicationExtension;
import org.testfx.framework.junit5.Start;

import Bookstore.Bookstore.TestingUtils;
import Bookstore.Bookstore.bll.services.CategoryService;
import Bookstore.Bookstore.bll.services.iservices.ICategoryService;
import Bookstore.Bookstore.commons.utils.Utils;
import Bookstore.Bookstore.controllers.HomepageController;
import Bookstore.Bookstore.dal.repositories.CategoryRepository;
import Bookstore.Bookstore.dal.repositories.DbContext;
import javafx.scene.Scene;
import javafx.stage.Stage;

@TestMethodOrder(OrderAnnotation.class)
@ExtendWith(ApplicationExtension.class)
public class TestCategoryInsertViewNeighborhood extends TestViewNeighborHoodBase {
	private static ICategoryService categoryService;
	private static HomepageController controller;
	
	@BeforeAll
	static void setUp() {
		categoryService = new CategoryService(new CategoryRepository(Utils.testDataDirPath, new DbContext()));
		controller = new HomepageController(null, categoryService, null, null, null);
	}
	
	@Start
	private void start(Stage stage) {				
		stage.setScene(new Scene(controller.getAddCategoryView()));
		stage.show();
	}
	
	@Order(1)
	@Test
	void testInsert_InvalidValues(FxRobot robot) {
		robot.clickOn("#submit-btn");
		
		TestingUtils.testErrorMessage(robot, "Input fields cannot be empty: Please enter a value for name");
		assertEquals(0, categoryService.count());
	}
	
	@Order(2)
	@Test
	void testInsert_ValidValues(FxRobot robot) {
		String name = "category";
		robot.clickOn("#category-name").write(name);
		robot.clickOn("#submit-btn");
		
		assertEquals(1, categoryService.count());
		assertEquals(name, categoryService.get(0).getName());
	}
}
