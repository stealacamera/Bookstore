package test.unit.bll;

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

import bll.BookPurchaseService;
import bll.dto.BookPurchaseDTO;
import dal.models.BookPurchase;
import exceptions.NonPositiveInputException;
import javafx.collections.FXCollections;
import test.unit.bll.mocks.BookPurchaseRepositoryMock;

public class TestBookPurchaseService {
	private BookPurchaseService service;
	private BookPurchaseRepositoryMock mockRepository;
	private static BookPurchase b1, b2, b3, b4, b5, b6;
	private static BookPurchaseDTO bDTO1, bDTO2, bDTO3, bDTO4, bDTO5, bDTO6;
	
	@BeforeAll
	static void setUpDummyData() throws NonPositiveInputException {
		b1 = new BookPurchase(1);
		b2 = new BookPurchase(2);
		b3 = new BookPurchase(3);
		b4 = new BookPurchase(4);
		b5 = new BookPurchase(5);
		b6 = new BookPurchase(6);
		
		bDTO1 = new BookPurchaseDTO(b1.getAmount(), b1.getDate().getDate());
		bDTO2 = new BookPurchaseDTO(b2.getAmount(), b2.getDate().getDate());
		bDTO3 = new BookPurchaseDTO(b3.getAmount(), b3.getDate().getDate());
		bDTO4 = new BookPurchaseDTO(b4.getAmount(), b4.getDate().getDate());
		bDTO5 = new BookPurchaseDTO(b5.getAmount(), b5.getDate().getDate());
		bDTO6 = new BookPurchaseDTO(b6.getAmount(), b6.getDate().getDate());
	}
	
	@BeforeEach
	void setUp() {
		mockRepository = new BookPurchaseRepositoryMock();
		mockRepository.addDummyData(b1, b2, b3, b4, b5, b6);
		
		service = new BookPurchaseService(mockRepository);
	}
	
	@Test
	void testGetAll_Empty() {
		service = new BookPurchaseService(new BookPurchaseRepositoryMock());
		assertIterableEquals(FXCollections.observableArrayList(), service.getAll());
	}
	
	@Test
	void testGetAll_NonEmpty() {
		assertIterableEquals(Arrays.asList(bDTO1, bDTO2, bDTO3, bDTO4, bDTO5, bDTO6), service.getAll());
	}
		
	@ParameterizedTest
	@MethodSource("provideValuesForGet")
	void testGet(BookPurchaseDTO model, int index) {
		assertEquals(model, service.get(index));
	}
	
	private static Stream<Arguments> provideValuesForGet() {
		return Stream.of(
			Arguments.of(bDTO1, 0),
			Arguments.of(bDTO2, 1),
			Arguments.of(bDTO3, 2),
			Arguments.of(bDTO5, 4),
			Arguments.of(bDTO6, 5)
		);
	}
}
