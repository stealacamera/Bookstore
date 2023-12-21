package Bookstore.Bookstore.unit.bll;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.stream.Stream;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

import Bookstore.Bookstore.bll.dto.EmployeeDTO;
import Bookstore.Bookstore.bll.dto.UserDTO;
import Bookstore.Bookstore.bll.services.EmployeeService;
import Bookstore.Bookstore.dal.models.Employee;
import Bookstore.Bookstore.dal.models.User;
import Bookstore.Bookstore.dal.models.utils.CustomDate;
import Bookstore.Bookstore.commons.exceptions.EmptyInputException;
import Bookstore.Bookstore.commons.exceptions.ExistingObjectException;
import Bookstore.Bookstore.commons.exceptions.IncorrectPermissionsException;
import Bookstore.Bookstore.commons.exceptions.NonPositiveInputException;
import Bookstore.Bookstore.commons.exceptions.WrongFormatException;
import Bookstore.Bookstore.commons.exceptions.WrongLengthException;
import Bookstore.Bookstore.unit.bll.mocks.EmployeeRepositoryMock;

public class TestEmployeeService {
	private static Employee[] models;
	private static EmployeeDTO[] modelDTOs;
	private static EmployeeRepositoryMock mockRepository;
	private EmployeeService service;
		
	@BeforeAll
	static void setUpDummyData() throws EmptyInputException, WrongFormatException, NonPositiveInputException {
		models = new Employee[7];
		modelDTOs = new EmployeeDTO[models.length];
		
		for(int i = 0; i < models.length; i++) {
			User user = new User((((char) (97 + i)) + "").repeat(5), "foo bar", i + "foo@gmail.com", "Password123", "069 123 1233", new CustomDate());
			models[i] = new Employee(user, 1, 1);
			
			UserDTO u = new UserDTO(
					user.getId(), user.getUsername(), user.getFullName(), 
					user.getEmail(), user.getHashedPassword(), user.getPhoneNum(), 
					user.getBirthdate().getDate());
			
			modelDTOs[i] = new EmployeeDTO(u, models[i].getSalary(), models[i].getAccessLvl(), models[i].getPermissions());
			modelDTOs[i].setPassword("Password123");
		}
	}
	
	@BeforeEach
	void setuUp() {
		mockRepository = new EmployeeRepositoryMock();
		mockRepository.addDummyData(models);
		
		service = new EmployeeService(mockRepository);
	}
	
	@Test
	void testGetAll_Empty() {
		service = new EmployeeService(new EmployeeRepositoryMock());
		assertEquals(0, service.getAll().size());
	}
	
	@Test
	void testGetAll_NonEmpty() {
		assertIterableEquals(Arrays.asList(modelDTOs), service.getAll());
	}
	
	@ParameterizedTest
	@MethodSource("provideValuesForGet")
	void testGet(EmployeeDTO expected, int index) {
		assertEquals(expected, service.get(index));
	}
	
	@Test
	void testGetById_NotInDatabase() {
		assertNull(service.getById(0));
		assertNull(service.getById(models[models.length - 1].getId() + 1));
	}
	
	@ParameterizedTest
	@MethodSource("provideValuesForExistingValues")
	void testGetById_InDatabase(EmployeeDTO expected) {
		assertEquals(expected, service.getById(expected.getId()));
	}
	
	@ParameterizedTest
	@ValueSource(strings = {"anonymous", "fakeSample"})
	void testGetByUsername_NotInDatabase(String username) {
		assertNull(service.getByUsername(username));
	}
	
	@ParameterizedTest
	@MethodSource("provideValuesForGetByUsername")
	void testGetByUsername_InDatabase(EmployeeDTO expected, String input) {
		assertEquals(expected, service.getByUsername(input));
	}
	
	@Test
	void testAdd_NullValue() throws EmptyInputException, NonPositiveInputException, WrongFormatException, WrongLengthException, ExistingObjectException, IncorrectPermissionsException {
		assertFalse(service.add(null));
	}
	
	@ParameterizedTest
	@MethodSource("provideValuesForInvalidValues")
	void testAdd_InvalidValues(EmployeeDTO model, Class<Exception> exceptionClass, String expectedError) throws EmptyInputException, NonPositiveInputException, WrongFormatException, WrongLengthException, ExistingObjectException, IncorrectPermissionsException {
		Exception exception = assertThrows(exceptionClass, () -> service.add(model));
		assertTrue(exception.getMessage().contains(expectedError));
		
	}
	
	@ParameterizedTest
	@MethodSource("provideValuesForExistingValues")
	void testAdd_ExistingUsername(EmployeeDTO model) {
		Exception exception = assertThrows(ExistingObjectException.class, () -> service.add(model));
		assertTrue(exception.getMessage().contains("Element with these details (username) already exists"));
	}
	
	@ParameterizedTest
	@MethodSource("provideValuesForNonExistingUsername")
	void testAdd_NonExistingUsername(String username, int indexOfInsert) throws EmptyInputException, NonPositiveInputException, WrongFormatException, WrongLengthException, ExistingObjectException, IncorrectPermissionsException {
		EmployeeDTO model = new EmployeeDTO(modelDTOs[0]);
		model.setUsername(username);
		
		assertTrue(service.add(model));
		assertEquals(model, service.get(indexOfInsert));		
	}
	
	@ParameterizedTest
	@MethodSource("provideValuesForInvalidValues")
	void testUpdate_InvalidValues(EmployeeDTO model, Class<Exception> exceptionClass, String expectedError) {
		Exception exception = assertThrows(exceptionClass, () -> service.update(model, model));
		assertTrue(exception.getMessage().contains(expectedError));
	}
	
	@ParameterizedTest
	@MethodSource("provideValuesForChangeToExistingUsername")
	void testUpdate_ChangeToExistingUsername(EmployeeDTO model, String newUsername) {
		EmployeeDTO changedModel = new EmployeeDTO(model);
		changedModel.setUsername(newUsername);
		
		Exception exception = assertThrows(ExistingObjectException.class, () -> service.update(model, changedModel));
		assertTrue(exception.getMessage().contains("Element with these details (username) already exists"));
	}
	
	@ParameterizedTest
	@MethodSource("provideValuesForExistingValues")
	void testUpdate_ChangeToNonExistingUsername(EmployeeDTO model) throws ExistingObjectException, IncorrectPermissionsException, EmptyInputException, NonPositiveInputException, WrongFormatException, WrongLengthException {
		double newSalary = 25000;
		EmployeeDTO changedModel = new EmployeeDTO(model);
		changedModel.setSalary(newSalary);
		
		service.update(model, changedModel);
		assertEquals(newSalary, mockRepository.getById(model.getId()).getSalary());
	}
	
	@Test
	void testChangePassword_IncorrectOldPassword() throws EmptyInputException, NonPositiveInputException, WrongFormatException, WrongLengthException, IncorrectPermissionsException {
		assertFalse(service.changePassword(modelDTOs[0], "foobar", ""));
	}
	
	@ParameterizedTest
	@ValueSource(strings = {"foo", "foobarsample", "1", "123456789", "foo123"})
	void testChangePassword_CorrectOldPassword_InvalidValues(String newPassword) throws EmptyInputException, WrongFormatException {
		Exception exception = assertThrows(WrongFormatException.class, () -> service.changePassword(modelDTOs[0], modelDTOs[0].getPassword(), newPassword));
		assertTrue(exception.getMessage().contains("Incorrect password format"));
	}
	
	@ParameterizedTest
	@ValueSource(strings = {"foobarsample1", "1foobarsample", "12345678a", "a12345678", "foobar123sample"})
	void testChangePassword_CorrectOldPassword_ValidValues(String newPassword) throws EmptyInputException, NonPositiveInputException, WrongFormatException, WrongLengthException, IncorrectPermissionsException {
		assertTrue(service.changePassword(modelDTOs[0], modelDTOs[0].getPassword(), newPassword));
	}
	
	@ParameterizedTest
	@MethodSource("provideValuesForInvalidLogin")
	void testCanLogin_InvalidValues(String username, String password) {
		Exception exception = assertThrows(EmptyInputException.class, () -> service.canLogin(username, password));
		assertTrue(exception.getMessage().contains("Input fields cannot be empty"));
	}
	
	@Test
	void testCanLogin_UserDoesntExist() throws EmptyInputException, WrongFormatException {
		assertFalse(service.canLogin("nonExistingUser", "password123"));
	}
	
	@ParameterizedTest
	@MethodSource("provideValuesForExistingValues")
	void testCanLogin_ExistingUserWrongPassword(EmployeeDTO model) throws EmptyInputException {
		assertFalse(service.canLogin(model.getUsername(), "wrongPassword"));
	}
	
	@ParameterizedTest
	@MethodSource("provideValuesForExistingValues")
	void testCanLogin_ExistingUserCorrectPassword(EmployeeDTO model) throws EmptyInputException {
		assertTrue(service.canLogin(model.getUsername(), model.getPassword()));
	}
	
	private static Stream<Arguments> provideValuesForInvalidLogin() {
		return Stream.of(
			Arguments.of(null, null),
			Arguments.of("", null),
			Arguments.of(" ", null),
			Arguments.of(models[0].getUsername(), null),
			Arguments.of(models[0].getUsername(), ""),
			Arguments.of(models[0].getUsername(), " ")
		);
	}
	
	private static Stream<Arguments> provideValuesForGet() {
		return Stream.of(
				Arguments.of(modelDTOs[0], 0), 
				Arguments.of(modelDTOs[1], 1), 
				Arguments.of(modelDTOs[3], 3), 
				Arguments.of(modelDTOs[5], 5),
				Arguments.of(modelDTOs[6], 6)
			);
	}
	
	private static Stream<Arguments> provideValuesForChangeToExistingUsername() {
		return Stream.of(
			Arguments.of(modelDTOs[0], modelDTOs[1].getUsername()),
			Arguments.of(modelDTOs[1], modelDTOs[0].getUsername()),
			Arguments.of(modelDTOs[3], modelDTOs[0].getUsername()),
			Arguments.of(modelDTOs[5], modelDTOs[0].getUsername()),
			Arguments.of(modelDTOs[6], modelDTOs[0].getUsername())
		);
	}
	
	private static Stream<Arguments> provideValuesForNonExistingUsername() {
		return Stream.of(
			Arguments.of(modelDTOs[0].getUsername() + "1", 1), 
			Arguments.of(modelDTOs[1].getUsername() + "1", 2),
			Arguments.of(modelDTOs[modelDTOs.length - 2].getUsername() + "1", mockRepository.count() - 1),
			Arguments.of(modelDTOs[modelDTOs.length - 1].getUsername() + "1", mockRepository.count())
		);
	}
	
	private static Stream<Arguments> provideValuesForExistingValues() {
		return Stream.of(
			Arguments.of(modelDTOs[0]), 
			Arguments.of(modelDTOs[1]), 
			Arguments.of(modelDTOs[3]),
			Arguments.of(modelDTOs[5]),
			Arguments.of(modelDTOs[6])
		);
	}
	
	private static Stream<Arguments> provideValuesForInvalidValues() {
		return Stream.of(
			Arguments.of(new EmployeeDTO(), EmptyInputException.class, "Input fields cannot be empty"),
			Arguments.of(new EmployeeDTO(new UserDTO("a", "a", "a", "a", "a", LocalDate.now()), 0, 0), WrongFormatException.class, "Correct format is")
		);
	}
	
	private static Stream<Arguments> provideValuesForGetByUsername() {
		return Stream.of(
			Arguments.of(modelDTOs[0], models[0].getUsername()),
			Arguments.of(modelDTOs[1], models[1].getUsername()),
			Arguments.of(modelDTOs[3], models[3].getUsername()),
			Arguments.of(modelDTOs[5], models[5].getUsername()),
			Arguments.of(modelDTOs[6], models[6].getUsername())
		);
	}
}
