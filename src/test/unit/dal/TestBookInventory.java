package test.unit.dal;

import static org.junit.Assert.assertNull;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

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
	
	@BeforeEach
	void setUpEach() throws IOException {
		dataFile = new File(dataDir, BookInventory.class.getSimpleName());
		dataFile.createNewFile();
	}
	
	@Test
	void testGetByISBN_NotInDatabase() {
		repository = new BookInventoryRepository(dataDirPath, dbContext);
		assertNull(repository.getByISBN("111-1-11-111111-1"));
	}
	
	@Test
	void testGetByISBN_InDatabase() throws EmptyInputException, WrongFormatException, WrongLengthException, NonPositiveInputException, IOException {
		BookInventory[] models = createTempData();
		setTempDataToFile(new ArrayList<>(Arrays.asList(models)));
		repository = new BookInventoryRepository(dataDirPath, dbContext);
		
		for(int i = 0; i < models.length; i++) {
			if(i == 2 || i == 4) continue;
			assertEquals(models[i], repository.getByISBN(models[i].getBook().getIsbn()));
		}
	}
	
	private BookInventory[] createTempData() throws EmptyInputException, WrongFormatException, WrongLengthException, NonPositiveInputException {
		BookInventory[] models = new BookInventory[7];
		
		for(int i = 0; i < models.length; i++) {
			Book book = new Book("111-1-11-111111-" + i, "foo", "foo bar", "foo", 1);
			models[i] = new BookInventory(book, 1, 1, 1, 1, new CustomDate());
		}
		
		return models;
	}
}
