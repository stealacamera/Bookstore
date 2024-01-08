package Bookstore.Bookstore.integration;

import java.io.File;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.io.TempDir;

import Bookstore.Bookstore.commons.utils.Utils;

public abstract class TestNeighborHoodBase {
	@TempDir
	protected static File tempDataDir;
	
	@BeforeAll
	protected static void setUpDataFolder() {
		tempDataDir = new File(Utils.testingUserDataDirPath);
	}
	
	@AfterAll
	protected static void tearDown() {
		for(File data: tempDataDir.listFiles())
			data.delete();
		
		tempDataDir.delete();
	}
}
