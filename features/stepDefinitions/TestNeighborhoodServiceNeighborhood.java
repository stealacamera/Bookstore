import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;

class TestNeighborhoodServiceNeighborhood {
	private static String tempDataDirPath = Utils.testingUserDataDirPath;
	private ICategoryRepository repository = new CategoryRepository(tempDataDirPath, new DbContext());
	private ICategory cat = new CategoryService(repository);


	private ArrayList<Category> data;
	private CategoryDTO dtoModelInUse;
	private CategoryDTO dtoModelInUse2;
	private CategoryDTO dtoModelInUse3;
	
	@AfterAll
	public static void deleteFolder() {
		File dataDir = new File(tempDataDirPath);
		File parentDir = dataDir.getParentFile();
		
		dataDir.delete();
		parentDir.delete();
	}
	
	@After
	public void deleteData() {
		new File(tempDataDirPath).delete();
		repository = new CategoryRepository(tempDataDirPath, new DbContext());
	}
	
	@Given("these existing Categories saved in the database")
	public void these_existing_Categories_saved_in_the_database(io.cucumber.datatable.DataTable dataTable) {
		List<Map<String, String>> rowData = dataTable.asMaps();
		this.data = new ArrayList<>();
		
		for(Map<String, String> data: rowData) {
			try {
				Category category = new Category(
						Integer.parseInt(data.get("ID")), data.get("Category")
					);
				
				repository.add(category);
				this.data.add(category);
			} catch (EmptyInputException e) {
				e.printStackTrace();
			}
		}
	}
	
	@When("All categories requested")
	public All_Categories_Requested(){
		dtoModelInUse= cat.get(0);
		dtoModelInUse2= cat.get(1);
		dtoModelInUse3= cat.get(2);
	}
	@Then("Categories {string} , {string}, {string} should be returned")
	public Categoires_should_be_returned(String Cat1,String Cat2,String Cat3){
		assertEquals(dtoModelInUse.getName(),Cat1);
		assertEquals(dtoModelInUse2.getName(),Cat2);
		assertEquals(dtoModelInUse3.getName(),Cat3);
	}
	@Given("the ID {string}")
	public Given_The_Id(String id) {
		int parsedId = Integer.parseInt(id);
		
		dtoModelInUse= cat.get(parsedId-1);		
	}
	@Then("Category {string} should be returned")
	public Category_should_be_returned(String cat){
		assertEquals(dtoModelInUse.getName(),cat);
	}
	
	@Given("Category {string}")
	public Given_Data_of_Cat_and_ID(string category){
		CategoryDTO cat = new CategoryDTO(parsedId,category);
		dtoModelInUse=CategoryDTO
	}
	@Then("Category should be saved in the database")
	public Save_category_in_database() {
		repository.saveChanges();
	    assertNotNull(service.getId(dtoModelInUse.getId()));
	}
	
}
