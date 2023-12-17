package test.unit.bll;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Arrays;
import java.util.stream.Stream;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import bll.BillService;
import bll.dto.BillDTO;
import dal.models.Bill;
import exceptions.EmptyInputException;
import exceptions.ExistingObjectException;
import exceptions.IncorrectPermissionsException;
import exceptions.NonPositiveInputException;
import exceptions.WrongFormatException;
import exceptions.WrongLengthException;
import test.unit.bll.mocks.BillRepositoryMock;

public class TestBillService {
	private BillService service;
	private BillRepositoryMock mockRepository;
	private static Bill[] models;
	private static BillDTO[] modelDTOs;
	
	@BeforeAll
	static void setUpDummyData() throws NonPositiveInputException {
		models = new Bill[7];
		modelDTOs = new BillDTO[models.length];
		
		for(int i = 0; i < models.length; i++) {
			models[i] = new Bill(i + 1, 1, 1);
			modelDTOs[i] = new BillDTO(models[i].getSellerId(), models[i].getSaleAmount(), models[i].getNumOfBooks());
		}
	}
	
	@BeforeEach
	void setUp() {
		mockRepository = new BillRepositoryMock();
		mockRepository.addDummyData(models);
		
		service = new BillService(mockRepository);
	}
	
	@Test
	void testGetAll_Empty() {
		service = new BillService(new BillRepositoryMock());
		assertEquals(0, service.getAll().size());
	}
	
	@Test
	void testGetAll_NonEmpty() {
		assertIterableEquals(Arrays.asList(modelDTOs), service.getAll());
	}
	
	@ParameterizedTest
	@MethodSource("provideValuesForGet")
	void testGet(BillDTO expected, int index) {
		assertEquals(expected, service.get(index));
	}
	
	@ParameterizedTest
	@MethodSource("provideValuesForInvalidAdd")
	void testAdd_InvalidValues(Class<Exception> exceptionType, BillDTO model, String errorMessage) {
		Exception exception = assertThrows(exceptionType, () -> service.add(model));
		assertTrue(exception.getMessage().contains(errorMessage));
	}
	
	@Test
	void testAdd_Null() throws ExistingObjectException, EmptyInputException, NonPositiveInputException, WrongFormatException, WrongLengthException, IncorrectPermissionsException {
		assertFalse(service.add(null));
	}
	
	@Test
	void testAdd_ValidValues() throws NonPositiveInputException, ExistingObjectException, EmptyInputException, WrongFormatException, WrongLengthException, IncorrectPermissionsException {
		BillDTO model = new BillDTO(9, 123, 123);
		
		service.add(model);
		assertEquals(new Bill(model.getSellerId(), model.getSaleAmount(), model.getNumOfBooks()), mockRepository.get(models.length));
	}
	
	private static Stream<Arguments> provideValuesForInvalidAdd() {
		return Stream.of(
			Arguments.of(NonPositiveInputException.class, new BillDTO(0, 0, 0), "Incorrect seller id: Please enter a positive number"),
			Arguments.of(NonPositiveInputException.class, new BillDTO(1, 0, 0), "Incorrect sale amount: Please enter a positive number"),
			Arguments.of(NonPositiveInputException.class, new BillDTO(1, 1, 0), "Incorrect nr. of books: Please enter a positive number")
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
}
