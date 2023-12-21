package Bookstore.Bookstore.unit.bll;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;

import java.util.Arrays;
import java.util.stream.Stream;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import Bookstore.Bookstore.bll.dto.BookPurchaseDTO;
import Bookstore.Bookstore.bll.services.BookPurchaseService;
import Bookstore.Bookstore.dal.models.BookPurchase;
import Bookstore.Bookstore.commons.exceptions.NonPositiveInputException;
import javafx.collections.FXCollections;
import Bookstore.Bookstore.unit.bll.mocks.BookPurchaseRepositoryMock;

public class TestBookPurchaseService {
	private BookPurchaseService service;
	private BookPurchaseRepositoryMock mockRepository;
	private static BookPurchase[] models;
	private static BookPurchaseDTO[] modelDTOs;
	
	@BeforeAll
	static void setUpDummyData() throws NonPositiveInputException {
		models = new BookPurchase[7];
		modelDTOs = new BookPurchaseDTO[models.length];
		
		for(int i = 0; i < models.length; i++) {
			models[i] = new BookPurchase(i + 1);
			modelDTOs[i] = new BookPurchaseDTO(models[i].getAmount(), models[i].getDate().getDate());
		}
	}
	
	@BeforeEach
	void setUp() {
		mockRepository = new BookPurchaseRepositoryMock();
		mockRepository.addDummyData(models);
		
		service = new BookPurchaseService(mockRepository);
	}
	
	@Test
	void testGetAll_Empty() {
		service = new BookPurchaseService(new BookPurchaseRepositoryMock());
		assertIterableEquals(FXCollections.observableArrayList(), service.getAll());
	}
	
	@Test
	void testGetAll_NonEmpty() {
		assertIterableEquals(Arrays.asList(modelDTOs), service.getAll());
	}
		
	@ParameterizedTest
	@MethodSource("provideValuesForGet")
	void testGet(BookPurchaseDTO model, int index) {
		assertEquals(model, service.get(index));
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
