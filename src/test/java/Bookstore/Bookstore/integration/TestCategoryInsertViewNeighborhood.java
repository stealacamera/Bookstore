package Bookstore.Bookstore.integration;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.testfx.api.FxAssert;
import org.testfx.api.FxRobot;
import org.testfx.framework.junit5.ApplicationExtension;
import org.testfx.framework.junit5.Start;
import org.testfx.matcher.control.LabeledMatchers;
import org.testfx.util.WaitForAsyncUtils;

import Bookstore.Bookstore.bll.services.CategoryService;
import Bookstore.Bookstore.bll.services.iservices.ICategoryService;
import Bookstore.Bookstore.commons.utils.Utils;
import Bookstore.Bookstore.controllers.HomepageController;
import Bookstore.Bookstore.dal.repositories.CategoryRepository;
import Bookstore.Bookstore.dal.repositories.DbContext;
import javafx.scene.Scene;
import javafx.stage.Stage;

@ExtendWith(ApplicationExtension.class)
public class TestCategoryInsertViewNeighborhood extends TestNeighborHoodBase {
	private static ICategoryService categoryService;
	private static HomepageController controller;
	
	@BeforeAll
	static void setUp() {
		categoryService = new CategoryService(new CategoryRepository(Utils.testingUserDataDirPath, new DbContext()));
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
		
		WaitForAsyncUtils.waitForFxEvents();	
		FxAssert.verifyThat("#alert_error_message", LabeledMatchers.hasText("Input fields cannot be empty: Please enter a value for name"));
		
		assertEquals(0, categoryService.getAll().size());
	}
	
	@Order(2)
	@Test
	void testInsert_ValidValues(FxRobot robot) {
		String name = "category";
		robot.clickOn("#category-name").write(name);
		robot.clickOn("#submit-btn");
		
		assertEquals(1, categoryService.getAll().size());
		assertEquals(name, categoryService.get(0).getName());
	}
}
