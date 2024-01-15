package Bookstore.Bookstore.integration.viewNeighborhoods;

import java.io.File;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.io.TempDir;

import Bookstore.Bookstore.TestingUtils;
import Bookstore.Bookstore.commons.utils.Utils;

public abstract class TestViewNeighborHoodBase {
	@TempDir
	protected static File tempDataDir;
	
	@BeforeAll
	protected static void setUpDataFolder() {
		tempDataDir = new File(Utils.testDataDirPath);
	}
	
	@AfterAll
	protected static void tearDown() {
		TestingUtils.deleteTestDatabase(tempDataDir);
	}
}
