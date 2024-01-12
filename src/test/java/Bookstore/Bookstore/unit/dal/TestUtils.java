package Bookstore.Bookstore.unit.dal;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import Bookstore.Bookstore.commons.utils.Utils;

class TestUtils {

	@ParameterizedTest
	@CsvSource({
		"0, 0",
		"1,1",
		"-3,2",
		"-5,4"
	})
	void TestGetOrderedList(int index, int result) {
		assertEquals(result,Utils.getOrderedListIndex(index));
	}

}
