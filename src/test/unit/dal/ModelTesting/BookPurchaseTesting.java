package test;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import dal.models.BookPurchase;
import exceptions.NonPositiveInputException;

class BookPurchaseTesting {
		BookPurchase DummyBookPurchase;
		@BeforeEach
		void setUP() throws NonPositiveInputException {
			DummyBookPurchase= new BookPurchase(3);
		}
		@Test
		void testAmountIsIllegal() {
			Exception exc= assertThrows(NonPositiveInputException.class,()->DummyBookPurchase.setAmount(-1));
			assertEquals("Incorrect sale amount: Please enter a positive number for the field",exc.getMessage());
		}

}
