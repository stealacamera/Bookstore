package Bookstore.Bookstore.unit.dal.models;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.LocalDate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import Bookstore.Bookstore.commons.exceptions.EmptyInputException;
import Bookstore.Bookstore.commons.exceptions.NonPositiveInputException;
import Bookstore.Bookstore.commons.exceptions.WrongFormatException;
import Bookstore.Bookstore.commons.exceptions.WrongLengthException;
import Bookstore.Bookstore.dal.models.Book;
import Bookstore.Bookstore.dal.models.BookInventory;
import Bookstore.Bookstore.dal.models.utils.CustomDate;

class TestBookInventory {
	Book bookdummy;
	BookInventory bInventory;
	
	@BeforeEach
	void setup() throws EmptyInputException, WrongFormatException, WrongLengthException, NonPositiveInputException {
		CustomDate a = new CustomDate(LocalDate.now());
		bookdummy=new Book("321-2-34-234567-2","a","b","c",1);
		bInventory = new BookInventory(bookdummy, 2, 2, 2, 2, a);
	}

	@Test
	void testInvalidDate() {
		Exception ex = assertThrows(EmptyInputException.class,()->bInventory.setPurchasedDate(""));
		assertEquals("Input fields cannot be empty: Please enter a value for purchased date",ex.getMessage());
	}
	@Test
	void testSetPurchasedDate_ValidValues() throws Exception {
		String date = "12/11/1990";
		
		bInventory.setPurchasedDate(date);
		assertEquals(new CustomDate(date), bInventory.getPurchasedDate());
	}
	@Test
	void TestInvalidPurchase() {
		Exception ex = assertThrows(NonPositiveInputException.class,()->bInventory.setPurchasedPrice(-1));
		assertEquals("Incorrect purchased price: Please enter a positive number for the field",ex.getMessage());
	}
	@Test
	void TestInvalidOGPrice() {
		Exception ex = assertThrows(NonPositiveInputException.class,()->bInventory.setOriginalPrice(-1));
		assertEquals("Incorrect original price: Please enter a positive number for the field",ex.getMessage());
		
	}
	
	@Test
	void TestInvalidSellPrice(){
		Exception ex = assertThrows(NonPositiveInputException.class,()->bInventory.setSellingPrice(-1));
		assertEquals("Incorrect selling price: Please enter a positive number for the field",ex.getMessage());
	}
	@Test
	void TestInvalidStock() {
		Exception ex = assertThrows(NonPositiveInputException.class,()->bInventory.setStock(-1));
		assertEquals("Incorrect stock: Please enter a positive number for the field",ex.getMessage());
	}
	
	@Test
	void testEquals_Unequals() throws Exception {
		BookInventory sample = new BookInventory(
			new Book("111-1-11-123123-1", "foo", "foo bar", "foobar", 1), 
			12, 14, 122, 2, new CustomDate()
		);
		
		assertAll(
			() -> assertFalse(bInventory.equals(new CustomDate())),
			() -> assertFalse(bInventory.equals(sample))
		);
	}
	
	@Test
	void testEquals() throws Exception {
		assertTrue(bInventory.equals(new BookInventory(bookdummy, bInventory.getPurchasedPrice(), bInventory.getOriginalPrice(), bInventory.getSellingPrice(), bInventory.getStock(), bInventory.getPurchasedDate())));
	}
}
