package test;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import utils.Utils;

class UtilsTesting {

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
