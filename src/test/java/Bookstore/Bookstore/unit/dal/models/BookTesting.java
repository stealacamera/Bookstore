package Bookstore.Bookstore.unit.dal.models;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import Bookstore.Bookstore.dal.models.Bill;
import Bookstore.Bookstore.dal.models.Book;
import Bookstore.Bookstore.commons.exceptions.EmptyInputException;
import Bookstore.Bookstore.commons.exceptions.NonPositiveInputException;
import Bookstore.Bookstore.commons.exceptions.WrongFormatException;
import Bookstore.Bookstore.commons.exceptions.WrongLengthException;

class BookTesting {
	Book BookDummy;
	
	@BeforeEach
	void setUp() throws EmptyInputException, WrongFormatException, WrongLengthException, NonPositiveInputException {
		BookDummy = new Book("321-2-34-234567-2","a","b","c",1);
	}

	@ParameterizedTest
	@CsvSource({
		"23-1-32-123456-3",
		"234- -32-123456-3",
		"234-1-3-123456-4",
		"234-1-45-1234567-4",
		"123-4-54-564234-56",
		"-----------",
	})
	@Test
	void BookTestingInvalidISDM(String ISDM) {
		Exception exc = assertThrows(WrongFormatException.class,()->BookDummy.setIsbn(ISDM));
		assertEquals("Incorrect ISBN format: Correct format is (3 digits)-(1 digit)-(2 digits)-(6 digits)-(1 digit)",exc.getMessage());
	}
	@Test
	void BookTestingEmptyISDM() {
		Exception exc = assertThrows(EmptyInputException.class,()->BookDummy.setIsbn(""));
		assertEquals("Input fields cannot be empty: Please enter a value for ISBN" ,exc.getMessage());
	}
	
	@Test
	void BookTestingEmptyTitle() {
		Exception exc = assertThrows(EmptyInputException.class,()->BookDummy.setTitle(""));
		assertEquals("Input fields cannot be empty: Please enter a value for title",exc.getMessage());
	}
	
	@Test
	void BookTestingInvalidLenght() {
		Exception exc = assertThrows(WrongLengthException.class,()->BookDummy.setTitle("The Awsome Book of Awsome Adventures Or maybe not cos who really knows"));
		assertEquals("Incorrect length: title should be 30 characters or less",exc.getMessage());
	}
	
	@Test
	void BookTestingAuthorEmpty() {
		Exception exc = assertThrows(EmptyInputException.class,()->BookDummy.setAuthor(""));
		assertEquals("Input fields cannot be empty: Please enter a value for author",exc.getMessage());
	}
	
	@Test
	void BookTestingAuthorInvalidNameLength() {
		Exception exc= assertThrows(WrongLengthException.class,()->BookDummy.setAuthor("AVeryLongNameInOrderTo FitTheCharLimitSetInTheBookModelSoItsVeryLong"));
		assertEquals("Incorrect length: author should be 45 characters or less",exc.getMessage());
	}
	
	@ParameterizedTest
	@CsvSource({
		"Ar4on L34nd",
		"Paul Lon3don",
		"J4ck Frost"
	})
	void BookTestingAuthorInvalidFormat(String Author) {
		Exception exc= assertThrows(WrongFormatException.class,()->BookDummy.setAuthor(Author));
		assertEquals("Incorrect author name format: Correct format is space-separated words, no numbers",exc.getMessage());
	}
	
	@Test
	void BookTestingSupplierEmpty() {
		Exception exc = assertThrows(EmptyInputException.class,()->BookDummy.setSupplier(""));
		assertEquals("Input fields cannot be empty: Please enter a value for supplier",exc.getMessage());
	}
	
	@Test
	void BookTestingSupplierInvalidLength(){
		Exception exc = assertThrows(WrongLengthException.class,()->BookDummy.setSupplier("A really REALLY long name in order to meet the extreme requirment we have set as 50 for the string length in order to test this is very very very very wrong"));
		assertEquals("Incorrect length: supplier should be 50 characters or less",exc.getMessage());
	}
	
	@Test
	void BookTestingIDNotPoistive(){
		Exception exc= assertThrows(NonPositiveInputException.class,()->BookDummy.setCategoryId(-2));
		assertEquals("Incorrect category id: Please enter a positive number for the field",exc.getMessage());
	}
	@Test
	void TestingWhenNotEqual() throws EmptyInputException, WrongFormatException, WrongLengthException, NonPositiveInputException{
		assertFalse(BookDummy.equals(new Book("321-2-34-134967-2","a","b","c",1)));
	}
	
	@Test
	void TestingWhenNotSame() throws NonPositiveInputException {
		assertFalse(BookDummy.equals(new Bill(1,3,4)));
	}
	
	@Test
	void TestingWhenTrue() throws EmptyInputException, WrongFormatException, WrongLengthException, NonPositiveInputException{
		assertTrue(BookDummy.equals(new Book("321-2-34-234567-2","a","b","c",1)));
	}
	
	
	
	
}
