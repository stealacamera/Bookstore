package Bookstore.Bookstore.unit.dal.models;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import Bookstore.Bookstore.commons.exceptions.EmptyInputException;
import Bookstore.Bookstore.commons.exceptions.NonPositiveInputException;
import Bookstore.Bookstore.dal.models.BookPurchase;
import Bookstore.Bookstore.dal.models.utils.CustomDate;

class TestBookPurchase {
	BookPurchase dummyBookPurchase;
	@BeforeEach
	void setUP() throws NonPositiveInputException {
		dummyBookPurchase= new BookPurchase(3);
	}
	@Test
	void testAmountIsIllegal() {
		Exception exc= assertThrows(NonPositiveInputException.class,()->dummyBookPurchase.setAmount(-1));
		assertEquals("Incorrect sale amount: Please enter a positive number for the field",exc.getMessage());
	}
	
	@ParameterizedTest
	@ValueSource(doubles = { 1, 2.4, 55, 300.07, 759.01, 800})
	void testSetAmount_ValidValues(double amount) throws Exception {
		dummyBookPurchase.setAmount(amount);
		assertEquals(amount, dummyBookPurchase.getAmount());
	}
	
	@Test
	void testSetDate_InvalidValues() {
		Exception error = assertThrows(EmptyInputException.class, () -> dummyBookPurchase.setDate(null));
		assertTrue(error.getMessage().contains("Input fields cannot be empty: Please enter a value for date"));
	}
	
	@Test
	void testSetDate_ValidValues() throws Exception {
		CustomDate date = new CustomDate("12/12/1990");
		dummyBookPurchase.setDate(date);
		
		assertEquals(date, dummyBookPurchase.getDate());
	}
	
	@Test
	void testEquals_InvalidValues() {
		assertAll(
			() -> assertFalse(dummyBookPurchase.equals(new CustomDate())),
			() -> assertFalse(dummyBookPurchase.equals(new BookPurchase(12.234, new CustomDate("12/12/2000"))))
		);
	}
	
	@Test
	void testEquals_ValidValues() throws Exception {
		assertTrue(dummyBookPurchase.equals(new BookPurchase(dummyBookPurchase.getAmount(), dummyBookPurchase.getDate())));
	}
}
