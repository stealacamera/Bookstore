package test.unit.bll;

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

import bll.BookInventoryService;
import bll.dto.BookDTO;
import bll.dto.BookInventoryDTO;
import dal.models.Book;
import dal.models.BookInventory;
import dal.models.utilities.CustomDate;
import exceptions.EmptyInputException;
import exceptions.ExistingObjectException;
import exceptions.NonPositiveInputException;
import exceptions.WrongFormatException;
import exceptions.WrongLengthException;
import javafx.collections.FXCollections;
import test.unit.bll.mocks.BookInventoryRepositoryMock;

public class TestBookInventoryService {
	private BookInventoryService service;
	private BookInventoryRepositoryMock mockRepository;
	private static BookInventory b1, b2, b3, b4, b5, b6;
	private static BookInventoryDTO bDTO1, bDTO2, bDTO3, bDTO4, bDTO5, bDTO6;
	
	@BeforeAll
	static void setUpDummyData() throws EmptyInputException, WrongFormatException, WrongLengthException, NonPositiveInputException {
		Book[] books = new Book[6];
		
		for(int i = 0; i < books.length; i++)
			books[i] = new Book(i + "11-1-11-111111-1", "foo", "foo bar", "foobar", 1);
		
		b1 = new BookInventory(books[0], 1, 1, 1, 1, new CustomDate());
		b2 = new BookInventory(books[1], 1, 1, 1, 1, new CustomDate());
		b3 = new BookInventory(books[2], 1, 1, 1, 1, new CustomDate());
		b4 = new BookInventory(books[3], 1, 1, 1, 1, new CustomDate());
		b5 = new BookInventory(books[4], 1, 1, 1, 1, new CustomDate());
		b6 = new BookInventory(books[5], 1, 1, 1, 1, new CustomDate());
		
		bDTO1 = createEquivalentDTO(b1);
		bDTO2 = createEquivalentDTO(b2);
		bDTO3 = createEquivalentDTO(b3);
		bDTO4 = createEquivalentDTO(b4);
		bDTO5 = createEquivalentDTO(b5);
		bDTO6 = createEquivalentDTO(b6);
	}
	
	@BeforeEach
	void setUp() {
		mockRepository = new BookInventoryRepositoryMock();
		mockRepository.addDummyData(b1, b2, b3, b4, b5, b6);
		
		service = new BookInventoryService(mockRepository);
	}
	
	@Test
	void testGetAll_Empty() {
		service = new BookInventoryService(new BookInventoryRepositoryMock());
		assertIterableEquals(FXCollections.observableArrayList(), service.getAll());
	}
	
	@Test
	void testGetAll_NonEmpty() {
		assertIterableEquals(Arrays.asList(bDTO1, bDTO2, bDTO3, bDTO4, bDTO5, bDTO6), service.getAll());
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
	@ValueSource(ints = { 0, -1})
	void testUpdateStock_InvalidValues_InvalidStock(int newStock) {		
		Exception exception = assertThrows(NonPositiveInputException.class, () -> service.updateStock(bDTO1, newStock));
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
			Arguments.of(bDTO1, b1),
			Arguments.of(bDTO2, b2),
			Arguments.of(bDTO3, b3),
			Arguments.of(bDTO5, b5),
			Arguments.of(bDTO6, b6)
		);
	}
	
	private static Stream<Arguments> provideValuesForExistingValues() {
		return Stream.of(
			Arguments.of(bDTO1),
			Arguments.of(bDTO2),
			Arguments.of(bDTO3),
			Arguments.of(bDTO5),
			Arguments.of(bDTO6)
		);
	}
	
	private static Stream<Arguments> provideValuesForInvalidValues() {
		return Stream.of(
			Arguments.of(new BookInventoryDTO(new BookDTO(null, null, null, null, 0), 0, 0, 0, 0, null), EmptyInputException.class, "Input fields cannot be empty"),
			Arguments.of(new BookInventoryDTO(new BookDTO("1", "a", "a", "a", 0), 0, 0, 0, 0, LocalDate.now()), WrongFormatException.class, "Correct format is")
		);
	}
}
