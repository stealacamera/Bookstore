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

import Bookstore.Bookstore.dal.models.Book;
import Bookstore.Bookstore.dal.models.BookInventory;
import Bookstore.Bookstore.dal.models.utils.CustomDate;
import Bookstore.Bookstore.dal.repositories.BookInventoryRepository;
import Bookstore.Bookstore.commons.exceptions.EmptyInputException;
import Bookstore.Bookstore.commons.exceptions.NonPositiveInputException;
import Bookstore.Bookstore.commons.exceptions.WrongFormatException;
import Bookstore.Bookstore.commons.exceptions.WrongLengthException;

public class TestBookInventoryRepository extends TestRepositoryBase {
	private BookInventoryRepository repository;
	private static BookInventory[] models;
	
	@BeforeAll
	public static void setUpDummyData() throws EmptyInputException, WrongFormatException, WrongLengthException, NonPositiveInputException {
		models = new BookInventory[7];
		
		for(int i = 0; i < models.length; i++) {
			Book book = new Book("111-1-11-111111-" + i, "foo", "foo bar", "foo", 1);
			models[i] = new BookInventory(book, 1, 1, 1, 1, new CustomDate());
		}
	}
	
	@BeforeEach
	public void setUpEach() throws IOException {
		dataFile = new File(dataDir, BookInventory.class.getSimpleName());
		dataFile.createNewFile();
		
		setTempDataToFile(new ArrayList<>(Arrays.asList(models)));
		repository = new BookInventoryRepository(dataDirPath, dbContext);
	}
	
	@Test
	public void testGetByISBN_NotInDatabase() {
		assertNull(repository.getByISBN("211-1-11-111111-1"));
	}
	
	@ParameterizedTest
	@MethodSource("provideValuesForExistingData")
	public void testGetByISBN_InDatabase(BookInventory model) throws EmptyInputException, WrongFormatException, WrongLengthException, NonPositiveInputException, IOException {
		assertEquals(model, repository.getByISBN(model.getBook().getIsbn()));
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
}
