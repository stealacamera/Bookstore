package Bookstore.Bookstore.unit.dal.models;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import Bookstore.Bookstore.dal.models.Book;
import Bookstore.Bookstore.dal.models.BookInventory;
import Bookstore.Bookstore.dal.models.utils.CustomDate;
import Bookstore.Bookstore.commons.exceptions.EmptyInputException;
import Bookstore.Bookstore.commons.exceptions.NonPositiveInputException;
import Bookstore.Bookstore.commons.exceptions.WrongFormatException;
import Bookstore.Bookstore.commons.exceptions.WrongLengthException;

class TestBookInventory {
	Book bookdummy;
	BookInventory BInv;
	
	@BeforeEach
	void setup() throws EmptyInputException, WrongFormatException, WrongLengthException, NonPositiveInputException {
		CustomDate a = new CustomDate(LocalDate.now());
		bookdummy=new Book("321-2-34-234567-2","a","b","c",1);
		BInv = new BookInventory(bookdummy, 2, 2, 2, 2, a);
	}

	@Test
	void testInvalidDate() {
		Exception ex = assertThrows(EmptyInputException.class,()->BInv.setPurchasedDate(""));
		assertEquals("Input fields cannot be empty: Please enter a value for purchased date",ex.getMessage());
	}
	@Test
	void TestInvalidPurchase() {
		Exception ex = assertThrows(NonPositiveInputException.class,()->BInv.setPurchasedPrice(-1));
		assertEquals("Incorrect purchased price: Please enter a positive number for the field",ex.getMessage());
	}
	@Test
	void TestInvalidOGPrice() {
		Exception ex = assertThrows(NonPositiveInputException.class,()->BInv.setOriginalPrice(-1));
		assertEquals("Incorrect original price: Please enter a positive number for the field",ex.getMessage());
		
	}
	
	@Test
	void TestInvalidSellPrice(){
		Exception ex = assertThrows(NonPositiveInputException.class,()->BInv.setSellingPrice(-1));
		assertEquals("Incorrect selling price: Please enter a positive number for the field",ex.getMessage());
	}
	@Test
	void TestInvalidStock() {
		Exception ex = assertThrows(NonPositiveInputException.class,()->BInv.setStock(-1));
		assertEquals("Incorrect stock: Please enter a positive number for the field",ex.getMessage());
	}
}
