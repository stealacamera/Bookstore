package Bookstore.Bookstore.integration.serviceNeighborhoods.stepDefinitions;

import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import Bookstore.Bookstore.TestingUtils;
import Bookstore.Bookstore.bll.dto.CategoryDTO;
import Bookstore.Bookstore.bll.services.CategoryService;
import Bookstore.Bookstore.bll.services.iservices.ICategoryService;
import Bookstore.Bookstore.commons.exceptions.EmptyInputException;
import Bookstore.Bookstore.commons.utils.Utils;
import Bookstore.Bookstore.dal.models.Category;
import Bookstore.Bookstore.dal.repositories.CategoryRepository;
import Bookstore.Bookstore.dal.repositories.DbContext;
import Bookstore.Bookstore.dal.repositories.irepositories.ICategoryRepository;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

public class TestCategoryServiceNeighborhood {
	private ICategoryRepository repository = new CategoryRepository(Utils.testDataDirPath, new DbContext());
	private ICategoryService service = new CategoryService(repository);

	private List<CategoryDTO> dtos;
	private CategoryDTO dtoModelInUse;
	
	@Before
	public void recreateDatabase() {		
		repository = new CategoryRepository(Utils.testDataDirPath, new DbContext());
		service = new CategoryService(repository);
	}
	
	@After
	public void tearDownDatabase() {
		TestingUtils.deleteTestDatabase(null);
	}
	
	@Given("these existing Categories saved in the database")
	public void these_existing_categories_saved_in_the_database(io.cucumber.datatable.DataTable dataTable) {
		List<Map<String, String>> rowData = dataTable.asMaps();
		
		for(Map<String, String> data: rowData) {
			try {
				Category category = new Category(Integer.parseInt(data.get("ID")), data.get("Category"));
				repository.add(category);
			} catch (EmptyInputException e) {
				e.printStackTrace();
			}
		}
	}

	@When("all categories requested")
	public void all_categories_requested() {
	    dtos = service.getAll();
	}

	@Then("categories {string} , {string}, {string} should be returned")
	public void categories_should_be_returned(String category1, String category2, String category3) {
		assertIterableEquals(
		   Arrays.asList(new CategoryDTO(1, category1), new CategoryDTO(2, category2), new CategoryDTO(3, category3)), 
		   dtos
		);
	}

	@Given("the ID {string}")
	public void the_id(String string) {
	    dtoModelInUse = service.getById(Integer.parseInt(string));
	}

	@Then("category {string} should be returned")
	public void category_should_be_returned(String string) {
		assertEquals(dtoModelInUse.getName(), string);
	}

	@Given("new category {string}")
	public void new_category(String string) throws Exception {
	    dtoModelInUse = new CategoryDTO(string);
	    service.add(dtoModelInUse);
	}

	@Then("category should be saved in the database")
	public void category_should_be_saved_in_the_database() {	    
	    assertTrue(repository.getAll().stream().anyMatch(e -> e.getName().equals(dtoModelInUse.getName())));
	}
}
