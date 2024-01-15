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
		if(testDataDir == null)
			testDataDir = new File(Utils.testDataDirPath);
		
		File[] dataFiles = testDataDir.listFiles();
		
		if(dataFiles != null && dataFiles.length != 0) {
			for(File data: dataFiles)
				data.delete();
		}
		
		testDataDir.delete();
	}
}
