package Bookstore.Bookstore.unit.dal.models;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

import Bookstore.Bookstore.commons.exceptions.EmptyInputException;
import Bookstore.Bookstore.dal.models.Category;
import Bookstore.Bookstore.dal.models.utils.CustomDate;

class TestCategory {

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

	@Test
	void testEquals_Unequals() throws Exception {
		Category base = new Category("horror");
		
		assertAll(
			() -> assertFalse(base.equals(new CustomDate())),
			() -> assertFalse(base.equals(new Category("romance")))
		);
	}
	
	@Test
	void testEquals() throws Exception {
		Category base = new Category("horro");
		assertTrue(base.equals(new Category(base.getId(), base.getName())));
	}
}
