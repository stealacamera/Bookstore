package Bookstore.Bookstore.unit.bll;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Arrays;
import java.util.stream.Stream;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;

import Bookstore.Bookstore.bll.dto.CategoryDTO;
import Bookstore.Bookstore.bll.services.CategoryService;
import Bookstore.Bookstore.dal.models.Category;
import Bookstore.Bookstore.commons.exceptions.EmptyInputException;
import Bookstore.Bookstore.commons.exceptions.ExistingObjectException;
import Bookstore.Bookstore.commons.exceptions.IncorrectPermissionsException;
import Bookstore.Bookstore.commons.exceptions.NonPositiveInputException;
import Bookstore.Bookstore.commons.exceptions.WrongFormatException;
import Bookstore.Bookstore.commons.exceptions.WrongLengthException;
import javafx.collections.FXCollections;
import Bookstore.Bookstore.unit.bll.mocks.CategoryRepositoryMock;

public class TestCategoryService {
	private static CategoryService service;
	private static CategoryRepositoryMock mockRepository;
	private static Category[] models;
	private static CategoryDTO[] modelDTOs;
	
	@BeforeAll
	public static void setUpDummyData() throws EmptyInputException {
		models = new Category[7];
		modelDTOs = new CategoryDTO[models.length];
		
		for(int i = 0; i < models.length; i++) {
			models[i] = new Category((((char) 97) + "").repeat(i + 1));
			modelDTOs[i] = new CategoryDTO(models[i].getId(), models[i].getName());
		}
	}
	
	@BeforeEach
	public void setUp() {
		mockRepository = new CategoryRepositoryMock();
		mockRepository.addDummyData(models);
		
		service = new CategoryService(mockRepository);
	}
	
	@Test
	public void testGetAll_Empty() {
		service = new CategoryService(new CategoryRepositoryMock());
		assertIterableEquals(FXCollections.observableArrayList(), service.getAll());
	}
	
	@Test
	public void testGetAll_NonEmpty() {
		assertIterableEquals(Arrays.asList(modelDTOs), service.getAll());
	}
	
	@ParameterizedTest
	@NullSource
	@ValueSource(strings = {"", "  "})
	public void testAdd_InvalidValues(String input) {
		Exception exception = assertThrows(EmptyInputException.class, () -> service.add(new CategoryDTO(0, input)));
		assertTrue(exception.getMessage().contains("Input fields cannot be empty: Please enter a value for name"));
	}
	
	@ParameterizedTest
	@MethodSource("provideValuesForExistingNames")
	public void testAdd_ExistingName(CategoryDTO model) {
		Exception exception = assertThrows(ExistingObjectException.class, () -> service.add(model));
		assertTrue(exception.getMessage().contains("Element with these details already exists"));
	}
	
	@Test
	public void testAdd_NullValue() throws ExistingObjectException, EmptyInputException, NonPositiveInputException, WrongFormatException, WrongLengthException, IncorrectPermissionsException {
		assertFalse(service.add(null));
	}
	
	@Test
	public void testAdd_NonExistingName() throws ExistingObjectException, EmptyInputException, NonPositiveInputException, WrongFormatException, WrongLengthException, IncorrectPermissionsException {
		CategoryDTO model = new CategoryDTO(0, "nonexisting name");
		
		assertTrue(service.add(model));
		
		Category addedModel = mockRepository.getByName(model.getName());
		assertNotNull(addedModel);
		assertEquals(addedModel.getName(), model.getName());
	}
	
	@Test
	public void testGetById_NotInDatabase() {
		assertNull(service.getById(0));
		assertNull(service.getById(models[models.length - 1].getId() + 1));
	}
	
	@ParameterizedTest
	@MethodSource("provideValuesForExistingData")
	public void testGetById_InDatabase(Category expected) {
		assertEquals(new CategoryDTO(expected.getId(), expected.getName()), service.getById(expected.getId()));
	}
	
	private static Stream<Arguments> provideValuesForExistingData() {
		return Stream.of(
			Arguments.of(models[0]),
			Arguments.of(models[1]),
			Arguments.of(models[3]),
			Arguments.of(models[5]),
			Arguments.of(models[6])
		);
	}
	
	private static Stream<Arguments> provideValuesForExistingNames() {
		return Stream.of(
			Arguments.of(modelDTOs[0]), 
			Arguments.of(modelDTOs[1]),
			Arguments.of(modelDTOs[3]),
			Arguments.of(modelDTOs[5]),
			Arguments.of(modelDTOs[6])
		);
	}
}
