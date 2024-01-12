import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.util.ArrayList;
import java.util.Map;

import org.junit.jupiter.api.Test;

class TestBookPurchaseServiceNeighborhood {
	private static String tempDataDirPath = Utils.testingUserDataDirPath;
	private IBookPurchaseRepository repository = new BookPurchaseRepository(tempDataDirPath, new DbContext());
	private IBookPurchase BP = new BookPurchaseService(repository);

	private ArrayList<BP> data;
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
	@Given("these existing Purchases saved in the database")
	public void these_existing_Purchases_saved_in_the_database(io.cucumber.datatable.DataTable dataTable) {
		List<Map<String, String>> rowData = dataTable.asMaps();
		this.data = new ArrayList<>();
		
		for(Map<String, String> data: rowData) {
			try {
				BookPurchase bookpurchase = new BookPurchse(
						Integer.parseInt(data.get("amount")), new CustomDate(data.get("date"))
					);
				
				repository.add(bookpurchase);
				this.data.add(bookpurchase);
			} catch (EmptyInputException e) {
				e.printStackTrace();
			}
		}
	}
	@When ("All purchases requested")
	public void All_purchases_reuested() {
		dtoModelInUse= BP.get(0);
		dtoModelInUse2= BP.get(1);
		dtoModelInUse3= BP.get(2);
	}
	@Then("Purchases {string}{string},{string}{string},{string}{string} should be returned")
	public void Purchases_should_be_returned(String a1 ,String d1,String a2,String d2,String a3,String a3 ) {
		assertEquals(dtoModelInUse.getAmount(),Integer.parseInt(a1));
		assertEquals(dtoModelInUse.getDate(),new CustomDate(d1));
		assertEquals(dtoModelInUse2.getAmount(),Integer.parseInt(a2));
		assertEquals(dtoModelInUse2.getDate(),new CustomDate(d2));
		assertEquals(dtoModelInUse3.getAmount(),Integer.parseInt(a3));
		assertEquals(dtoModelInUse.getDate(),new CustomDate(d3));
	}
	
	@Given("Amount {string}")
	public Given_Amount(string amount){
		BookPurchaseDTO bookPur = new CategoryDTO(Integer.parseInt(amount,new CustomDate);
		dtoModelInUse=BookPur
	}
	@Then("Purchase should be saved in the database")
	public Save_Purchase_in_database() {
		repository.saveChanges();
	    assertNotNull(service.get(dtoModelInUse.getDate()));
	}
}
