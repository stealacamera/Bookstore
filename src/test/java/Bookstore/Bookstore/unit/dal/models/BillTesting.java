package Bookstore.Bookstore.unit.dal.models;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import Bookstore.Bookstore.dal.models.Bill;
import Bookstore.Bookstore.commons.exceptions.NonPositiveInputException;

class BillTesting {
	
	@ParameterizedTest
	@CsvSource({
		"-1,3,4",
		"2,-3,6",
		"4,5,-6"
	})
	void TestNegativeValuesForBill(int id , int amount, int books) throws NonPositiveInputException {
		Exception exception = assertThrows(NonPositiveInputException.class,()->new Bill(id,amount,books));
		if (id<0) {
			assertEquals("Incorrect seller id: Please enter a positive number for the field",exception.getMessage());
		}
		else if (amount<0){
			assertEquals("Incorrect sale amount: Please enter a positive number for the field",exception.getMessage());
		}
		else{
			assertEquals("Incorrect nr. of books: Please enter a positive number for the field",exception.getMessage());
		}
	}
	@Test
	void TestEverythingFine() throws NonPositiveInputException {
		Bill a = new Bill(3,4,5);
		assertTrue(a.getSellerId()==3 && a.getSaleAmount()==4 && a.getNumOfBooks()==5);
	}

}
