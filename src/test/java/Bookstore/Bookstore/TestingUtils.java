package Bookstore.Bookstore;

import java.io.File;

import org.testfx.api.FxAssert;
import org.testfx.api.FxRobot;
import org.testfx.matcher.control.LabeledMatchers;
import org.testfx.util.WaitForAsyncUtils;

import Bookstore.Bookstore.commons.utils.Utils;

public class TestingUtils {
	public static void testErrorMessage(FxRobot robot, String message) {
		WaitForAsyncUtils.waitForFxEvents();
		FxAssert.verifyThat("#alert_error_message", LabeledMatchers.hasText(message));
		robot.clickOn("#error-ok-btn");
	}
	
	public static void deleteTestDatabase(File testDataDir) {
		File tempDataDir = testDataDir != null ? testDataDir : new File(Utils.testingUserDataDirPath);
		
		for(File data: tempDataDir.listFiles())
			data.delete();
		
		tempDataDir.delete();
	}
}
