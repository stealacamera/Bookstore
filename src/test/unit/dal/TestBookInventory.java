package test.unit.dal;

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

import dal.BookInventoryRepository;
import dal.models.Book;
import dal.models.BookInventory;
import dal.models.utilities.CustomDate;
import exceptions.EmptyInputException;
import exceptions.NonPositiveInputException;
import exceptions.WrongFormatException;
import exceptions.WrongLengthException;

public class TestBookInventory extends TestRepositoryBase {
	private BookInventoryRepository repository;
	private static BookInventory[] models;
	
	@BeforeAll
	static void setUpDummyData() throws EmptyInputException, WrongFormatException, WrongLengthException, NonPositiveInputException {
		models = new BookInventory[7];
		
		for(int i = 0; i < models.length; i++) {
			Book book = new Book("111-1-11-111111-" + i, "foo", "foo bar", "foo", 1);
			models[i] = new BookInventory(book, 1, 1, 1, 1, new CustomDate());
		}
	}
	
	@BeforeEach
	void setUpEach() throws IOException {
		dataFile = new File(dataDir, BookInventory.class.getSimpleName());
		dataFile.createNewFile();
		
		setTempDataToFile(new ArrayList<>(Arrays.asList(models)));
		repository = new BookInventoryRepository(dataDirPath, dbContext);
	}
	
	@Test
	void testGetByISBN_NotInDatabase() {
		assertNull(repository.getByISBN("211-1-11-111111-1"));
	}
	
	@ParameterizedTest
	@MethodSource("provideValuesForExistingData")
	void testGetByISBN_InDatabase(BookInventory model) throws EmptyInputException, WrongFormatException, WrongLengthException, NonPositiveInputException, IOException {
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
