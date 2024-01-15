package Bookstore.Bookstore.unit.dal.models;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.format.DateTimeParseException;
import java.util.stream.Stream;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;

import Bookstore.Bookstore.commons.exceptions.EmptyInputException;
import Bookstore.Bookstore.commons.exceptions.WrongFormatException;
import Bookstore.Bookstore.dal.models.User;
import Bookstore.Bookstore.dal.models.utils.CustomDate;

class TestUser {
	User RandUser;
	@BeforeEach
	void setUp() throws DateTimeParseException, WrongFormatException, EmptyInputException {
		CustomDate a=new CustomDate("02/02/2003");
		RandUser = new User("Krisi02", "Kris Gj", "kgj@gmail.com", "Ukraine321","069 443 3332", a);
	}
	
	@Test
	void UserNameEmptyAndNull() {
		Exception ex= assertThrows(EmptyInputException.class,()->RandUser.setUsername(""));
		Exception ex2= assertThrows(EmptyInputException.class,()->RandUser.setUsername(null));
		assertEquals(ex.getMessage(),ex2.getMessage());
		assertEquals("Input fields cannot be empty: Please enter a value for username",ex.getMessage());
	}
	
	@ParameterizedTest
	@CsvSource({
		"krse",
		"polsaewserrwwedsdeeedffdd21341234124121324124"
	})
	void UserLengthNotValid(String user) {
		Exception ex= assertThrows(WrongFormatException.class,()->RandUser.setUsername(user));
		assertEquals("Incorrect username format: Correct format is 5-25 characters",ex.getMessage());
	}
	@Test
	void IsNotCorrectPassword() throws EmptyInputException {
		assertFalse(RandUser.isCorrectPassword("ukraine321"));
	}
	@Test
	void IsCorrectPassword() throws EmptyInputException {
		assertTrue(RandUser.isCorrectPassword("Ukraine321"));
	}
	@ParameterizedTest
	@MethodSource("provideFullNameInvalidValues")
	void testSetFullName_InvalidValues(String fullName, Class<Exception> errorType, String errorMessage) {
		Exception ex = assertThrows(errorType, ()->RandUser.setFullName(fullName));
		assertTrue(ex.getMessage().contains(errorMessage));
	}
	@ParameterizedTest
	@CsvSource({
		"Kri",
		"KristianGjinopulli12345678"
	})
	void UsernameNotValid(String name) {
		Exception ex= assertThrows(WrongFormatException.class,()->RandUser.setUsername(name));
		assertEquals("Incorrect username format: Correct format is 5-25 characters",ex.getMessage());
	}
	@Test
	void EmailEmptyAndNull() {
		Exception ex= assertThrows(EmptyInputException.class,()->RandUser.setEmail(""));
		Exception ex2= assertThrows(EmptyInputException.class,()->RandUser.setEmail(null));
		assertEquals(ex.getMessage(),ex2.getMessage());
		assertEquals("Input fields cannot be empty: Please enter a value for email",ex.getMessage());
	}
	
	@ParameterizedTest
	@CsvSource({
		"k@gmail.com",
		"kgj@outlook.it",
		"kgj@gmail.alb",		
	})
	void EmailNotValid(String user) {
		Exception ex= assertThrows(WrongFormatException.class,()->RandUser.setEmail(user));
		assertEquals("Incorrect email format: Correct format is (2 characters)@gmail.com",ex.getMessage());
	}
	
	@Test
	void EmptyHashPassword() {
		Exception ex= assertThrows(EmptyInputException.class,()->RandUser.setHashedPassword(""));
		Exception ex2= assertThrows(EmptyInputException.class,()->RandUser.setHashedPassword(null));
		assertEquals(ex.getMessage(),ex2.getMessage());
		assertEquals("Input fields cannot be empty: Please enter a value for hashed password",ex.getMessage());
	}
	@Test
	void EmptyPassword() {
		Exception ex= assertThrows(EmptyInputException.class,()->RandUser.setPassword(""));
		Exception ex2= assertThrows(EmptyInputException.class,()->RandUser.setPassword(null));
		assertEquals(ex.getMessage(),ex2.getMessage());
		assertEquals("Input fields cannot be empty: Please enter a value for password",ex.getMessage());
	}
	@Test
	void NonValidPasswords() {
		Exception ex= assertThrows(WrongFormatException.class,()->RandUser.setPassword("randoms"));
		assertEquals("Incorrect password format: Correct format is at least 8 characters with at least 1 digit and 1 letter",ex.getMessage());
	}
	@Test
	void IsPhoneEmpty() {
		Exception ex= assertThrows(EmptyInputException.class,()->RandUser.setPhoneNum(""));
		Exception ex2= assertThrows(EmptyInputException.class,()->RandUser.setPhoneNum(null));
		assertEquals(ex.getMessage(),ex2.getMessage());
		assertEquals("Input fields cannot be empty: Please enter a value for phone number",ex.getMessage());
	}
	
	@ParameterizedTest
	@CsvSource({
		"037 445 6789",
		"064 445 6778",
		"067 5543 4443",
		"067 554 44343",
		"0674 5544 444333"
	})
	void isPhoneValid(String Phone) {
		Exception ex= assertThrows(WrongFormatException.class,()->RandUser.setPhoneNum(Phone));
		assertEquals("Incorrect phone number format: Correct format is 06X XXX XXXX",ex.getMessage());
		
	}
	
	@Test
	void testEquals_Unequal() throws Exception {
		User user = new User(
			"different_user", "different name", "diffuser@gmail.com", 
			"password132", "068 123 1324", new CustomDate()
		);
		
		assertAll(
			() -> assertFalse(RandUser.equals(new CustomDate())),
			() -> assertFalse(RandUser.equals(user))
		);
	}
	
	@Test
	void testEquals() throws Exception {
		assertTrue(RandUser.equals(new User(RandUser)));
	}
	
	@ParameterizedTest
	@NullSource
	@ValueSource(strings = {""})
	void testSetBirthdate_InvalidValues(String birthdateString) {
		Exception error = assertThrows(EmptyInputException.class, () -> RandUser.setBirthdate(birthdateString));
		assertTrue(error.getMessage().contains("Input fields cannot be empty: Please enter a value for birthdate"));
	}
	
	@Test
	void testSetBirthdate_ValidValues() throws Exception {
		String birthdateString = "12/10/1998";
		
		RandUser.setBirthdate(birthdateString);
		assertEquals(new CustomDate(birthdateString), RandUser.getBirthdate());
	}
	
	private static Stream<Arguments> provideFullNameInvalidValues() {
		return Stream.of(
			Arguments.of(null, EmptyInputException.class, "Input fields cannot be empty: Please enter a value for name"),
			Arguments.of("", EmptyInputException.class, "Input fields cannot be empty: Please enter a value for name"),
			Arguments.of("sample_name", WrongFormatException.class, "Incorrect full name format: Correct format is (first name) (last name)")
		);
	}
}
