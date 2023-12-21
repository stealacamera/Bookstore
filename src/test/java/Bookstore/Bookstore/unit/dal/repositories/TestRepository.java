package Bookstore.Bookstore.unit.dal.repositories;

import static org.junit.jupiter.api.Assertions.assertIterableEquals;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import Bookstore.Bookstore.dal.repositories.irepositories.IRepository;
import Bookstore.Bookstore.unit.dal.repositories.mocks.RepositoryStub;

public class TestRepository extends TestRepositoryBase {
	private static String fileName = "String";
	private IRepository<String> repository;
	
	@BeforeEach
	void setUpEach() throws IOException {
		dataFile = new File(dataDir, fileName);
		dataFile.createNewFile();
	}
	
	@Test
	void testGetAll_WithNoElements() throws ClassNotFoundException, IOException {
		repository = new RepositoryStub(dbContext, dataDirPath);
		assertIterableEquals(repository.getAll(), getDataFromFile());
	}
	
	@Test
	void testGetAll_WithElements() throws NoSuchFieldException, SecurityException, Exception {
		setTempDataToFile(new ArrayList<>(Arrays.asList("foo", "bar")));
		
		repository = new RepositoryStub(dbContext, dataDirPath);
		assertIterableEquals(repository.getAll(), getDataFromFile());
	}
	
	@Test
	void testSaveChanges_AddElement() throws ClassNotFoundException, IOException {
		repository = new RepositoryStub(dbContext, dataDirPath);
		
		repository.add("foo");
		repository.saveChanges();

		assertIterableEquals(repository.getAll(), getDataFromFile());
	}
	
	@Test
	void testSaveChanges_RemoveElements() throws ClassNotFoundException, IOException {
		setTempDataToFile(new ArrayList<>(Arrays.asList("foo", "bar")));
		repository = new RepositoryStub(dbContext, dataDirPath);
		
		// Test with populated data table
		repository.remove(0);
		repository.saveChanges();
		
		assertIterableEquals(repository.getAll(), getDataFromFile());
		
		// Test with empty data table
		repository.remove(0);
		repository.saveChanges();
		
		assertIterableEquals(repository.getAll(), getDataFromFile());
	}
}