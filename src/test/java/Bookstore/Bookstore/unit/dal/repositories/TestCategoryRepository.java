package Bookstore.Bookstore.unit.dal.repositories;

import static org.junit.Assert.assertNull;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.Stream;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

import Bookstore.Bookstore.dal.models.Category;
import Bookstore.Bookstore.dal.repositories.CategoryRepository;
import Bookstore.Bookstore.commons.exceptions.EmptyInputException;

public class TestCategoryRepository extends TestRepositoryBase {
	private CategoryRepository repository;
	private static Category[] categories;
	
	@BeforeAll
	static void setUpDummyData() throws EmptyInputException {
		categories = new Category[7];
		
		for(int i = 0; i < categories.length; i++)
			categories[i] = new Category("a".repeat(i + 1));
	}
	
	@BeforeEach
	void setUpEach() throws IOException {
		dataFile = new File(dataDir, Category.class.getSimpleName());
		dataFile.createNewFile();
		
		setTempDataToFile(new ArrayList<>(Arrays.asList(categories)));
		repository = new CategoryRepository(dataDirPath, dbContext);
	}
	
	@Test
	void testGetById_NotInDatabase() throws EmptyInputException, IOException {
		assertNull(repository.getById(0));
		assertNull(repository.getById(categories[categories.length - 1].getId() + 1));
	}
	
	@ParameterizedTest
	@MethodSource("provideValuesForExistingData")
	void testGetById_InDatabase(Category model) throws EmptyInputException, IOException {
		assertEquals(model, repository.getById(model.getId()));
	}
	
	@ParameterizedTest
	@ValueSource(strings = {"foo", "foo bar"})
	void testGetByName_NotInDatabase(String input) throws EmptyInputException, IOException {
		assertNull(repository.getByName(input));
	}
	
	@ParameterizedTest
	@MethodSource("provideValuesForExistingData")
	void testGetByName_InDatabase(Category model) throws EmptyInputException, IOException {
		assertEquals(model, repository.getByName(model.getName()));
	}
	
	private static Stream<Arguments> provideValuesForExistingData() {		
		return Stream.of(
			Arguments.of(categories[0]),
			Arguments.of(categories[1]),
			Arguments.of(categories[3]),
			Arguments.of(categories[5]),
			Arguments.of(categories[6])
		);
	}
}
