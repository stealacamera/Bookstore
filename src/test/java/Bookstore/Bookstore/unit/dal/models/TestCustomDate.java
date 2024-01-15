package Bookstore.Bookstore.unit.dal.models;

import static org.junit.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

import java.time.LocalDate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import Bookstore.Bookstore.dal.models.utils.CustomDate;
import Bookstore.Bookstore.commons.exceptions.EmptyInputException;
import Bookstore.Bookstore.commons.exceptions.WrongFormatException;

class TestCustomDate {
	CustomDate dummyDate;
	@BeforeEach
	void setUp() {
		dummyDate= new CustomDate();
	}
	@ParameterizedTest
	@CsvSource({
		"024/04/2003",
		"02/064/2003",
		"03/07/20003",
	})
	void SetInvalidDate(String date) {
		Exception exc = assertThrows(WrongFormatException.class,()->dummyDate.setDate(date));
		assertEquals("Incorrect date format: Correct format is DD/MM/YYYY (eg.: 21/03/1998)",exc.getMessage());
	}
	
	@Test
	void SetNullDate(){
		LocalDate a = null;
		Exception exc = assertThrows(EmptyInputException.class,()->dummyDate.setDate(a));
		assertEquals("Input fields cannot be empty: Please enter a value for date",exc.getMessage());
	}
	
	@Test
	void TestEndDateIsBeforeAfterDate() {
		LocalDate a = LocalDate.now();
		LocalDate b = LocalDate.of(1999, 02, 04);
		LocalDate c = LocalDate.of(2023, 12, 25);
		assertFalse(CustomDate.isInDate(a, b, c));
	}
	
	@Test
	void TestSearchDateIsBeforeAfterDate() {
		LocalDate a = LocalDate.now();
		LocalDate b = LocalDate.of(2024, 02, 04);
		LocalDate c = LocalDate.of(2023, 12, 1);
		assertFalse(CustomDate.isInDate(a, b, c));
	}
	
	@Test
	void TestSearchDateIsAfterEndDate() {
		LocalDate a = LocalDate.now();
		LocalDate b = LocalDate.of(2024, 02, 04);
		LocalDate c = LocalDate.of(2025, 12, 1);
		assertFalse(CustomDate.isInDate(a, b, c));
	}
	
	@Test
	void TestIsInDateWorksCorrectly() {
		LocalDate a = LocalDate.now();
		LocalDate b = LocalDate.of(2024, 02, 04);
		LocalDate c = LocalDate.of(2023, 12, 31);
		assertFalse(CustomDate.isInDate(a, b, c));
	}
}
