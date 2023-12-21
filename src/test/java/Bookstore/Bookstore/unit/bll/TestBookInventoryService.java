package Bookstore.Bookstore.unit.bll;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.stream.Stream;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

import Bookstore.Bookstore.bll.dto.BookDTO;
import Bookstore.Bookstore.bll.dto.BookInventoryDTO;
import Bookstore.Bookstore.bll.services.BookInventoryService;
import Bookstore.Bookstore.dal.models.Book;
import Bookstore.Bookstore.dal.models.BookInventory;
import Bookstore.Bookstore.dal.models.utils.CustomDate;
import Bookstore.Bookstore.commons.exceptions.EmptyInputException;
import Bookstore.Bookstore.commons.exceptions.ExistingObjectException;
import Bookstore.Bookstore.commons.exceptions.NonPositiveInputException;
import Bookstore.Bookstore.commons.exceptions.WrongFormatException;
import Bookstore.Bookstore.commons.exceptions.WrongLengthException;
import javafx.collections.FXCollections;
import Bookstore.Bookstore.unit.bll.mocks.BookInventoryRepositoryMock;

public class TestBookInventoryService {
	private BookInventoryService service;
	private BookInventoryRepositoryMock mockRepository;
	private static BookInventory[] models;
	private static BookInventoryDTO[] modelDTOs; 
	
	@BeforeAll
	static void setUpDummyData() throws EmptyInputException, WrongFormatException, WrongLengthException, NonPositiveInputException {
		models = new BookInventory[7];
		modelDTOs = new BookInventoryDTO[models.length];
		
		for(int i = 0; i < models.length; i++) {
			Book book = new Book(i + "11-1-11-111111-1", "foo", "foo bar", "foobar", 1);
			models[i] = new BookInventory(book, 1, 1, 1, 1, new CustomDate());
			modelDTOs[i] = createEquivalentDTO(models[i]);
		}
	}
	
	@BeforeEach
	void setUp() {
		mockRepository = new BookInventoryRepositoryMock();
		mockRepository.addDummyData(models);
		
		service = new BookInventoryService(mockRepository);
	}
	
	@Test
	void testGetAll_Empty() {
		service = new BookInventoryService(new BookInventoryRepositoryMock());
		assertIterableEquals(FXCollections.observableArrayList(), service.getAll());
	}
	
	@Test
	void testGetAll_NonEmpty() {
		assertIterableEquals(Arrays.asList(modelDTOs), service.getAll());
	}
	
	@Test
	void testAdd_NullValue() throws ExistingObjectException, EmptyInputException, WrongFormatException, WrongLengthException, NonPositiveInputException {
		assertFalse(service.add(null));
	}
	
	@ParameterizedTest
	@MethodSource("provideValuesForInvalidValues")
	void testAdd_InvalidValues(BookInventoryDTO model, Class<Exception> exceptionType, String expectedError) {
		Exception exception = assertThrows(exceptionType, () -> service.add(model));
		assertTrue(exception.getMessage().contains(expectedError));
	}
	
	@ParameterizedTest
	@MethodSource("provideValuesForExistingValues")
	void testAdd_ExistingISBN(BookInventoryDTO model) {		
		Exception exception = assertThrows(ExistingObjectException.class, () -> service.add(model));
		assertTrue(exception.getMessage().contains("with these details (ISBN) already exists"));
	}
	
	@ParameterizedTest
	@ValueSource(strings = { "888-8-88-888888-8", "999-9-99-999999-9" })
	void testAdd_NonExistingISBN(String isbn) throws NonPositiveInputException, EmptyInputException, WrongFormatException, WrongLengthException, ExistingObjectException {
		BookInventory model = new BookInventory(
				new Book(isbn, "foo bar", "foobar", "foo", 1), 
				1, 1, 1, 1, new CustomDate());
		
		assertTrue(service.add(createEquivalentDTO(model)));
		assertEquals(model, mockRepository.get(mockRepository.count() - 1));
	}
	
	@ParameterizedTest
	@ValueSource(ints = { 0, -1 })
	void testUpdateStock_InvalidValues_InvalidStock(int newStock) {		
		Exception exception = assertThrows(NonPositiveInputException.class, () -> service.updateStock(modelDTOs[0], newStock));
		assertTrue(exception.getMessage().contains("Please enter a positive number for the field"));
	}
	
	@ParameterizedTest
	@ValueSource(strings = { "888-8-88-888888-8", "999-9-99-999999-9" })
	void testUpdateStock_InvalidValues_NonExistingISBN(String isbn) {
		BookInventoryDTO model = new BookInventoryDTO(
				new BookDTO(isbn, "foo bar", "foobar", "foo", 1), 
				1, 1, 1, 1, LocalDate.now());
		
		Exception exception = assertThrows(EmptyInputException.class, () -> service.updateStock(model, 1));
		assertTrue(exception.getMessage().contains("Input fields cannot be empty"));
	}
	
	@ParameterizedTest
	@MethodSource("provideValuesForUpdateStock")
	void testUpdateStock_ValidValues(BookInventoryDTO model, BookInventory base) throws EmptyInputException, WrongFormatException, WrongLengthException, NonPositiveInputException {		
		int newStock = 12;
		service.updateStock(model, newStock);
		assertEquals(newStock, base.getStock());
	}
	
	private static BookInventoryDTO createEquivalentDTO(BookInventory model) {
		return new BookInventoryDTO(
				new BookDTO(model.getBook().getIsbn(), model.getBook().getTitle(), 
						model.getBook().getAuthor(), model.getBook().getSupplier(), model.getBook().getCategoryId()), 
				model.getPurchasedPrice(), model.getOriginalPrice(), 
				model.getSellingPrice(), model.getStock(), model.getPurchasedDate().getDate());
	}
	
	private static Stream<Arguments> provideValuesForUpdateStock() {
		return Stream.of(
			Arguments.of(modelDTOs[0], models[0]),
			Arguments.of(modelDTOs[1], models[1]),
			Arguments.of(modelDTOs[3], models[3]),
			Arguments.of(modelDTOs[5], models[5]),
			Arguments.of(modelDTOs[6], models[6])
		);
	}
	
	private static Stream<Arguments> provideValuesForExistingValues() {
		return Stream.of(
			Arguments.of(modelDTOs[0]),
			Arguments.of(modelDTOs[1]),
			Arguments.of(modelDTOs[3]),
			Arguments.of(modelDTOs[5]),
			Arguments.of(modelDTOs[6])
		);
	}
	
	private static Stream<Arguments> provideValuesForInvalidValues() {
		return Stream.of(
			Arguments.of(new BookInventoryDTO(new BookDTO(null, null, null, null, 0), 0, 0, 0, 0, null), EmptyInputException.class, "Input fields cannot be empty"),
			Arguments.of(new BookInventoryDTO(new BookDTO("1", "a", "a", "a", 0), 0, 0, 0, 0, LocalDate.now()), WrongFormatException.class, "Correct format is")
		);
	}
}
