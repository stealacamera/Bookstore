package test.unit.dal;

import static org.junit.Assert.assertNull;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import dal.CategoryRepository;
import dal.models.Category;
import exceptions.EmptyInputException;

public class TestCategoryRepository extends TestRepositoryBase {
	private CategoryRepository repository;
	
	@BeforeEach
	void setUpEach() throws IOException {
		dataFile = new File(dataDir, Category.class.getSimpleName());
		dataFile.createNewFile();
	}
	
	@Test
	void testGetById_NotInDatabase() throws EmptyInputException, IOException {
		Category[] categories = createTempData();
		setTempDataToFile(new ArrayList<>(Arrays.asList(categories)));
		repository = new CategoryRepository(dataDirPath, dbContext);
		
		assertNull(repository.getById(0));
		assertNull(repository.getById(categories[categories.length - 1].getId() + 1));
	}
	
	@Test
	void testGetById_InDatabase() throws EmptyInputException, IOException {
		Category[] categories = createTempData();
		setTempDataToFile(new ArrayList<>(Arrays.asList(categories)));
		repository = new CategoryRepository(dataDirPath, dbContext);
		
		// Testing at index 0, 1, 3, 5, 6 for id 1, 2, 4, 6, 7
		for(int i = 0; i < categories.length; i++) {
			if(i == 2 || i == 4) continue;
			assertEquals(categories[i], repository.getById(categories[i].getId()));
		}
	}
	
	@ParameterizedTest
	@ValueSource(strings = {"foo", "foo bar"})
	void testGetByName_NotInDatabase(String input) throws EmptyInputException, IOException {
		repository = new CategoryRepository(dataDirPath, dbContext);
		assertNull(repository.getByName(input));
	}
	
	@Test
	void testGetByName_InDatabase() throws EmptyInputException, IOException {
		Category[] categories = createTempData();
		setTempDataToFile(new ArrayList<>(Arrays.asList(categories)));
		repository = new CategoryRepository(dataDirPath, dbContext);
		
		// Testing at index 0, 1, 3, 5, 6 for id 1, 2, 4, 6, 7
		for(int i = 0; i < categories.length; i++) {
			if(i == 2 || i == 4) continue;
			assertEquals(categories[i], repository.getByName(categories[i].getName()));
		}
	}
	
	private Category[] createTempData() throws EmptyInputException {
		Category[] categories = new Category[7];
		
		for(int i = 0; i < categories.length; i++)
			categories[i] = new Category("a".repeat(i + 1));
		
		return categories;
	}
}
