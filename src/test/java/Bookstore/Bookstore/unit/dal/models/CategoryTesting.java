package Bookstore.Bookstore.unit.dal.models;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import Bookstore.Bookstore.dal.models.Category;
import Bookstore.Bookstore.commons.exceptions.EmptyInputException;

class CategoryTesting {

	@Test
	void TestNameIsntEMPTY() {
		Exception ex = assertThrows(EmptyInputException.class,()->new Category(123445,""));
		assertEquals("Input fields cannot be empty: Please enter a value for name",ex.getMessage());
	}
	@Test
	void EverythingISCorrect() throws EmptyInputException {
		Category a = new Category(123445,"Jeremy");
		assertTrue(a.getName()=="Jeremy" && a.getId()==123445);
	}

}
