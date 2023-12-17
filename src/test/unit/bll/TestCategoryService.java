package test.unit.bll;

import static org.junit.Assert.assertFalse;
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

import bll.CategoryService;
import bll.dto.CategoryDTO;
import dal.models.Category;
import exceptions.EmptyInputException;
import exceptions.ExistingObjectException;
import exceptions.IncorrectPermissionsException;
import exceptions.NonPositiveInputException;
import exceptions.WrongFormatException;
import exceptions.WrongLengthException;
import javafx.collections.FXCollections;
import test.unit.bll.mocks.CategoryRepositoryMock;

public class TestCategoryService {
	private static CategoryService service;
	private static CategoryRepositoryMock mockRepository;
	private static Category c1, c2, c3, c4, c5, c6;
		
	@BeforeAll
	static void setUpDummyData() throws EmptyInputException {
		c1 = new Category("foo");
		c2 = new Category("bar");
		c3 = new Category("lorem");
		c4 = new Category("ipsum");
		c5 = new Category("dolorem");
		c6 = new Category("maenecas");
	}
	
	@BeforeEach
	void setUp() {
		mockRepository = new CategoryRepositoryMock();
		mockRepository.addDummyData(c1, c2, c3, c4, c5, c6);
		
		service = new CategoryService(mockRepository);
	}
	
	@Test
	void testGetAll_Empty() {
		service = new CategoryService(new CategoryRepositoryMock());
		assertIterableEquals(FXCollections.observableArrayList(), service.getAll());
	}
	
	@Test
	void testGetAll_NonEmpty() {
		assertIterableEquals(Arrays.asList(c1, c2, c3, c4, c5, c6), service.getAll());
	}
	
	@ParameterizedTest
	@NullSource
	@ValueSource(strings = {""})
	void testAdd_InvalidValues(String input) {
		Exception exception = assertThrows(EmptyInputException.class, () -> service.add(new CategoryDTO(0, input)));
		assertTrue(exception.getMessage().contains("Input fields cannot be empty: Please enter a value for name"));
	}
	
	@ParameterizedTest
	@MethodSource("provideValuesForExistingNames")
	void testAdd_ExistingName(String input) {
		Exception exception = assertThrows(ExistingObjectException.class, () -> service.add(new CategoryDTO(0, input)));
		assertTrue(exception.getMessage().contains("Element with these details already exists"));
	}
	
	@Test
	void testAdd_NullValue() throws ExistingObjectException, EmptyInputException, NonPositiveInputException, WrongFormatException, WrongLengthException, IncorrectPermissionsException {
		assertFalse(service.add(null));
	}
	
	@Test
	void testAdd_NonExistingName() throws ExistingObjectException, EmptyInputException, NonPositiveInputException, WrongFormatException, WrongLengthException, IncorrectPermissionsException {
		String input = "sample";
		
		assertTrue(service.add(new CategoryDTO(0, input)));
		assertEquals(new Category(mockRepository.count(), input), mockRepository.getByName(input));
	}
	
	@Test
	void testGetById_NotInDatabase() {
		assertNull(service.getById(0));
		assertNull(service.getById(c6.getId() + 1));
	}
	
	@ParameterizedTest
	@MethodSource("provideValuesForExistingData")
	void testGetById_InDatabase(Category expected) {
		assertEquals(new CategoryDTO(expected.getId(), expected.getName()), service.getById(expected.getId()));
	}
	
	private static Stream<Arguments> provideValuesForExistingData() {
		return Stream.of(
			Arguments.of(c1),
			Arguments.of(c2),
			Arguments.of(c3),
			Arguments.of(c5),
			Arguments.of(c6)
		);
	}
	
	private static Stream<Arguments> provideValuesForExistingNames() {
		return Stream.of(
			Arguments.of(c1.getName()), 
			Arguments.of(c2.getName()),
			Arguments.of(c3.getName()),
			Arguments.of(c5.getName()),
			Arguments.of(c6.getName())
		);
	}
}
