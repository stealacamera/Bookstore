package Bookstore.Bookstore.unit.dal.repositories;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.io.TempDir;

import Bookstore.Bookstore.commons.utils.Utils;
import Bookstore.Bookstore.dal.repositories.DbContext;

class TestRepositoryBase {
	protected static String dataDirPath = Utils.testingUserDataDirPath;
	@TempDir
	protected static File dataDir, dataFile;
	protected static DbContext dbContext;
	
	@BeforeAll
	public static void setUp() throws IOException {
		dataDir = new File(dataDirPath);
		dataDir.mkdirs();
		
		dbContext = new DbContext();
	}
	
	@AfterEach
	public void tearDown() {
		dataFile.delete();
	}
	
	@AfterAll
	public static void delete() {
		File parentDir = dataDir.getParentFile();
		dataDir.delete();
		parentDir.delete();
	}
	
	protected static <T extends Serializable> void setTempDataToFile(List<T> data) throws IOException {
		FileOutputStream fileOutputStr = new FileOutputStream(dataFile);
		ObjectOutputStream objOutputStr = new ObjectOutputStream(fileOutputStr);
		objOutputStr.writeObject(data);
		
		fileOutputStr.close();
		objOutputStr.close();
	}
	
	@SuppressWarnings("unchecked")
	protected static <T extends Serializable> List<T> getDataFromFile() throws ClassNotFoundException, IOException {
		List<T> data = new ArrayList<>();
		
		if(dataFile.length() == 0)
			return data;
		
		FileInputStream fileInputStr = new FileInputStream(dataFile);
		ObjectInputStream objInputStr = new ObjectInputStream(fileInputStr);
		data = (ArrayList<T>) objInputStr.readObject();
		
		fileInputStr.close();
		objInputStr.close();
		
		return data;
	 }
}
