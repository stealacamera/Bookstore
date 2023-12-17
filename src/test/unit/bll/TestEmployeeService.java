package test.unit.bll;

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

import bll.EmployeeService;
import bll.dto.EmployeeDTO;
import bll.dto.UserDTO;
import dal.models.Employee;
import dal.models.User;
import dal.models.utilities.CustomDate;
import exceptions.EmptyInputException;
import exceptions.ExistingObjectException;
import exceptions.IncorrectPermissionsException;
import exceptions.NonPositiveInputException;
import exceptions.WrongFormatException;
import exceptions.WrongLengthException;
import test.unit.bll.mocks.EmployeeRepositoryMock;

public class TestEmployeeService {
	private static Employee e1, e2, e3, e4, e5, e6;
	private static EmployeeDTO eDTO1, eDTO2, eDTO4, eDTO3, eDTO5, eDTO6;
	private static EmployeeRepositoryMock mockRepository;
	private static EmployeeService service;
	
	private static EmployeeDTO createEquivalentDTO(Employee base) {
		UserDTO u = new UserDTO(base.getId(), base.getUsername(), base.getFullName(), base.getEmail(), base.getHashedPassword(), base.getPhoneNum(), base.getBirthdate().getDate());
		EmployeeDTO model = new EmployeeDTO(u, base.getSalary(), base.getAccessLvl(), base.getPermissions());
		model.setPassword("Password123");
		
		return model;
	}
	
	@BeforeAll
	static void setUpDummyData() throws EmptyInputException, WrongFormatException, NonPositiveInputException {
		User[] users = new User[6];
		
		for(int i = 0; i < users.length; i++)
			users[i] = new User((((char) (97 + i)) + "").repeat(5), "foo bar", i + "foo@gmail.com", "Password123", "069 123 1233", new CustomDate());
		
		e1 = new Employee(users[0], 1, 1);
		e2 = new Employee(users[1], 1, 1);
		e3 = new Employee(users[2], 1, 1);
		e4 = new Employee(users[3], 1, 1);
		e5 = new Employee(users[4], 1, 1);
		e6 = new Employee(users[5], 1, 1);
		
		eDTO1 = createEquivalentDTO(e1);
		eDTO2 = createEquivalentDTO(e2);
		eDTO3 = createEquivalentDTO(e3);
		eDTO4 = createEquivalentDTO(e4);
		eDTO5 = createEquivalentDTO(e5);
		eDTO6 = createEquivalentDTO(e6);
	}
	
	@BeforeEach
	void setuUp() {
		mockRepository = new EmployeeRepositoryMock();
		mockRepository.addDummyData(e1, e2, e3, e4, e5, e6);
		
		service = new EmployeeService(mockRepository);
	}
	
	@Test
	void testGetAll_Empty() {
		service = new EmployeeService(new EmployeeRepositoryMock());
		assertEquals(0, service.getAll().size());
	}
	
	@Test
	void testGetAll_NonEmpty() {
		assertIterableEquals(Arrays.asList(eDTO1, eDTO2, eDTO3, eDTO4, eDTO5, eDTO6), service.getAll());
	}
	
	@ParameterizedTest
	@MethodSource("provideValuesForGet")
	void testGet(EmployeeDTO expected, int index) {
		assertEquals(expected, service.get(index));
	}
	
	@Test
	void testGetById_NotInDatabase() {
		assertNull(service.getById(0));
		assertNull(service.getById(e6.getId() + 1));
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
		EmployeeDTO model = new EmployeeDTO(eDTO1);
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
		assertFalse(service.changePassword(eDTO1, "foobar", ""));
	}
	
	@ParameterizedTest
	@ValueSource(strings = {"foo", "foobarsample", "1", "123456789", "foo123"})
	void testChangePassword_CorrectOldPassword_InvalidValues(String newPassword) throws EmptyInputException, WrongFormatException {
		Exception exception = assertThrows(WrongFormatException.class, () -> service.changePassword(eDTO1, eDTO1.getPassword(), newPassword));
		assertTrue(exception.getMessage().contains("Incorrect password format"));
	}
	
	@ParameterizedTest
	@ValueSource(strings = {"foobarsample1", "1foobarsample", "12345678a", "a12345678", "foobar123sample"})
	void testChangePassword_CorrectOldPassword_ValidValues(String newPassword) throws EmptyInputException, NonPositiveInputException, WrongFormatException, WrongLengthException, IncorrectPermissionsException {
		assertTrue(service.changePassword(eDTO1, eDTO1.getPassword(), newPassword));
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
			Arguments.of(e1.getUsername(), null),
			Arguments.of(e1.getUsername(), ""),
			Arguments.of(e1.getUsername(), " ")
		);
	}
	
	private static Stream<Arguments> provideValuesForGet() {
		return Stream.of(
				Arguments.of(eDTO1, 0), 
				Arguments.of(eDTO2, 1), 
				Arguments.of(eDTO3, 2), 
				Arguments.of(eDTO5, 4), 
				Arguments.of(eDTO6, 5)
			);
	}
	
	private static Stream<Arguments> provideValuesForChangeToExistingUsername() {
		return Stream.of(
			Arguments.of(eDTO1, eDTO2.getUsername()),
			Arguments.of(eDTO2, eDTO1.getUsername()),
			Arguments.of(eDTO3, eDTO1.getUsername()),
			Arguments.of(eDTO5, eDTO1.getUsername()),
			Arguments.of(eDTO6, eDTO1.getUsername())
		);
	}
	
	private static Stream<Arguments> provideValuesForNonExistingUsername() {
		return Stream.of(
			Arguments.of(eDTO1.getUsername() + "1", 1), 
			Arguments.of(eDTO2.getUsername() + "1", 2),
			Arguments.of(eDTO6.getUsername() + "1", mockRepository.count())
		);
	}
	
	private static Stream<Arguments> provideValuesForExistingValues() {
		return Stream.of(
			Arguments.of(eDTO1), 
			Arguments.of(eDTO2), 
			Arguments.of(eDTO3), 
			Arguments.of(eDTO5), 
			Arguments.of(eDTO6)
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
			Arguments.of(eDTO1, e1.getUsername()),
			Arguments.of(eDTO2, e2.getUsername()),
			Arguments.of(eDTO3, e3.getUsername()),
			Arguments.of(eDTO5, e5.getUsername()),
			Arguments.of(eDTO6, e6.getUsername())
		);
	}
}
