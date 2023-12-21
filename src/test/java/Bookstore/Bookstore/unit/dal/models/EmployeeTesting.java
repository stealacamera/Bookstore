package Bookstore.Bookstore.unit.dal.models;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.format.DateTimeParseException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import Bookstore.Bookstore.dal.models.Employee;
import Bookstore.Bookstore.dal.models.User;
import Bookstore.Bookstore.dal.models.utils.CustomDate;
import Bookstore.Bookstore.commons.exceptions.EmptyInputException;
import Bookstore.Bookstore.commons.exceptions.NonPositiveInputException;
import Bookstore.Bookstore.commons.exceptions.WrongFormatException;

class EmployeeTesting {
	User BaseDummy;
	Employee EDummy;
	@BeforeEach
	void startup() throws DateTimeParseException, WrongFormatException, EmptyInputException, NonPositiveInputException{
		CustomDate a=new CustomDate("02/02/2003");
		BaseDummy= new User("Krisi02", "Kris Gj", "kgj@gmail.com", "Ukraine321","069 443 3332", a);
		EDummy = new Employee(BaseDummy,2,2);
	}

	@Test
	void TestNegativeSalary() {
		Exception ex = assertThrows(NonPositiveInputException.class,()->EDummy.setSalary(-3));
		assertEquals("Incorrect salary: Please enter a positive number for the field", ex.getMessage());
	}
	
	@ParameterizedTest
	@CsvSource({
		"0",
		"4"
	})
	void TestNonValidRole(int val) {
		Exception ex = assertThrows(WrongFormatException.class,()->EDummy.setAccessLvl(val));
		assertEquals("Incorrect access level format: Correct format is 1 - Librarian; 2 - Manager; 3 - Admin", ex.getMessage());
	}
	
	@ParameterizedTest
	@CsvSource({
		"1",
		"2",
		"3"
	})
	void TestIfToStringWorksCorrectlyForEachRole(int value) throws WrongFormatException {
		EDummy.setAccessLvl(value);
		if(value==1)
		    assertEquals("Krisi02 (Kris Gj) [Librarian]",EDummy.toString());
		else if(value==2)
		    assertEquals("Krisi02 (Kris Gj) [Manager]",EDummy.toString());
		else
		    assertEquals("Krisi02 (Kris Gj) [Administrator]",EDummy.toString());
		
	}

}
